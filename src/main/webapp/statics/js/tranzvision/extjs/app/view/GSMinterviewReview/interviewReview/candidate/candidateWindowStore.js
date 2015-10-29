Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.candidateWindowModel',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindowModel',
    autoLoad:false,
    comID: 'TZ_GSM_CANDIDATE_COM',
    pageID: 'TZ_MSPS_APPLY_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});