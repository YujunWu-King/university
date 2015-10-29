Ext.define('KitchenSink.view.template.sitetemplate.column.LMtemplate', {
    extend: 'Ext.data.Store',
    alias: 'store.LMtemplate',
    fields: ['areatypeId','areatypeName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/column/LMtemplate.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
