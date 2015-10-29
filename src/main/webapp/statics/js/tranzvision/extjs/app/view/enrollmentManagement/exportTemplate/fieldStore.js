Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.fieldStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportTemplateFieldStore',
    model: 'KitchenSink.view.enrollmentManagement.exportTemplate.fieldModel',
    pageSize:0,
    autoLoad:false,
    comID: 'TZ_BMGL_DCMB_COM',
    pageID: 'TZ_DCMB_INFO_STD',
    proxy: Ext.tzListProxy()
});
