Ext.define('KitchenSink.view.automaticScreen.autoScreenStore', {
    extend: 'Ext.data.Store',
    alias: 'store.autoScreenStore',
    model: 'KitchenSink.view.automaticScreen.autoScreenModel',
	autoLoad: true,
	pageSize: 500,
	comID: 'TZ_AUTO_SCREEN_COM',
	pageID: 'TZ_AUTO_SCREEN_STD ',
	tzStoreParams: '',
	proxy: Ext.tzListProxy(),
	remoteSort: true,
	
	constructor: function(config){
		var classId = config.classId;
		var batchId = config.batchId;
		var itemColumns = config.itemColumns;
		var itemMsColumns = config.itemMsColumns;
		var itemZjColumns = config.itemZjColumns;
		
		var items = [];
		for(var i=0; i<itemColumns.length; i++){
			items.push(itemColumns[i].columnId);
		}
		var itemsMs = [];
		for(var i=0; i<itemMsColumns.length; i++){
			itemsMs.push(itemMsColumns[i].columnId);
		}
		var itemsZj = [];
		for(var i=0; i<itemZjColumns.length; i++){
			itemsZj.push(itemZjColumns[i].columnId);
		}
		
		var itemsParams = 'items:'+Ext.JSON.encode(items);
		var itemsMsParams = 'itemsMs:'+Ext.JSON.encode(itemsMs);
		var itemsZjParams = 'itemsZj:'+Ext.JSON.encode(itemsZj);
		
		if(batchId == undefined || batchId == ''){
			this.tzStoreParams = '{'+itemsParams+','+itemsMsParams+','+itemsZjParams+',"cfgSrhId":"TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "'+ classId+'"}}';
		}else{
			this.tzStoreParams = '{'+itemsParams+','+itemsMsParams+','+itemsZjParams+',"cfgSrhId":"TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "'+ classId+'","TZ_BATCH_ID-operator": "01","TZ_BATCH_ID-value": "'+ batchId+'"}}';
		}
		
		this.callParent();	
	}
});
