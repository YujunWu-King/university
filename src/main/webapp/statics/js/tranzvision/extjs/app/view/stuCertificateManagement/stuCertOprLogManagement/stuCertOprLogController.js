Ext.define('KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.stuCertOprLogController',
    //查询
    queryList:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_STUZS_LOG_COM.TZ_STUZS_LOG_STD.TZ_STUZS_LOG_VW', 
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
    //关闭
    closeList:function(){
        this.getView().close();
     }
    
});
