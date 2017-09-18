Ext.define('KitchenSink.view.automaticScreen.sdbqListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.sdbqListStore',
    model: 'KitchenSink.view.automaticScreen.autoTagOrFmListModel',
	autoLoad: true,
	pageSize: 100,
	comID: 'TZ_AUTO_SCREEN_COM',
	pageID: 'TZ_ZDCS_INFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy(),
	constructor: function(config){
		var appId = config.appId;
		
		this.tzStoreParams = '{"queryType":"SDBQ","appId":"'+appId+'"}';
		
		this.callParent();	
	}
});
