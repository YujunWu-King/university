Ext.define('KitchenSink.view.clueManagement.clueManagement.audit.fileStore', {
    extend: 'Ext.data.Store',
    alias: 'store.fileStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.audit.fileModel',
    pageSize:10,
	comID: 'TZ_XSXS_INFO_COM',
	pageID: 'TZ_BMR_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
