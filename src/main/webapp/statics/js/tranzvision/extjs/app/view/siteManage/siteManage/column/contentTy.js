Ext.define('KitchenSink.view.siteManage.siteManage.column.contentTy', {
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
