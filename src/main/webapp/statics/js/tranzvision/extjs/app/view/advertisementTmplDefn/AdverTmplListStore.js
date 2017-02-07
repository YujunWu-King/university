Ext.define('KitchenSink.view.advertisementTmplDefn.AdverTmplListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.advertisementTmplDefn.AdverTmplListModel',
    pageSize: 5,
    autoLoad:true,
    comID: 'TZ_AD_TMPL_COM',
    pageID: 'TZ_AD_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_AD_TMPL_COM.TZ_AD_LIST_STD.TZ_ADTMPL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
