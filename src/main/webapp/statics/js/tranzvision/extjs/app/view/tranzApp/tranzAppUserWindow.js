Ext.define('KitchenSink.view.tranzApp.tranzAppUserWindow', {
    extend: 'Ext.window.Window',
    xtype: 'tranzAppUserWindow', 
    title: '登录用户对应关系页面', 
	reference: 'tranzAppUserWindow',
    width: 700,
    height: 380,
    minWidth: 300,
    minHeight: 300,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
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
			xtype: 'hiddenfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.jgId","机构id"),
			name: 'jgId'
		},{
			xtype: 'hiddenfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.appId","appId"),
			name: 'appId'
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.otherUserName","第三方用户名"),
			name: 'otherUserName',
			maxLength: 100,
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.userName","姓名"),
			name: 'userName',
			maxLength: 100,
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		},{
			xtype: 'checkboxfield',
			fieldLabel  : Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.isEnable","是否启用"),
			name      : 'isEnable',
			inputValue: 'Y'
		},{
			layout : {
				type : 'column'
			},
			items : [{
				columnWidth : .55,
				xtype : 'textfield',
				fieldLabel : Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TAPPUSER_STD.accountId","指定登录用户"),
				name : 'accountId',
				editable : false,
				triggers : {
					clear : {
						cls : 'x-form-clear-trigger',
						handler : 'clearPmtSearchUser'
					},
					search : {
						cls : 'x-form-search-trigger',
						handler : "pmtSearchUser"
					}
				}
			},
			{
				columnWidth : .45,
				xtype : 'displayfield',
				hideLabel : true,
				style : 'margin-left:5px',
				name : 'accountName'
			}]
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'ontranzAppUserSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'ontranzAppUserEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'ontranzAppUserClose'
	}]
});
