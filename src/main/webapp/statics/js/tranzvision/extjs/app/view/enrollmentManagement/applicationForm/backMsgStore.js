Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.backMsgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.classBackMsgStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.backMsgModel',
	pageSize: 10,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_BKMSG_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});