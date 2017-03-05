Ext.define('KitchenSink.view.activity.viewArtModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'siteId'},
        {name: 'coluId'},
        {name: 'artId'},
        {name: 'coluName'},
		{name: 'artPubType'},
        {name: 'publicState'},
        {name: 'previewUrl'},
        {name: 'publicUrl'}
	]
});
