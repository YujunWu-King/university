Ext.define('paraModelDef', {
    extend: 'Ext.data.Model',
    fields: [
 		{name: 'paraid',type:'string'},
        {name: 'paraname',type:'string'},
        {name: 'paraalias',type:'string'},
        {name: 'systvar',type:'string'},
		{name: 'systvarname',type:'string'}
    ],
});

Ext.define('contentModelDef', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'keyfield',type:'string'},
        {name: 'parafield',type:'string'},
        {name: 'paradesc',type:'string'}
    ],
});


