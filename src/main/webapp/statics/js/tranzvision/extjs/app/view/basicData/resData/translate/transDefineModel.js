Ext.define('KitchenSink.view.basicData.resData.translate.transDefineModel', {
    extend: 'Ext.data.Model',
    fields: [
	    {name: 'transSetID'},
        {name: 'transID'},
        {name: 'effeDate'},
		{name: 'isEffe'},
		{name: 'shortDesc'},
		{name: 'longDesc'}
	]
});
