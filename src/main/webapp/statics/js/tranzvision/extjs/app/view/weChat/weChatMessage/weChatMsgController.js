Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatMsgController',
    //发送微信消息
    sendWxMsg:function(btn){
    	var form=btn.findParentByType("panel").down("form").getForm();
    	if(!form.isValid()){
            return false;
        }
        var formParams = form.getValues();
        var tabPanel=form.findField("weChatTabPanel");
        var tabPanel=btn.findParentByType("panel").down("form").items.items[4];
        var activeForm=tabPanel.getActiveTab().config.name;
        var sendType="";
        if(activeForm=="form1"){
        	//文字消息
        	sendType="C";
        }
        if(activeForm=="form2"){
        	//图片消息
        	sendType="A";
        }
        if(activeForm=="form3"){
        	//图文消息
        	sendType="B";
        }
        var tzParams = '{"ComID":"TZ_GD_WXMSG_COM","PageID":"TZ_GD_WXMSG_STD","OperateType":"U","comParams":{"add":[{"sendType":"'+sendType+'","data":'+Ext.JSON.encode(formParams)+'}]}}';
        console.log(tzParams);
        
       /* Ext.tzSubmit(tzParams,function(response){
        	form.findField("sendStatus").setValue("Y");
        },"发送成功",true,this);*/
    },
    //关闭微信窗口
    closeWxPanel:function(){
     this.getView().close();
    }
});
