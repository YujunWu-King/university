Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewApplicants',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsModel',
    pageSize: 5,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_APPS_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_APPS_STD.PS_TZ_CLPS_KSH_VW"}',
    proxy: Ext.tzListProxy()
});
