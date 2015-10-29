Ext.define('KitchenSink.view.sendEmailAndSMS.paraMg.paraInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'paraInfo', 
	controller: 'paraInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.paraMg.paraInfoMth',
        'KitchenSink.view.sendEmailAndSMS.paraMg.paraModel'
	],
    title: '函件参数定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'paraForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_SET_STD.paraid','参数编号'),
			name: 'paraid',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
        },{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_SET_STD.chaname','中文名称'),
			name: 'chaname',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		},{
			xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_SET_STD.datatype','数据类型'),
			forceSelection: true,
			editable:false,
			store: new KitchenSink.view.common.store.appTransStore("TZ_DATA_TYPE"),
            valueField: 'TValue',
            displayField: 'TSDesc',
            queryMode: 'remote',
			name: 'datatype',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
        },{
            xtype: 'textarea',
            fieldLabel: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_SET_STD.desc','描述'),
			name:'desc'
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
