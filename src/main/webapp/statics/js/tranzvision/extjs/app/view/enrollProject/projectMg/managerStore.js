//专业方向store
Ext.define('KitchenSink.view.enrollProject.projectMg.managerStore', {
    extend: 'Ext.data.Store',
    alias: 'store.managerStore',
    model: 'KitchenSink.view.enrollProject.projectMg.managerModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_PRJ_PROMG_COM',
	pageID: 'TZ_PRJ_PROINFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
	
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/enrollProject/projectMg/manager.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}
	*/
});
