Ext.define('KitchenSink.view.enrollProject.projectMg.projectMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.projectMgStore',
    model: 'KitchenSink.view.enrollProject.projectMg.projectMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PRJ_PROMG_COM',
	pageID: 'TZ_PRJ_PROMG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.TZ_PRJ_PROMG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/enrollProject/projectMg/projectMg.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}
	*/
});
