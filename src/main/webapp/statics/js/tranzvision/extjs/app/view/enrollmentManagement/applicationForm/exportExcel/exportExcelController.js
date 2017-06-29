Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.exportExcel.exportExcelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.appFormExportExcel',
    exportEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        var form = win.child("form").getForm();
        if (form.isValid()) {
            var appFormModalID = win.modalID;
            var excelTpl = form.getValues()['excelTpl'];
            var excelName = form.getValues()['excelName']
            if(win.selList&&win.selList.length>0){
                this.doExportExcelAction(win,win.selList,appFormModalID,excelTpl,excelName);
            }else{
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.noApplicantsSelected","没有选中任何考生，无法导出Excel"));
            }

        }
    },
    doExportExcelAction: function(win,applicantsList,appFormModalID,excelTpl,excelName){
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_EXP_EXCEL_STD","OperateType":"U","comParams":{"add":[{"excelTpl":"'+excelTpl+'","appFormModalID":"'+appFormModalID+'","excelName":"'+excelName+'","applicantsList":'+Ext.JSON.encode(applicantsList)+'}]} }';

        var tabPanel = win.lookupReference("exportExcelTabPanel");
        Ext.tzSubmit(tzParams,function(responseData){
            tabPanel.setActiveTab(1);
			tabPanel.child("grid").store.reload();
            },"",true,this);
    },
    exportExcelWindowClose: function(btn){
        var win = btn.findParentByType("window");
        win.close();
    },
    queryExcel:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.TZ_GD_DCEXCEL_V',
            condition:
            {
                TZ_DLZH_ID: TranzvisionMeikecityAdvanced.Boot.loginUserId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
//    deleteExcel: function(btn){
//        var me=this;
//        var win = btn.findParentByType("window");
//        var tabPanel = win.lookupReference("exportExcelTabPanel");
//        //选中行
//        var selList = tabPanel.child("grid").getSelectionModel().getSelection();
//        //选中行长度
//        var checkLen = selList.length;
//        if(checkLen == 0){
//            Ext.Msg.alert("提示","请选择要删除的记录");
//            return;
//        }else{
//            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
//                if(btnId == 'yes'){
//                    var store = btn.findParentByType('grid').store;
//                    store.remove(selList);
//                }
//            },this);
//        }
//    },
    exportExcelWindowSave: function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            var comParams = '"delete":[' + removeJson + "]";
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_EXP_EXCEL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    downloadFile: function(grid, rowIndex){
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
       var viewUrl = record.get("fileUrl");
        if(viewUrl.length>0){
            window.open(viewUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
        }else{
            //do nothing
        }
    },
    //Tab2删除
    excelDelete: function(btn){
        var me=this;
        var win = btn.findParentByType("window");
        var tabPanel = win.lookupReference("exportExcelTabPanel");
        //选中行
        var selList = tabPanel.child("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.qxzyscdjl","请选择要删除的记录"));
            return;
        }else{
            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.qdscsxjlm","您确定要删除所选记录吗?"), function(btnId){
                if(btnId == 'yes'){
                    var store = btn.findParentByType('grid').store;
                    store.remove(selList);
                }
            },this);
        }
    },
    exportExcelSave: function(btn){
       //组件注册信息列表
        var grid = btn.findParentByType("grid");
        //组件注册信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        var comParams ="";
        if(removeJson != ""){
             comParams = '"delete":[' + removeJson + "]";
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_EXP_EXCEL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    }
});

