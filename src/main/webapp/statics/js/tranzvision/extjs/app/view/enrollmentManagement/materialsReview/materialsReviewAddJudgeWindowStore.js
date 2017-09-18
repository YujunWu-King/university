Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewAddJudgeWindowStore',{
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewAddJudgeWindowStore',
    model: 'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewAddJudgeWindowModel',
    autoLoad:true,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_ADDPW_STD',
    pageSize:500,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});