Ext.define('KitchenSink.view.template.sitetemplate.menu.menuColumnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuColumnStore',
    fields: ['arealmId','arealmName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menuColumns.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
