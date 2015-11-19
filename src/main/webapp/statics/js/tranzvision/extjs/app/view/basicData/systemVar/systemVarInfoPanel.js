Ext.define('KitchenSink.view.basicData.systemVar.systemVarInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'sysVarInfo', 
	actType:'',
	controller: 'systemVarMg',
	requires: [
	    'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
		'KitchenSink.view.basicData.systemVar.systemVarController'
	],
    title: '系统变量定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'systemVarForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.systemVarId","系统变量ID"),
			name: 'systemVarId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold">*</span>'
            ],
            allowBlank: false,
			blankText: '系统变量ID不能为空'
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.systemVarName","系统变量名称"),
			name: 'systemVarName'
        },{
        	  xtype: 'tabpanel',
        	  frame: true,
        	  items:[{
        	  	title: "基本信息",
				bodyPadding: 10,
        	  	layout: {
            		type: 'vbox',
					align: 'stretch',
        		},
        	  	items:[{
					xtype: 'combo',
					labelWidth: 100,
					editable: false,
					fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.isValid", "有效状态"),
					name: 'isValid',
					emptyText: '请选择',
					mode: "remote",
					valueField: 'TValue',
					displayField: 'TSDesc',
					store: new KitchenSink.view.common.store.appTransStore("TZ_ISVALID")
				},{
					xtype: 'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.systemVarDesc","系统变量描述"),
					name: 'systemVarDesc',
					height:200
				}]
        	  },{
        	  	title: "系统变量取值方式",
        	  	bodyPadding: 10,
				layout: {
					  type: 'vbox',
					  align: 'stretch'
				},
        	  	items: [{
					xtype: 'combo',
					labelWidth:	140,
					editable: false,
					fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.sysVarDataType","系统变量数据类型"),
					name: 'sysVarDataType',
					emptyText:'请选择',
					mode:"remote",
					valueField: 'TValue',
            		displayField: 'TSDesc',
					store:new KitchenSink.view.common.store.appTransStore("TZ_SYSVAR_DTYPE")
					
					/*
					listeners: {afterrender: function(tvType){
						Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_SYSVAR_DTYPE"}',function(response){
							tvType.store = new Ext.data.Store({		
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:response.TZ_SYSVAR_DTYPE,
							});
							tvType.setValue("01");
						})
					}}
					*/
			    },{
					xtype: 'fieldset',
					title: '取值方式',
					layout: {
					  	type: 'vbox',
					  	align: 'stretch'
					},
					items:[{
							xtype:'radio',
							boxLabel: 'SQL取值',
                    		name: 'getValType',
                    		inputValue: 'SQL',
							checked: true,
							handler: function(checkbox ,checked){
								if(checked){
									this.findParentByType("form").getForm().findField("sqlValue").setDisabled(false);
									//this.findParentByType("form").getForm().findField("appClass").setDisabled(true);
									//this.findParentByType("form").getForm().findField("constant").setDisabled(true);	
								}else{
									this.findParentByType("form").getForm().findField("sqlValue").setDisabled(true);
								}
							},
						},{
							xtype:'textarea',
							fieldLabel:Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.sqlValue","sql语句"),
							//hideLabel: true,
							labelAlign:'top',
							name: 'sqlValue',
							inputId: 'sqlValue'	,
							margin:'0 0 0 25'
						},{
							xtype:'component',
							hideLabel: true,
							style:'color:#ff0000',
							margin: '0 0 10 25',
							html:['如果SQL语句中需要调用其他系统变量，采用$系统变量编号$的格式，若非内嵌系统变量，请不要使用$符号；</br>',
								'如果想得到日期时间类型的变量值，请在SQL中使用TO_CHAR函数；']	
						},{
							xtype:'radio',
							boxLabel: 'AppClass取值',
                    		name: 'getValType',
                    		inputValue: 'APP',
							handler: function(checkbox ,checked){
								if(checked){
									this.findParentByType("form").getForm().findField("appClass").setDisabled(false);
									//this.findParentByType("form").getForm().findField("sqlValue").setDisabled(true);
									//this.findParentByType("form").getForm().findField("constant").setDisabled(true);	
								}else{
									this.findParentByType("form").getForm().findField("appClass").setDisabled(true);
								}	
							}
						},{
							xtype: 'textfield',
							fieldLabel: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.appClass","应用程序类"),
							name: 'appClass',
							margin:'0 0 0 25',
						},{
							xtype:'radio',
							boxLabel: '常量',
                    		name: 'getValType',
                    		inputValue: 'CON',
							handler: function(checkbox ,checked){
								if(checked){
									this.findParentByType("form").getForm().findField("constant").setDisabled(false);
									//this.findParentByType("form").getForm().findField("sqlValue").setDisabled(true);
									//this.findParentByType("form").getForm().findField("appClass").setDisabled(true);	
								}else{
									this.findParentByType("form").getForm().findField("constant").setDisabled(true);
								}		
							},
						},{
							xtype:'textfield',
							fieldLabel:Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARDY_STD.constant","常量"),
							name:'constant'	,
							margin:'0 0 10 25'
						},{
							xtype:'component',
							hideLabel: true,
							style:'color:#ff0000',
							margin: '0 0 10 130',
							html: ['日期常量写成YYYY-MM-DD的格式；</br>',
								'时间常量写成HH:mm:ss的格式；</br>',
								'日期时间常量写成YYYY-MM-DD HH:mm:ss的格式；']
						}]
				}]
        	}]
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
