Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'appId'},
        {name: 'stuName'},
		{name: 'appFormSta'},
		{name: 'msAppFormAuditSta'},
        {name: 'msZGFlag'},
        {name: 'batchName'},
        {name: 'msDate',type:'date'},
	    {name: 'msStartTime'},
        {name: 'msEndTime'}
    ]
});
