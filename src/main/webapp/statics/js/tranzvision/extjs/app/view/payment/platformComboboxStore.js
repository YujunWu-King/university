Ext.define('KitchenSink.view.payment.platformStore', {
    extend: 'Ext.data.Store',
    alias: 'store.platformStore',
    model: 'KitchenSink.view.payment.platformModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZFPTGL_COM',
    pageID:'TZ_ZFPTGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZFPTGL_COM.TZ_ZFPTGL_STD","OperateType":"EJSON"}',
    proxy: Ext.tzListProxy()
})