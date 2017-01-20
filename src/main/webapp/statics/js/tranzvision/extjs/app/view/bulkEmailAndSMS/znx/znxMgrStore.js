Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxMgrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxMgrStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.znxMgrModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_ZNX_GL_COM',
    pageID: 'TZ_ZNX_GL_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.TZ_ZNXQF_LIST_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
