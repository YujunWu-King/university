Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportExcelStore',
    model: 'KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_KS_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(classBatch){
    	this.tzStoreParams = '{"type":"expHistory","cfgSrhId":"TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_EXCEL_DC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'
    		+ TranzvisionMeikecityAdvanced.Boot.loginUserId
    		+'","TZ_DR_LXBH-operator": "01","TZ_DR_LXBH-value":"'+ classBatch 
    		+'","TZ_COM_ID-operator": "01","TZ_COM_ID-value": "TZ_REVIEW_CL_COM","TZ_PAGE_ID-operator": "01","TZ_PAGE_ID-value": "TZ_CLPS_KS_STD"}}';
    	
    	this.callParent();
    },
});

