Ext.define('KitchenSink.view.basicData.export.exportTplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportTplStore',
    model: 'KitchenSink.view.basicData.export.exportTplModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_EXP_TPL_COM',
	pageID: 'TZ_TPL_LST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_EXP_TPL_COM.TZ_TPL_LST_STD.TZ_EXP_TPL_DFN_T"}',
	proxy: Ext.tzListProxy()
});
