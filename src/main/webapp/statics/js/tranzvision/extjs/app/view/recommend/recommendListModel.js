Ext.define('KitchenSink.view.recommend.recommendListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'startDate'},
        {name: 'className'},
        {name: 'batchName'},
        {name: 'entranceTime'},
        {name: 'applyState'},
	]
});
