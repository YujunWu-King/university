Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMinterviewReviewJudgeXSAccount',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_GSM_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_APPS_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
