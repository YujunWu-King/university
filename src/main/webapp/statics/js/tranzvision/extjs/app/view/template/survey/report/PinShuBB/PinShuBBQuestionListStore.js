Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.PinShuBBQuestionListStore',
    model: 'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZXDC_PSBB_COM',
    pageID: 'TZ_ZXDC_PSBB_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});