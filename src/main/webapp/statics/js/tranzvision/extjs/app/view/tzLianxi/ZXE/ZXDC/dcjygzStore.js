Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dcjygzStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_ZXDC_JYGZ_COM',
    pageID: 'TZ_DCJYGZ_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_ZXDC_JYGZ_COM.TZ_DCJYGZ_LIST_STD.TZ_DCJYGZ_VW"}',
    proxy: Ext.tzListProxy()
});
