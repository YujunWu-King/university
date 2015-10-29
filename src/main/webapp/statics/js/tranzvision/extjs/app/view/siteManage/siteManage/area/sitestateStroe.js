Ext.define('KitchenSink.view.siteManage.siteManage.area.sitestateStroe', {
    extend: 'Ext.data.Store',
    alias: 'store.sitestateStroe',
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
