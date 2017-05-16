Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.materialsReviewExamineeController',
    //材料评审考生名单-查询
    queryExaminee:function(btn) {
    	var panel = btn.findParentByType("materialsReviewExaminee");
        var form = btn.findParentByType("form").getForm();
        var classId = form.findField("classId").getValue();
        var batchId = form.findField("batchId").getValue();
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW',
            condition:{
                TZ_CLASS_ID:classId,
                TZ_APPLY_PC_ID:batchId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                store.tzStoreParams = seachCfg;
                store.load();
                
                var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD","OperateType":"getSearchSql","comParams":'+seachCfg+'}';
				Ext.tzLoad(tzParams,function(responseData){
					var getedSQL = responseData.searchSql;
					panel.getedSQL = getedSQL;
				});
            }
        });
    },
    //材料评审考生名单-新增
    addExaminee:function(btn) {

        var form = btn.findParentByType("form").getForm();
        var classId = form.findField("classId").getValue();
        var batchId = form.findField("batchId").getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_ADDKS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_ADDKS_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }

            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        cmp = new ViewClass();

        cmp.on('afterrender',function(win){
            var store = win.child('grid').getStore();
            var tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_ADDKS_STD.TZ_CLPS_ADDKS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value":"'+classId+'","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value":"'+batchId+'"}}';
            store.tzStoreParams = tzStoreParams;
            store.load();
        });

        cmp.show();
    },
    //材料评审考生名单-新增考生-查询
    queryExamineeAdd:function(btn) {
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            form = activeTab.down("form").getForm();
        var classId = form.findField("classId").getValue();
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_REVIEW_CL_COM.TZ_CLPS_ADDKS_STD.TZ_CLPS_ADDKS_VW',
            condition:{
                TZ_CLASS_ID:classId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //材料评审考生名单-新增考生-添加所有考生
    addExamineeAll:function(btn) {
        var store = btn.findParentByType("grid").store;
        var tzStoreParams = store.tzStoreParams;
    },
    //材料评审考生名单-新增考生-确定
    addExamineeEnsure:function(btn) {
        var grid = btn.findParentByType("panel").child("grid");

        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else {

            var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
                actType = activeTab.actType,
                form = activeTab.down("form").getForm(),
                targetStore = activeTab.down("grid[name=materialsReviewExamineeGrid]").getStore();
            var classId = form.findField("classId").getValue();
            //var className = form.findField("className").getValue();
            var batchId = form.findField("batchId").getValue();
            //var batchName = form.findField("batchName").getValue();


            var editJson = "";

            if(actType=="add") {
                var formValues = form.getValues();
                editJson = '{"typeFlag":"RULE","data":' + Ext.JSON.encode(formValues) + '}';
            }

            for(var x =0;x<selectLength;x++){
                selectRecords[x].data.batchId = batchId;
                if(editJson!="") {
                    editJson = editJson + ',' + '{"typeFlag":"EXAMINEE","data":' + Ext.JSON.encode(selectRecords[x].data) + '}';
                } else {
                    editJson = '{"typeFlag":"EXAMINEE","data":' + Ext.JSON.encode(selectRecords[x].data) + '}';
                }
            }


            var comParams = '"add":[' + editJson + ']';

            //提交参数
            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD","OperateType":"U","comParams":{' + comParams + '}}';
            Ext.tzSubmit(tzParams, function (responseData) {
                if(actType=="add") {
                    activeTab.actType = "update";
                }
                var tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
                targetStore.tzStoreParams = tzStoreParams;
                targetStore.load();
                btn.findParentByType("panel").close();
            }, "添加考生成功", true, this);


            /*
            var judgeNumSet = form.findField("judgeNumSet").getValue();
            if(judgeNumSet!=null) {

            } else {
                judgeNumSet = 2;
            }

            var isExist = false,
                newRecord = [];

            //循环中将非重复数据保存到中间变量，避免之后的循环会额外查询到之前的循环插入的数据
            for(var x =0;x<selectLength;x++){
                if(targetStore.find('appinsId',selectRecords[x].data.appinsId)<0) {
                    delete selectRecords[x].data.id;
                    selectRecords[x].data.classId = classId;
                    selectRecords[x].data.className = className;
                    selectRecords[x].data.batchId = batchId;
                    selectRecords[x].data.batchName = batchName;
                    selectRecords[x].data.judgeList="";
                    selectRecords[x].data.judgeTotal="0";
                    selectRecords[x].data.reviewStatusDesc="未完成（0/"+judgeNumSet+"）";
                    selectRecords[x].data.interviewStatus = "W";
                    selectRecords[x].data.interviewStatusDesc = "待定";
                    newRecord.push(selectRecords[x].data);
                }else{
                    isExist = true;
                }
            }
            //循环完毕后再向store中添加数据
            targetStore.add(newRecord);
            if(isExist){
                Ext.Msg.alert("提示","在您所选的记录中，有考生已经存在于名单中");
            }*/

        }
    },
    //材料评审考生名单-新增考生-关闭
    addExamineeClose:function(btn) {
        btn.findParentByType("panel").close();
    },
    //材料评审考生名单-删除
    removeExaminee:function(btn) {
        var form=btn.findParentByType('form').getForm();
        if(form.findField('dqpsStatus').getValue()=='A'){
            Ext.Msg.alert('提示','当前评审状态为进行中，不可移除考生');
            return ;
        }
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var resSetStore =  btn.findParentByType("grid").store;
        var checkLen = selList.length;
        var tiShi=0;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    for ( var i=0;i<checkLen;i++)
                    {
                        if (selList[i].data.judgeList!="")
                        {
                            tiShi=tiShi+1;
                            selList.splice(i,"1"," ");
                        }

                    }
                    if(tiShi>0)
                    {
                        Ext.Msg.alert('提示','选中考生中存在已被指定评委的考生，不可删除');

                    }
                    resSetStore.remove(selList);
                }

            },"",true,this);
        }
    },
    //材料评审考生-指定评委
    setJudgeForExaminee:function(btn) {
        var form=btn.findParentByType('form').getForm();
        var classId = form.findField('classId').getValue();
        var batchId =form.findField('batchId').getValue();

        var dqpsStatus = form.findField("dqpsStatus").getValue();
        if(dqpsStatus=='A'){
            Ext.Msg.alert('提示','当前评审状态为进行中，不可为考生指定评委');
            return ;
        }

        var grid=btn.findParentByType("grid");
        var modiefyGrid=grid.getStore().getModifiedRecords(),
            removeGrid=grid.getStore().getRemovedRecords();
        if (modiefyGrid!=0||removeGrid!=0)
        {
            Ext.Msg.alert('提示','请您先保存页面考生数据，再指定评委');
            return;
        }
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var resSetStore =  btn.findParentByType("grid").store;
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要操作的记录");
            return;
        }else{

            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_SETPW_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className == "" || className == undefined){
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_SETPW_STD，请检查配置。');
                return;
            }
            var contentPanel, cmp, ViewClass, clsProto;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
	
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);
            clsProto = ViewClass.prototype;

            if (clsProto.themes) {
                clsProto.themeInfo = clsProto.themes[themeName];

                if (themeName === 'gray') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
                } else if (themeName !== 'neptune' && themeName !== 'classic') {
                    if (themeName === 'crisp-touch') {
                        clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                    }
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
                }
	
                if (!clsProto.themeInfo) {
                    Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                        themeName + '\'. Is this intentional?');
                }
            }

            var examineeList='';
            if(checkLen>1){
                //批量考生
                for (var d = 0; d < selList.length; d++) {
                    var  appinsId = selList[d].data.appinsId;
                    if (examineeList == '') {
                        examineeList=appinsId;
                    } else {
                        examineeList = examineeList  + "," + appinsId;
                    }
                }
                appinsId="";
            }
            else{
                appinsId=selList[0].data.appinsId;
                examineeList=appinsId;
            }
            cmp = new ViewClass({
                appRowIndex:"",
                appSelList:examineeList
            });

            cmp.on('afterrender',function(win){
                var grid= win.child('grid'),
                    store =grid.getStore();

                var  tzStoreParams = '{"classId":"'+classId+'","batchId":"'+batchId+'","appinsId":"'+appinsId+'"}';
                store.tzStoreParams = tzStoreParams;
                store.load();

            });
            cmp.show();
        }
    },
    //材料评审考生-指定评委-确定
    setJudgeEnsure:function(btn) {
        var win=btn.findParentByType("window");

        var appRowIndex = win.down("textfield[name=appRowIndex]").value;
        var appSelList = win.down("textfield[name=appSelList]").value;

        //向后台提交的报名表编号
        var appinsId='';
        //为单行考生
        if(appRowIndex!=""){
            appinsId = appRowIndex;
        } else{
            appinsId=appSelList;
        }

        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            examineeGrid = activeTab.down("grid[name=materialsReviewExamineeGrid]");

        var form = examineeGrid.findParentByType('form').getForm();
        var dqpsLunc = form.findField("dqpsLunc").getValue();
        var clpsksNum = form.findField("clpsksNum").getValue();

        var grid = win.down('grid'),
            store = grid.getStore(),
            selectJudge = "",
            selectJudgeID = " " ,
            NoselectJudgeID = "";

        var total = 0;
        var tongzu = 0;
        if (store.getCount() != 0) {
            for (var select = 0; select < store.getCount(); select++) {
                var selectFlag = store.getAt(select).get('selectFlag');

                if (selectFlag == true) {
                    var PWZBH = store.getAt(select).get('judgeGroup');
                    total = total + 1;
                    for (var select2 = 0; select2 < store.getCount(); select2++) {
                        if ( store.getAt(select2).get('selectFlag') == true) {
                            if (store.getAt(select2).get('judgeGroup') == PWZBH) {
                                tongzu = tongzu + 1;
                            }
                        }
                    }
                    if (selectJudge == "") {
                        selectJudge = store.getAt(select).get('judgeOprid');
                }
                    else {
                        selectJudge = selectJudge + "," + store.getAt(select).get('judgeOprid');
                    }
                }
                else {
                    if (NoselectJudgeID == "") {
                        NoselectJudgeID = store.getAt(select).get('judgeOprid');
                    }
                    else {
                        NoselectJudgeID = NoselectJudgeID + "," + store.getAt(select).get('judgeOprid');
                    }
                }
            }

            var classId = store.getAt(0).get('classId'),
                batchId = store.getAt(0).get('batchId'),
                selectJudge = selectJudge;
            if (tongzu > total) {
                Ext.Msg.alert('注意', '不可为考生指定同组评委,请改正');
                return;
            }

            var comParams={};
            comParams.classId=classId;
            comParams.batchId=batchId;
            comParams.appinsId=appinsId;
            comParams.selectJudge=selectJudge;
            comParams.NOselectJudgeID=NoselectJudgeID;
            comParams.clpsksNum=clpsksNum;
            comParams.dqpsLunc=dqpsLunc;

            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SETPW_STD","OperateType":"tzSetJudge","comParams":'+Ext.JSON.encode(comParams)+'}';

            Ext.tzSubmit(tzParams, function (respData) {
                examineeGrid.getStore().reload();
               /* var pweiList = respData.pweiList;
                //选中行
                var selList = examineeGrid.getSelectionModel().getSelection();
                for(var i=0;i<selList.length;i++) {
                    var appinsId = selList[i].get("appinsId");
                    for(var j=0;j<pweiList.length;j++) {
                        if(pweiList[j].appinsId==appinsId) {
                            selList[i].set('judgeList',pweiList[j].pweiDlzhDesc);
                        }
                    }
                }*/

                if(respData.Nomal==1){
                    Ext.Msg.alert ("注意","在您所选的评委中,有评委指定的考生已达上限，未能成功指定评委，请修改");
                }
                else{
                    Ext.Msg.alert ("提示","指定评委成功");
                }
                win.close();
            },'',true,this);
        }
    },
    //材料评审考生-指定评委-关闭
    setJudgeClose:function(btn) {
        btn.findParentByType("window").close();
    },
    //材料评审考生-grid操作列-指定评委
    setJudgeForOne:function(btn,rowIndex) {
        var me = this,
            view = me.getView();

        var grid= view.down("grid[name=materialsReviewExamineeGrid]"),
            store = grid.getStore();

        var modiefyGrid=store.getModifiedRecords(),
            removeGrid=store.getRemovedRecords();
        if (modiefyGrid!=0||removeGrid!=0)
        {
            Ext.Msg.alert('提示','请您先保存页面考生数据，再指定评委');
            return;
        }

        var form=btn.findParentByType('form').getForm();
        var dqpsStatus = form.findField("dqpsStatus").getValue();
        if(dqpsStatus=='A'){
            Ext.Msg.alert('提示','当前评审状态为进行中，不可为考生指定评委');
            return ;
        }

        var classId = form.findField('classId').value;
        var batchId =form.findField('batchId').value;
        var appinsId=store.getAt(rowIndex).data.appinsId;
        var judgeList=store.getAt(rowIndex).data.judgeList.split(',');

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_SETPW_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_SETPW_STD，请检查配置。');
            return;
        }

        var contentPanel, cmp, ViewClass, clsProto;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }

            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        cmp = new ViewClass({
            appRowIndex:appinsId,
            appSelList:""
        });

        cmp.on('afterrender',function(win){
            var grid= win.child('grid'),
                store =grid.getStore();

            var  tzStoreParams = '{"classId":"'+classId+'","batchId":"'+batchId+'","appinsId":"'+appinsId+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load();
        });
        cmp.show();
    },
    //材料评审考生-grid操作列-删除
    removeExamineeOne:function(view,rowIndex) {
        var form = view.findParentByType("grid").findParentByType("form").getForm();
        if(form.findField('dqpsStatus').getValue()=='A') {
            Ext.Msg.alert('提示','当前评审状态为进行中，不可移除考生');
            return ;
        }
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },"",true,this);
    },
    //材料评审考生-更多操作-导出选中考生评议数据
    exportExcel:function(btn) {
        var grid = this.getView().down("grid[name=materialsReviewExamineeGrid]");
        var selList = grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0) {
            Ext.Msg.alert("提示","请选择需要导出的考生记录");
            return;
        }

        var classId = selList[0].data.classId;
        var batchId = selList[0].data.batchId;

        var appinsIds = "";
        for(var i=0;i<checkLen;i++){
            if(i==0){
                appinsIds += '"' + selList[i].data.appinsId + '"';
            }else{
                appinsIds += ',"' + selList[i].data.appinsId + '"';
            }
        }

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD","OperateType":"tzExportExaminee","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'","appinsIds":['+appinsIds+']}}';
        Ext.tzSubmit(tzParams,function(respDate){
            var fileUrl = respDate.fileUrl;
            window.open(fileUrl);
        },"导出考生评议数据成功",true,this);
    },
    //材料评审考生-更多操作-导出查询考生评议数据
    exportAllExcel:function(btn) {
        var grid = this.getView().down("grid[name=materialsReviewExamineeGrid]");
        var store = grid.getStore();
        var data = store.getData();
        var dataLen = data.length;
        if(dataLen == 0) {
            Ext.Msg.alert("提示","当前查询无记录");
            return;
        }

        var classId = this.getView().classId;
        var batchId = this.getView().batchID;
        
        var totalCount = store.totalCount;
        
        var tzStoreParams = store.tzStoreParams;

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD","OperateType":"tzExportAllExaminee","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '","tzStoreParams":' + Ext.JSON.encode(tzStoreParams) + ',"totalCount":"' + totalCount + '"}}';
        Ext.tzSubmit(tzParams,function(respDate){
            var fileUrl = respDate.fileUrl;
            window.open(fileUrl);
        },"导出考生评议数据成功",true,this);
    },
    
    //材料评审考生-保存
    onExamineeSave:function(btn) {
        var me = this;
        var panel = btn.findParentByType("panel");
        var actType = panel.actType;
        var store = panel.down('grid[name=materialsReviewExamineeGrid]').store;
        var form = me.getView().child("form").getForm();

        if (form.isValid()) {
            var tzParams = this.getExamineeParams(actType);
            if(tzParams!="") {
                var comView = this.getView();

                Ext.tzSubmit(tzParams, function (responseData) {
                    if(actType=="add") {
                        panel.actType = "update";
                    }
                    var grid = btn.findParentByType('panel').down("grid[name=materialsReviewExamineeGrid]");
                    var store = grid.getStore();
                    if (tzParams.indexOf("add") > -1 || tzParams.indexOf("delete") > -1) {
                        store.reload();
                    }
                }, "", true, this);
            }
        }
    },
    //材料评审考生-确定
    onExamineeEnsure:function(btn) {
        var me =this;
        var panel = btn.findParentByType("panel");
        var actType = panel.actType;
        var store = panel.down('grid[name=materialsReviewExamineeGrid]').store;
        var form = me.getView().child("form").getForm();

        if (form.isValid()) {
            var tzParams = this.getExamineeParams(actType);
            if(tzParams!="") {
                var comView = this.getView();
                Ext.tzSubmit(tzParams,function(responseData){
                    if(actType=="add") {
                        panel.actType = "update";
                    }
                    var grid = btn.findParentByType('panel').down("grid[name=materialsReviewExamineeGrid]");
                    var store = grid.getStore();
                    if(tzParams.indexOf("delete")>-1){
                        store.reload();
                    }
                    comView.close()
                },"",true,this);
            }
        }
    },
    //材料评审考生-关闭
    onExamineeClose:function(btn) {
        this.getView().close();
    },
    //材料评审考生-保存获取参数
    getExamineeParams:function(actType) {
        var me = this,
            view = me.getView();

        var form = view.child("form").getForm();
        var classId = form.findField('classId').getValue(),
            batchId = form.findField('batchId').getValue();

        var formValues = form.getValues();

        var grid = view.down("grid[name=materialsReviewExamineeGrid]");

        var store = grid.getStore(),
            storeNumber = store.getCount();
        form.findField('clpsksNum').setValue(storeNumber);

        var comParams = "";

        var removeJson = "";
        var removedRecs = store.getRemovedRecords();

        for (var i = 0; i < removedRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removedRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removedRecs[i].data);
            }
        }

        if (removeJson != "") {
            if (comParams == "") {
                comParams = '"delete":[' + removeJson + ']';
            } else {
                comParams = comParams + ',"delete":[' + removeJson + ']';
            }
        }

        var editJson = "";
        editJson = '{"typeFlag":"RULE","data":' + Ext.JSON.encode(formValues) + '}';

        var editRecs = store.getModifiedRecords();
        for (var i = 0; i < editRecs.length; i++) {
            editJson = editJson + ',' + '{"typeFlag":"EXAMINEE","data":' + Ext.JSON.encode(editRecs[i].data) + '}'
        }

        if (editJson != "") {
            if (comParams == "") {
                comParams = '"' + actType + '":[' + editJson + ']';
            } else {
                comParams = comParams + ',"' + actType + '":[' + editJson + ']';
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD","OperateType":"U","comParams":{' + comParams + '}}';
        return tzParams;
    },
    
    
    /**
     * 导出选中的考生评议数据
     */
    exportSelectedExcel: function(btn){
    	var panel = btn.findParentByType('materialsReviewExaminee');
		var grid = panel.down('grid');
		var classId = panel.classId;
		var batchId = panel.batchID;
		
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要导出的考生记录");
			return;
		}
		
		var appInsIds = [];
	    for(var i=0;i<checkLen;i++){
	    	appInsIds.push(selList[i].data.appinsId);
		}
	    var comParamsObj = {
	    	ComID: "TZ_REVIEW_CL_COM",
			PageID: "TZ_CLPS_KS_STD",
			OperateType: "EXPORT",
			comParams: {
				classId: classId,
				batchId: batchId,
				appInsIds: appInsIds
			}
	    };
	   
	    var className = 'KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	exportObj: comParamsObj
        });
       
        win.show();
    },
    
    /**
     * 导出搜索结果中考生的评议数据
     */
    exportSearchExcel: function(btn){
    	var panel = btn.findParentByType('materialsReviewExaminee');
		var classId = panel.classId;
		var batchId = panel.batchID;
		
		//构造搜索sql
		if((typeof panel.getedSQL) == "undefined"){
			searchSql = "SELECT TZ_APP_INS_ID FROM PS_TZ_CLPS_KS_VW WHERE TZ_CLASS_ID='"+ classId +"' AND TZ_APPLY_PC_ID='"+ batchId +"'";
		}else{
			searchSql = panel.getedSQL;
		}
		
		var comParamsObj = {
			ComID: "TZ_REVIEW_CL_COM",
			PageID: "TZ_CLPS_KS_STD",
			OperateType: "EXPORT",
			comParams: {
				classId: classId,
				batchId: batchId,
				searchSql: searchSql
			}
		};
		
		
		var className = 'KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	exportObj: comParamsObj
        });
        
        win.show();
    },
    
    /**
     * 下载考生评议数据导出结果
     */
    downloadHisExcel: function(btn){
    	var panel = btn.findParentByType('materialsReviewExaminee');
		var classId = panel.classId;
		var batchId = panel.batchID;
		
		var className = 'KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelWindow';
    	
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	type: 'download'
        });
        
        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);
        
        win.show();
    }
});
