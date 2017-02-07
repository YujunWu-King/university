Ext.define('KitchenSink.view.blackListManage.blackMg.blackMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.blackMgStore',
    model: 'KitchenSink.view.blackListManage.blackMg.blackMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BLACK_LIST_COM',
	pageID: 'TZ_BLACK_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BLACK_LIST_COM.TZ_BLACK_LIST_STD.TZ_BLACK_LIST_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
//	tzStoreParams: '{"cfgSrhId":"TZ_BLACK_LIST_COM.TZ_BLACK_LIST_STD.TZ_BLACK_LIST_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+'SEM'+'"}}',
	proxy: Ext.tzListProxy()
});
