Ext.define('KitchenSink.view.automaticScreen.export.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportExcelStore',
    model: 'KitchenSink.view.automaticScreen.export.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_AUTO_SCREEN_COM',
    pageID: 'TZ_AUTO_SCREEN_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(classBatch){
    	this.tzStoreParams = '{"type":"expHistory","cfgSrhId":"TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_ZDCS_DC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'
    		+ TranzvisionMeikecityAdvanced.Boot.loginUserId
    		+'","TZ_CLASS_BATCH-operator": "01","TZ_CLASS_BATCH-value":"'+ classBatch +'"}}';
    	
    	this.callParent();
    },
});

