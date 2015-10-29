
Ext.define('resTempletInfoStore', {
    extend: 'Ext.data.Store',
	requires: [
       'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfoModel'
    ],
    model: 'contentModelDef',
	autoLoad: true,
	pageSize: 20,
	proxy: {
			type: 'ajax',
			url : '/tranzvision/kitchensink/app/view/sendEmailAndSMS/resTempletDef/resTempletInfo.json',
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'root'
			}
	}	
});