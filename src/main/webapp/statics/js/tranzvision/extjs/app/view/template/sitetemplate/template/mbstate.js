Ext.define('KitchenSink.view.template.sitetemplate.template.mbstate', {
    extend: 'Ext.data.Store',
    alias: 'store.mbstate',
    fields: ['id','state'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/template/mbstate.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
