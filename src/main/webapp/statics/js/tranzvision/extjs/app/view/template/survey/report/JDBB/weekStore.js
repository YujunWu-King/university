/**
 * Created by carmen on 2015/9/8.
 */
Ext.define('KitchenSink.view.template.survey.report.JDBB.weekStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weekStore',
    model: 'KitchenSink.view.template.survey.report.JDBB.weekModel',
    autoLoad:false,
    comID: 'TZ_ZXDC_WJGL_COM',
    pageID: 'TZ_ZXDC_JDBB_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
