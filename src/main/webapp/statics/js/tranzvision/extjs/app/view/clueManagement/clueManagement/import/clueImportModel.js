Ext.define('KitchenSink.view.clueManagement.clueManagement.import.clueImportModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'name'},
        {name:'mobile'},
        {name:'email'},
        {name:'companyName'},
        {name:'position'},
        {name:'memo'},
        {name:'chargeName'},
        {name:'createWay'},
        {name:'createDttm',type:'date',dateFormat:'Y-m-d H:i'},
        {name:'validationResult'},
        {name:'validationMsg'}
    ]
});
