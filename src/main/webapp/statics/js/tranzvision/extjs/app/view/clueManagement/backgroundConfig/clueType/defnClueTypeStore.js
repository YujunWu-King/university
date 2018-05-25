Ext.define('KitchenSink.view.clueManagement.backgroundConfig.clueType.defnClueTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnClueTypeStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.clueType.defnClueTypeModel',
    autoLoad:true,
    comID: 'TZ_XSXS_XSLB_COM',
    pageID: 'TZ_XSXS_XSLB_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_XSXS_XSLB_COM.TZ_XSXS_XSLB_STD.TZ_XSXS_XSLB_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
