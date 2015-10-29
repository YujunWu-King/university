Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewScheduleJudgeStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleJudgeModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_SCHE_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});