Ext.define('KitchenSink.view.template.sitetemplate.column.contentTe', {
    extend: 'Ext.data.Store',
    alias: 'store.contentTe',
    fields: ['arealmId','arealmName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/column/contentTe.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
