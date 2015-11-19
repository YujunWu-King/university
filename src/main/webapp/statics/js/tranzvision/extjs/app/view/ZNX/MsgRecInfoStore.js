Ext.define('KitchenSink.view.ZNX.MsgRecInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.MsgRecInfoStore',
    model: 'KitchenSink.view.ZNX.MsgRecInfoModel',
  //  pageSize:10,
    autoLoad:true,
    comID: 'TZ_ZNX_COM',
    pageID: 'TZ_ZNX_RECINFO_STD',
    tzStoreParams:'{}',
    proxy: Ext.tzListProxy()
});

