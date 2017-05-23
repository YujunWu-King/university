Ext.define('KitchenSink.view.template.survey.question.export.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportExcelStore',
    model: 'KitchenSink.view.template.survey.question.export.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_WJGL_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(activetyId){
    	this.tzStoreParams = '{"type":"expHistory","cfgSrhId":"TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DCJG_DC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'
    		+ TranzvisionMeikecityAdvanced.Boot.loginUserId +'"}}';
    	
    	this.callParent();
    },
});

