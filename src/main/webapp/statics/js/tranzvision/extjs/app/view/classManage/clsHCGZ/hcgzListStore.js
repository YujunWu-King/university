Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.hcgzListStore',
    model: 'KitchenSink.view.classManage.clsHCGZ.hcgzListModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_CLS_HCGZ_COM',
    pageID: 'TZ_CLS_HCGZ_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.TZ_CLS_HCGZ_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
