Ext.define('KitchenSink.view.common.store.searchMailHistoryStore', {
    extend: 'Ext.data.Store',
    alias: 'store.searchMailHistoryStore',
    model: 'KitchenSink.view.common.store.searchMailHistoryModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_MAIL_HISTORY_COM',
	pageID: 'TZ_MIAL_HIS_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy(),
	constructor: function (emailAddr) {
	     //交互参数
	     this.tzStoreParams =  '{"emailAddress":"' + emailAddr + '"}';
	     this.callParent();
	  }	
});
