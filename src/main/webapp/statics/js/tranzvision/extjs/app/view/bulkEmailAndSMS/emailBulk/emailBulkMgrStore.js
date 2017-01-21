Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkMgrStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_MGR_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.TZ_EMLQ_LIST_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
