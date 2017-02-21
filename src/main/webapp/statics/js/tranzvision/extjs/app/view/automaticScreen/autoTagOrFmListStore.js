Ext.define('KitchenSink.view.automaticScreen.autoTagOrFmListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.autoTagOrFmListStore',
    model: 'KitchenSink.view.automaticScreen.autoTagOrFmListModel',
	autoLoad: true,
	pageSize: 100,
	comID: 'TZ_AUTO_SCREEN_COM',
	pageID: 'TZ_ZDCS_INFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy(),
	constructor: function(config){
		var classId = config.classId;
		var batchId = config.batchId;
		var appId = config.appId;
		var queryType = config.queryType;
		
		this.tzStoreParams = '{"queryType":"'+queryType+'","classId":"'+classId+'","batchId":"'+batchId+'","appId":"'+appId+'"}';
		
		this.callParent();	
	}
});
