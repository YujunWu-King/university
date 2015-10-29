Ext.define('KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jcbbQuestionListStore',
    model: 'KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_JCBBLB_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});