Ext.define('KitchenSink.view.common.commonEmailPreviewPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*'
  ],
  xtype: 'commonEmailPreviewPanel',
	title: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.preview","预览"),
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
			//minHeight: 800,
		    items: [{
		      xtype: 'hidden',
					name: 'audCyrTotal'
		    },{
		      xtype: 'hidden',
					name: 'currentPageNum'
		    },{
		      xtype: 'displayfield',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
					name: 'senderEmail'
		    },{
				xtype:'displayfield',
				fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.AddresseeEmail","收件人"),
				name:'AddresseeEmail'
			},{
				xtype: 'displayfield',
				fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTheme","主题"),
				name:'emailTheme'
			}]
		  },{
				xtype: 'component',
				padding: 10,
		      	fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailContent","邮件内容"),
		      	html: me.emailContentHtml,
		      	name:'emailContentHtml'
		  }],
		  buttons: [{
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.previous","上一页"),
				iconCls:"prev",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonEmailPreviewPanel");
					//信息表单
					var form = win.child("form").getForm();
					var htmlCom = win.down("component[name=emailContentHtml]");
					var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
					if(currentPageNum > 1){
						currentPageNum = currentPageNum - 1;
						var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_EML_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+me.tmpId+'","emailTheme":'+Ext.encode(me.emailTheme)+',"emailContent":'+Ext.encode(me.emailContent)+',"audienceId":"'+me.audienceId+'","showNum":"'+currentPageNum+'"}}';
						//加载数据
						Ext.tzLoadAsync(tzParams,function(responseData){
								  var formData = responseData.formData;
									form.setValues(formData);
									//htmlCom.getEl().dom.innerHTML = formData.emailContent;
                                    htmlCom.getEl().update(formData.emailContent);
									htmlCom.updateLayout();
							});
					}else{
						Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.firstPage","已经是第一页"));
					}
				}
			},{
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.next","下一页"),
				iconCls:"next",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonEmailPreviewPanel");
					//信息表单
					var form = win.child("form").getForm();
					var htmlCom = win.down("component[name=emailContentHtml]");
					var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
					var audCyrTotal = parseInt(form.findField("audCyrTotal").getValue());
					if(currentPageNum < audCyrTotal){
						currentPageNum = currentPageNum + 1;
						var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_EML_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+me.tmpId+'","emailTheme":'+Ext.encode(me.emailTheme)+',"emailContent":'+Ext.encode(me.emailContent)+',"audienceId":"'+me.audienceId+'","showNum":"'+currentPageNum+'"}}';
						//加载数据
						Ext.tzLoadAsync(tzParams,function(responseData){
								  var formData = responseData.formData;
									form.setValues(formData); 
									//htmlCom.getEl().dom.innerHTML = formData.emailContent;
									htmlCom.getEl().update(formData.emailContent);
									htmlCom.updateLayout();						
							});
					}else{
						 Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.lastPage","已经是最后一页"));
					}
				}
			},{
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
				iconCls:"close",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonEmailPreviewPanel");
					//信息表单
					var form = win.child("form").getForm();
					//关闭窗口
					win.close();
				}
			}]	
		});
		this.callParent();
	},
	constructor: function (config) {
		var meThis = this;
		//发送的听众;
		this.audienceId = config.audienceId; 
		//模板id;
		this.tmpId = config.tmpId; 
		//内容;
		this.emailContentHtml = ""; 
		//主题; 
		this.emailTheme = config.emailTheme;
		//邮件内容;
		this.emailContent = config.emailContent;
		
		var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_EML_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+this.tmpId+'","emailTheme":'+Ext.encode(this.emailTheme)+',"emailContent":'+Ext.encode(this.emailContent)+',"audienceId":"'+this.audienceId+'","showNum":"1"}}';
		//加载数据
		Ext.tzLoadAsync(tzParams,function(responseData){
				  var formData = responseData.formData;
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
