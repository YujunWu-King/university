Ext.define('KitchenSink.view.template.survey.question.wjdcPeopleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wjdcPeopleStore',
    model: 'KitchenSink.view.template.survey.question.wjdcPeopleModel',
    autoLoad: true,
    pageSize: 10, 
    comID:'TZ_ZXDC_WJGL_COM',
    pageID:'TZ_ZXDC_PERSON_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZXDC_WJGL_COM.TZ_ZXDC_PERSON_STD.TZ_ZXDC_CYR_VW","condition":{"TZ_DC_WJ_ID-operator": "01","TZ_DC_WJ_ID-value": ""}}', 
    proxy: Ext.tzListProxy()  
}) 
