Ext.define('KitchenSink.view.template.sitetemplate.column.contentTy', {
    extend: 'Ext.data.Store',
    alias: 'store.contentTy',
    fields: ['areapositionId','areapositionName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/column/contentTy.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}
});
