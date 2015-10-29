Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewApplicants',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_APPS_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy(),
    filters: []
});
