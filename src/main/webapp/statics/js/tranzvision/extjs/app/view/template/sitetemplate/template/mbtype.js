Ext.define('KitchenSink.view.template.sitetemplate.template.mbtype', {
    extend: 'Ext.data.Store',
    alias: 'store.mbtype',
    fields: ['id','type'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/template/mbtype.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
