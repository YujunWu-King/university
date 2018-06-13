Ext.define('KitchenSink.view.clueManagement.clueManagement.import.clueImportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.clueImportController',
    //校验
    validate: function(store){
        var me = this;
        var range = store.getRange();

        if(range==undefined||range.length==0){
            return;
        }

        var validationJson = "";
        Ext.each(range,function(record,index){
            var createDttm = record.data.createDttm;
            if(createDttm!=null && createDttm!="") {
                var createDttm_date = new Date(createDttm);
                if(createDttm_date=="Invalid Date") {
                    record.data.createDttm = "invalid";
                } else {
                    record.data.createDttm = Ext.Date.format(createDttm_date,'Y-m-d H:i');
                }
            }

            if(validationJson == ""){
                validationJson = '{"data":'+Ext.JSON.encode(record.data)+'}';
            }else{
                validationJson = validationJson + ','+'{"data":'+Ext.JSON.encode(record.data)+'}';
            }
        });

        //查询从哪个页面点击的导入数据，执行的方法不同
        var importFrom;
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
        if(activeTab.viewModelKey=="enrollmentCluePanel"){
            importFrom = "ZSXS";
        }
        if(activeTab.viewModelKey=="myEnrollmentCluePanel"){
            importFrom = "MYXS";
        }

        var comParams = '"importFrom":"'+importFrom+'","validate":[' + validationJson + "]";
        //提交参数
        var tzParams ='{"ComID":"TZ_XSXS_DRDC_COM","PageID":"TZ_XSXS_IMP_STD","OperateType":"tzValidate","comParams":{'+comParams+'}}';

        Ext.MessageBox.show({
            msg: '校验数据中，请稍候...',
            progress: true,
            progressText:'校验中...',
            width: 300,
            wait: {
                interval: 50
            }
        });

        Ext.tzLoad(tzParams,function(response){
            var validationList = response;
            Ext.each(validationList,function(item){
                var record = store.getById(item["id"]);
                record.set("validationResult",item["validationResult"]);
                record.set("validationMsg",item["validationMsg"]);
            });
            store.commitChanges();
            Ext.MessageBox.hide();
        });
    },
    //删除当前行
    deleteCurrImpRec: function (view, rowIndex) {
        var wingridstore = view.findParentByType("grid").store;
        wingridstore.removeAt(rowIndex);
        //更新总条数
        var winform = view.findParentByType("grid").findParentByType("window").down("form").getForm();
        var winformrec = winform.getFieldValues();
        var strtmp = "共 ";
        strtmp = strtmp + "<span style='color:red;font-size:22px'>" + wingridstore.getCount() + "</span>";
        strtmp = strtmp + " 条数据";
        var formpara = {ImpClueCount: strtmp};
        winform.setValues(formpara);
    },
    //重新检验
    validateAgain:function(btn){
        var win = btn.findParentByType("window"),
            store = win.down("grid").getStore();

        this.validate(store);
    },
    //导入
    onWinImport:function(btn){
		var me = this;
        var win = btn.findParentByType("window"),
            grid = win.down("grid"),
            columns = grid.columns,
            store = grid.getStore(),
            range = store.getRange();

        if(range==undefined||range.length==0){
            Ext.MessageBox.alert('提示', '没有需要导入的数据！');
            return;
        }

        if(store.isChanged()){
            Ext.MessageBox.alert('提示', '您修改或者删除了数据，请重新校验通过之后再点击导入！');
            return;
        }

        var valid = true;

        Ext.each(range,function(record) {
            if(record.get("validationResult")!=true) {
                Ext.MessageBox.alert('提示', '数据校验未通过，请修改或者删除之后重新校验通过之后再点击导入！');
                valid = false;
                return false;
            }
        });

        if(!valid) {
            return;
        } else {

            //查询从哪个页面点击的导入数据，执行的方法不同
            var importFrom;
            var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
            if(activeTab.viewModelKey=="enrollmentCluePanel"){
                importFrom = "ZSXS";
            }
            if(activeTab.viewModelKey=="myEnrollmentCluePanel"){
                importFrom = "MYXS";
            }

            var strJson = "";
            Ext.each(range,function(record,index){
                record.data.createDttm=Ext.util.Format.dateRenderer('Y-m-d H:i')(record.data.createDttm);
                if(strJson==""){
                    strJson = '{"importFrom":"'+importFrom+'","data":'+Ext.JSON.encode(record.data)+'}';
                } else {
                    strJson = strJson+',{"importFrom":"'+importFrom+'","data":'+Ext.JSON.encode(record.data)+'}';
                }
            });

            var comParams = '"add":[' + strJson + "]";

            //提交参数
            var tzParams = '{"ComID":"TZ_XSXS_DRDC_COM","PageID":"TZ_XSXS_IMP_STD","OperateType":"U","comParams":{'+comParams+'}}';

            Ext.tzSubmit(tzParams,function(responseData){
                var insertNum = responseData.insertNum;
                var unInsertNum =  responseData.unInsertNum;
                var unFindChargeClue = responseData.unFindChargeClue;
                var completeSameClue = responseData.completeSameClue;

                var msgTip = "共导入"+insertNum+"条数据";
                if(unInsertNum>0) {
                    msgTip += "，未导入"+unInsertNum+"条";
                    if(unFindChargeClue!="") {
                        msgTip += "<br>因为输入的责任人未找到，以下线索未导入：<br>" + unFindChargeClue;
                    }
                    if(completeSameClue!="") {
                        msgTip += "<br>因为手机或邮箱已存在对应线索，以下线索未导入：<br>" + completeSameClue;
                    }
                }
                
                Ext.Msg.alert("提示",msgTip);
                win.close();

                //刷新Grid
                var panelStore;
                if(importFrom=="ZSXS"){
                    panelStore=activeTab.store;
                    panelStore.reload();
                }
                if(importFrom=="MYXS"){
                    panelStore=activeTab.store;
                    panelStore.reload();
                }
            },"",true,this);
        }
    },
    //关闭
    onWinClose:function(btn){
		var win = btn.findParentByType("window");
		if(win.impres == "0"){
			win.close();
		}else{
			Ext.MessageBox.confirm('提示','数据尚未导入成功，是否确定要离开当前页面？',function(btnId){
				if(btnId=='yes'){
					win.close();
				}else {
				}
			},this);
		}
    },
    //选择常住地
    searchLocal:function(field) {
        Ext.tzShowPromptSearch({
            recname: 'TZ_XSXS_YXDQ_VW',
            searchDesc: '选择常住地',
            maxRow:20,
            condition:{
                srhConFields:{
                    TZ_LABEL_DESC:{
                        desc:'常住地',
                        operator:'07',
                        type:'01'
                    },
                    TZ_JG_ID:{
                        desc:'机构编号',
                        operator:'01',
                        type:'01',
                        value:Ext.tzOrgID,
                        editable:false
                    }
                }
            },
            srhresult:{
                TZ_LABEL_NAME: '常住地编号',
                TZ_LABEL_DESC: '常住地'
            },
            multiselect: false,
            callback: function(selection){
                var localId=selection[0].data.TZ_LABEL_NAME;
                var localAddress=selection[0].data.TZ_LABEL_DESC;

                field.setValue(localAddress);
            }
        });
    },
    //清除常住地
    clearLocal:function(field) {
        field.setValue("");
    },
    //选择责任人
    searchCharge:function(btn) {
        var me = this,
            view = me.getView();

        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_ZRR_XZ_STD"];
        if(pageResSet==""||pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet['jsClassName'];
        if(className==""||className==undefined) {
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_ZRR_XZ_STD，请检查配置。');
            return;
        }

        var win = me.lookupReference("personChooseWindow");
        if(!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);

            win = new ViewClass(view);

            view.add(win);
            win.show();
        }
    },
    //清除责任人
    clearCharge:function(field) {
        field.setValue("");

        var selList = field.findParentByType("grid").getSelectionModel().getSelection();
        selList[0].set('chargeOprid',"");
    }
});
