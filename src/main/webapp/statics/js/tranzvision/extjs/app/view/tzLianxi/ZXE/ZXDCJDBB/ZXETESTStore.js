/**
 * Created by carmen on 2015/9/9.
 */
Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.ZXETESTStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ZXETESTStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.ZXETESTModel',
    autoLoad:true,
    comID: 'TZ_ZXDC_JDBB_COM',
    pageID: 'TZ_ZXE_TEST_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
