Ext.define('KitchenSink.view.basicData.resData.theme.themeInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.themeInfoStore',
    model: 'KitchenSink.view.basicData.resData.theme.themeInfoModel',
    pageSize: 10,
    comID: 'TZ_GD_THEMEGL_COM',
    pageID: 'TZ_GD_THEME_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});

