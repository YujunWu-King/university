Ext.define('KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnAreaLabelStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabelModel',
    autoLoad:true,
    comID: 'TZ_XSXS_DQBQ_COM',
    pageID: 'TZ_XSXS_DQBQ_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_XSXS_DQBQ_COM.TZ_XSXS_DQBQ_STD.TZ_XSXS_DQBQ_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
