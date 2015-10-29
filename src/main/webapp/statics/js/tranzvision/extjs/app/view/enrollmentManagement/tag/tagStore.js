Ext.define('KitchenSink.view.enrollmentManagement.tag.tagStore', {
    extend: 'Ext.data.Store',
    alias: 'store.tagStore',
    model: 'KitchenSink.view.enrollmentManagement.tag.tagModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_BMGL_TAG_COM',
    pageID: 'TZ_BMGL_TAG_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
