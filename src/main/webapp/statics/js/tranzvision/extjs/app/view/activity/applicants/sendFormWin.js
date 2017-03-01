Ext.define('KitchenSink.view.activity.applicants.sendFormWin', {
    extend: 'Ext.window.Window',
    xtype: 'sendMessagesType',
	
	reference: 'sendFormWindow',
	//controller: 'applicantsMg',
	title: '请选择发送类型',
	width: 330,
    height: 200,
    minWidth: 300,
    minHeight: 200,
  	layout: 'fit',
    resizable: false,
    modal: true,
    closeAction: 'hide',
	/*报名人参数*/
    recArray: [],
	 items: [{
        xtype: 'form',
        reference: 'sendForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		items:[{
			xtype: 'dataview',
            tpl: [
                '<tpl for=".">',
                    '<div class="send-type-item" style="float:left;margin:20px 35px;cursor:pointer">',
                        '<img src="'+ TzUniversityContextPath +'/statics/images/tranzvision/{thumb}" />',
                        '<h3 style="text-align:center;font-size:12px;font-weight:bold;">{name}</h3>',
                    '</div>',
                '</tpl>'
            ],
			itemSelector: 'div.send-type-item',
			//focusCls:'cursor:pointer',
			store: Ext.create('Ext.data.Store', {
                autoLoad: true,
                sortOnLoad: true,
                fields: [{name:'name'}, {name:'thumb'}, {name:'type'}],
                data:[{name:'电子邮件',thumb:'email2.png',type:'E'},{name:'短信',thumb:'sms2.png',type:'M'}/*,{name:'微信',thumb:'wechat2.png',type:'W'}*/]
            }),
			listeners:{
				itemclick:function(view, record, item, index, e){
					var win = view.findParentByType("window");
					var mbrArr = win.recArray;
					var parmJson = "";
					var _bmrJson = "";
					
					parmData = '"activityId":"'+mbrArr[0].data.activityId+'","bmrIds":[';
					
					for(i=0;i<mbrArr.length;i++){
						_bmrJson = _bmrJson + '"' + mbrArr[i].data.applicantsId + '",';
					}
					if (_bmrJson.indexOf(',')) { 
						_bmrJson = _bmrJson.substr(0, _bmrJson.lastIndexOf(',')); 
					}
					parmData = parmData + _bmrJson + "]"

					if (record.data.type == 'E'){
						parmJson = '{"ComID": "TZ_GD_BMRGL_COM","PageID": "TZ_GD_EMAIL_STD","OperateType": "EJSON","comParams": {'+parmData+'}}';
						Ext.tzLoad(parmJson,function(resp){
							win.close();
							Ext.tzSendEmail({
								 //发送的邮件模板;
								 "EmailTmpName": ["TZ_HDBM_DZMP_M","TZ_HDBM_GXH_M"],
								 //创建的需要发送的听众ID;
								 "audienceId": resp.audienceId,
								 //是否有附件: Y 表示可以发送附件,"N"表示无附件;
								 "file": "Y"
							});
						});
						
					}else if (record.data.type == 'M'){
						parmJson = '{"ComID": "TZ_GD_BMRGL_COM","PageID": "TZ_GD_SMS_STD","OperateType": "EJSON","comParams": {'+parmData+'}}';
						Ext.tzLoad(parmJson,function(resp){
							win.close();
							Ext.tzSendSms({
							   //发送的短信模板;
							   "SmsTmpName": ["TZ_HDBM_DZMP_M","TZ_HDBM_GXH_M"],
								//发送的听众;
								"audienceId": resp.audienceId
							});
						});
					}else if (record.data.type == 'W'){
						Ext.Msg.alert("提示信息","系统暂不支持此功能！");
					}
					
					/*张浪注释，改用统一邮件短信发送界面 @20150723
					var parmJson,_bmrJson="";
					var _bmrArr = [];
					var contentPanel,cmp, className, ViewClass;
					var pageName;
					var themeName = Ext.themeName;
					
					contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
					contentPanel.body.addCls('kitchensink-example');
					
					if(index==0){
						//发送电子邮件
						pageName="TZ_GD_EMAIL_STD";
						parmJson = '{"ComID": "TZ_GD_BMRGL_COM","PageID": "TZ_GD_EMAIL_STD","OperateType": "QF","comParams": {';
					}else if(index==1){
						//发送短信
						pageName="TZ_GD_SMS_STD";
						parmJson = '{"ComID": "TZ_GD_BMRGL_COM","PageID": "TZ_GD_SMS_STD","OperateType": "QF","comParams": {';
					}else if(index==2){
						//发送微信
						pageName="TZ_GD_WECHAT_STD";
						parmJson = '{"ComID": "TZ_GD_BMRGL_COM","PageID": "TZ_GD_WECHAT_STD","OperateType": "QF","comParams": {';
					}

					parmJson = parmJson + '"activityId":"'+mbrArr[0].data.activityId+'","bmrIds":[';
					
					for(i=0;i<mbrArr.length;i++){
						_bmrJson = _bmrJson + '"' + mbrArr[i].data.applicantsId + '",';
						_bmrArr[i] = mbrArr[i].data.applicantsId;
					}
					if (_bmrJson.indexOf(',')) { 
						_bmrJson = _bmrJson.substr(0, _bmrJson.lastIndexOf(',')); 
					}
					parmJson = parmJson + _bmrJson + "]}}"
					
					//是否有访问权限
					var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BMRGL_COM"][pageName];
					if( pageResSet == "" || pageResSet == undefined){
						Ext.MessageBox.alert('提示', '您没有修改数据的权限');
						return;
					}
					//该功能对应的JS类
					var className = pageResSet["jsClassName"];
					if(className == "" || className == undefined){
						Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：'+pageName+'，请检查配置。');
						return;
					}
					
					

					if(!Ext.ClassManager.isCreated(className)){
						Ext.syncRequire(className);
					}
					ViewClass = Ext.ClassManager.get(className);
					
					cmp = new ViewClass();
					cmp.bmrIdArr = _bmrArr;
					win.close();	
					
					cmp.on('afterrender',function(panel){
						var field;
						//表单
						var form = panel.child('form').getForm();
						//活动ID
						form.findField('activetyId').setValue(mbrArr[0].data.activityId);
						
						if(index==0){
							//加载邮件模板下拉框的值
							field = form.findField('emailModel');
							Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_BMR_EMAIL_MODE"}',function(respData){
								field.store = new Ext.data.Store({		
									fields: ['TValue', 'TSDesc', 'TLDesc'],
									data:respData.TZ_BMR_EMAIL_MODE,
								});
							})
						}else if(index==1){
							//加载短信模板下拉框的值
							field = form.findField('smsModel');
							Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_BMR_SMS_MODE"}',function(respData){
								field.store = new Ext.data.Store({		
									fields: ['TValue', 'TSDesc', 'TLDesc'],
									data:respData.TZ_BMR_SMS_MODE,
								});
							})
						}

						Ext.tzLoad(parmJson,function(respData){
							form.setValues(respData);
						});
						
						
					});
					
					tab = contentPanel.add(cmp);     
					contentPanel.setActiveTab(tab);
					Ext.resumeLayouts(true);
					if (cmp.floating) {
						cmp.show();
						
					}
					*/
				}	
			}
		}]		
	}],
});
