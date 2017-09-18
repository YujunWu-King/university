Ext.define('KitchenSink.view.basicData.filter.FldDataSetCondStore', {
    extend: 'Ext.data.Store',
    alias: 'store.FldDataSetCondStore',
    model: 'KitchenSink.view.basicData.filter.FldDataSetCondModel',
    autoLoad: true,
    pageSize: 15,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FLDDST_FLD_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});