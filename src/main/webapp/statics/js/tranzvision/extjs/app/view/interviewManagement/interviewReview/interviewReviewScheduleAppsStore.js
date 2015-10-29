Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleAppsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewScheduleAppsStore',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleAppsModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_PLAN_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});