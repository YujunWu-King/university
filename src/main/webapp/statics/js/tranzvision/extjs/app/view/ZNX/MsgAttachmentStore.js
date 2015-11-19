/**
 * Created by carmen on 2015/11/12.
 */
Ext.define("KitchenSink.view.ZNX.MsgAttachmentStore",{
    extend:'Ext.data.Store',
    alias:"store.MsgAttachmentStore",
    model:'KitchenSink.view.ZNX.MsgAttachmentModel',
    tzStoreParams:'',
    pageSize:10,
    autoLoad:false,
    comID:"TZ_ZNX_COM",
    pageID:"TZ_ZNX_NEWMSG_STD",
    proxy:Ext.tzListProxy()
});