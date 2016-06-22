Ext.define('KitchenSink.view.template.bmb.myBmbPdfStore', {
    extend: 'Ext.data.Store',
    alias: 'store.myBmbPdfStore',
    model: 'KitchenSink.view.template.bmb.myBmbPdfModel',
    pageSize:0,
    autoLoad:true,
    comID: 'TZ_ONLINE_REG_COM',
    pageID: 'TZ_ONREG_PDF_STD',
    proxy: Ext.tzListProxy()
});
