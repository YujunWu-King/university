Ext.define('KitchenSink.view.template.bmb.myBmbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.myBmbStore',
    model: 'KitchenSink.view.template.bmb.myBmbModel',
    pageSize: 10,
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/bmb/mybmb.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
});