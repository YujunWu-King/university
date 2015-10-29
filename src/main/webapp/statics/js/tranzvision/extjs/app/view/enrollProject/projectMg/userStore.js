//管理人员store
Ext.define('KitchenSink.view.enrollProject.projectMg.userStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userStore',
    model: 'KitchenSink.view.enrollProject.projectMg.userModel',
	autoLoad: true,
	pageSize: 5,
	comID: 'TZ_PRJ_PROMG_COM',
	pageID: 'TZ_PRJ_USER_STD',
	//tzStoreParams: '{"orgId":"'+Ext.tzOrgID+'"}',
	tzStoreParams: '{"cfgSrhId":"TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.TZ_ADMIN_YHXX_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/enrollProject/projectMg/user.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}
	*/
});
