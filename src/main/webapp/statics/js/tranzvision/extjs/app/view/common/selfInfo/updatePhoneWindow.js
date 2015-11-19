Ext.define('KitchenSink.view.common.selfInfo.updatePhoneWindow', {
    extend: 'Ext.window.Window',
    xtype: 'updatePhoneWindow', 
    title: '绑定手机', 
	id: 'updatePhoneWindow201506031019',
    width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 520 : 520,
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
			type: 'table',
			columns:2
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
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_PHONE_STD.newPhone","手机"),
			name: 'newPhone',
			ignoreChangesFlag: true,
			width:350,
			allowBlank: false
		},{
			xtype: 'button',
			text:  Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_PHONE_STD.buttonSendCode","发送验证码"),
			name: 'buttonSendCode',
			width:120,
			height:30,
			style:'margin-bottom:5px;margin-left:10px',
			handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
		
			win.sendAuthCode(win);
			}
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_PHONE_STD.authCode","验证码"),
			name: 'authCode',
			width:350,
			ignoreChangesFlag: true,
			allowBlank: false
		},{
			xtype: 'displayfield',
			value:''
		}]
	}],
    buttons: [ {
		text: '绑定登录手机',
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			win.updatePhone(win);
			//关闭窗口
			//win.close();
		}
	},{
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
	// 发送验证码
	sendAuthCode: function(win){	
		//修改手机信息表单
		var form = win.child("form").getForm();
		if(form.getValues()["newPhone"]=""){
			return false;
		}

		//form datas
		var formParams = form.getValues();
		//Params	
		var tzParams = 'tzParams={"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_PHONE_STD","OperateType":"HTML","comParams":'+Ext.JSON.encode(formParams)+'}';
		//send AuthCode to New Phone
		Ext.Ajax.request({
			url: Ext.tzGetGeneralURL(),
			params: tzParams,
			success: function(response){
				var text = response.responseText;
				// process server response here
				console.log(text);
				if (text=="SUCCESS"){
				
					Ext.tzShowToast("验证码已发送成功",'提示','t');
				}else{
				
					Ext.tzShowToast(text,'提示','t');
				}
				 
			}
		});
	},
	//确认修改
	updatePhone:function(win){
		//修改手机信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//form datas
		var formParams = form.getValues();
		//Params	
				
		var tzParams = '{"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_PHONE_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(){
			
		},"",true,this);
	
	},
	bindPhone: function(win){	
		//修改手机信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}

		//form datas
		var formParams = form.getValues();
		//Params	
		var tzParams = 'tzParams={"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_SELF_INFO_STD","OperateType":"BINDPHONE","comParams":'+Ext.JSON.encode(formParams)+'}';
		//send AuthCode to New Phone
		Ext.Ajax.request({
			url: Ext.tzGetGeneralURL(),
			params: tzParams,
			success: function(response){
				var json = response.responseText;
				var o =Ext.decode(json);
				
				var text=o.comContent;
				// process server response here
				console.log(text);
				//Ext.getCmp('buttonBindPhone201506070235').setText('解除绑定');
				if (text=="BINDPHONE"){
					//绑定手机成功
					Ext.tzShowToast("绑定登录手机成功",'提示','t');
					//Ext.getCmp('buttonBindPhone201506070235').setText('解除绑定');
				}else if (text=="UNBINDPHONE"){
					Ext.tzShowToast("解除绑定登录手机成功",'提示','t');
					//Ext.getCmp('buttonBindPhone201506070235').setText('绑定登录手机');
					//form.setValues({bindPhoneFlg:"N"});
					//form.setValues({Phone:"未绑定"});
				}else{
					Ext.tzShowToast(text,'提示','t');
				}
				 
			}
		});
	}
});
