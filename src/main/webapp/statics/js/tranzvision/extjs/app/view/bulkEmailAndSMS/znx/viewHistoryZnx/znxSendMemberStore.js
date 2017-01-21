Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxSendMemberStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxSendMemberStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxSendMemberModel',
		autoLoad: true,
		pageSize: 20,
		comID: 'TZ_ZNXQ_VIEWTY_COM',
		pageID: 'TZ_ZNXQ_MEMBER_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (config) {
			var taskId = config.taskId;
			var sendStatus = config.sendStatus;
	     //交互参数
		 if(sendStatus != ""){
	     	this.tzStoreParams =  '{"cfgSrhId":"TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_MEMBER_STD.TZ_ZNXHIS_RY_V","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value": "'+ taskId + '","TZ_FS_ZT-operator": "01","TZ_FS_ZT-value": "'+ sendStatus +'"}}';
		 }else{
			this.tzStoreParams =  '{"cfgSrhId":"TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_MEMBER_STD.TZ_ZNXHIS_RY_V","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value": "'+ taskId + '"}}';
		 }
	     this.callParent();
	  }	
});
