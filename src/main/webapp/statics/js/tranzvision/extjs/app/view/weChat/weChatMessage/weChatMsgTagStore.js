Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgTagStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatMsgTagStore',
    model: 'KitchenSink.view.weChat.weChatMessage.weChatMsgTagModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_GD_WXMSG_COM',
    pageID: 'TZ_GD_WXMSG_STD',
    tzStoreParams:'{"wxAppId":"'+wxAppId+'"}',
    //tzStoreParams:'{"wxAppId":""}',
    proxy: Ext.tzListProxy()  
});

//console.log(window.top.location.href);
var url=window.top.location.href;
//var url="http://localhost:8080/university/index#SEM_A0000001982?appId=1&sendMode=B&tags=1";
var wxAppId=0;
var num=url.indexOf("?") 
var str=url.substr(num+1);
var arr=str.split("&"); 
for(var i=0;i < arr.length;i++){ 
 num=arr[i].indexOf("="); 
 if(num>0){ 
  name=arr[i].substring(0,num);
  value=arr[i].substring(num+1,arr[i].length);
   if(name=="appId"){
	   wxAppId=value;
   }
 }
}
