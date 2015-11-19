Ext.define('KitchenSink.view.template.bmb.myBmbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.myBmbStore',
    model: 'KitchenSink.view.template.bmb.myBmbModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_ONLINE_REG_COM',
    pageID: 'TZ_ONREG_MNG_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ONLINE_REG_COM.TZ_ONREG_MNG_STD.TZ_APPTPL_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});