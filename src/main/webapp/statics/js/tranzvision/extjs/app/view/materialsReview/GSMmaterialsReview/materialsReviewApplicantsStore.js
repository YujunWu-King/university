Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewApplicants',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_GSMCL_COM',
    pageID: 'TZ_GSMCL_APPS_STD',
tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
