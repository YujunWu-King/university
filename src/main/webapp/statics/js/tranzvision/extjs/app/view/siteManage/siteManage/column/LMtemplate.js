Ext.define('KitchenSink.view.siteManage.siteManage.column.LMtemplate', {
    extend: 'Ext.data.Store',
    alias: 'store.LMtemplateI',
    fields: ['areatypeId','areatypeName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/siteManage/siteManage/column/LMtemplate.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
