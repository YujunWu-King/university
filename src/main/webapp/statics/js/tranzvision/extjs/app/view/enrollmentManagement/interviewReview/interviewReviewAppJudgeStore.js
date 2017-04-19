Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewAppJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewAppJudgeStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewAppJudgeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_APPJUG_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
