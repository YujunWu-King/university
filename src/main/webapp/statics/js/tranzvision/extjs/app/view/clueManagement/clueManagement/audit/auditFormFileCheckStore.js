Ext.define('KitchenSink.view.clueManagement.clueManagement.audit.auditFormFileCheckStore', {
    extend: 'Ext.data.Store',
    alias: 'store.auditFormFileCheckStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.audit.auditFormFileCheckModel',
    pageSize:10,
	comID: 'TZ_XSXS_INFO_COM',
	pageID: 'TZ_BMR_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
