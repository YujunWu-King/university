Ext.define('KitchenSink.view.siteManage.siteManage.menu.menudelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menudelStore',
    fields: ['menudelId','menudelName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menudels.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
