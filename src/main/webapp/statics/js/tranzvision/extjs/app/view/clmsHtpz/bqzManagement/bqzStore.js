Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bzqStore',
    model: 'KitchenSink.view.clmsHtpz.bqzManagement.bqzModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BIAOQZ_COM',
	pageID: 'TZ_BIAOQZ_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BIAOQZ_COM.TZ_BIAOQZ_MG_STD.PS_TZ_BIAOQZ_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
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
