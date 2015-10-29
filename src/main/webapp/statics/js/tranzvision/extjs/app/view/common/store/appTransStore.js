Ext.define('KitchenSink.view.common.store.appTransStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appTransStore',
    fields: ['TValue','TSDesc','TLDesc'],
	autoLoad: true,
	fieldName: '',
	tzStoreParams:'',
	tzType:'TV',
	proxy: {
		type: 'ajax',
		url : Ext.tzGetGeneralURL(),
		reader: {
			type: 'json',
			rootProperty: 'comContent'
		}
	},
	constructor: function (fieldName) {
        this.fieldName = fieldName;
        this.tzStoreParams = '{"OperateType":"TV","fieldName":"'+this.fieldName+'"}';
        this.config.proxy.reader.rootProperty = 'comContent.'+this.fieldName;
        this.callParent();
    }	
});
