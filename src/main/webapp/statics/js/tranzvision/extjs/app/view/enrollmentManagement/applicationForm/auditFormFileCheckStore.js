Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.auditFormFileCheckStore', {
    extend: 'Ext.data.Store',
    alias: 'store.auditFormFileCheckStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.auditFormFileCheckModel',
    pageSize:10,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
