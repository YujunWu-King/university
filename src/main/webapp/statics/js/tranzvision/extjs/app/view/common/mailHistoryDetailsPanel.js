/*=============================================================
说明：历史邮件详情页面，张浪，20151113
============================================================*/
Ext.define('KitchenSink.view.common.mailHistoryDetailsPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*'
  ],
  xtype: 'mailHistoryDetailsPanel',
	title: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.mailHistoryDetails","邮件发送详情"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	initComponent: function(){
		var me = this;
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				ignoreLabelWidth: true,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
				  msgTarget: 'side',
				  labelWidth: 100,
				  labelStyle: 'font-weight:bold'
				},
				autoHeight:true,
				items: [{
						xtype: 'displayfield',
						fieldLabel: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.senderEmail","发送人"),
						name: 'senderEmail'
					},{
						xtype:'displayfield',
						fieldLabel: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.AddresseeEmail","收件人"),
						name:'AddresseeEmail'
					},{
						xtype: 'displayfield',
						fieldLabel: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.emailTheme","主题"),
						name:'emailTheme'
					},{
						xtype: 'displayfield',
						fieldLabel: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.sendTime","发送时间"),
						name:'sendTime'
					}]
			},{
				xtype: 'component',
				fieldLabel: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.emailContent","邮件内容")	,
				html: me.emailContentHtml,
				name:'emailContentHtml',
				padding: 10
			}],
		  	buttons: [{
				text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MAIL_LSXQ_STD.close","关闭"),
				iconCls:"close",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("mailHistoryDetailsPanel");
					//关闭窗口
					win.close();
				}
			}]	
		});
		this.callParent();
	},
	constructor: function (rwInsId) {
		var meThis = this;
		//任务实例ID;
		this.rwInsID = rwInsId; 
		
		var tzParams = '{"ComID":"TZ_MAIL_HISTORY_COM","PageID":"TZ_MAIL_LSXQ_STD","OperateType":"QF","comParams":{"rwInsID":"'+this.rwInsID+'"}}';
		//加载数据
		Ext.tzLoadAsync(tzParams,function(formData){
			meThis.formData = formData;
			meThis.emailContentHtml = formData.emailContent;
		});
		this.callParent();
	},
	listeners:{
		resize: function(win){
			win.doLayout();
		},
		afterrender: function(win){
			var form = win.child('form').getForm();
			form.setValues(this.formData);
			
		}
	}
	
});
