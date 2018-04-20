Ext.define('KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnReasonOfReturnStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturnModel',
    autoLoad:true,
    comID: 'TZ_THYY_XSGL_COM',
    pageID: 'TZ_THYY_XSGL_STD',
    // tzStoreParams:'{"cfgSrhId": "TZ_THYY_XSGL_COM.TZ_THYY_XSGL_STD.TZ_THYY_XSGL_V"}',
    tzStoreParams:'{"cfgSrhId": "TZ_THYY_XSGL_COM.TZ_THYY_XSGL_STD.TZ_THYY_XSGL_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
