Ext.define('KitchenSink.view.content.artMg.artModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'isEnable'},
        {name: 'articleTitle'},
        {name: 'artseq'},
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
