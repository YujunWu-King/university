Ext.define('KitchenSink.view.automaticScreen.autoScreenStore', {
    extend: 'Ext.data.Store',
    alias: 'store.autoScreenStore',
    model: 'KitchenSink.view.automaticScreen.autoScreenModel',
	autoLoad: true,
	pageSize: 50,
	comID: 'TZ_AUTO_SCREEN_COM',
	pageID: 'TZ_AUTO_SCREEN_STD ',
	tzStoreParams: '',
	proxy: Ext.tzListProxy(),
	remoteSort: true,
	
	constructor: function(config){
		var classId = config.classId;
		var batchId = config.batchId;
		var itemColumns = config.itemColumns;
		
		var items = [];
		for(var i=0; i<itemColumns.length; i++){
			items.push(itemColumns[i].columnId);
		}
		
		var itemsParams = 'items:'+Ext.JSON.encode(items);
		
		this.tzStoreParams = '{'+itemsParams+',"cfgSrhId":"TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "'+ classId+'","TZ_BATCH_ID-operator": "01","TZ_BATCH_ID-value": "'+ batchId+'"}}';
		
		this.callParent();	
	}
});
