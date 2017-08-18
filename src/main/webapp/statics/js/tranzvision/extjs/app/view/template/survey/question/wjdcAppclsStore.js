Ext.define('KitchenSink.view.template.survey.question.wjdcAppclsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.wjdcAppclsStore',
    model: 'KitchenSink.view.template.survey.question.wjdcAppclsModel',
    autoLoad: false,
    pageSize: 10,
    comID:'TZ_ZXDC_WJGL_COM',
    pageID:'TZ_ZXDC_WJSZ_STD',
    tzStoreParams:  '{"wjId":"' + "" + '"}',
    proxy: Ext.tzListProxy()
})
