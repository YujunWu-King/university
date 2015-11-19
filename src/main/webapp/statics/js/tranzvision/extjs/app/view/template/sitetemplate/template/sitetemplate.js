﻿Ext.define('KitchenSink.view.template.sitetemplate.template.sitetemplate', {
 	extend: 'Ext.window.Window',
 	xtype: 'sitetemp', 
 	controller: 'siteTemplateInfo',
 	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.template.sitetemplate.template.sitetempController',//保存
		'KitchenSink.view.template.sitetemplate.template.mbstate',
		'KitchenSink.view.template.sitetemplate.template.mbtype',
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
	    		/*
				listeners: {
				  	afterrender: function(tvType){
						Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_TEMP_STATE"}',function(response){
							tvType.setStore(new Ext.data.Store({		
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:response.TZ_TEMP_STATE
							}));
						});
					}
				}*/
	        },{
            xtype: 'textfield',
            fieldLabel: '模板名称',
			name: 'templateName'
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
	    		/*
				listeners: {
				  	afterrender: function(tvType){
						Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_TEMP_TYPE"}',function(response){
							tvType.setStore(new Ext.data.Store({		
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:response.TZ_TEMP_TYPE
							}));
						});
					}
				}*/
	        },{
            xtype: 'textarea',
            fieldLabel: 'PC模板源码',
            labelSeparator:':',//分隔符
            labelWindth:300,
            height: 150,
			name: 'templatePCCode'
            },{
            xtype: 'textarea',
            fieldLabel: '手机端模板源码',
            labelSeparator:':',//分隔符
            labelWindth:300,
            height: 150,
			name: 'templateMBCode'
            }]
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