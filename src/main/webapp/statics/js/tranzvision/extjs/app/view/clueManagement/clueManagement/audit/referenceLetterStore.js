Ext.define('KitchenSink.view.clueManagement.clueManagement.audit.referenceLetterStore', {
    extend: 'Ext.data.Store',
    alias: 'store.referenceLetterStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.audit.referenceLetterModel',
    pageSize:10,
	comID: 'TZ_XSXS_INFO_COM',
	pageID: 'TZ_BMR_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
