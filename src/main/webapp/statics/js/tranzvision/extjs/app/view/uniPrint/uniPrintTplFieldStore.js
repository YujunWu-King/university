Ext.define('KitchenSink.view.uniPrint.uniPrintTplFieldStore', {
    extend: 'Ext.data.Store',
    alias: 'store.uniPrintTplFieldStore',
    model: 'KitchenSink.view.uniPrint.uniPrintTplFieldModel',
	autoLoad: false,
	pageSize: 0,
	comID: 'TZ_DYMB_COM',
	pageID: 'TZ_DYMB_INF_STD',
	proxy: Ext.tzListProxy()
});
