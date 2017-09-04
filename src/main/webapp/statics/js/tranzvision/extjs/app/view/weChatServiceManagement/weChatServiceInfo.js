Ext.define('KitchenSink.view.weChatServiceManagement.weChatServiceInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'weChatServiceInfo', 
	controller: 'weChatServiceController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.weChatServiceManagement.weChatServiceController'
	],
    title: '微信服务号定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'weChatServiceForm',
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
		
        items: [/*{
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.jgId","机构ID"),
			name: 'jgId',
			hidden: true
        },*/ {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.wxName","微信服务号名称"),
			name: 'wxName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.wxId","服务号ID"),
			name: 'wxId',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.wxSecret","服务号Secret"),
			name: 'wxSecret',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
    		xtype: 'combobox',
	        fieldLabel: "有效状态",
	        editable:false,
	        // emptyText:'请选择',
	        queryMode: 'remote',
	    	name: 'wxState',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_WX_STATE"),
    		// allowBlank: false,
			value:'Y'
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.wxParam","指定参数"),
			name: 'wxParam',
        }, /*{
            xtype:'label',
            text:'区分来源指定参数值，参数名统一为from，配置参数示例：from=xxxxxx',
            cls: 'lable_1',
            style:"margin:133px"
        },*/ {
            xtype:'container',
            html:"<span><font color='red'>"+Ext.tzGetResourse("TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.html","区分来源指定参数值，参数名统一为from，配置参数示例：from=xxxxxx")+"</font></span>",
            style:"margin-left:133px"
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'saveInfo'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'esureInfo'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'closeInfo'
	}]
});
