Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMmaterialsReviewJudgeAccount',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountModel',
    pageSize:10,
    autoLoad:false,
	comID: 'TZ_REVIEW_GSMCL_COM',
	pageID: 'TZ_GSMCL_RULE_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy()
});
