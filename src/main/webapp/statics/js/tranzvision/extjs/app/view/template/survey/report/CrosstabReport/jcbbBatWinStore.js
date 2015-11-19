Ext.define('KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jcbbBatWinStore',
    model: 'KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_JCBBBT_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.TZ_JCBB_LIST_V","condition":{}}',
    proxy: Ext.tzListProxy()
});