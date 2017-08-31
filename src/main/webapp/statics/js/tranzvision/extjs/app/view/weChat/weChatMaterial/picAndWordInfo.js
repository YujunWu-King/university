Ext.define('KitchenSink.view.weChat.weChatMaterial.picAndWordInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'picAndWordInfo', 
	controller: 'weChatMaterialController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.weChat.weChatMaterial.weChatMaterialController'
	],
    title: "图文素材", 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'picAndWordform',
				layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: "素材名称", 
			name: 'name',
            allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: "备注信息",
			name: 'bzInfo',
			allowBlank: false
        }, {
        	//图文控件
        	
        }, {
            xtype: 'textfield',
			fieldLabel: "发布状态",
			name: 'status',
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: "新增时间",
			name: 'addTime',
			allowBlank: false
        },
        {
            xtype: 'textfield',
			fieldLabel: "新增人",
			name: 'addMan',
			allowBlank: false
        },
        {
            xtype: 'textfield',
			fieldLabel: "修改时间",
			name: 'editTime',
			allowBlank: false
        },
        {
            xtype: 'textfield',
			fieldLabel: "修改人",
			name: 'editMan',
			allowBlank: false
        }]
    }],
    buttons: [{
		text: '发布',
		iconCls:"issue",
		handler: 'pwIssue'
	},{
		text: '撤销发布',
		iconCls:"revoke",
		handler: 'pwRevoke'
	},{
		text: '保存',
		iconCls:"save",
		handler: 'pwSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'pwEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'pwClose'
	}]
});
