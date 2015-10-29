Ext.define('KitchenSink.view.common.drExcel.drExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.drExcelStore',
    model: 'KitchenSink.view.common.drExcel.drExcelModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_IMPORTEXCEL_STD',
    tzStoreParams:  '{"cfgSrhId":"TZ_GD_FILTER_COM.TZ_IMPORTEXCEL_STD.TZ_EXCEL_DRXX_V","condition":{}}',
    proxy: Ext.tzListProxy()
});

