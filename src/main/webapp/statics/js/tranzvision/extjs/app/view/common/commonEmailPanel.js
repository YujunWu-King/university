Ext.define('KitchenSink.view.common.commonEmailPanel', {
  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.ux.Ueditor'
  ],
  xtype: 'commonEmailPanel',
	title: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.sendEmail","发送邮件"),
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
		    ignoreLabelWidth: true,
		  	bodyStyle:'overflow-y:auto;overflow-x:hidden',
				
		    fieldDefaults: {
		      msgTarget: 'side',
		      labelWidth: 100,
		      labelStyle: 'font-weight:bold'
		    },
		    items: [{
		      xtype: 'combobox',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTmp","邮件模板"),
				emptyText:Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.eTmpEmptText","请选择邮件模板"),
				allowBlank: false,
				editable: false,
				blankText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.eTmpBlankText","邮件模板不能为空"),
		      valueField: 'tmpId',
		      displayField: 'tmpName',
		      //typeAhead: true,
					mode:"remote",
					name: 'emailTmp',
					ignoreChangesFlag: true,
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
		    	//modity by caoy 2016-6-6
		    	xtype: 'combobox',
			    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
				emptyText:Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.sEmailEmptText","请选择发送人地址"),
				allowBlank: false,
				editable: false,
				blankText:Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.sEmailBlankText","发送人地址不能为空"),
			    valueField: 'tmpId',
			    displayField: 'tmpName',
			      //typeAhead: true,
				mode:"remote",
				name: 'senderEmail',
				ignoreChangesFlag: true,
				store: {
			    	fields: ['tmpId', 'tmpName'],
			    	data : this.semdData
			    }
//		      xtype: 'textfield',
//		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
//					name: 'senderEmail',
//					ignoreChangesFlag: true,
//					readOnly:true,
//					allowBlank:false,
//					blankText:Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.sEmailBlankText","发送人地址不能为空")
		    },{
					xtype:'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.AddresseeEmail","收件人"),
					ignoreChangesFlag: true,
					name:'AddresseeEmail',
					readOnly:true,
					allowBlank:false,
					blankText:Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.aEmailBlankText","收件人地址不能为空")
				},{
					xtype:'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ccAddresseeEmail","抄送"),
					ignoreChangesFlag: true,
					emptyText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ccAddrEmptText","多个邮箱使用英文分号隔开"),
					name:'ccAddresseeEmail'
				},{
					xtype:'textarea',
					fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.bcAddresseeEmail","密送"),
					ignoreChangesFlag: true,
					emptyText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ccAddrEmptText","多个邮箱使用英文分号隔开"),
					name:'bcAddresseeEmail'
				},{
					xtype: 'button',
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.viewSJR","查看收件人信息"),
					ignoreChangesFlag: true,
					maxWidth: 210,
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
					ignoreChangesFlag: true,
					emptyText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.eThemeEmptText","请输入邮件主题"),
					allowBlank:false,
					blankText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.eThemeBlankText","邮件主题不能为空"),
				},{
		      xtype: 'ueditor',
		      fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailContent","邮件内容"),
		      height: 350,
		      zIndex: 900,
		      ignoreChangesFlag: true,
					name: 'emailContent'
		    },{
		    	  layout: {
            		type: 'column',
        		},
        		items:[{
        			columnWidth:.2,
	            xtype: "fileuploadfield",
	            ignoreChangesFlag: true,
	            fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.attachUpload","附件"), 
							buttonText: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.upload","上传"),
							name: 'orguploadfile',
							buttonOnly:true,
							listeners:{
									change: function(file, value, eOpts){
											addAttach(file, value);
									}
						 }
					},{
        			columnWidth:.2,
	            xtype: "button",
	            ignoreChangesFlag: true,
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
								
								var fileuploadfield = fm.down("fileuploadfield[name=orguploadfile]");
								fileuploadfield.show();
								bt.hide();
						}
				 }
			},{
				xtype: 'component',
			      html: '',
			      ignoreChangesFlag: true,
			      name:'attachUrl'
			    }]	
        },{
		      xtype: 'hidden',
		      ignoreChangesFlag: true,
					name: 'fjLj'
		    },{
		      xtype: 'hidden',
		      ignoreChangesFlag: true,
					name: 'fjMc'
		    }]
			}],
		  buttons: [{
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.preview","预览"),
				iconCls:"preview",
				ignoreChangesFlag: true,
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
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.send","发送"),
				ignoreChangesFlag: true,
				iconCls:"send",
				handler: function(btn){
					Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ensure","确认"), 
					Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ensureSendDesc","确认要发送邮件?"), function(btnId){
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
							Ext.tzLoad(tzParams,function(responseData){
								 // var success = responseData.success;
								 // Ext.Msg.alert("提示",success);
							});
							Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.sendEmailEnsure","邮件已发送"));
						}
					},this);
				}
			},{
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.viewEmailHis","查看发送历史"),
				ignoreChangesFlag: true,
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
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
				iconCls:"close",
				ignoreChangesFlag: true,
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonEmailPanel");
					//信息表单
					var form = win.child("form").getForm();
;
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
		this.EmailTmpName = config.EmailTmpName; 

		var tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"QG","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpNames":'+Ext.JSON.encode(this.EmailTmpName)+'}}';
		//加载数据
		var tmps;
		Ext.tzLoadAsync(tzParams,function(responseData){
			  tmps = responseData.formData.tmpNames;
		});
		
		this.modelData = tmps;
		
		//modity by caoy 2016-6-6 加载发件人信息 
		tzParams = '{"ComID":"TZ_COMMON_EMAIL_COM","PageID":"TZ_COM_EMAIL_STD","OperateType":"QG","comParams":{"jgId": "'+ Ext.tzOrgID +'","tmpNames":["sender"]}}'; //["TZ_HDBM_DZMP_M","TZ_HDBM_GXH_M"]
		//加载数据
		Ext.tzLoadAsync(tzParams,function(responseData){
			  tmps = responseData.formData.tmpNames;
		});
		this.semdData = tmps;
		
		

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
					win.down('fileuploadfield[name=orguploadfile]').hide();
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
				Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTmpInvalid","模板无效或不存在"));
			}
		},
		beforeclose: function(pnl){
			pnl.commitChanges(pnl);
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
	    msg    : Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.loading","加载中..."),
	    target : Ext.getCmp('tranzvision-framework-content-panel')
		});
		
		 myMask.show();
	
		form.submit({
			url: upUrl,
			//waitMsg: '图片正在上传，请耐心等待....',
			success: function (form, action) {
				htmlCom.getEl().dom.innerHTML ='<a target="_blank" href="' + TzUniversityContextPath + action.result.msg.accessPath+"/"+action.result.msg.sysFileName+'">'+action.result.msg.filename+'</a>';
				form.findField("fjLj").setValue(action.result.msg.accessPath+"/"+action.result.msg.sysFileName);
				form.findField("fjMc").setValue(action.result.msg.filename);
				
				var deleteFile = file.findParentByType("form").down("button[name=deleteFile]");
				deleteFile.show();
				file.hide();

				myMask.hide();
			},
			failure: function (form, action) {
				myMask.hide();
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"), action.result.msg);
			}
		});
		
		
	}
}
