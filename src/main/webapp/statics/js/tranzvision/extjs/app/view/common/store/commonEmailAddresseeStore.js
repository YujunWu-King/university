Ext.define('KitchenSink.view.common.store.commonEmailAddresseeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.commonEmailAddresseeStore',
    model: 'KitchenSink.view.common.store.commonEmailAddresseeModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_COMMON_EMAIL_COM',
		pageID: 'TZ_AUD_EMLSMS_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (audienceId) {
	     //交互参数
	     this.tzStoreParams =  '{"cfgSrhId":"TZ_COMMON_EMAIL_COM.TZ_AUD_EMLSMS_STD.TZ_AUD_EMLSMS_V","condition":{"TZ_AUDIENCE_ID-operator": "01","TZ_AUDIENCE_ID-value": "'+ audienceId +'"}}';
	     this.callParent();
	  }	
});
