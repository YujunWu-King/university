Ext.define('KitchenSink.view.common.selfInfo.selfInfoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'selfInfoWindow', 
    title: '自助信息维护', 
	id: 'selfInfoWindow201506011708',
    width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 500 : 500,
    height: 380,
    minWidth: 400,
    minHeight: 100,
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
			labelWidth: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 80 : 80,
			labelStyle: 'font-weight:bold'
		},
		items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.accountID","账号"),
			name: 'accountID',
			readOnly:true,
			colspan:2,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 450 : 450,
			fieldStyle:'background:#F4F4F4'
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.accountName","姓名"),
			name: 'accountName',
			readOnly:true,
			colspan:2,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 450 : 450,
			fieldStyle:'background:#F4F4F4'
        },{
			xtype: 'textfield',
			vtype: 'email',
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.contactEmail","联系邮箱"),
			name: 'contactEmail',
			colspan:2,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 450 : 450
		},{
			xtype: 'textfield',
			//vtype: 'email',
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.contactPhone","联系手机"),
			name: 'contactPhone',
			colspan:2,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 450 : 450
		},{
			xtype: 'textfield',
			//vtype: 'email',
			fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.Email1","绑定邮箱"),
			name: 'Email',
			readOnly:true,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 330 : 330,
			fieldStyle:'background:#F4F4F4'
		},/*{
			xtype: 'button',
			text: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.buttonUpdateEmail","修改"),
			name: 'buttonUpdateEmail',
			style:'margin-bottom:5px;',
			handler:function(btn){
				var win = btn.findParentByType("window");
				win.updateEmail(win);
			}
		},*/{
			xtype: 'button',
			text: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.buttonBindEmail","绑定登录邮箱"),
			id:'buttonBindEmail201506070235',
			name: 'buttonBindEmail',
			style:'margin-bottom:5px;',
			width:120,
			handler:function(btn){
				var win = btn.findParentByType("window");
				//绑定邮箱;
				//win.bindEmail(win);
				//打开绑定二级页面;
				win.updateEmail(win);
			}
		},{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.Phone1","绑定手机"),
			name: 'Phone',
			readOnly:true,
			width: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 330 : 330,
			fieldStyle:'background:#F4F4F4'
        },/*{
			xtype: 'button',
			text: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.buttonUpdatePhone","修改"),
			name: 'buttonUpdatePhone',
			style:'margin-bottom:5px;',
			handler:function(btn){
				var win = btn.findParentByType("window");
				win.updatePhone(win);
			}
		},*/{
			xtype: 'button',
			text: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.buttonBindPhone","绑定登录手机"),
			id:'buttonBindPhone201506070235',
			name: 'buttonBindPhone',
			style:'margin-bottom:5px;',
			width:120,
			handler:function(btn){
				var win = btn.findParentByType("window");
				//直接绑定手机;
				//win.bindPhone(win);
				
				//打开绑定二级页面;
				//短信未开放，暂时注释
				//win.updatePhone(win);
				Ext.tzShowToast('手机绑定功能暂未开放，敬请期待','提示','t');
				
			}
		},{
            xtype: 'hiddenfield',
            fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.bindPhoneFlg","绑定手机标记"),
			name: 'bindPhoneFlg'
        },{
            xtype: 'hiddenfield',
            fieldLabel: Ext.tzGetResourse("TZ_SELF_INFO_COM.TZ_SELF_INFO_STD.bindEmailFlg","绑定邮箱标记"),
			name: 'bindEmailFlg'
        }]
	}],
    buttons: [{
		text: '刷新',
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//重置表单
			//form.reset();
			//关闭窗口
			//win.close();
			win.refresh(win);
		}
	},{
		text: '保存',
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//重置表单
			//form.reset();
			//关闭窗口
			//win.close();
			win.doSave(win);
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
	refresh:function(win){
		//参数
		var tzParams = '{"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_SELF_INFO_STD","OperateType":"QF","comParams":{}}';
		//加载数据
		var form = win.child('form').getForm();
		Ext.tzLoad(tzParams,function(responseData){
			var Email = responseData.Email;
			var Phone= responseData.Phone;
			var accountID=responseData.accountID;
			var accountName=responseData.accountName;
			var bindPhoneFlg=responseData.bindPhoneFlg;
			var bindEmailFlg=responseData.bindEmailFlg;
			var contactEmail=responseData.contactEmail;
			var contactPhone=responseData.contactPhone;
			form.setValues({Email:Email,accountID:accountID,accountName:accountName,Phone:Phone,contactPhone:contactPhone,contactEmail:contactEmail,bindPhoneFlg:bindPhoneFlg,bindEmailFlg:bindEmailFlg});
			if (bindPhoneFlg=="Y"){
				 Ext.getCmp('buttonBindPhone201506070235').setText('解除绑定');
			}else{
				 Ext.getCmp('buttonBindPhone201506070235').setText('绑定登录手机');
			}
			if (bindEmailFlg=="Y"){
				 Ext.getCmp('buttonBindEmail201506070235').setText('解除绑定');
			}else{
				 Ext.getCmp('buttonBindEmail201506070235').setText('绑定登录邮箱');
			}
			
		});	
	},
	modifyEmail: function(win){
			
		//修改密码信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//表单数据
		var formParams = form.getValues();

		//提交参数
		var tzParams = '{"ComID":"TZ_GD_BEMAIL_COM","PageID":"TZ_GD_BEMAIL_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(){
			
		},"",true,this);
	},
	doSave:function(win){
		//保存
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//form datas
		var formParams = form.getValues();
		//Params	
				
		var tzParams = '{"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_SELF_INFO_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(){
			
		},"",true,this);
		
	},
	updateEmail:function(win){
		var className = 'KitchenSink.view.common.selfInfo.sendMailWindow';
		Ext.syncRequire(className);
		var ViewClass = Ext.ClassManager.get(className);
		var newwin;
		if(Ext.getCmp("sendMailWindow201506021945") == undefined){
			//新建类
			newwin = new ViewClass();
		}else{
			newwin = Ext.getCmp("sendMailWindow201506021945");
		}
		var bindEmailFlg;
		var defaultEmail;
		
		var form = win.child("form").getForm();
		bindEmailFlg=form.getValues()["bindEmailFlg"]; 
		defaultEmail=form.getValues()["contactEmail"]; 
		
		if (bindEmailFlg=="Y"){
			win.bindEmail(win);
		}else{
			var newform = newwin.child('form').getForm();
			newform.setValues({newEmail:defaultEmail});
			newwin.show();
		}
		

	},
	updatePhone:function(win){
		var className = 'KitchenSink.view.common.selfInfo.updatePhoneWindow';
		Ext.syncRequire(className);
		var ViewClass = Ext.ClassManager.get(className);
		var newwin;
		if(Ext.getCmp("updatePhoneWindow201506031019") == undefined){
			//新建类
			newwin = new ViewClass();
		}else{
			newwin = Ext.getCmp("updatePhoneWindow201506031019");
		}
		var bindPhoneFlg;
		var defaultPhone;
		
		var form = win.child("form").getForm();
		defaultPhone=form.getValues()["contactPhone"]; 
		bindPhoneFlg=form.getValues()["bindPhoneFlg"]; 
		if (bindPhoneFlg==""){
			win.bindPhone(win);	
		}else{
			var newform = newwin.child('form').getForm();
			newform.setValues({newPhone:defaultPhone});
			newwin.show();
		}
	

	},
	bindPhone: function(win){	
		//修改手机信息表单
		var form = win.child("form").getForm();
		if(form.getValues()["Phone"]=""){
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
					Ext.getCmp('buttonBindPhone201506070235').setText('解除绑定');
				}else if (text=="UNBINDPHONE"){
					Ext.tzShowToast("解除绑定登录手机成功",'提示','t');
					Ext.getCmp('buttonBindPhone201506070235').setText('绑定登录手机');
					form.setValues({bindPhoneFlg:"N"});
					form.setValues({Phone:"未绑定"});
				}else{
					Ext.tzShowToast(text,'提示','t');
				}
				 
			}
		});
	},
	bindEmail: function(win){	
		//修改手机信息表单
		var form = win.child("form").getForm();
		if(form.getValues()["Email"]=""){
			return false;
		}

		//form datas
		var formParams = form.getValues();
		//Params	
		var tzParams = 'tzParams={"ComID":"TZ_SELF_INFO_COM","PageID":"TZ_SELF_INFO_STD","OperateType":"BINDEMAIL","comParams":'+Ext.JSON.encode(formParams)+'}';
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
				if (text=="BINDEMAIL"){
					//绑定邮箱成功
					Ext.tzShowToast("绑定登录邮箱成功",'提示','t');
					Ext.getCmp('buttonBindEmail201506070235').setText('解除绑定');
				}else if (text=="UNBINDEMAIL"){
					Ext.tzShowToast("解除绑定登录邮箱成功",'提示','t');
					Ext.getCmp('buttonBindEmail201506070235').setText('绑定登录邮箱');
					form.setValues({bindEmailFlg:"N"});
					form.setValues({Email:"未绑定"});
				}else{
					Ext.tzShowToast(text,'提示','t');
				}
				 
			}
		});
	}
});
