Ext.define('KitchenSink.view.template.sitetemplate.picEdit', {
    extend: 'Ext.window.Window',
    xtype: 'picEdit', 
    title: '编辑图片', 
    width: 400,
    height: 250,
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
		items: [{
            xtype: 'hiddenfield',
            fieldLabel: '系统文件名',
			name: 'sysFileName',
        	},{
			xtype: 'textfield',
			fieldLabel: '图片描述',
			name: 'caption',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		},{
			xtype: 'numberfield',
			fieldLabel: '排列顺序',
			name: 'index',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false,
			validator:function(value){
				//var num = Ext.getCmp("picView").store.getCount();
				var num = Ext.ComponentQuery.query('dataview[name=picView]')[0].store.getCount();
		        if(value <= 0 || value >= num){
		        	Ext.Msg.alert('提示', "请填写大于0且小于" + num + "的整数");
		            return false;
		        }
		        return true;
		    }
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
