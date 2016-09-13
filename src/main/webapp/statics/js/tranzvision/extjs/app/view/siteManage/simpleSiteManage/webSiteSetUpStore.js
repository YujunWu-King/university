Ext.define('KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpStore', {
    extend: 'Ext.data.Store',
    alias: 'store.webSiteSetUpStore',
    model: 'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpMode',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_WEBSIT_SET_COM',
	pageID: 'TZ_WEBSIT_SET_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_WEBSIT_SET_COM.TZ_WEBSIT_SET_STD.TZ_WEBSIT_SET_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
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
