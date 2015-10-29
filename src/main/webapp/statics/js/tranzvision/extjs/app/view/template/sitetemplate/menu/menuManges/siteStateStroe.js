Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.siteStateStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.siteStateStroe',
    fields: ['sitestateId','sitestateName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menuManges/siteStates.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
