Ext.define('KitchenSink.view.bugManagement.bugProg.bugProgModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'bugID'},
        {name: 'resOpr'},
        {name: 'estDate'},
        {name: 'bugPercent',type: 'float'},
        {name: 'bugUpdateProDTTM',type:'date',format: 'Y-m-d H:i:s'}
//       {name: 'bugUpdateProDTTM',type: 'date',format: 'Y-m-d h:i:s AM'}
//               {name: 'bugUpdateProDTTM',type: 'date'}
    ]
});