Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewJudgeAccountStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMinterviewReviewJudgeAccount',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewJudgeAccountModel',
    pageSize:10,
    autoLoad:false,
	comID: 'TZ_GSM_REVIEW_MS_COM',
	pageID: 'TZ_MSPS_RULE_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy()
});
