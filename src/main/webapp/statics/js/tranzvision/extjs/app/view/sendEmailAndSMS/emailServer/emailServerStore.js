Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServerStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailServerStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_EMLSER_MG_COM',
    pageID: 'TZ_EMLSER_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.TZ_EMLS_DEF_VW","condition":{"TZ_JG_ID_1-operator": "01","TZ_JG_ID_1-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/sendEmailAndSMS/emailServer/emailServer.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
	*/
});
