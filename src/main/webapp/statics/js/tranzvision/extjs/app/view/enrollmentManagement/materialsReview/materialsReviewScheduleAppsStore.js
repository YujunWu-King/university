Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewScheduleAppsStore',
    model: 'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsModel',
    pageSize:50,
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_SCHE_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});