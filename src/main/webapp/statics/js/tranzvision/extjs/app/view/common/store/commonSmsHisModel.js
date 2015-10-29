Ext.define('KitchenSink.view.common.store.commonSmsHisModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'status'},
        {name: 'sendNum'},
        {name: 'sendSucNum'},
        {name: 'sendFailNum'},
        {name: 'sendDt'},
		{name: 'operator'}
	]
});
