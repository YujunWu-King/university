Ext.define('KitchenSink.view.callCenter.callCenterModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'recieveId'},
		{name: 'callType'},
        {name: 'callPhone'},
        {name: 'callDTime'},
        {name: 'dealWithZT'},
        {name: 'callDesc'}
	]
});
