Ext.define('KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryStore', {
    extend: 'Ext.data.Store',
    alias: 'store.copyFromHistoryStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_SMSQ_COM',
		pageID: 'TZ_SMSQ_DET_STD',
		tzStoreParams: '{"queryID": "myHistoryRw"}',
		proxy: Ext.tzListProxy(),
		constructor: function (tType) {
	     	this.tzStoreParams = '{"queryID": "myHistoryRw","taskType":"'+tType+'","searchText":""}';
		    this.callParent();
	  }	
});
