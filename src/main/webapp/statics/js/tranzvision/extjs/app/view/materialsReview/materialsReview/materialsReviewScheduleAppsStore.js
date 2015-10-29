Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewScheduleAppsStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppsModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_SCHE_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});