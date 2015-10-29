Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.fileStore', {
    extend: 'Ext.data.Store',
    alias: 'store.fileStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.fileModel',
    pageSize:10,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
