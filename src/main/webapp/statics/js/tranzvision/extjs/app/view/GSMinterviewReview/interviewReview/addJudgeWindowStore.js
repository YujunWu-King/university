Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.addJudgeWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.addJudgeWindowStore',
    model: 'KitchenSink.view.GSMinterviewReview.interviewReview.addJudgeWindowModel',
    autoLoad:false,
    comID: 'TZ_GSM_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_ADDJUG_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy(),
    filters: []
});