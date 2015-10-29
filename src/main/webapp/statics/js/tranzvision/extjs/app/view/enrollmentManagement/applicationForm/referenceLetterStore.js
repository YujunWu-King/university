Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.referenceLetterStore', {
    extend: 'Ext.data.Store',
    alias: 'store.referenceLetterStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.referenceLetterModel',
    pageSize:10,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_AUDIT_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
