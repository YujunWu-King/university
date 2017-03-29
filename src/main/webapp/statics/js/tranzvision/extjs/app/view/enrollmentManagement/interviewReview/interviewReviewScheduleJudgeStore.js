Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewScheduleJudgeStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleJudgeModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_SCHE_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});