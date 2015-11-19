Ext.define('KitchenSink.view.common.commonSmsPreviewPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*'
  ],
  xtype: 'commonSmsPreviewPanel',
	title: '预览',
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
		    items: [{
		      xtype: 'hidden',
					name: 'audCyrTotal'
		    },{
		      xtype: 'hidden',
					name: 'currentPageNum'
		    },{
					xtype:'displayfield',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_SMS_PREVIEW_STD.AddresseeSms","接收人"),
					name:'AddresseeSms'
				},{
					xtype: 'textarea',
					readOnly : true,
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_SMS_PREVIEW_STD.smsContent","短信内容"),
		      height: 250,
		      name:'smsContent'
		    }]
			}],
		  buttons: [{
				text: '上一页',
				iconCls:"save",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonSmsPreviewPanel");
					//信息表单
					var form = win.child("form").getForm();
					
					var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
					if(currentPageNum > 1){
						currentPageNum = currentPageNum - 1;
						var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_SMS_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+me.tmpId+'","smsContent":'+Ext.encode(me.smsContent)+',"audienceId":"'+me.audienceId+'","showNum":"'+currentPageNum+'"}}';
						//加载数据
						Ext.tzLoadAsync(tzParams,function(responseData){
								  var formData = responseData.formData;
									form.setValues(formData);
							});
					}else{
						Ext.Msg.alert("提示","已经是第一页");
					}
				}
			},{
				text: '下一页',
				iconCls:"save",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonSmsPreviewPanel");
					//信息表单
					var form = win.child("form").getForm();
					
					var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
					var audCyrTotal = parseInt(form.findField("audCyrTotal").getValue());
					if(currentPageNum < audCyrTotal){
						currentPageNum = currentPageNum + 1;
						var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_SMS_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+me.tmpId+'","smsContent":'+Ext.encode(me.smsContent)+',"audienceId":"'+me.audienceId+'","showNum":"'+currentPageNum+'"}}';
						//加载数据
						Ext.tzLoadAsync(tzParams,function(responseData){
								  var formData = responseData.formData;
									form.setValues(formData);
							});
					}else{
						 Ext.Msg.alert("提示","已经是最后一页");
					}
				}
			},{
				text: '关闭',
				iconCls:"close",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonSmsPreviewPanel");
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

    //短信内容;
    this.smsContent = config.smsContent;
    
    var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_SMS_PREVIEW_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+this.tmpId+'","smsContent":'+Ext.encode(this.smsContent)+',"audienceId":"'+this.audienceId+'","showNum":"1"}}';
		//加载数据
		Ext.tzLoadAsync(tzParams,function(responseData){
				  var formData = responseData.formData;
					meThis.formData = formData;
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
