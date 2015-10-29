Ext.define('KitchenSink.view.common.store.commonSmsHisStore', {
    extend: 'Ext.data.Store',
    alias: 'store.commonSmsHisStore',
    model: 'KitchenSink.view.common.store.commonSmsHisModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_COMMON_SMS_COM',
		pageID: 'TZ_AUD_SMS_HIS_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (tmpId) {
	     //交互参数
	     this.tzStoreParams =  '{"cfgSrhId":"TZ_COMMON_SMS_COM.TZ_AUD_SMS_HIS_STD.TZ_SMS_HIS_TJ_V","condition":{"TZ_TMPL_ID-operator": "01","TZ_TMPL_ID-value": "'+ tmpId + '","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID +'"}}';
	     this.callParent();
	  }	
});
