Ext.define('KitchenSink.view.enrollProject.userApply.viewJFPlanStore', {
    extend: 'Ext.data.Store',
    alias: 'store.viewJFPlanStore',
    model: 'KitchenSink.view.enrollProject.userApply.viewJFPlanModel',
	autoLoad: true,
    pageSize: 3,
    comID: 'TZ_UM_USERAPPLY_COM',
    pageID: 'TZ_VIEW_JFJH_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});