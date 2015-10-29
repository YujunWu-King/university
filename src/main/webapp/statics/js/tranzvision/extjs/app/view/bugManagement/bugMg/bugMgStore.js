Ext.define('KitchenSink.view.bugManagement.bugMg.bugMgStore',{
    extend:'Ext.data.Store',
    alias:"store.bugMgStore",
    model:'KitchenSink.view.bugManagement.bugMg.bugModel',
    autoLoad: true,
//    groupField: 'module',
    pageSize: 300,
    comID:"TZ_BUGMG_COM",
    pageID:"TZ_BUG_LIST_STD",
    sortInfo: {field: 'bugID', direction: 'DESC'},
    tzStoreParams:'{"cfgSrhId": "TZ_BUGMG_COM.TZ_BUG_LIST_STD.TZ_BUG_QUERY_VW","condition":{"TZ_BUG_STA-operator": "10","TZ_BUG_STA-value": ["0","1","2","4","6"]}}',
    proxy:Ext.tzListProxy()
});
