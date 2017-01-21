Ext.define('KitchenSink.view.template.survey.testQuestion.testWjdcStore', {
    extend: 'Ext.data.Store',
    alias: 'store.testWjdcStore',
    model: 'KitchenSink.view.template.survey.testQuestion.testWjdcModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_CSWJ_LIST_COM',
    pageID:'TZ_CSWJ_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_CSWJ_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
})
