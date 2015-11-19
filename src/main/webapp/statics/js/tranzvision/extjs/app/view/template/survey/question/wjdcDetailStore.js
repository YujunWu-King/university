Ext.define('KitchenSink.view.template.survey.question.wjdcDetailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wjdcDetailStore',
    model: 'KitchenSink.view.template.survey.question.wjdcDetailModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZXDC_WJGL_COM',
    pageID:'TZ_ZXDC_WJXQ_STD',
    tzStoreParams:  '{"wjId":"' + "" + '"}',
    proxy: Ext.tzListProxy()
})
