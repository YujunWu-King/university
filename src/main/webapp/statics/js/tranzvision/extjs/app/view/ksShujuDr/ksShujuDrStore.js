Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ksShujuDrStore',
    model: 'KitchenSink.view.ksShujuDr.ksShujuDrModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_STUDATA_COM',
	pageID: 'TZ_DATALIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_STUDATA_COM.TZ_DATALIST_STD.TZ_IMP_TPL_DFN_T"}',
	proxy: Ext.tzListProxy()
});