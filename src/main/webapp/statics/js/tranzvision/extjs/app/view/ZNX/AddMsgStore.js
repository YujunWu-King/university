/**
 * Created by carmen on 2015/11/12.
 */
/**
 * Created by carmen on 2015/11/12.
 */
Ext.define("KitchenSink.view.ZNX.AddMsgStore",{
    extend:'Ext.data.Store',
    alias:"store.AddMsgStore",
    model:'KitchenSink.view.ZNX.AddMsgModel',
    tzStoreParams:'',
    pageSize:10,
    autoLoad:false,
    comID:"TZ_ZNX_COM",
    pageID:"TZ_ZNX_NEWMSG_STD",
    proxy:Ext.tzListProxy()
});