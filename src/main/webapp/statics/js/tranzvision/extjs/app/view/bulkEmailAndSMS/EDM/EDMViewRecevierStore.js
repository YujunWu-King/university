Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMViewRecevierStore', {
    extend: 'Ext.data.Store',
    alias: 'store.EDMViewRecevierStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.EDM.EDMViewRecevierModel',
    comID: 'TZ_GK_EDM_COM',
    pageID:'TZ_EDM_VIEWRY_STD',
    pageSize: 10,
    autoLoad: true,
    tzStoreParams:  '{"emailID":" "}',
    proxy: Ext.tzListProxy()
});
