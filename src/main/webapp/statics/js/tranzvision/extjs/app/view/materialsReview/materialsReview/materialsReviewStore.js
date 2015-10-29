Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewModel',
    autoLoad:true,
    pageSize:10,
	comID: 'TZ_REVIEW_CL_COM',
	pageID: 'TZ_CLPS_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_LIST_STD.TZ_CLS_BATCH_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    sorters: [{
        property: 'startDate',
        direction: 'desc'
    }, {
        property: 'endDate',
        direction: 'desc'
    }]
});
