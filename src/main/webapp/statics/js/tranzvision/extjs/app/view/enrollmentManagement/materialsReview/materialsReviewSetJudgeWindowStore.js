Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowStore',{
	extend: 'Ext.data.Store',
    alias: 'store.materialsReviewSetJudgeWindowStore',
    model: 'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_SETPW_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});