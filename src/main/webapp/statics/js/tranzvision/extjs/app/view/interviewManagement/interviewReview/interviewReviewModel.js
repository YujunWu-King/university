Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
        {name: 'batchID'},
		{name: 'batchName'},
        {name: 'startDate'},
        {name: 'endDate'},
        {name: 'status'}       
	]
});
