Ext.define('KitchenSink.view.template.survey.question.wjdcStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wjdcStore',
    model: 'KitchenSink.view.template.survey.question.wjdcModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZXDC_WJGL_COM',
    pageID:'TZ_ZXDC_WJGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_ZXDC_WJ_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
})
