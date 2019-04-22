Ext.define('KitchenSink.view.viewByPro.proListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.proListStore',
    model: 'KitchenSink.view.viewByPro.proListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BY_PRO_COM',
	pageID: 'TZ_PRO_LIST_PAGE',
	tzStoreParams: '{"cfgSrhId":"TZ_BY_PRO_COM.TZ_PRO_LIST_PAGE.TZ_PRJ_PROMG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
