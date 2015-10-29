/**
 * Created by carmen on 2015/9/8.
 */
Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.weekStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weekStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.weekModel',
    autoLoad:true,
    comID: 'TZ_ZXDC_JDBB_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
