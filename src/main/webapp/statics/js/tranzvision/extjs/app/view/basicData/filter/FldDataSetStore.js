Ext.define('KitchenSink.view.basicData.filter.FldDataSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.FldDataSetStore',
    model: 'KitchenSink.view.basicData.filter.FldDataSetModel',
    autoLoad: true,
    pageSize: 15,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FILTER_DEFN_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});