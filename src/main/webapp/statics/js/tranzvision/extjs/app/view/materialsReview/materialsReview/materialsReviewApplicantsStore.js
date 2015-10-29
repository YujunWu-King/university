Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewApplicants',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_APPS_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
