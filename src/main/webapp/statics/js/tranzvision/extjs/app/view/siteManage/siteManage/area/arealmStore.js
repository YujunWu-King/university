Ext.define('KitchenSink.view.siteManage.siteManage.area.arealmStore', {
    extend: 'Ext.data.Store',
    alias: 'store.arealmStore',
    fields: ['arealmId','arealmName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/area/arealms.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
