Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewAppJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewAppJudgeStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewAppJudgeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_APPJUG_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
