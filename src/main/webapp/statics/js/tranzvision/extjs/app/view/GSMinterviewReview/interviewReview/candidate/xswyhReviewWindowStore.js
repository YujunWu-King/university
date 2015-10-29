Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.xswyhReviewWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.xswyhReviewWindowStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.xswyhReviewWindowModel',
    autoLoad:false,
    comID: 'TZ_GSM_CANDIDATE_COM',
    pageID: 'TZ_XSWYH_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});