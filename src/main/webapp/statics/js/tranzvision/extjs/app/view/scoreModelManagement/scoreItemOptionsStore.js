Ext.define('KitchenSink.view.scoreModelManagement.scoreItemOptionsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.scoreItemOptionsStore',
    model: 'KitchenSink.view.scoreModelManagement.scoreItemOptionsModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_SCORE_MOD_COM',
	pageID: 'TZ_TREE_NODE_STD ',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
