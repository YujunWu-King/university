Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsReplyStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyModel',
		autoLoad: true,
		pageSize: 20,
		comID: 'TZ_SMSQ_VIEWTY_COM',
    	pageID: 'TZ_SMSQ_REPLY_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (config) {
			var taskId = config.taskId;
	     	this.tzStoreParams =  '{"cfgSrhId":"TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_REPLY_STD.TZ_SMS_REPLY_V","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value": "'+ taskId + '"}}';
			
	    	this.callParent();
	  }	
});
