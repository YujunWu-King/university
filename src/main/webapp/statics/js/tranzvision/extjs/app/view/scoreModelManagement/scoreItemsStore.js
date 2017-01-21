Ext.define('KitchenSink.view.scoreModelManagement.scoreItemsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.scoreItemsStore',
    model: 'KitchenSink.view.scoreModelManagement.scoreItemsModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_SCORE_MOD_COM',
	pageID: 'TZ_SCRMOD_DEFN_STD ',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
