Ext.define('KitchenSink.view.bugManagement.bugMg.bugModel',{
    extend:'Ext.data.Model',
    fields:[
        {name: 'orgID'},
        {name: 'bugID'},
        {name: 'bugName'},
        {name: 'bugStatus'},
        {name: 'bugType'},
        {name: 'bugPrior'},
        {name: 'bugPercent'},
        {name: 'bugUpdateDate',type:'date'},
        {name: 'bugUpdateTime'},
        {name: 'bugUpdateDTTM',type:'date'},
        {name: 'recOpr'},
        {name: 'recDate',type:'date'},
        {name: 'estDate',type:'date'},
        {name: 'resOpr'},
        {name: 'expDate',type:'date'},
        {name: 'bugMigration'},
        {name: 'bugBZ'},
        {name: 'module'}
/*        ,
        {name: 'bugAddDate'}*/
    ]
});