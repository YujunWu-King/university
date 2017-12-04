Ext.define('KitchenSink.view.uniPrint.uniPrintTplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.uniPrintTplStore',
    model: 'KitchenSink.view.uniPrint.uniPrintTplModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_DYMB_COM',
	pageID: 'TZ_DYMB_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_DYMB_COM.TZ_DYMB_LIST_STD.TZ_DYMB_VW"}',
	proxy: Ext.tzListProxy()
});
