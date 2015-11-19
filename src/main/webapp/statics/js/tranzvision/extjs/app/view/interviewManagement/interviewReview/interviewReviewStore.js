Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewStore',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewModel',
    autoLoad:false,
    pageSize:10,
	comID: 'TZ_REVIEW_MS_COM',
	pageID: 'TZ_MSPS_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_MS_COM.TZ_MSPS_LIST_STD.TZ_CLS_BATCH_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    sorters: [{
        property: 'startDate',
        direction: 'desc'
    }, {
        property: 'endDate',
        direction: 'desc'
    }]
});
