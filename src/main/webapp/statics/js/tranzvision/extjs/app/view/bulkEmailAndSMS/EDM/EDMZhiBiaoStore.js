Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.EDMZhiBiaoStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_GK_EDM_COM',
    pageID:'TZ_EDM_CKITEM_STD',
    tzStoreParams:'',
   // tzStoreParams: '{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_FSZS_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": ""}}',
    proxy: Ext.tzListProxy()
})
