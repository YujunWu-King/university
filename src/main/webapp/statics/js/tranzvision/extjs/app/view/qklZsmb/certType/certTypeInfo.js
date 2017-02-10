Ext.define('KitchenSink.view.qklZsmb.certType.certTypeInfo', {
	extend: 'Ext.window.Window',
    reference: 'certTypeInfo',
    xtype: 'certTypeInfo',
    controller:'certTypeController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.qklZsmb.certType.certTypeController'
    ],
    width: 500,
    height: 160,
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
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_INFO_STD.JgId","机构编号"),
			name: 'JgId',
			hidden:true,
			value:Ext.tzOrgID
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_INFO_STD.certTypeId","证书类型编号"),
			name: 'certTypeId',
			maxLength: 15,
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_INFO_STD.certName","证书名称"),
			name: 'certName',
			afterLabelTextTpl: [
			    				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			    			],
			allowBlank: false
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onCertTypeSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onCertTypeEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onCertTypeClose'
	}]
});
