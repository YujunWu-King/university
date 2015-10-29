Ext.define('KitchenSink.view.common.bindEmailWindow', {
    extend: 'Ext.window.Window',
    xtype: 'bindEmailWindow', 
    title: '绑定邮箱', 
	id: 'tzBindEmailWindow201505140917',
    width: 500,
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
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 120,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			vtype: 'email',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BEMAIL_COM.TZ_GD_BEMAIL_STD.Email","邮箱"),
			name: 'Email',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			win.modifyEmail(win);			
		}
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			win.modifyEmail(win);
			//修改密码信息表单
			var form = win.child("form").getForm();
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
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
	}
});
