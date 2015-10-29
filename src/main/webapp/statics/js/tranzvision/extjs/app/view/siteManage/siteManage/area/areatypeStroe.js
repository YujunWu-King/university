Ext.define('KitchenSink.view.siteManage.siteManage.area.areatypeStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.areatypeStroe',
    fields: ['areatypeId','areatypeName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/area/areatypes.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
