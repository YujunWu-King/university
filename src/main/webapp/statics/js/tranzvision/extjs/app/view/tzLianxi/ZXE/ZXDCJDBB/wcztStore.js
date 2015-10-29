Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.wcztStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wcztStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.wcztModel',
    autoLoad:true,
    comID: 'TZ_ZXDC_JDBB_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
