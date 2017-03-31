Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleAppsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewScheduleAppsStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleAppsModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_SCHE_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});