Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dcauditKjYtStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dcauditKjYtStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dcauditKjYtModel',
    pageSize:10,
    comID: 'TZ_ZXDC_KJGL_COM',
    pageID: 'TZ_DCKJGL_INFO_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
