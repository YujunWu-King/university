Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.fsetDropdownStore', {
    extend: 'Ext.data.Store',
    alias: 'store.fsetDropdownStore',
    model: 'KitchenSink.view.enrollmentManagement.attributeSet.fsetDropdownModel',
	autoLoad: true,
    pageSize: 0,
    comID: 'TZ_BMGL_SXSZ_COM',
    pageID: 'TZ_BMGL_LIST_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
