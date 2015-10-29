Ext.define('KitchenSink.view.template.sitetemplate.menu.menulmStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menulmStore',
    fields: ['menulmId','menulmName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menulms.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
