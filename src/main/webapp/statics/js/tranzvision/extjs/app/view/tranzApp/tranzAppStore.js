Ext.define('KitchenSink.view.tranzApp.tranzAppStore', {
    extend: 'Ext.data.Store',
    alias: 'store.tranzAppStore',
    model: 'KitchenSink.view.tranzApp.tranzAppModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_TRANZAPP_COM',
	pageID: 'TZ_GD_TAPP_MNG_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_GD_TRANZAPP_COM.TZ_GD_TAPP_MNG_STD.TZ_TRANZ_APP_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
