Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMmaterialsReviewJudgeWindowStore',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_GSMCL_COM',
    pageID: 'TZ_GSMCL_ADDJD_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()


});