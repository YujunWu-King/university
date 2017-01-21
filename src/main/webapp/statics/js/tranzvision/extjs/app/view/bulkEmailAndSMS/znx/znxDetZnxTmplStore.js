Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxTmplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxBulkDetZnxTmplStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxTmplModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZNX_GL_COM',
    pageID: 'TZ_ZNX_DET_STD',
    tzStoreParams: '{"queryID": "znxtmpl"}',
    proxy: Ext.tzListProxy()
});
