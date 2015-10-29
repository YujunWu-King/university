Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewModel',
    autoLoad:true,
    pageSize:10,
	comID: 'TZ_GSM_REVIEW_MS_COM',
	pageID: 'TZ_GSMS_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_GSM_REVIEW_MS_COM.TZ_GSMS_LIST_STD.TZ_GSMS_BATCH_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
