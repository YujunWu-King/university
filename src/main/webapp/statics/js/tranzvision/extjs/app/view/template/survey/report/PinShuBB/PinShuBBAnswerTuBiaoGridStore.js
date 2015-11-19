Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoGridStore', {
    extend: 'Ext.data.Store',
    alias: 'store.PinShuBBAnswerTuBiaoGridStore',
    model: 'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoGridModel',
    autoLoad:false,
    comID: 'TZ_ZXDC_PSBB_COM',
    pageID: 'TZ_ZXDC_PSBB_W_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
