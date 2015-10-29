Ext.define('KitchenSink.view.activity.activityListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.activityListStore',
    model: 'KitchenSink.view.activity.activityListModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_HD_MANAGER_COM',
		pageID: 'TZ_HD_MANAGER_STD',
		tzStoreParams: '{"cfgSrhId":"TZ_HD_MANAGER_COM.TZ_HD_MANAGER_STD.TZ_GD_HDCFG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
		proxy: Ext.tzListProxy()
		/*
		proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/activities.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
		}	
		*/
});
