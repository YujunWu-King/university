Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMmaterialsReviewViewAppJudgeStore',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_GSMCL_COM',
    pageID: 'TZ_GSMCL_VWJUD_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
