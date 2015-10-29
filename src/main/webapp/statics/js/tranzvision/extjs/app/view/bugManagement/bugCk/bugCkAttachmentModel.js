Ext.define('KitchenSink.view.bugManagement.bugCk.bugCkAttachmentModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'bugID'},
        {name:'attachmentID'},
        {name:'attachmentSysName'},
        {name: 'attachmentName'},
        {name: 'attachmentUrl'}
    ]
});