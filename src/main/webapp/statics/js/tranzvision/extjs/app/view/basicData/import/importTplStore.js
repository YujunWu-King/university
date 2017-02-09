Ext.define('KitchenSink.view.basicData.import.importTplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.importTplStore',
    model: 'KitchenSink.view.basicData.import.importTplModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_IMP_TPL_COM',
	pageID: 'TZ_TPL_LST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_IMP_TPL_COM.TZ_TPL_LST_STD.TZ_IMP_TPL_DFN_T"}',
	proxy: Ext.tzListProxy()
});
