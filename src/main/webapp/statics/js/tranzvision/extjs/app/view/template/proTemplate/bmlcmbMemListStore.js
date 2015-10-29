Ext.define('KitchenSink.view.template.proTemplate.bmlcmbMemListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bmlcmbMemListStore',
    model: 'KitchenSink.view.template.proTemplate.bmlcmbdetailModel',
	pageSize: 10,
	comID: 'TZ_PM_BMLCMBGL_COM',
	pageID: 'TZ_PM_BMLCMB_STD',
	tzStoreParams: '{}',
	/*proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/orgmgmt/orgs.json',
		reader: {
			type: 'json',
			rootProperty: 'root'
		}
	}*/
	proxy: Ext.tzListProxy()
});
