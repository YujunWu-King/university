Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewApplicantsWindowStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_ADDAPP_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()


});