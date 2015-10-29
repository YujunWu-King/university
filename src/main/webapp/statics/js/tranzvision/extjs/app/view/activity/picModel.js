Ext.define('KitchenSink.view.activity.picModel', {
    extend: 'Ext.data.Model',
    fields: [
    	  { name:'sysFileName', type:'string' },
        { name:'index', type:'number' },
        { name:'src', type:'string' },
        { name:'caption', type:'string' },
        { name:'picURL',type:'string' },
        { name:'sltUrl',type:'string' }
    ]
});
