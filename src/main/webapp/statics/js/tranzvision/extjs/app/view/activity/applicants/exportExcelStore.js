Ext.define('KitchenSink.view.activity.applicants.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportExcelStore',
    model: 'KitchenSink.view.activity.applicants.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_GD_BMRGL_COM',
    pageID: 'TZ_GD_BMRGL_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(activetyId){
    	this.tzStoreParams = '{"type":"expHistory","cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_HD_BMRDC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'
    		+ TranzvisionMeikecityAdvanced.Boot.loginUserId
    		+'","TZ_ART_ID-operator": "01","TZ_ART_ID-value":"'+ activetyId +'"}}';
    	
    	this.callParent();
    },
});

