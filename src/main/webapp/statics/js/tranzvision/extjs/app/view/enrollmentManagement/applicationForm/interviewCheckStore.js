Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewCheckStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.interviewCheckModel',
	autoLoad: false,
    pageSize: 200,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_INTERVIEW_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
