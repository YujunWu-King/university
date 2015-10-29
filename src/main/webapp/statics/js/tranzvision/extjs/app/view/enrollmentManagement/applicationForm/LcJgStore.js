Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.LcJgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.LcJgStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.LcJgModel',
    autoLoad: false,
    pageSize: 0,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_AUDIT_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});

