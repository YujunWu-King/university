Ext.define('KitchenSink.view.bugManagement.bugProg.bugProgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bugProgStore',
    model: 'KitchenSink.view.bugManagement.bugProg.bugProgModel',
    pageSize: 10,
    comID: "TZ_BUGMG_COM",
    pageID: 'TZ_BUG_PROG_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});