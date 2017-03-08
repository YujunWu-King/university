Ext.define('KitchenSink.view.enrollProject.userMg.userMgAudStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userMgAudStore',
    model: 'KitchenSink.view.audienceManagement.newAudWindowModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_AUD_COM',
	pageID: 'TZ_AUD_NEW_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{}}',
//	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_LIST_STD.PS_TZ_AUDCX_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
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
