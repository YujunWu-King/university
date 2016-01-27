Ext.define('KitchenSink.view.siteManage.siteManage.template.sitetemplate', {
 extend: 'Ext.window.Window',
 xtype: 'sitetemp', 
 controller: 'siteTemplateInfoI',
 	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.siteManage.siteManage.template.sitetempController',//保存
		//'KitchenSink.view.template.sitetemplate.template.mbstate',
		//'KitchenSink.view.template.sitetemplate.template.mbtype',
	],
    title: '模板设置',
    reference:'sitetemp',
    width:800,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	items:[
	{
        xtype: 'form',
        reference: 'sitetempAccountForm',
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
        items:[{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId'
        	},{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '模板编号',
			name: 'templateId',
			value: ''
        	},{
	            xtype: 'combobox',
	            fieldLabel: '状态',
	            emptyText:'请选择',
	            queryMode: 'remote',
	            editable:false,
				name: 'templateState',
				valueField: 'TValue',
	    		displayField: 'TSDesc',
	    		store: new KitchenSink.view.common.store.appTransStore("TZ_TEMP_STATE")
	        },{
            xtype: 'textfield',
            fieldLabel: '模板名称',
			name: 'templateName',
        	},{
	            xtype: 'combobox',
	            fieldLabel: '模板类型',
	            emptyText:'请选择',
	            queryMode: 'remote',
	            editable:false,
				name: 'templateType',
				valueField: 'TValue',
	    		displayField: 'TSDesc',
	    		store: new KitchenSink.view.common.store.appTransStore("TZ_TEMP_TYPE")
	        },{
            xtype: 'textarea',
            fieldLabel: 'PC模板源码',
            labelSeparator:':',//分隔符
            labelWindth:300,
            height: 110,
			name: 'templatePCCode'
            },{
				xtype: 'textfield',
	            fieldLabel: 'PC端JS代码HTML控件名称',
	            emptyText:'请输入HTML控件名称，比如：TzScriptsNewsList',
				name: 'pcScriptName'
			},{
            xtype: 'textarea',
            fieldLabel: '手机端模板源码',
            labelSeparator:':',//分隔符
            labelWindth:300,
            height: 110,
			name: 'templateMBCode'
            },{
				xtype: 'textfield',
	            fieldLabel: '手机端JS代码HTML控件名称',
	            emptyText:'请输入HTML控件名称，比如：TzScriptsNewsListM',
				name: 'mbScriptName'
			}],
	}],//页面itemend
        
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