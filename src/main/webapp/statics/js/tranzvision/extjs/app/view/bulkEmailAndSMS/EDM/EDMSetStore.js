Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.EDMSetStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_GK_EDM_COM',
    pageID:'TZ_GK_EDM_STD',
    tzStoreParams: '{"emailID":""}',
    proxy: Ext.tzListProxy()
});