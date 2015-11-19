Ext.define('KitchenSink.view.common.store.searchMailHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'rwInsid'},
        {name: 'sender'},
        {name: 'sendTime'},
        {name: 'subject'},
		{name: 'operator'}
	]
});
