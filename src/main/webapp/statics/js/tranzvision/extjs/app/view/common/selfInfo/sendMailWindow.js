Ext.define('KitchenSink.view.common.selfInfo.sendMailWindow', {
    extend: 'Ext.window.Window',
    xtype: 'sendMailWindow', 
    title: '绑定邮箱', 
	id: 'sendMailWindow201506021945',
    width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 500 : 500,
    height: 200,
    minWidth: 400,
    minHeight: 140,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	
	items: [{
		xtype: 'form',	
		layout: {
			type:'absolute'
			//type: 'vbox',
			//align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
		ignoreLabelWidth: true,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 100 : 100,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			x:20,
			y:10,
			width:400,
			xtype: 'textfield',
			vtype: 'email',
			ignoreChangesFlag: true,
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_EMAIL_STD.newEmail","邮箱"),
			name: 'newEmail',
			allowBlank: false
		},{
			x:125,
			y:50,
			xtype: 'button',
			text:  Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_EMAIL_STD.buttonSendEmail","发送确认信到新邮箱"),
			name: 'buttonSendEmail',
			width:200,
			handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			
			win.sendEmail(win);
		}
		}]
	}],
    buttons: [ {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//关闭窗口
			win.close();
		}
	}],
	sendEmail: function(win){
		
		//修改邮箱信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		
		//form datas
		var formParams = form.getValues();
		//Params	
		var tzParams = 'tzParams={"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_EMAIL_STD","OperateType":"JHTML","comParams":'+Ext.JSON.encode(formParams)+'}';
		//send Email to  New Email Address
		Ext.Ajax.request({
			url: Ext.tzGetGeneralURL(),
			params: tzParams,
			success: function(response){
			 var e = Ext.util.JSON.decode(response.responseText.trim());
				var text = e.comContent;
				// process server response here
				
				//console.log(text);
				if (text=="SUCCESS"){
				
					Ext.tzShowToast("修改链接已经发送到指定邮箱，请根据邮件提示完成修改",'提示','t');
				}else{
				
					Ext.tzShowToast(text,'提示','t');
				}
				 
			}
		});
		

	}
});
