Ext.define('KitchenSink.view.template.sitetemplate.area.areapositionStore', {
    extend: 'Ext.data.Store',
    alias: 'store.areapositionStore',
    fields: ['areapositionId','areapositionName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/area/areapositions.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
