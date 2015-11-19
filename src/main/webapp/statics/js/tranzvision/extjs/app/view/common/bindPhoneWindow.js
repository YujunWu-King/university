Ext.define('KitchenSink.view.common.bindPhoneWindow', {
    extend: 'Ext.window.Window',
    xtype: 'bindPhoneWindow', 

    title: '绑定手机', 
	id: 'tzBindPhoneWindow201505131528',
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
		ignoreLabelWidth: true,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 120,
			labelStyle: 'font-weight:bold'
		},

		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BPHONE_COM.TZ_GD_BPHONE_STD.telPhone","手机号"),
			name: 'telPhone',
			regex: /^1[3|4|5|8][0-9]\d{4,8}$/,
			regexText:'格式错误',
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
			win.modifyTelphone(win);			
		}
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			win.modifyTelphone(win);
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
	modifyTelphone: function(win){
	
		//修改密码信息表单
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//表单数据
		var formParams = form.getValues();

		//提交参数
		var tzParams = '{"ComID":"TZ_GD_BPHONE_COM","PageID":"TZ_GD_BPHONE_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(){
			
		},"",true,this);
	}
});
