Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewExamineeStore',
    model: 'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_KS_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW","condition":{}}',
    pageSize:50,
    proxy: Ext.tzListProxy(),
    sorters: {property: 'mssqh', direction: 'ASC'}
});