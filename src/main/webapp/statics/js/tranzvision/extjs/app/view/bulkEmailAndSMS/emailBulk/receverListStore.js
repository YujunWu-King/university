Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.receverListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.receverListStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.receverListModel',
	autoLoad: true,
	pageSize: 50,
	comID: 'TZ_EMLQ_COM',
	pageID: 'TZ_EMLQ_SJR_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy(),
	constructor: function (config) {
		var emlQfId = config.emlQfId;
		var sendModel = config.sendModel;

	    this.tzStoreParams =  '{"emlQfId":"' + emlQfId + '","sendModel":"' + sendModel + '"}';
	     this.callParent();
	  }	
});
