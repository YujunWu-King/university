Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowStore',{
    extend:'Ext.data.Store',
    alias:'store.materialsReviewExamineeWindowStore',
    model:'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_ADDKS_STD',
    tzStoreParams:'{"cfgSrhId":"TZ_REVIEW_CL_COM.TZ_CLPS_ADDKS_STD.PS_TZ_CLPS_ADDKS_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:5000,
    proxy: Ext.tzListProxy()
});
