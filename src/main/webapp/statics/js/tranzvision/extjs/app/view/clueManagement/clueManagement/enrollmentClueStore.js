Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClueStore',{
    extend:'Ext.data.Store',
    alias:'store.enrollmentClueStore',
    model:'KitchenSink.view.clueManagement.clueManagement.enrollmentClueModel',
    pageSize:20,
    autoLoad: true,
    comID:'TZ_XSXS_ZSXS_COM',
    pageID:'TZ_XSXS_ZSXS_STD',
    tzStoreParams:'',
    proxy:Ext.tzListProxy()
});