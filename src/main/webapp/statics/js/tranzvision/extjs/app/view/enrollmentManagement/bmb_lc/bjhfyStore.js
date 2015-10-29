Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bjhfyStore',
    model: 'KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMSH_HFY_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
