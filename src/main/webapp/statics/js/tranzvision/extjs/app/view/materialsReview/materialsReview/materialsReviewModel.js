Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewModel', {
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
