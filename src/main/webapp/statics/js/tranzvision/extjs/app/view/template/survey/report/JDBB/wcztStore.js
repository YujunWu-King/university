Ext.define('KitchenSink.view.template.survey.report.JDBB.wcztStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wcztStore',
    model: 'KitchenSink.view.template.survey.report.JDBB.wcztModel',
    autoLoad:false,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
