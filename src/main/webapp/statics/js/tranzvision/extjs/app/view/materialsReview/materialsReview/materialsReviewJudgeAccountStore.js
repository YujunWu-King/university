Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewJudgeAccountStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewJudgeAccount',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewJudgeAccountModel',
    pageSize:10,
    autoLoad:false,
	comID: 'TZ_REVIEW_CL_COM',
	pageID: 'TZ_CLPS_RULE_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy()
});
