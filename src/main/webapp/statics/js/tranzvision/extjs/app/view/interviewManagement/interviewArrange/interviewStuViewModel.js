Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
		{name: 'msPlanNo'},
		{name: 'oprid'},
        {name: 'name'},
        {name: 'skypeId'},
        {name: 'city'},
        {name: 'country'},
        {name: 'lxEmail'},
        {name: 'timezone'},
        {name: 'timezoneDiff'},
        {name: 'localStartDate'},
        {name: 'localStartTime'},
        {name: 'localFinishDate'},
        {name: 'localFinishTime'}
    ]
});
