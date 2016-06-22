Ext.define('KitchenSink.view.onTrialManagement.trialManagementModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'seqNum'},
		{name: 'contactName'},
        {name: 'contactPhone'},
        {name: 'contactEmail'},
        {name: 'orgName'},
        {name: 'website'},
        {name: 'submitTime'},
        {name: 'shRst'},
        {name: 'startTime'},
        {name: 'endTime'},
        {name: 'hmsr'}
	]
});
