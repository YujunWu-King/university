Ext.define('KitchenSink.view.common.commonEmailPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.ux.Ueditor'
  ],
  xtype: 'commonEmailPanel',
	title: '发送邮件',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	initComponent: function(){
		var me = this;
		Ext.apply(this,{
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
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTmp","邮件模板"),
					emptyText:"请选择邮件模板",
					allowBlank: false,
					blankText: '邮件模板不能为空',
		      valueField: 'tmpId',
		      displayField: 'tmpName',
		      typeAhead: true,
					mode:"remote",
					name: 'emailTmp',
					store: {
		    		fields: ['tmpId', 'tmpName'],
		    		data : this.modelData
		    	},
		    	listeners:{
								change: function( emailTmp, newValue, oldValue, eOpts ){
									 var form = emailTmp.findParentByType("form").getForm();
										var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+newValue+'","audienceId":"'+me.audienceId+'"}}';
										//加载数据
										Ext.tzLoadAsync(tzParams,function(responseData){
											  var formData = responseData.formData;
												form.setValues(formData);
												form.findField("emailTmp").setValue(newValue);
										});
								}
					}	
		    },{
		      xtype: 'textfield',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
					name: 'senderEmail',
					readOnly:true,
					allowBlank:false,
					blankText:'发送人地址不能为空'
		    },{
					xtype:'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.AddresseeEmail","收件人"),
					name:'AddresseeEmail',
					readOnly:true,
					allowBlank:false,
					blankText:'收件人地址不能为空'
				},{
					xtype: 'button',
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.viewSJR","查看收件人信息"),
					maxWidth: 170,
					margin: '0 0 10 105',
					handler: function(btn){
						var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonEmailAddresseePanel';
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
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTheme","主题"),
					name:'emailTheme',
					emptyText:'输入邮件主题',
					allowBlank:false,
					blankText:'邮件主题不能为空'	
				},{
		      xtype: 'ueditor',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailContent","邮件内容"),
		      height: 350,
		      zIndex: 900,
					name: 'emailContent'
		    },{
		    	  layout: {
            		type: 'column',
        		},
        		items:[{
        			columnWidth:.2,
	            xtype: "fileuploadfield",
	            fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.attachUpload","附件"), 
							buttonText: '上传',
							name: 'attachUpload',
							buttonOnly:true,
							listeners:{
									change: function(file, value, eOpts){
											addAttach(file, value);
									}
						 }
					},{
        			columnWidth:.2,
	            xtype: "button",
	            fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.attachUpload","附件"), 
							text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.deleteFile","删除"),
							name: 'deleteFile',
							margin: '0 20 10 105',
							minWidth: 120,
							hidden :true,
							listeners:{
									click: function(bt){
										  var fm = bt.findParentByType("form");
											var form = fm.getForm();
											form.findField("fjLj").setValue("");
											form.findField("fjMc").setValue("");
											
											var htmlCom = fm.down("component[name=attachUrl]");
											htmlCom.getEl().dom.innerHTML = "";
											
											var fileuploadfield = fm.down("fileuploadfield[name=attachUpload]");
											fileuploadfield.show();
											bt.hide();
									}
						 }
					},{
						xtype: 'component',
			      html: '',
			      name:'attachUrl'
			    }]	
        },{
		      xtype: 'hidden',
					name: 'fjLj'
		    },{
		      xtype: 'hidden',
					name: 'fjMc'
		    }]
			}],
		  buttons: [{
				text: '预览',
				iconCls:"preview",
				handler: function(btn){
					//获取窗口
						var win = btn.findParentByType("commonEmailPanel");
						//信息表单
						var form = win.child("form").getForm();
						var tmpId = form.findField("emailTmp").getValue();
						var emailTheme = form.findField("emailTheme").getValue();
						var emailContent = form.findField("emailContent").getValue();
					
					  var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonEmailPreviewPanel';
						if(!Ext.ClassManager.isCreated(className)){
							Ext.syncRequire(className);
						}	
						ViewClass = Ext.ClassManager.get(className);
						
						var configuration ={
							 //模板ID;
							 "tmpId": tmpId,
							 "audienceId": me.audienceId,
							 "emailTheme": emailTheme,
							 "emailContent": emailContent
							 
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
				iconCls:"email",
				handler: function(btn){
					Ext.MessageBox.confirm('确认', '确认要发送邮件?', function(btnId){
						if(btnId == 'yes'){
							//获取窗口
							var win = btn.findParentByType("commonEmailPanel");
							//信息表单
							var form = win.child("form").getForm();
							
							var tmpId = form.findField("emailTmp").getValue();
							var editJson = "";
							
							editJson = '{"jgId":"'+ Ext.tzOrgID+'","audienceId":"'+ me.audienceId +'","data":'+ Ext.JSON.encode(form.getValues())+'}';
							
							comParams = '"update":[' + editJson + "]";
							
							var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"U","comParams":{'+ comParams + '}}';
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
						var win = btn.findParentByType("commonEmailPanel");
						//信息表单
						var form = win.child("form").getForm();
						var tmpId = form.findField("emailTmp").getValue();
					
					  var contentPanel,cmp, className, ViewClass;
		
						contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						contentPanel.body.addCls('kitchensink-example');
				
						className = 'KitchenSink.view.common.commonEmailHisPanel';
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
					var win = btn.findParentByType("commonEmailPanel");
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
		//发送的邮件模板;
		this.EmailTmpName = config.EmailTmpName; 

		var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"QG","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpNames":'+Ext.JSON.encode(this.EmailTmpName)+'}}';
		//加载数据
		var tmps;
		Ext.tzLoadAsync(tzParams,function(responseData){
			  tmps = responseData.formData.tmpNames;
		});
		
		this.modelData = tmps;

		//发送的听众;
		this.audienceId = config.audienceId; 
		//是否有附件;
		this.file = config.file; 
    
		this.callParent();
	},
	listeners:{
		resize: function(win){
			win.doLayout();
		},
		afterrender: function(win){
			try{
				
				var form = win.child('form').getForm();
				
				if(this.file != "Y"){
					win.down('fileuploadfield[name=attachUpload]').hide();
				}
	
				var tmpId = this.modelData[0]["tmpId"];
				var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"QF","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpId":"'+tmpId+'","audienceId":"'+this.audienceId+'"}}';
				//加载数据
				var tmps;
				Ext.tzLoadAsync(tzParams,function(responseData){
					  var formData = responseData.formData;
						form.setValues(formData);
						form.findField("emailTmp").setValue(tmpId);
				});
			}catch(e){
				Ext.Msg.alert("提示","模板无效或不存在");
			}
		}
		
	}
	
});


function addAttach(file, value){
	
	var form = file.findParentByType("form").getForm();
	
	var htmlCom = file.findParentByType("commonEmailPanel").down("component[name=attachUrl]");
	
	if(value != ""){
		
	
		//如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
		var dateStr = Ext.Date.format(new Date(), 'Ymd');
		
		//var upUrl = TzUniversityContextPath + '/UpdServlet?filePath=/linkfile/FileUpLoad/imagesWall/'+dateStr;
		var upUrl = TzUniversityContextPath + '/UpdServlet?filePath=email';

		var myMask = new Ext.LoadMask({
	    msg    : '加载中...',
	    target : Ext.getCmp('tranzvision-framework-content-panel')
		});
		
		 myMask.show();
	
		form.submit({
			url: upUrl,
			//waitMsg: '图片正在上传，请耐心等待....',
			success: function (form, action) {
				htmlCom.getEl().dom.innerHTML ='<a target="_blank" href="'+ action.result.msg.accessPath+"/"+action.result.msg.sysFileName+'">'+action.result.msg.filename+'</a>';
				form.findField("fjLj").setValue(action.result.msg.path+"/"+action.result.msg.sysFileName);
				form.findField("fjMc").setValue(action.result.msg.filename);
				
				var deleteFile = file.findParentByType("form").down("button[name=deleteFile]");
				deleteFile.show();
				file.hide();

				myMask.hide();
			},
			failure: function (form, action) {
				myMask.hide();
				Ext.MessageBox.alert("错误", action.result.msg);
			}
		});
		
		
	}
}
