Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dcauditKjJygzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dcauditKjJygzStore',
    model: 'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dcauditKjJygzModel',
    pageSize:10,
    comID: 'TZ_ZXDC_KJGL_COM',
    pageID: 'TZ_DCKJGL_INFO_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
