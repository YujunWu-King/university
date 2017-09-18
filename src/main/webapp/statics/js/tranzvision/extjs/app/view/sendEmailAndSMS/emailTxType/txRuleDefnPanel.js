Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleDefnPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'txRuleDefnPanel', 
	controller: 'txRuleDefnController',
	requires: [
	    'Ext.data.*',
        'Ext.util.*'
	],
    title: '退信条件定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.ruleId','退信条件ID'),
			name: 'ruleId',
			allowBlank: false
        },{
        	xtype: 'textfield',
        	fieldLabel: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.ruleName','退信条件名称'),
        	name: 'ruleName',
        	allowBlank: false
        },{
            xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.compareType','匹配类型'),
			forceSelection: true,
			editable: false,
			valueField: 'TValue',
            displayField: 'TSDesc',
            queryMode: 'local',
			name: 'compareType',
            allowBlank: false,
            store: new KitchenSink.view.common.store.appTransStore("TZ_TX_MATCH_TYPE")
        },{
        	xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.status','有效状态'),
			forceSelection: true,
			editable: false,
			valueField: 'TValue',
            displayField: 'TSDesc',
            queryMode: 'local',
			name: 'status',
            allowBlank: false,
            store: new KitchenSink.view.common.store.appTransStore("TZ_IS_USED"),
            value: 'Y'
        },{
            xtype: 'textarea',
            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.keyword','匹配关键字'),
			name:'keyword'
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
