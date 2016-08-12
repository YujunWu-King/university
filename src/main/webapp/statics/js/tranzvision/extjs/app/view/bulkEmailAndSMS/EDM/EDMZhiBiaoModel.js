Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'zhiBiaoName'},
        {name: 'personName'},
        {name: 'sendEmail'},
        {name: 'sendTime'} ,
        {name:'refusedEmail'},
        {name:'refusedTime'},
        {name:'refusedReason'},
        {name:'openEmail'},
        {name:'openTime'},
        {name:'clickEmail'},
        {name:'clickTime'},
        {name:'clickWebsite'},
        {name:'backEmail'},
        {name:'backTime'},
        {name:'backChannel'}
    ]
});
