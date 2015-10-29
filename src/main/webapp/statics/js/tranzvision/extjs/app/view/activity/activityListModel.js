Ext.define('KitchenSink.view.activity.activityListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'activityId'},
        {name: 'activityName'},
        {name: 'activityPlace'},
        {name: 'activityStartDate'},
        {name: 'activityEndDate'},
        {name: 'applyStartDate'},
        {name: 'applyEndDate'},
        {name: 'applyNum'}
	]
});
