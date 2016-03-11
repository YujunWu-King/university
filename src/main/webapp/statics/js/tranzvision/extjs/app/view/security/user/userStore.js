Ext.define('KitchenSink.view.security.user.userStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userStore',
    model: 'KitchenSink.view.security.user.userModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_AQ_YHZHGL_COM',
	pageID: 'TZ_AQ_YHZHGL_STD',
//	tzStoreParams: '{"cfgSrhId":"TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.TZ_YHZH_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	tzStoreParams: '{"cfgSrhId":"TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.TZ_YHZH_VW"}',
	proxy: Ext.tzListProxy(),
	constructor: function () {
		  if( (Ext.tzOrgID).toUpperCase() == 'ADMIN'){
		  	 this.tzStoreParams = '{"cfgSrhId":"TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.TZ_YHZH_VW"}';
		  }else{
		  	 this.tzStoreParams ='{"cfgSrhId":"TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.TZ_YHZH_NB_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}';
		  }

		  this.callParent();
	}
});
