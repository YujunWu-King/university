Ext.define('KitchenSink.view.scholarShipManage.scholarStore', {
    extend: 'Ext.data.Store',
    alias:  'store.scholarStore',
    model: 'KitchenSink.view.scholarShipManage.scholarModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_SCHOLAR_COM',
    pageID:'TZ_SCHLR_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_SCHLR_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
})
