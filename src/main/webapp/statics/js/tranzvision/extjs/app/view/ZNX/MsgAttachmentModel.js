/**
 * Created by carmen on 2015/11/12.
 */
Ext.define('KitchenSink.view.ZNX.MsgAttachmentModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'MsgID'},
        {name:'attachmentID'},
        {name:'attachmentSysName'},
        {name: 'attachmentName'},
        {name: 'attachmentUrl'}
    ]
});