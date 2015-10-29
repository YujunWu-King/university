Ext.define('KitchenSink.view.template.sitetemplate.menu.menutypeStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.menutypeStroe',
    fields: ['menutypeId','menutypeName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menutypes.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
