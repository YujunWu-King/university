Ext.define('KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.searchStuStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_EMLSMS_STU_COM',
		pageID: 'TZ_EMLSMS_STU_STD',
		tzStoreParams: '{"cfgSrhId": "TZ_EMLSMS_STU_COM.TZ_EMLSMS_STU_STD.TZ_QFKSXX_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID +'"}}',
		proxy: Ext.tzListProxy()/*,
		constructor: function (tType) {
	     	this.tzStoreParams = '{"queryID": "searchStu","taskType":"'+tType+'","searchText":""}';
		    this.callParent();
	  }	*/
});
