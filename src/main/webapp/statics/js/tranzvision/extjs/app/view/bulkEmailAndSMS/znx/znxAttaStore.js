Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxAttaStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxAttaStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.znxAttaModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZNX_GL_COM',
    pageID: 'TZ_ZNX_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
