Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewScheduleJudgeStore',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleJudgeModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_PLAN_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});