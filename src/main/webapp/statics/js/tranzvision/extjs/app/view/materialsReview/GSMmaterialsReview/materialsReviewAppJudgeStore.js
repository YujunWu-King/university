Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewAppJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMmaterialsReviewAppJudgeStore',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewAppJudgeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_GSMCL_COM',
    pageID: 'TZ_GSMCL_APJUG_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
