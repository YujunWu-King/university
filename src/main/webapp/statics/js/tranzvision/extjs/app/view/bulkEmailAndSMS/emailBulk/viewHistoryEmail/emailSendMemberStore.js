Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailSendMemberStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailSendMemberStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailSendMemberModel',
		autoLoad: true,
		pageSize: 20,
		comID: 'TZ_EMLQ_VIEWTY_COM',
		pageID: 'TZ_EMLQ_MEMBER_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (config) {
			var taskId = config.taskId;
			var sendStatus = config.sendStatus;
	     //交互参数
		 if(sendStatus != ""){
	     	this.tzStoreParams =  '{"cfgSrhId":"TZ_EMLQ_VIEWTY_COM.TZ_EMLQ_MEMBER_STD.TZ_EMLHIS_RY_V","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value": "'+ taskId + '","TZ_FS_ZT-operator": "01","TZ_FS_ZT-value": "'+ sendStatus +'"}}';
		 }else{
			this.tzStoreParams =  '{"cfgSrhId":"TZ_EMLQ_VIEWTY_COM.TZ_EMLQ_MEMBER_STD.TZ_EMLHIS_RY_V","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value": "'+ taskId + '"}}';
		 }
	     this.callParent();
	  }	
});
