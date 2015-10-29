Ext.define('KitchenSink.view.template.sitetemplate.area.sitestateStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.siteAreaStroe',
    fields: ['sitestateId','sitestateName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/area/sitestates.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
