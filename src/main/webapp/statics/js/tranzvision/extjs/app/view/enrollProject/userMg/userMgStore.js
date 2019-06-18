Ext.define('KitchenSink.view.enrollProject.userMg.userMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userMgStore',
    model: 'KitchenSink.view.enrollProject.userMg.userMgModel',
	autoLoad: true,
	pageSize: 500,
	comID: 'TZ_UM_USERMG_COM',
	pageID: 'TZ_UM_USERMG_STD',
	//tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.TZ_REG_USER_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.TZ_REG_USE2_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_IS_CMPL-operator": "01","TZ_IS_CMPL-value": "Y","ACCTLOCK-operator":"01","ACCTLOCK-value":"0"}}',
	proxy: Ext.tzListProxy(),
	remoteSort:true
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/enrollProject/userMg/userMgs.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
	*/	
});
