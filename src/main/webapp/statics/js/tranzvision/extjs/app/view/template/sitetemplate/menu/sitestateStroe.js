Ext.define('KitchenSink.view.template.sitetemplate.menu.sitestateStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.siteMenuStroe',
    fields: ['sitestateId','sitestateName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/sitestates.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
