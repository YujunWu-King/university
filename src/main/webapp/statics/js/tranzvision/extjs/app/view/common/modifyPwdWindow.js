Ext.define('KitchenSink.view.common.modifyPwdWindow', {
    extend: 'Ext.window.Window',
    xtype: 'modifyPwdWindow', 
    title: '用户密码修改', 
	id: 'tzModifyPwdWindow201504201420',
    width: 500,
    height: 320,
    minWidth: 400,
    minHeight: 280,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
		ignoreLabelWidth: true,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: TranzvisionMeikecityAdvanced.Boot.language === 'ZHS' ? 120 : 120,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_XGPWD_COM.TZ_GD_XGPWD_STD.oldPwd","旧密码"),
			name: 'oldPwd',
			inputType: 'password',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_XGPWD_COM.TZ_GD_XGPWD_STD.newPwd","新密码"),
			name: 'newPwd',
			inputType: 'password',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_XGPWD_COM.TZ_GD_XGPWD_STD.comfirmPwd","确认新密码"),
			name: 'comfirmPwd',
			inputType: 'password',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}]
	}],
    buttons: [
    /*
    {
		text: '保存',
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			win.modifyPassword(win);			
		}
	}, */
	{
		text: '确定',
		iconCls:"ensure",
		handler: function(btn){
			var bl = true;
			//获取窗口
			var win = btn.findParentByType("window");
			//win.modifyPassword(win);
			//修改密码信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			bl =  false;
		}
		//表单数据
		var formParams = form.getValues();
		//密码
		if(formParams["newPwd"] != formParams["comfirmPwd"]){
			Ext.MessageBox.alert('提示', '两次密码输入不一致', this);
			bl =  false;
		}
		//新旧密码不能相同
		if(formParams["oldPwd"] == formParams["newPwd"]){
			Ext.MessageBox.alert('提示', '新旧密码不能相同', this);
			bl =  false;
		}
		if (bl == true){
			//提交参数
			var tzParams = '{"ComID":"TZ_GD_XGPWD_COM","PageID":"TZ_GD_XGPWD_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
			Ext.tzSubmit(tzParams,function(){
				//修改密码信息表单
				var form = win.child("form").getForm();
				//重置表单
				form.reset();
				//关闭窗口
				win.close();
			},"",true,this);
			
		}
		
		
			
		}
	}, {
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
	modifyPassword: function(win){
		//修改密码信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//表单数据
		var formParams = form.getValues();
		//密码
		if(formParams["newPwd"] != formParams["comfirmPwd"]){
			Ext.MessageBox.alert('提示', '两次密码输入不一致', this);
			return false;
		}
		//新旧密码不能相同
		if(formParams["oldPwd"] == formParams["newPwd"]){
			Ext.MessageBox.alert('提示', '新旧密码不能相同', this);
			return false;
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_XGPWD_COM","PageID":"TZ_GD_XGPWD_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(){
			
		},"",true,this);
	}
});
