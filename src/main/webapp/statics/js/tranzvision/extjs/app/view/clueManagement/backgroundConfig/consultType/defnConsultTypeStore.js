Ext.define('KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnConsultTypeStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeModel',
    autoLoad:true,
    comID: 'TZ_XSXS_ZXLB_COM',
    pageID: 'TZ_XSXS_ZXLB_STD',
    // tzStoreParams:'{"cfgSrhId": "TZ_XSXS_ZXLB_COM.TZ_XSXS_ZXLB_STD.TZ_XSXS_ZXLB_V"}',
    tzStoreParams:'{"cfgSrhId": "TZ_XSXS_ZXLB_COM.TZ_XSXS_ZXLB_STD.TZ_XSXS_ZXLB_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
