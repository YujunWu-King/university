Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.zpldteamReviewWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.zpldteamReviewWindowStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.zpldteamReviewWindowModel',
    autoLoad:false,
    comID: 'TZ_GSM_CANDIDATE_COM',
    pageID: 'TZ_ZPLDTEAM_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});