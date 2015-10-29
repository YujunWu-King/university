Ext.define('KitchenSink.view.common.store.commonSmsAddresseeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.commonSmsAddresseeStore',
    model: 'KitchenSink.view.common.store.commonSmsAddresseeModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_COMMON_SMS_COM',
		pageID: 'TZ_AUD_SMS_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (audienceId) {
	     //交互参数
	     this.tzStoreParams =  '{"cfgSrhId":"TZ_COMMON_SMS_COM.TZ_AUD_SMS_STD.TZ_AUD_EMLSMS_V","condition":{"TZ_AUDIENCE_ID-operator": "01","TZ_AUDIENCE_ID-value": "'+ audienceId +'"}}';
	     this.callParent();
	  }	
});
