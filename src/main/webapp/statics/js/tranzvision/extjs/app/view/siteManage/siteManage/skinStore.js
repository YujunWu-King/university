Ext.define('KitchenSink.view.siteManage.siteManage.skinStore', {
    extend: 'Ext.data.Store',
    alias: 'store.skinStore',
    model: 'KitchenSink.view.template.sitetemplate.skinModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDMB_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/skins.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});
