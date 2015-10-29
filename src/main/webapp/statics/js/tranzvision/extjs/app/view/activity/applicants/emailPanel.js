Ext.define('KitchenSink.view.activity.applicants.emailPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.activity.applicants.emailModStore',
		'KitchenSink.view.activity.applicants.sendHistoryGrid',
		'KitchenSink.view.activity.applicants.applicantsController'
    ],
    xtype: 'emailInfoPanel',
	controller: 'applicantsMg',
	title: '发送邮件',
	sendType:'E',
	bmrIdArr:[],
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	 items: [{
        xtype: 'form',
        reference: 'emailSendForm',
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
            xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.emailModel","邮件模板"),
			emptyText:"请选择邮件模板",
			allowBlank: false,
			blankText: '邮件模板不能为空',
            valueField: 'TValue',
            displayField: 'TSDesc',
            typeAhead: true,
			mode:"remote",
			name: 'emailModel',
			listeners:{
				change: {
					fn: function(){
						var mode = this.value;
						var obj=this;
						/*
						if (mode=="GXH"){
							this.findParentByType('form').getForm().findField("emailContent").setValue("");
						}else if(mode=="DZMP"){
							this.findParentByType('form').getForm().findField("emailContent").setValue("张三你好，欢迎报名");
						}
						*/
						var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_EMAIL_STD","OperateType":"EMODE","comParams":{"emailMode":"'+mode+'"}}';
						Ext.tzLoad(tzParams,function(respDate){
							var mailContent = respDate.malTmpContent;
							//this.findParentByType('panel').child('form').getForm().setValues([{id:'emailContent', value:'1'}]);	
							obj.findParentByType('form').getForm().findField("emailContent").setValue(mailContent);
							//this.findParentByType('form').getForm().findField("emailContent").setValue("张三你好，欢迎报名");
							
						})
					}
				}
			}
        }, {
            xtype: 'displayfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.sendEmailAddr","发送人"),
			name: 'sendEmailAddr',
			emptyText: '发送人地址',
			allowBlank:false,
			blankText:'发送人地址不能为空',
			//value: 'zhangsan@tranzvision.co.cnm'
        },{
			xtype:'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.acceptEmailAddrs","收件人"),
			name:'acceptEmailAddrs',
			emptyText: '添加收件人，多个收件人之间用“;”隔开',
			readOnly:true,
			allowBlank:false,
			blankText:'收件人地址不能为空',
			regex:/^((([a-zA-Z0-9_\.-]+)@([\da-zA-Z\.-]+)\.([a-zA-Z\.]{2,6}\;))*(([a-zA-Z0-9_\.-]+)@([\da-zA-Z\.-]+)\.([a-zA-Z\.]{2,6})))$/,
			regexText:'邮箱格式不正确'
		},{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.otherEmailAddrs","其他收件人"),
			name:'otherEmailAddrs',
			emptyText: '添加其他收件人,多个收件人之间用“;”隔开',
			regex:/^((([a-zA-Z0-9_\.-]+)@([\da-zA-Z\.-]+)\.([a-zA-Z\.]{2,6}\;))*(([a-zA-Z0-9_\.-]+)@([\da-zA-Z\.-]+)\.([a-zA-Z\.]{2,6})))$/,
			regexText:'邮箱格式不正确'
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.emailTheme","主题"),
			name:'emailTheme',
			emptyText:'输入邮件主题',
			allowBlank:false,
			blankText:'邮件主题不能为空'	
		},{
			xtype: 'ueditor',
			name: 'emailContent',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_EMAIL_STD.emailContent","邮件内容"),
			emptyText: '请在此处输入邮件内容',
			allowBlank:false,
			blankText:'邮件内容不能为空',
			height: 300,
            zIndex: 900,
		},{
			xtype: 'hidden',
			name: 'activetyId'	
		}]
	}],
    buttons: [{
		text: '发送',
		iconCls:"save",
		handler: 'onSendMsg'
	}, {
		text: '查看发送历史',
		iconCls:"ensure",
		handler: 'onCheckHistory'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
	
});
