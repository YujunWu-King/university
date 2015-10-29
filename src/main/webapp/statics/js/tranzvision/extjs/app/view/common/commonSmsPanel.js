Ext.define('KitchenSink.view.common.commonSmsPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.ux.Ueditor'
  ],
  xtype: 'commonSmsPanel',
	title: '发送短信',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	initComponent: function(){
		var me = this;
		Ext.apply(this,{
			items: [{
		    xtype: 'form',
		    reference: 'smsSendForm',
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
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_COM_SMS_STD.smsTmp","短信模板"),
					emptyText:"请选择短信模板",
					allowBlank: false,
					blankText: '短信模板不能为空',
		      valueField: 'tmpId',
		      displayField: 'tmpName',
		      editable: false,
		      //typeAhead: true,
					mode:"remote",
					name: 'smsTmp',
					store: {
		    		fields: ['tmpId', 'tmpName'],
		    		data : this.modelData
		    	},
		    	listeners:{
								change: function( smsTmp, newValue, oldValue, eOpts ){
									 var form = smsTmp.findParentByType("form").getForm();
										var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_COM_SMS_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+newValue+'","audienceId":"'+me.audienceId+'"}}';
										//加载数据
										Ext.tzLoadAsync(tzParams,function(responseData){
											  var formData = responseData.formData;
												form.setValues(formData);
												form.findField("smsTmp").setValue(newValue);
										});
								}
					}	
		    },{
					xtype:'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_COM_SMS_STD.AddresseeSms","接收人"),
					name:'AddresseeSms',
					readOnly:true,
					allowBlank:false,
					blankText:'接收人不能为空'
				},{
					xtype: 'button',
					text: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_COM_SMS_STD.viewButton","查看接收人信息"),
					maxWidth: 170,
					margin: '0 0 10 105',
					handler: function(btn){
						var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonSmsAddresseePanel';
						if(!Ext.ClassManager.isCreated(className)){
							Ext.syncRequire(className);
						}	
						ViewClass = Ext.ClassManager.get(className);
						
						var configuration ={
							 //发送的听众;
							 "audienceId": me.audienceId
							 
						}
						
						cmp = new ViewClass(configuration);
						
						tab = contentPanel.add(cmp);     
						
						contentPanel.setActiveTab(tab);
				
						Ext.resumeLayouts(true);
				
						if (cmp.floating) {
							cmp.show();
						}
					}
				},{
		      xtype: 'textarea',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_SMS_COM.TZ_COM_SMS_STD.smsContent","短信内容"),
					name: 'smsContent',
					height: 250,
		    }]
			}],
		  buttons: [{
				text: '预览',
				iconCls:"preview",
				handler: function(btn){
					//获取窗口
						var win = btn.findParentByType("commonSmsPanel");
						//信息表单
						var form = win.child("form").getForm();
						var tmpId = form.findField("smsTmp").getValue();
						var smsContent = form.findField("smsContent").getValue();
					
					  var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonSmsPreviewPanel';
						if(!Ext.ClassManager.isCreated(className)){
							Ext.syncRequire(className);
						}	
						ViewClass = Ext.ClassManager.get(className);
						
						var configuration ={
							 //模板ID;
							 "tmpId": tmpId,
							 "audienceId": me.audienceId,
							 "smsContent": smsContent
							 
						}
						
						cmp = new ViewClass(configuration);
						
						tab = contentPanel.add(cmp);     
						
						contentPanel.setActiveTab(tab);
				
						Ext.resumeLayouts(true);
				
						if (cmp.floating) {
							cmp.show();
						}
				}
			}, {
				text: '发送',
				iconCls:"sms",
				handler: function(btn){
					Ext.MessageBox.confirm('确认', '确认要发送短信?', function(btnId){
						if(btnId == 'yes'){
							//获取窗口
							var win = btn.findParentByType("commonSmsPanel");
							//信息表单
							var form = win.child("form").getForm();
							
							var tmpId = form.findField("smsTmp").getValue();
							var editJson = "";
							
							editJson = '{"jgId":"'+ Ext.tzOrgID+'","audienceId":"'+ me.audienceId +'","data":'+ Ext.JSON.encode(form.getValues())+'}';
							
							comParams = '"update":[' + editJson + "]";
							
							var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_COM_SMS_STD","OperateType":"U","comParams":{'+ comParams + '}}';
							//加载数据
							var tmps;
							Ext.tzLoadAsync(tzParams,function(responseData){
								  var success = responseData.success;
								  Ext.Msg.alert("提示",success);
							});
						}
					},this);
				}
			},{
				text: '查看发送历史',
				iconCls:"view",
				handler: function(btn){
					  //获取窗口
						var win = btn.findParentByType("commonSmsPanel");
						//信息表单
						var form = win.child("form").getForm();
						var tmpId = form.findField("smsTmp").getValue();
					
					  var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonSmsHisPanel';
						if(!Ext.ClassManager.isCreated(className)){
							Ext.syncRequire(className);
						}	
						ViewClass = Ext.ClassManager.get(className);
						
						var configuration ={
							 //模板ID;
							 "tmpId": tmpId
							 
						}
						
						cmp = new ViewClass(configuration);
						
						tab = contentPanel.add(cmp);     
						
						contentPanel.setActiveTab(tab);
				
						Ext.resumeLayouts(true);
				
						if (cmp.floating) {
							cmp.show();
						}
				}
			},{
				text: '关闭',
				iconCls:"close",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonSmsPanel");
					//信息表单
					var form = win.child("form").getForm();
					
					win.commitChanges(win);
					
					//关闭窗口
					win.close();
				}
			}]	
		});
		this.callParent();
	},
	constructor: function (config) {
		//发送的邮件模板;
		this.SmsTmpName = config.SmsTmpName; 
		console.log(this.SmsTmpName);

		var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_COM_SMS_STD","OperateType":"QG","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpNames":'+Ext.JSON.encode(this.SmsTmpName)+'}}';
		//加载数据
		var tmps;
		Ext.tzLoadAsync(tzParams,function(responseData){
			  tmps = responseData.formData.tmpNames;
		});
		
		this.modelData = tmps;

		//发送的听众;
		this.audienceId = config.audienceId; 
    
		this.callParent();
	},
	listeners:{
		resize: function(win){
			win.doLayout();
		},
		afterrender: function(win){
			
			try{
			
			var tmpId = this.modelData[0]["tmpId"];
			var form = win.child('form').getForm();
			
			var tzParams = '{"ComID":"TZ_COMMON_SMS_COM","PageID":"TZ_COM_SMS_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+tmpId+'","audienceId":"'+this.audienceId+'"}}';
			//加载数据
			var tmps;
			Ext.tzLoadAsync(tzParams,function(responseData){
				  var formData = responseData.formData;
					form.setValues(formData);
					form.findField("smsTmp").setValue(tmpId);
			});
			}catch(e){
				Ext.Msg.alert("提示","模板无效或不存在");
			}
		},
		beforeclose: function(pnl){
			pnl.commitChanges(pnl);
		}
		
	}
	
});