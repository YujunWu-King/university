Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewStore',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewModel',
    autoLoad:true,
    pageSize:10,
	comID: 'TZ_REVIEW_GSMCL_COM',
	pageID: 'TZ_GSMCL_LIST_STD',
        tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_GSMCL_COM.TZ_GSMCL_LIST_STD.TZ_GSM_CL_BAT_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
