Ext.define('KitchenSink.view.content.contentMg.contentModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'isEnable'},
        {name: 'articleTitle'},
        {
            name: 'releaseTime',
            mapping: 'releaseTime',
            type: 'date',
            dateFormat: 'datetime'
        },
        {name: 'lastUpdate'},
        {name: 'releaseOrUndo'},
        {name: 'topOrUndo'}
	]
});
