Ext.define('KitchenSink.view.basicData.resData.import.importTplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.importTplStore',
    model: 'KitchenSink.view.basicData.resData.import.importTplModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_IMP_TPL_COM',
		pageID: 'TZ_TPL_LST_STD',
		tzStoreParams: '{"cfgSrhId":"TZ_IMP_TPL_COM.TZ_TPL_LST_STD.TZ_IMP_DFN_TBL"}',
		proxy: Ext.tzListProxy()
});
