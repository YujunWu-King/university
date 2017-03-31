Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
        {name: 'batchID'},
		{name: 'batchName'},
        {name: 'startDateTime'},
        {name: 'endDateTime'},
        {name: 'materialStudents'},
        {name: 'status'}       
	]
});
