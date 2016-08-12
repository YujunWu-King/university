Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMTxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.EDMTxStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.EDM.EDMTxModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_GK_EDM_COM',
    pageID: 'TZ_VIEW_TDRZ_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.TZ_YJQFTXRZ_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": ""}}',
    proxy: Ext.tzListProxy()
});

