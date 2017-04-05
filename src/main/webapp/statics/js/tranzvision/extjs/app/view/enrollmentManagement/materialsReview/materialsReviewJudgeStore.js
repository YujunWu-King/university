Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewJudgeStore',{
    extend:'Ext.data.Store',
    alias:'store.materialsReviewJudgeStore',
    model:'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewJudgeModel',
    pageSize:10,
    autoLoad:false,
    comID:'TZ_REVIEW_CL_COM',
    pageID:'TZ_CLPS_RULE_STD',
    tzStoreParams:'',
    proxy:Ext.tzListProxy()
});
