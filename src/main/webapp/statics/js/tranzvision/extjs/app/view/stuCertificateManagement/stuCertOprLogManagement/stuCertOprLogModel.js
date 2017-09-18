Ext.define('KitchenSink.view.stuCertificateManagement.stuCertOprLogManagement.stuCertOprLogModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'jgId'},
        {name: 'certNo'},
        {name: 'certTypeName'},
        {name: 'name'},
        {name: 'className'},
        {name: 'operationType'},
//        {name:'operationer'},
        {name: 'timeStamp'},
        {name: 'timeStampDate'}
    ]
});