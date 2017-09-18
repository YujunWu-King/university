Ext.define('KitchenSink.view.signIn.signInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.signInfoController',
    /*查询空白卡片*/
    selectSign:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_QDGL_COM.TZ_QDGL_STD.SIGN_INFO', //
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    deleteOneSign:function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //删除,从界面中删除，必须保存才能从数据库中删除
    deleteSign:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    //空白卡管理显示界面 关闭
    SignInfoWindowClose:function(btn){
    	var panel=btn.findParentByType("panel");
    	panel.close();
    },
    //空白卡管理显示界面 保存
    SignInfoWindowSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //console.log(comParams);
        var tzParams = '{"ComID":"TZ_QDGL_COM","PageID":"TZ_QDGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    }
});

