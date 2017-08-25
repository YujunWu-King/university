Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatMsgController',
    
    
    //关闭微信窗口
    closeWxPanel:function(){
     this.getView().close();
    }
});
