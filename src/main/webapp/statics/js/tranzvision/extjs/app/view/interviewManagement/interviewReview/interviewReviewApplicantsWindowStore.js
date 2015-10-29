Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewReviewApplicantsWindowStore',
    model: 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_ADDSTU_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});