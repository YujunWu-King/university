Ext.define('KitchenSink.view.audienceManagement.audienceManagementStore', {
    extend: 'Ext.data.Store',
    alias: 'store.audStore',
    model: 'KitchenSink.view.audienceManagement.audienceManagementModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_AUD_COM',
	pageID: 'TZ_AUD_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_LIST_STD.PS_TZ_AUDCX_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	/*proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/security/com/coms.json',
		reader: {
			type: 'json',
			rootProperty: ''
		}
	}*/
	proxy: Ext.tzListProxy()
});
