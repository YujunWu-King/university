Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GSMmaterialsReviewApplicantsWindowStore',
    model: 'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_GSMCL_COM',
    pageID: 'TZ_GSMCL_ADDAP_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()


});