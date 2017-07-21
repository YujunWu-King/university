Ext.define('KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogStore', {
    extend: 'Ext.data.Store',
    alias:  'store.stuCertOprLogStore',
    model:  'KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_STUZS_LOG_COM',
    pageID:'TZ_STUZS_LOG_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_STUZS_LOG_COM.TZ_STUZS_LOG_STD.TZ_STUZS_LOG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy() 
})
