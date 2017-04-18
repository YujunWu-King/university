Ext.define('KitchenSink.view.template.survey.testQuestion.testWjXxxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.testWjXxxStore',
    model: 'KitchenSink.view.template.survey.testQuestion.testWjXxxModal',
    //autoLoad: true,
    pageSize: 10,
    comID:'TZ_CSWJ_LIST_COM',
    pageID:'TZ_CSWJ_DETAIL_STD',
    //tzStoreParams: '{"cfgSrhId":"TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.TZ_CSWJ_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}'
    tzStoreParams: '{}', 
    proxy: Ext.tzListProxy()
})
