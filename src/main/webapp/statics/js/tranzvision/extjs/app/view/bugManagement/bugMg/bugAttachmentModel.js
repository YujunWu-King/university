Ext.define('KitchenSink.view.bugManagement.bugMg.bugAttachmentModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'bugID'},
        {name:'attachmentID'},
        {name:'attachmentSysName'},
        {name: 'attachmentName'},
        {name: 'attachmentUrl'}
    ]
});