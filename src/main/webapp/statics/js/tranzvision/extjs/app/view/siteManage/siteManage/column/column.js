Ext.define('KitchenSink.view.siteManage.siteManage.column.column', {
    extend: 'Ext.data.Store',
    alias: 'store.column',
    fields: ['sitestateId','sitestateName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/column/column.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
