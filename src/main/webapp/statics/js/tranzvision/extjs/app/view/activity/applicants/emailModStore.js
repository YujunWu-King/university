Ext.define('KitchenSink.view.activity.applicants.emailModStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailModStore',
    fields: ['modelId','modelName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/emailModel.json',
				reader: {
					type: 'json',
					rootProperty: 'comContent.root'
				}
			}	
});
