Ext.define('KitchenSink.view.content.contentMg.artPicModel', {
    extend: 'Ext.data.Model',
    fields: [
    	  { name:'sysFileName', type:'string' },
        { name:'index', type:'number' },
        { name:'src', type:'string' },
        { name:'caption', type:'string' },
        { name:'picURL',type:'string' }
    ]
});
