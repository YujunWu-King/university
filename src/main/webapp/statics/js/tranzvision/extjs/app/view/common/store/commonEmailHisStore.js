Ext.define('KitchenSink.view.common.store.commonEmailHisStore', {
    extend: 'Ext.data.Store',
    alias: 'store.commonEmailHisStore',
    model: 'KitchenSink.view.common.store.commonEmailHisModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_COMMON_EMAIL_COM',
		pageID: 'TZ_AUD_EML_HIS_STD',
		tzStoreParams:'',
		proxy: Ext.tzListProxy(),
		constructor: function (tmpId) {
	     //交互参数
	     this.tzStoreParams =  '{"cfgSrhId":"TZ_COMMON_EMAIL_COM.TZ_AUD_EML_HIS_STD.TZ_EML_HIS_TJ_V","condition":{"TZ_TMPL_ID-operator": "01","TZ_TMPL_ID-value": "'+ tmpId + '","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID +'"}}';
	     this.callParent();
	  }	
});
