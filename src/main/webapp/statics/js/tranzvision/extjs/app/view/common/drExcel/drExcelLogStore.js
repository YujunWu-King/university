Ext.define('KitchenSink.view.common.drExcel.drExcelLogStore', {
    extend: 'Ext.data.Store',
    alias: 'store.drExcelLogStore',
    model: 'KitchenSink.view.common.drExcel.drExcelLogModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_FILTER_COM',
	pageID: 'TZ_EXCEL_LOG_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()	
});
