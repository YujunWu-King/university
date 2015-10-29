/**
 * Created by carmen on 2015/9/7.
 */
Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.formStore', {
    extend: 'Ext.data.Store',
    alias: 'store.formStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.formModel',
    autoLoad:true,
    comID: 'TZ_ZXDC_JDBB_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    //tzStoreParams:'{"cfgSrhId": "TZ_ZXDC_JDBB_COM.TZ_ZXDC_JDBB_STD.TZ_DCJYGZ_VW"}',
    proxy: Ext.tzListProxy()
});

