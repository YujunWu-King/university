Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewJudgeAccountStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewJudgeAccount',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewJudgeAccountModel',
    pageSize:10,
    autoLoad:false,
	comID: 'TZ_REVIEW_MS_COM',
	pageID: 'TZ_MSPS_RULE_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy()
});
