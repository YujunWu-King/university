/**
 * Created by carmen on 2015/9/7.
 */
Ext.define('KitchenSink.view.template.survey.report.JDBB.formStore', {
    extend: 'Ext.data.Store',
    alias: 'store.formStore',
    model: 'KitchenSink.view.template.survey.report.JDBB.formModel',
    autoLoad:true,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});

