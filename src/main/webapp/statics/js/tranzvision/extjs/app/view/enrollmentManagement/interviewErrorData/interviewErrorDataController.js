//面试异常数据处理    控制层，所有的页面公用控制层
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewErrorDataController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.errorDataController',
    
    //关闭 默认首页
    onSyClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    
    //关闭异常数据信息列表
    onErrorDataClose: function(btn) {
		var comView = btn.findParentByType("msInfoPanel");
		comView.close();
	},
    
    // 默认页面可配置查询
    queryClassList:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MSYCSJ_COM.TZ_MSYCSJLIST_STD.TZ_BMBSH_ECUST_VW',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID:Ext.tzOrgID,
                TZ_IS_APP_OPEN:'Y'
        },
        callback: function(seachCfg){
            var store = btn.findParentByType("grid").store;
            store.tzStoreParams = seachCfg;
            store.load();
        }
        });
    },
    
    //显示该班级批次下的异常面试信息列表
    errorDataShow:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSYCSJ_COM"]["TZ_MSYCSJINFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSYCSJ_COM.TZ_MSYCSJINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_MSYCSJ_COM.TZ_MSYCSJINFO_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSYCSJ_COM.TZ_MSYCSJINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_MSYCSJ_COM.TZ_MSYCSJINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        //console.log(className);
        ViewClass = Ext.ClassManager.get(className);
        //console.log(ViewClass);
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var className=record.data.className;
        var batchName=record.data.batchName;
        var jgid=Ext.tzOrgID;

        cmp = new ViewClass({
                    classID:classID,
                    batchID:batchID,
                    className:className,
                    batchName:batchName,
                    jgid:jgid
                }
           );
        cmp.on('afterrender',function(panel){
            console.log("1111");
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_MSYCSJ_COM","PageID":"TZ_MSYCSJINFO_STD",' + '"OperateType":"QF","comParams":{"classId":"'+classID+'","batchId":"'+batchID+'"}}';
            var panelGrid = panel.down('grid');

            var tzStoreParams = '{"classID": "' + classID + '","batchID": "' + batchID + '"}';
            panelGrid.store.tzStoreParams = tzStoreParams;

            Ext.tzLoad(tzParams,function(){
                var formData = {"className":className,"batchName":batchName,"classID":classID,"batchID":batchID,"jgid":jgid};
                if(formData!="" && formData!=undefined) {
                    form.setValues(formData);
                    panelGrid.store.load();
                }
            });
        });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },

    //删除异常数据
    delErrorData:function(grid, rowIndex, colIndex){
        //var record = grid.store.getAt(rowIndex);
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                grid.store.removeAt(rowIndex);
            }
        },this);
    },

    //批量删除异常数据
    deleteSelected:function(btn){
        var msErrorDataGrid = btn.up('grid');
        var msErrorDataGridSelectedRecs=msErrorDataGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = msErrorDataGridSelectedRecs.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要批量删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    msErrorDataGrid.store.remove(msErrorDataGridSelectedRecs);
                }
            },this);
        }
    },

    onErrorDataSave:function(btn){
        var msArrPanel = btn.up('panel');
        var msArrForm = msArrPanel.child('form');

        if(msArrForm.isValid()){
            var msArrFormRec = msArrForm.getForm().getValues();

            var classID = msArrFormRec["classID"];
            var batchId = msArrFormRec["batchID"];


            var msArrGrid = msArrPanel.child('grid');
            var msArrGridAllRecs = msArrGrid.store.getRange();
            var msArrGridRemovedRecs;
            msArrGridRemovedRecs= msArrGrid.store.getRemovedRecords();

            var comParams="";
            //删除记录
            var removeJson="";
            for(var i=0;i<msArrGridRemovedRecs.length;i++){
                if(removeJson == ""){
                    removeJson = Ext.JSON.encode(msArrGridRemovedRecs[i].data);
                }else{
                    removeJson = removeJson + ','+Ext.JSON.encode(msArrGridRemovedRecs[i].data);
                }
            }
            if(removeJson != ""){
                if(comParams==""){
                    comParams ='"removerecs":[' + removeJson + ']';
                }else{
                    comParams =comParams+ ',"removerecs":[' + removeJson + ']';
                }
            }else{
                comParams ='"removerecs":[]';
            }

            var tzParams = '{"ComID":"TZ_MSYCSJ_COM","PageID":"TZ_MSYCSJINFO_STD","OperateType":"deleteErrorData","comParams":{'+comParams+'}}';

            Ext.tzSubmit(tzParams,function(responseData){
                if(responseData.result=='success'){
                    Params= '{"classID":"'+classID+'","batchID":"'+batchId+'"}';
                    msArrGrid.store.tzStoreParams = Params;
                    msArrGrid.store.reload();
                };
            },"",true,this);
        }
    },
    onErrorDataEnsure: function(btn){
        this.onErrorDataSave(btn);
        this.onErrorDataClose(btn);
    },
});

