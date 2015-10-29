Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateStore', {
    extend: 'Ext.data.Store',
    alias: 'store.candidateStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateModel',
    autoLoad:false,
    comID: 'TZ_GSM_CANDIDATE_COM',
    pageID: 'TZ_MSPS_APPS_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy(),
    filters: []
});
