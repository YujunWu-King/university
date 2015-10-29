Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportTemplateStore',
    model: 'KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateModel',
    pageSize:10,
    autoLoad:true,
	comID: 'TZ_BMGL_DCMB_COM',
	pageID: 'TZ_DCMB_LIST_STD',
	tzStoreParams:'{"cfgSrhId": "TZ_BMGL_DCMB_COM.TZ_DCMB_LIST_STD.TZ_EXPORT_TMP_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
