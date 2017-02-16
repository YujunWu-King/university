Ext.define('KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ckzlManagementStore',
    model: 'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_ZDCS_CKZL_COM',
	pageID: 'TZ_CKZL_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_ZDCS_CKZL_COM.TZ_CKZL_MG_STD.TZ_CKZL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
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
