Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowStore',{
    extend:'Ext.data.Store',
    alias:'store.materialsReviewExamineeWindowStore',
    model:'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_ADDKS_STD',
    tzStoreParams:'',
    pageSize:50,
    proxy: Ext.tzListProxy()
});
