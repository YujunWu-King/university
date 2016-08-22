Ext.define('KitchenSink.view.content.artMg.picEdit', {
    extend: 'Ext.window.Window',
    xtype: 'picEdit', 
    title: '编辑图片', 
    width: 600,
    height: 300,
    layout: 'fit',
    resizable: true,
    modal: true,
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 100,
			labelStyle: 'font-weight:bold'
		},
		items: [
		{
      xtype: 'hiddenfield',
      fieldLabel: '系统文件名',
			name: 'sysFileName',
    },{
			xtype: 'textareafield',
			fieldLabel: '图片描述',
			height: 130,
			name: 'caption',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		},{
			xtype: 'textfield',
			fieldLabel: '跳转URL',
			name: 'picURL',
		}]
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onPicEditEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onPicEditClose'
	}]
});
