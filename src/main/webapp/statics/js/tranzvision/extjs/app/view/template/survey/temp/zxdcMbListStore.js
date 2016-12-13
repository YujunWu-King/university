Ext.define('KitchenSink.view.template.survey.temp.zxdcMbListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.zxdcMbListStore',
    model: 'KitchenSink.view.template.survey.temp.zxdcMbModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZXDC_MBGL_COM',
    pageID:'TZ_ZXDC_MBGL_STD',
   // tzStoreParams:  '{"cfgSrhId":"TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBGL_STD.TZ_ZXDC_MB_VW"}',
    tzStoreParams: '{"cfgSrhId":"TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBGL_STD.TZ_ZXDC_MB_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
})
