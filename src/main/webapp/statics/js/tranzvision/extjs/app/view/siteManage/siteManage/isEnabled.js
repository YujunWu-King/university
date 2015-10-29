Ext.define('KitchenSink.view.siteManage.siteManage.isEnabled', {
    extend: 'Ext.data.Store',
    alias: 'store.isEnabled',
    fields: ['id','name'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/enabled.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
