Ext.define('KitchenSink.view.activity.applicants.signStaStore', {
    extend: 'Ext.data.Store',
    alias: 'store.signStaStore',
    fields: ['signStaId','signStaName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/signStaModel.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
