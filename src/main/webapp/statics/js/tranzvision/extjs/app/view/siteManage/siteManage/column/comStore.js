Ext.define('KitchenSink.view.siteManage.siteManage.column.comStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.template.sitetemplate.column.comModel',
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/column/comStore.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
});
