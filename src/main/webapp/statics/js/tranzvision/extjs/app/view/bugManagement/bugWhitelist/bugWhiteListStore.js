Ext.define('KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListStore',{
    extend:'Ext.data.Store',
    model:'KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListModel',
    alias:"store.bugWhiteListStore",
    autoLoad: true,
    comID:"TZ_BUG_WHITE_COM",
    pageID:"TZ_BUG_WHITE_STD",
    tzStoreParams:'{"cfgSrhId": "TZ_BUG_WHITE_COM.TZ_BUG_WHITE_STD.TZ_BUGMG_WLS_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy:Ext.tzListProxy(),
    pageSize:10,
    listeners:{
        update:function(store){
            console.log(arguments);
        },
        load:function(){
            console.log(arguments);
        }
    }
});
