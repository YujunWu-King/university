Ext.define('KitchenSink.view.bugManagement.bugCk.bugCkStore',{
    extend:'Ext.data.Store',
    model:'KitchenSink.view.bugManagement.bugCk.bugModel',
    alias:"store.bugCkStore",
    autoLoad: true,
    comID:"TZ_BUGCK_COM",
    pageID:"TZ_BUG_LIST_STD1",
    tzStoreParams: '{}',
    proxy:Ext.tzListProxy(),
    pageSize:800
});
