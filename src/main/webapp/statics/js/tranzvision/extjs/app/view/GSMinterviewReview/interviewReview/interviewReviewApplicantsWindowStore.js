Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMinterviewReviewApplicantsWindowStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsWindowModel',
    autoLoad:false,
    comID: 'TZ_GSM_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_RULE_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});