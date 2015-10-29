Ext.define('KitchenSink.view.bugManagement.bugCk.bugModel',{
    extend:'Ext.data.Model',
    fields:[
        {name: 'bugID'},
        {name: 'bugName'},
        {name: 'bugStatus'},
        {name: 'bugType'},
        {name: 'bugPrior'},
        {name: 'bugPercent'},
        {name: 'bugUpdateDate',type:'date'},
        {name: 'bugUpdateTime'},
        {name: 'recOpr'},
        {name: 'recDate',type:'date'},
        {name: 'estDate',type:'date'},
        {name: 'resOpr'},
        {name: 'expDate',type:'date'},
        {name: 'bugMigration'}
    ]
});