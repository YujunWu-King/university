Ext.define('KitchenSink.view.clueManagement.backgroundConfig.commonPhrase.defnCommonPhraseStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnCommonPhraseStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.commonPhrase.defnCommonPhraseModel',
    autoLoad:true,
    comID: 'TZ_XSXS_CYDY_COM',
    pageID: 'TZ_XSXS_CYDY_STD',
    // tzStoreParams:'{"cfgSrhId": "TZ_XSXS_CYDY_COM.TZ_XSXS_CYDY_STD.TZ_XSXS_CYDY_V"}',
    tzStoreParams:'{"cfgSrhId": "TZ_XSXS_CYDY_COM.TZ_XSXS_CYDY_STD.TZ_XSXS_CYDY_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
