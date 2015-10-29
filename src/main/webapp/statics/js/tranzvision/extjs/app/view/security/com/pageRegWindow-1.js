Ext.define('KitchenSink.view.security.com.pageRegWindow', {
    extend: 'Ext.window.Window',
    xtype: 'pageRegWindow', 
    title: '页面注册信息', 
	reference: 'pageRegWindow',
    width: 600,
    height: 500,
    minWidth: 300,
    minHeight: 380,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	actType: 'add',
	
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
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.pageID","页面编号"),
			name: 'pageID',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.pageName","页面名称"),
			name: 'pageName'
		}, {
			xtype: 'numberfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.orderNum","序号"),
			name: 'orderNum',
			minValue: 1
		},{
			xtype: 'checkboxfield',
			fieldLabel  : Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.isExURL","外部链接"),
			name      : 'isExURL',
			inputValue: 'Y'
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.exURL","外部URL"),
			name: 'exURL'
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.jsClass","客户端页面处理JS类"),
			name: 'jsClass'
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.appClass","服务端页面处理类"),
			name: 'appClass'
		},{
			xtype: 'checkboxfield',
			fieldLabel  : Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.isExURL","默认首页"),
			name      : 'isDefault',
			inputValue: 'Y'
		},{
			xtype: 'checkboxfield',
			fieldLabel  : Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.isExURL","新开窗口"),
			name      : 'isNewWin',
			inputValue: 'Y'
		}, {
			xtype: 'hiddenfield',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.comID","组件ID"),
			name: 'comID'
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onPageRegSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onPageRegEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onPageRegClose'
	}]
});
