Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dckjStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_ZXDC_KJGL_COM',
    pageID: 'TZ_DCKJGL_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_ZXDC_KJGL_COM.TZ_DCKJGL_LIST_STD.TZ_DCKJ_VW"}',
    proxy: Ext.tzListProxy()
});
