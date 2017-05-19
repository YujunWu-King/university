Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.appFormPackageController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.appFormPackage',
    qrZldb: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面打包表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.savePageRegInfo(win);
        }
    },
    savePageRegInfo: function(win){
        //页面打包表单
        var form = win.child("form").getForm();

        //表单数据
        var formParams = form.getValues();

        var strAppId = form.findField("appInsID").getValue();
        strAppId = strAppId.substring(1,strAppId.length-1);
        var zldbName = form.findField("ysFilesName").getValue();
        var packageType = form.findField("packageType").getValue();
        //提交参数
        if (strAppId!=""){
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_DBDL_STD","OperateType":"U","comParams":{"add":[{"strAppId":"'+strAppId+'","zldbName":"'+zldbName+'","packageType":"'+packageType+'"}]} }';

        var tabPanel = win.lookupReference("packageTabPanel");
        Ext.tzSubmit(tzParams,function(responseData){tabPanel.setActiveTab(1);},"",true,this);
        //0624 end
        }else{
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.myxydbdzl","没有需要打包的资料"));
        }
    },
    qxZldb: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表
        var form = win.child("form").getForm();
        win.close();
    },
    //Tab2删除
    db0Delete: function(btn){
        var me=this;
        var win = btn.findParentByType("window");
        var tabPanel = win.lookupReference("packageTabPanel");
        //选中行
        var selList = tabPanel.child("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.qxzyscdjl","请选择要删除的记录"));
            return;
        }else{
            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.qdscsxjlm","您确定要删除所选记录吗?"), function(btnId){
                if(btnId == 'yes'){
                    var store = btn.findParentByType('grid').store;
                    store.remove(selList);
                }
            },this);
        }
    },
    db0Save: function(btn){
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
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_DBDL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    downloadFile: function(grid, rowIndex){
        //Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var AEId=record.get("AEId");

        if(AEId!=""){
        	var viewUrl = TzUniversityContextPath+"/admission/exprar/"+AEId;
            window.open(viewUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
          }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.zbdfj","找不到附件"));
        }
    },
    /*Tab2 查询*/
    db0Query: function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_DBDL_STD.TZ_GD_ZLDL_V',
            condition:
            {
                OPRID: TranzvisionMeikecityAdvanced.Boot.loginUserId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});

