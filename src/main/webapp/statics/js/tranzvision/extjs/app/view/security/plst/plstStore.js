Ext.define('KitchenSink.view.security.plst.plstStore', {
    extend: 'Ext.data.Store',
    alias: 'store.plstStore',
    model: 'KitchenSink.view.security.plst.plstModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_AQ_PLST_COM',
    pageID: 'TZ_AQ_PLST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_AQ_PLST_STD.PSCLASSDEFN_SRC"}',
    proxy: Ext.tzListProxy()
});
