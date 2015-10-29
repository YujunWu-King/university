Ext.define('KitchenSink.view.siteManage.siteManage.column.contentTe', {
    extend: 'Ext.data.Store',
    alias: 'store.contentTeI',
    fields: ['arealmId','arealmName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/siteManage/siteManage/column/contentTe.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
