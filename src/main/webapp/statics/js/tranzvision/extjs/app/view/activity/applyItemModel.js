Ext.define('KitchenSink.view.activity.applyItemModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'activityId'},
        {name: 'applyItemId'},
        {name: 'applyItemNum'},
        {name: 'applyItemName'},
        {name: 'applyItemRequired'},
        {name: 'applyItemType'},
		{name: 'applyItemNameEng'}
	]
});
