Ext.define('KitchenSink.view.activity.applicants.smsModStore', {
    extend: 'Ext.data.Store',
    alias: 'store.smsModStore',
    fields: ['modelId','modelName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/smsModel.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
