Ext.define('KitchenSink.view.callCenter.viewUserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.viewUserController', 
	requires: [
       'Ext.ux.IFrame'
    ],
    
    createUserInfoClass: function(){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_USERINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_USERINFO_STD，请检查配置。');
			return;
		}
		
    	var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
	
        if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
        ViewClass = Ext.ClassManager.get(className);

        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        cmp = new ViewClass();
		
        
        
        return cmp;
    },
    viewUserBtn: function(){
    	Ext.tzSetCompResourses("TZ_UM_USERMG_COM");
		 //选中行
		var form = this.getView().lookupReference("userForm");		
		var formValues = form.getValues();
	    var OPRID = formValues.oprId;
		
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
  		contentPanel.body.addCls('kitchensink-example');
	
		var cmp = this.createUserInfoClass();		
	
		cmp.on('afterrender',function(){
			var msgForm = this.lookupReference('userMgForm');
			var form = this.lookupReference('userMgForm').getForm();
			var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]').getForm();
			var processInfoForm =this.lookupReference('userMgForm').down('form[name=processInfoForm]').getForm();
			//var ksdrInfoForm =this.lookupReference('userMgForm').down('form[name=ksdrInfoForm]').getForm();

			var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
			var formData = responseData.formData;
		
			form.setValues(formData);
			
			//考生导入信息;
			//ksdrInfoForm.setValues(formData.ksdrInfo);
			//考生个人信息
			userInfoForm.setValues(formData.perInfo)
			//录取流程
			processInfoForm.setValues(formData.lqlcInfo);
			
			if(msgForm.down('hiddenfield[name=titleImageUrl]').getValue()){
				msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + msgForm.down('hiddenfield[name=titleImageUrl]').getValue());	
			}else{
				msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
			}
		  });
		 
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);
		
		Ext.resumeLayouts(true);
		
		if (cmp.floating) {
		    cmp.show();
		}
		 
    },
    viewThisApplicationForm : function(grid, rowIndex, colIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}

		var store = grid.getStore();
		var record = store.getAt(rowIndex);
		
		var classID = record.get("classID");
		var oprID = record.get("oprID");
		var appInsID = record.get("insID");
		var clpsBmbTplId = record.get("clpsBmbTplId");
        
		if (appInsID != "") {
			var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"' + appInsID + '","OPRID":"' + oprID + '","TZ_APP_TPL_ID":"' + clpsBmbTplId + '","isReview":"Y"}}';
			var viewUrl = Ext.tzGetGeneralURL() + "?tzParams="	+ encodeURIComponent(tzParams);
			var mask;
			var win = new Ext.Window(
					{
						name : 'applicationFormWindow',
						title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
						maximized : true,
						controller : 'viewUserController',
						classID : classID,
						oprID : oprID,
						appInsID : appInsID,
						gridRecord : record,
						width : Ext.getBody().width,
						height : Ext.getBody().height,
						autoScroll : true,
						border : false,
						bodyBorder : false,
						isTopContainer : true,
						modal : true,
						resizable : false,
						items : [ new Ext.ux.IFrame(
								{
									xtype : 'iframepanel',
									layout : 'fit',
									style : "border:0px none;scrollbar:true",
									border : false,
									src : viewUrl,
									height : "100%",
									width : "100%"
								}) 
						],
						buttons : [
								{
									text : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
									iconCls : "send",
									handler : "auditApplicationForm"
								},
								{
									text : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
									iconCls : "close",
									handler : function() {
										win.close();
									}
								} 
						]
					})
			win.show();
		} else {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
		}
	 },
	 onClose:function(){
		 this.getView().close();
	 },
	 activeAccount:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"ACTIVE","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrAccActiveStatus]").setValue("已激活");
			 }
		 });
	 },
	 updatePsw:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"UPDATEPSW","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){});
	 },
	 invalidAccount:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"INVALID","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrLockStatus]").setValue("锁定");
			 }			 
		 });
	 },
	 addBlackList:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"ADDBALCK","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrBlackList]").setValue("是");
			 }
		 });
	 },
	 saveInfo:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 console.log(formValues);
		 
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var dealwithZT = formValues.dealwithZT;
		 var callDesc = formValues.callDesc;
		 var tzParams = {
		     ComID:"TZ_CALLCR_USER_COM",
		     PageID:"TZ_CALLC_USER_STD",
		     OperateType:"SAVEINFO",
		     comParams:{
		    	 OPRID:oprId,
		    	 callXh:callXh,
		    	 dealwithZT:dealwithZT,
		    	 callDesc:callDesc
		     }
		 }
		 //var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SAVEINFO","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '","dealwithZT":"' + dealwithZT + '","callDesc":"' + Ext.JSON.encode(callDesc) + '"}}';
		 Ext.tzSubmit(Ext.JSON.encode(tzParams),function(response){ }); 
	 },
	 search:function(){
		 var me = this;
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 console.log(formValues);
		 var callXh = formValues.receiveId;
		 var searchName = formValues.searchName;
		 var searchPhone = formValues.searchPhone;
		 var searchEmail = formValues.searchEmail;
		 if(searchName!=""||searchPhone!=""||searchEmail!=""){
			 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SEARCHUSER","comParams":{"phone":"' + searchPhone + '","email":"' + searchEmail + '","callXh":"' + callXh + '","name":"' + searchName + '"}}';
			 Ext.tzLoadAsync(tzParams,function(response){
				oprid = response.OPRID;
				historyCount = response.viewHistoryCall;
				if(oprid!=null&&oprid!=undefined&&oprid!=""){
					tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"QF","comParams":{"OPRID":"' + oprid + '","callXh":"' + callXh + '","phone":"' + searchPhone +'","type":""}}';
					var formData;
					Ext.tzLoadAsync(tzParams,function(response){
						//formData = response;
						form.getForm().setValues(response);						
					});	
					var Grid = form.down("grid[name=bmInfoList]");
					var store = Grid.getStore();
					var tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_USER_STD.TZ_USER_CALL1_VW","condition":{"OPRID-operator": "01","OPRID-value": "'+ oprid+'"}}';

					store.tzStoreParams = tzStoreParams;
					store.load();
					//将按钮恢复点击
					me.disabledButton(form,false);
				}else{
					Ext.MessageBox.alert("提示","搜索不到考生信息");
				}
			 });
		 }else{
			 Ext.MessageBox.alert("提示","请输入搜索条件，搜索考生");
		 }
	 },
	 
	 //可配置搜索
	 searchCallList: function(btn){
		 Ext.tzShowCFGSearch({
			 cfgSrhId: 'TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW',
			 callback: function(seachCfg){
				 var store = btn.findParentByType("grid").store;
				 store.tzStoreParams = seachCfg;
				 store.load();
			 }
		 });	
	 },
	 viewCallData:function(view, rowIndex, colIndex, item, e, record, row){
		 var me = this;
		 	var phone = record.data.callPhone;
		 	var type = record.data.callType;
		 	var callXh = record.data.receiveId;
		 	console.log(record.data);
		 	//是否有访问权限
		 	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_USER_STD"];
		 	if( pageResSet == "" || pageResSet == undefined){
			 	Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_USER_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
			contentPanel.body.addCls('kitchensink-example');

			//className = 'KitchenSink.view.basicData.systemVar.systemVarInfoPanel';
			if(!Ext.ClassManager.isCreated(className)){
				Ext.syncRequire(className);
			}	
			ViewClass = Ext.ClassManager.get(className);

			clsProto = ViewClass.prototype;

			if (clsProto.themes) {
				clsProto.themeInfo = clsProto.themes[themeName];

				if (themeName === 'gray') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
				} else if (themeName !== 'neptune' && themeName !== 'classic') {
					if (themeName === 'crisp-touch') {
						clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
					}
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
				}
				// <debug warn>
				// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
				if (!clsProto.themeInfo) {
					Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
						themeName + '\'. Is this intentional?');
				}
				// </debug>
			}
			
			cmp = new ViewClass();
			cmp.actType="edit";
			
			cmp.on('afterrender',function(panel){
				//用户账号信息表单
				var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"GETUSER","comParams":{"phone":"' + phone + '","type":"' + type + '","callXh":"' + callXh + '"}}';
				var oprid = "";
				var historyCount;
				
				Ext.tzLoadAsync(tzParams,function(response){
					oprid = response.OPRID;
					historyCount = response.viewHistoryCall;
				});
				
				
				var _this = panel.child('form');
				
				var form = _this.getForm();
				var Grid = _this.down("grid[name=bmInfoList]");
				
				
				tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"QF","comParams":{"OPRID":"' + oprid + '","type":"' + type + '","callXh":"' + callXh + '","phone":"' + phone +'"}}';
				
				Ext.tzLoadAsync(tzParams,function(responseData){
					//系统变量信息数据
					form.setValues(responseData);
					
					if(responseData.titleImageUrl){
						_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + responseData.titleImageUrl);	
					}else{
						_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
					}
				});
				
				var buttonT = panel.child('form').down("button[name=historyCount]");
				buttonT.setText('<span style="text-decoration:underline;color:blue;">查看历史来电记录（' + historyCount + '）</span>');
				
				if(oprid==null||oprid==""||oprid==undefined){
					/*禁用按钮*/
					me.disabledButton(_this,true);
				}else{
					var store = Grid.getStore();
					var tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_USER_STD.TZ_USER_CALL1_VW","condition":{"OPRID-operator": "01","OPRID-value": "'+ oprid+'"}}';

					store.tzStoreParams = tzStoreParams;
					store.load();
				}
			});
			
			tab = contentPanel.add(cmp);     
			
			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
	 },
	 viewHistoryCall:function(btn){
		 Ext.tzSetCompResourses("TZ_CALLCR_USER_COM");
		 
		 var form = this.getView().lookupReference("userForm");
		 var callXh = form.down("textfield[name=receiveId]").getValue();
		 var phone = form.down("textfield[name=phoneNum]").getValue();
		 var name = form.down("displayfield[name=bmrName]").getValue();
		 var gender = form.down("displayfield[name=bmrGender]").getValue();
 		 //是否有访问权限
	     var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_HIST_STD"];
	     if( pageResSet == "" || pageResSet == undefined){
	         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	         return;
	     }
	     //该功能对应的JS类
	     var className = pageResSet["jsClassName"];
	     if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_HIST_STD，请检查配置。');
	         return;
	     }

	     var win = this.lookupReference('historyCallWindow');

	     if (!win) {
	    	 Ext.syncRequire(className);
	         ViewClass = Ext.ClassManager.get(className);
	         //新建类
	         win = new ViewClass();
	         this.getView().add(win);
	     }

	      //操作类型设置为更新
	      win.actType = "update";

	      var form = win.child("form").getForm();
	      var pageGrid = Ext.getCmp('historyCallGrid');

	      form.setValues(
	            [
	                {id:'callPhone', value:phone},
	                {id:'bmrName', value:name},
	                {id:'bmrGender',value:gender}
	            ]
	        );
	        
	        pageGrid.store.tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW","condition":{"TZ_PHONE-operator": "01","TZ_PHONE-value": "'+ phone+'","TZ_XH-operator":"02","TZ_XH-value":"' + callXh + '"}}';
	        pageGrid.store.load();

	        win.show();
	 },
	 
	 disabledButton: function(form,disalbed){
		 
		 form.down("button[name=viewUserBtn]").disabled=disalbed;		 
		 form.down("button[name=activeAccount]").disabled=disalbed;		 
		 form.down("button[name=updatePsw]").disabled=disalbed;		 
		 form.down("button[name=invalidAccount]").disabled=disalbed;		 
		 form.down("button[name=addBlackList]").disabled=disalbed;
		 
		 if(disalbed){
			 form.down("button[name=viewUserBtn]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=activeAccount]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=updatePsw]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=invalidAccount]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=addBlackList]").addCls('x-item-disabled x-btn-disabled');
		 }else{
			 form.down("button[name=viewUserBtn]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=activeAccount]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=updatePsw]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=invalidAccount]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=addBlackList]").removeCls('x-item-disabled x-btn-disabled');
		 }
	 }
});