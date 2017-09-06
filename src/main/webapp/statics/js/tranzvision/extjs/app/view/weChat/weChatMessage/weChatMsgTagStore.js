Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgTagStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatMsgTagStore',
    model: 'KitchenSink.view.weChat.weChatMessage.weChatMsgTagModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_GD_WXMSG_COM',
    pageID: 'TZ_GD_WXMSG_STD',
    tzStoreParams:'{"wxAppId":"'+GetQueryString(window.top.location.href,"appId")+'"}',
    proxy: Ext.tzListProxy()  
});

//获取参数
function GetQueryString(url,name) {
	//url="http://localhost:8080/university/index#SEM_A0000001982?appId=111&sendMode=A&openIds=11,22,33";
	var num=url.indexOf("?");
    var str=url.substr(num+1);
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
    var r = str.match(reg); 
    var context = "";  
    if (r != null)  
         context = r[2];  
    reg = null;  
    r = null;  
    return context == null || context == "" || context == "undefined" ? "" : context;  
}
