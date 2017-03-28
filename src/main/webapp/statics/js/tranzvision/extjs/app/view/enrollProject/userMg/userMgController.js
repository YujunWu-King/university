Ext.define('KitchenSink.view.enrollProject.userMg.userMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.userMgController', 
	requires: [
       'KitchenSink.view.enrollProject.userMg.userMgInfoPanel'
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
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_YHZHXX_STD，请检查配置。');
			return;
		}
		
    	var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        //contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		//contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.security.user.userInfoPanel';
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
    //查询
    queryUser:function(btn){
    	//搜索条件过多，无法使用可配置搜索
		/*Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.TZ_REG_USER_V',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            }, 
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	*/
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_MGSEARCH_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_MGSEARCH_STD，请检查配置。');
            return;
        }
        
        var win = this.lookupReference('userCfgSearch');
        
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        win.show();
    },
    saveUserMgInfos: function(btn){
		//组件注册信息列表
		var grid = btn.findParentByType("grid");
		//组件注册信息数据
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	onListClose: function(btn){
		//组件注册信息列表
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	viewUser: function(){
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要查看的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要查看的记录");   
		   return;
	    }
	    var OPRID = selList[0].get("OPRID");
   		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_USERINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_USERINFO_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('userMgInfoPanel');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		var form = win.child('form').getForm();
		form.findField("OPRID").setReadOnly(true);
		//页面注册信息列表
		//var grid = win.child('grid');
		var grid = win.down('form[name=userInfoForm]');
		//参数
		var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//组件注册信息数据
			var formData = responseData.formData;
			form.setValues(formData);
			
			var userInfoItems = [];
			var userInfoForm = grid;
			var fields = formData.column;
			//var fields = '[{"qq":"123","desc":"asdfsdf"}]';
			//fields = eval('(' + fields + ')');
			var size = fields.length;
			typeField = {};
			for(var i = 0;i < size;i++){
				var field = fields[i];
				var fieldLabel,name,value;
				for(var fieldName in field){
					if(fieldName == "desc"){
						fieldLabel = field["desc"];
					}else{
						name = fieldName;
						value = field[fieldName];
					}
				}
				typeField = {
					xtype: 'textfield',
					fieldLabel: fieldLabel,
					readOnly:true,
					name: name,
					value: value
				}
				userInfoForm.add(typeField);
			}
			if(win.down('hiddenfield[name=titleImageUrl]').getValue()){
				win.down('image[name=titileImage]').setSrc(TzUniversityContextPath + win.down('hiddenfield[name=titleImageUrl]').getValue());	
			}else{
				win.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
			}
		});
        win.show();
    },
	viewUserByBtn: function(){
		
		 //选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要查看的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要查看的记录");   
		   return;
	    }
	   
	    var OPRID = selList[0].get("OPRID");
		
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');
	
		var cmp = this.createUserInfoClass();		
	
		cmp.on('afterrender',function(){
			var msgForm = this.lookupReference('userMgForm');
			var form = this.lookupReference('userMgForm').getForm();
			var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]').getForm();
			var ksdrInfoForm =this.lookupReference('userMgForm').down('form[name=ksdrInfoForm]').getForm();

			var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
			var formData = responseData.formData;
		
			form.setValues(formData);
			
			//考生导入信息;
			ksdrInfoForm.setValues(formData.ksdrInfo);
			//考生个人信息
			userInfoForm.setValues(formData.perInfo)
			/*var userInfoItems = [];
		
			var fields = formData.column;
			var size = fields.length;
			typeField = {};*/
			/*for(var i = 0;i < size;i++){
				var field = fields[i];
				var fieldLabel,name,value;
				for(var fieldName in field){
					if(fieldName == "desc"){
						fieldLabel = field["desc"];
					}else{
						name = fieldName;
						value = field[fieldName];
					}
				}
				typeField = {
					xtype: 'textfield',
					fieldLabel: fieldLabel,
					readOnly:true,
					name: name,
					value: value,
					fieldStyle:'background:#F4F4F4',
				}
				userInfoForm.add(typeField);					
			}*/
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
	viewUserByRow: function(view, rowIndex){
		
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 
	      var OPRID = selRec.get("OPRID");
		
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');
	
		var cmp = this.createUserInfoClass();		
	
		cmp.on('afterrender',function(){
			var msgForm = this.lookupReference('userMgForm');
			var form = this.lookupReference('userMgForm').getForm();
			var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]').getForm();
			var ksdrInfoForm =this.lookupReference('userMgForm').down('form[name=ksdrInfoForm]').getForm();

			var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
			var formData = responseData.formData;
		
			form.setValues(formData);

			//考生导入信息;
			ksdrInfoForm.setValues(formData.ksdrInfo);
			//考生个人信息
			userInfoForm.setValues(formData.perInfo)
			
			/*var userInfoItems = [];
		
			var fields = formData.column;
			var size = fields.length;
			typeField = {};
			for(var i = 0;i < size;i++){
				var field = fields[i];
				var fieldLabel,name,value;
				for(var fieldName in field){
					if(fieldName == "desc"){
						fieldLabel = field["desc"];
					}else{
						name = fieldName;
						value = field[fieldName];
					}
				}
				typeField = {
					xtype: 'textfield',
					fieldLabel: fieldLabel,
					readOnly:true,
					name: name,
					value: value,
					fieldStyle:'background:#F4F4F4',
				}
				userInfoForm.add(typeField);					
			}*/
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
	resetPassword: function(){
		var comView = this.getView();
		//选中行
	   	var selList = comView.getSelectionModel().getSelection();
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要重置密码的记录");   
			return;
	   	}
	   	
	   	
	    //选中数据参数
		var comParams = "";
		var editJson = "";
		for(var i=0;i<checkLen;i++){
			if(editJson == ""){
				editJson = Ext.JSON.encode(selList[i].data);
			}else{
				editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
			}
		}
		comParams = '"data":[' + editJson + "]";
		//提交参数
		var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"CHGPWD","comParams":{'+comParams+'}}';
		
		//重置密码窗口
		var win = this.lookupReference('setHyPasswordWindow');
								
		Ext.Ajax.request({
		    url: Ext.tzGetGeneralURL(),
		    params: {
		        tzParams: tzParams
		    },
		    success: function(response){
		        var text = response.responseText;
		    
		        var responseText = eval( "(" + response.responseText + ")" );
						if(responseText.comContent.success == "true"){
  
					      if (!win) {
					            className = 'KitchenSink.view.enrollProject.userMg.setPassword';
											Ext.syncRequire(className);
											ViewClass = Ext.ClassManager.get(className);
							    		//新建类
					            win = new ViewClass();
					            comView.add(win);
					      }
					        
					      win.show();										
						}else{
							 Ext.MessageBox.alert("提示", responseText.state.errdesc);										
						}
		        
		    }
		});
		
	   	
	},
	onSetPwdClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//重置密码信息表单
		var form = win.child("form").getForm();
		//重置表单
			form.reset();
		//关闭窗口
		win.close();
	},
	onSetPwdEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//重置密码信息表单
		var form = win.child("form").getForm();
		if (!form.isValid()) {//表单校验未通过
			return false;
		}
			var grid = btn.findParentByType("userMgGL");
	
		//选中行
	   	var selList = grid.getSelectionModel().getSelection();
		//选中行长度
	    var checkLen = selList.length;
	    //表单数据
		var formParams = form.getValues();
		//密码
		var password = formParams["password"];
	    //密码参数
	    var pwdParams = '"password":"'+password+'"';
	    //选中数据参数
		var comParams = "";
		var editJson = "";
		for(var i=0;i<checkLen;i++){
			if(editJson == ""){
				editJson = Ext.JSON.encode(selList[i].data);
			}else{
				editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
			}
		}
		comParams = '"data":[' + editJson + "]";
		//提交参数
		var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"PWD","comParams":{'+pwdParams+","+comParams+'}}';
		form.reset();
		Ext.tzSubmit(tzParams,function(){
			//重置表单
			//form.reset();
			//关闭窗口
			win.close();						   
		},"重置密码成功",true,this);
		
	},
    /****
    resetPassword: function(){
		//选中行
	   	var selList = this.getView().getSelectionModel().getSelection();
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要重置密码的记录");   
			return;
	   	}else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要重置密码的记录");   
		   return;
	    }
	    
	    var OPRID = selList[0].get("OPRID");
	    var JGID = Ext.tzOrgID;
	    var tzChParams = '{"OPRID":"' + OPRID + '","JGID":"' + JGID + '"}';
	    var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"HTML","comParams":'+tzChParams+'}';	
	    Ext.Ajax.request({
    		//url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_UM.TZ_GD_USERMG.FieldFormula.Iscript_ResetPassword',
    		url:Ext.tzGetGeneralURL(),
    		params: {
        		tzParams: tzParams
    		},
    		success: function(response){
		        Ext.Msg.alert("提示",response.responseText); 
		        //Ext.MessageBox.alert("错误", "已发送邮件至该用户邮箱"); 
   			}
		});
	},
	*****/
	deleteUser: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要关闭的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要关闭所选账号吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   //删除json字符串
					var removeJson = "";
					var OPRID = "";
					for(var i=0;i<selList.length;i++){
						OPRID = selList[i].get("OPRID");
						if(removeJson == ""){
							removeJson = '{"OPRID":"' + OPRID + '"}';
						}else{
							removeJson = removeJson + ','+'{"OPRID":"' + OPRID + '"}';
						}
					}
					var comParams = "";
					if(removeJson != ""){
						comParams = '"delete":[' + removeJson + "]";
					}
					//提交参数
					var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			        //保存数据
					Ext.tzSubmit(tzParams,function(){
						store.reload();			   
					},"",true,this);
				   
				}												  
			},this);   
	   }
	},
	addHmd: function(){
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要加入黑名单的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要將所选账户加入黑名单吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   //删除json字符串
					var updateJson = "";
					var OPRID = "";
					for(var i=0;i<selList.length;i++){
						OPRID = selList[i].get("OPRID");
						if(updateJson == ""){
							updateJson = '{"OPRID":"' + OPRID + '"}';
						}else{
							updateJson = updateJson + ','+'{"OPRID":"' + OPRID + '"}';
						}
					}
					var comParams = "";
					if(updateJson != ""){
						comParams = '"update":[' + updateJson + "]";
					}
					//提交参数
					var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			        //保存数据
					Ext.tzSubmit(tzParams,function(){
						store.reload();			   
					},"",true,this);
					   
				}												  
			},this);   
	   }
	},
	saveDataInfo: function(){
		
		var win =this.getView();

		//页面注册信息表单
		var form = this.getView().child('form').getForm();

		//表单数据
		var formParams = form.getValues();
	

		win.actType = "update";

		//提交参数
		var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"U","comParams":{"'+win.actType+'":[{"data":'+Ext.JSON.encode(formParams)+'}]}}';
		
		Ext.tzSubmit(tzParams,function(){

	    },"",true,this);
	},
    onFormClose: function(){
		this.getView().close();
	},
	onFormSave:function(){
		this.saveDataInfo();
	},
	onFormEnsure:function(){
		this.saveDataInfo();
	
		this.getView().close();
		
		 
	},
	
	/*查看邮件发送历史--测试*/
	viewMailHistory: function(){
		 //选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要查看的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要查看的记录");   
		   return;
	    }
		
		var mailAddr = selList[0].get("userEmail");
		Ext.tzSearchMailHistory(mailAddr);
	},
	
	/*关闭窗口*/
	onPageRegClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	},
	
	/*保存并关闭窗口*/
	onPageRegEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.savePageRegInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	
	
	/*添加到现有听众*/
	 saveToStaAud:function(btn){
	        var arrAddAudience=[];
	        var addAudirec;
	        var arrAddNameValue=[];
	        
	        var arrAddStatValue=[];
	        var arrAddTypeValue=[];
	        var arrAddMsValue=[];
	        var arrAddSQLValue=[];
	        
	        var selList = this.getView().getSelectionModel().getSelection();
	        if(selList.length==0){
    			Ext.MessageBox.alert('提示', '请先选择用户');
    			return;
    		}
        		
	        var arrAddAudiValue=[];
	        Ext.tzShowPromptSearch({
	            recname: 'PS_TZ_AUDCX_VW',
	            searchDesc: '选择听众',
	            maxRow:50,
	            condition:{
	                presetFields:{
	                	TZ_JG_ID:{
	                        value: Ext.tzOrgID,
	                        type: '01'
	                    }
	                },
	                srhConFields:{
	                    TZ_AUD_NAM:{
	                        desc:'听众名称',
	                        operator:'07',
	                        type:'01'
	                    }
	                }
	            },
	            srhresult:{
	            	TZ_AUD_ID:'听众ID',
	            	TZ_AUD_NAME: '听众名称'
	       /*     	TZ_AUD_STAT: '听众状态',
	            	TZ_AUD_TYPE: '听众类型',
	            	TZ_AUD_MS: '描述',
	            	TZ_AUD_SQL: 'SQL'
	            	*/
	            },
	            multiselect: true,
	            callback: function(selection){
	                if (selection.length>0){
	                    for(j=0;j<selection.length;j++){
	                        addAudirec="";
	                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAME};
	                        arrAddAudience.push(addAudirec);
	                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
	                        arrAddNameValue.push(selection[j].data.TZ_AUD_NAME);
	             /*           arrAddStatValue.push(selection[j].data.TZ_AUD_STAT);
	                        arrAddTypeValue.push(selection[j].data.TZ_AUD_TYPE);
	                        arrAddMsValue.push(selection[j].data.TZ_AUD_MS);
	                        arrAddSQLValue.push(selection[j].data.TZ_AUD_SQL);
	                        */
	                        
	                    };
	                    
	                	//是否有访问权限
	       /*     		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_AUDNEW_STD"];
	            	//	console.log(pageResSet);
	            		if( pageResSet == "" || pageResSet == undefined){
	            			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	            			return;
	            		}
	            		//该功能对应的JS类
	            		var className = pageResSet["jsClassName"];
	            	//	console.log(className);
	            		
	            		if(className == "" || className == undefined){
	            			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_AUDNEW_STD，请检查配置。');
	            			return;
	            		}

	            		var win = this.lookupReference('pageRegWindow');

	            		if (!win) {
	            			className = 'KitchenSink.view.enrollProject.userMg.userMgNewAud';
	            			Ext.syncRequire(className);
	            			ViewClass = Ext.ClassManager.get(className);
	            			//新建类
	            			win = new ViewClass();
	            	//		this.getView().add(win);
	            		}
	            		win.actType = "update";
	       */     		
	            		var audId = arrAddAudiValue;
	            		var audName = arrAddNameValue;
	          
	            /*		var audStat = arrAddStatValue;
	            		var audType = arrAddTypeValue;
	            		var audMS = arrAddMsValue;
	            		var audSQL = arrAddSQLValue;
	            		
	            	*/	
	            		
	            		AudID =audId;
						
	        			var oprIDJson = "";
	        			for(var i=0;i<selList.length;i++){
	        				var OPRID = selList[i].get("OPRID");
	        				console.log(OPRID);
	        				if(oprIDJson == ""){
	        					oprIDJson = '{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
	        				}else{
	        					oprIDJson = oprIDJson + ','+'{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
	        				}
	        			}
	        			var comParamsOPRID = "";
	        			if(oprIDJson != ""){
	        				comParamsOPRID = '"add":[' + oprIDJson + "]";
	        			}
	        			var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_LIST_STD","OperateType":"U","comParams":{'+comParamsOPRID+'}}';
	        			console.log(tzParams2);
	        			
	        			Ext.tzSubmit(tzParams2,function(resp){
	        				
	        			},"",true,this,AudID);
	            		
	            		
	            		
	            		
	            		
	            		
	            		
	            		
	        			/* 		
	            		
	            		//参数
	            		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'"}}';
	            		//页面注册信息表单
	            		
	            		var form = win.child("form").getForm();
	            		
	            		var gridStore =win.child("form").child("grid").getStore();
	            		var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';

	            		Ext.tzLoad(tzParams,function(responseData){
	            			console.log(responseData);
	            			form.setValues(responseData);
	            		//	form.findField("audName").setReadOnly(true);

	            		
	            			gridStore.tzStoreParams = tzStoreParams;
	            			gridStore.reload();
	            			
	            		});
	            		

	            		win.show();
	                   var SmsGroupDetForm = btn.findParentByType('smsGroupDet').child('form');
	                    var storereceive=SmsGroupDetForm.child('tagfield[reference="receverTagField"]').getStore();
	                    storereceive.add(arrAddAudience);
	                    SmsGroupDetForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
	                    SmsGroupDetForm.child('tagfield[reference="receverTagField"]').addValue(arrAddAudiValue);
	                    SmsGroupDetForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');

	                    SmsGroupDetForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').disabled=false;
	                    SmsGroupDetForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled');
	                */
	                }
	            }
	        })
	    },
	
	    deleteAudInfo: function(view, rowIndex){
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = view.findParentByType("grid").store;
				   store.removeAt(rowIndex);
				}												  
			},this);  
		},
		
	
	/*选中申请人另存为听众*/
	saveAsStaAud: function() {

		//获取选中人员；
		var selList = this.getView().getSelectionModel().getSelection();
		//拼接参数，新开听众页面；
		
	    if(selList.length==0){
 			Ext.MessageBox.alert('提示', '请先选择用户');
 			return;
 		}
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{"add":[{"audJG":"ADMIN","audID":"NEXT","audName":"","audStat":"1","audType":"2","audMS":"","audSQL":"","audLY":"ZCYH"}]}}';
		
		//后台执行插入表操作
		var AudID ="";
		Ext.tzSubmit(tzParams,function(resp){
			AudID =resp;
						
			var oprIDJson = "";
			for(var i=0;i<selList.length;i++){
				var OPRID = selList[i].get("OPRID");
				console.log(OPRID);
				if(oprIDJson == ""){
					oprIDJson = '{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
				}else{
					oprIDJson = oprIDJson + ','+'{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
				}
			}
			var comParamsOPRID = "";
			if(oprIDJson != ""){
				comParamsOPRID = '"add":[' + oprIDJson + "]";
			}
			var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_LIST_STD","OperateType":"U","comParams":{'+comParamsOPRID+'}}';
			console.log(tzParams2);
			
			Ext.tzSubmit(tzParams2,function(resp){
				
			},"",true,this,AudID);
			
	
			
	    },"",true,this);
		
				
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_AUDNEW_STD"];
	//	console.log(pageResSet);
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
	//	console.log(className);
		
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_AUDNEW_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('pageRegWindow');

		if (!win) {
			//className = 'KitchenSink.view.enrollProject.userMg.userMgNewAud';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		win.actType = "update";
		
		var audId = AudID;
		console.log(audId);
		
		var audName = "";
		var audStat = "1";
		var audType = "2";
		var audMS = "";
		var audSQL = "";
		
		
		//参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'"}}';
		//页面注册信息表单
		
		var form = win.child("form").getForm();
		
		var gridStore =win.child("form").child("grid").getStore();
		var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';

		Ext.tzLoad(tzParams,function(responseData){
			console.log(responseData);
			form.setValues(responseData);		
			gridStore.tzStoreParams = tzStoreParams;
			gridStore.reload();
			
		});
		

		win.show();
		
	},
	    
	    onPageRegSave: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//页面注册信息表单
			var form = win.child("form").getForm();
			if (form.isValid()) {
				/*保存页面注册信息*/
			//	console.log("1");
				this.savePageRegInfo(win);
			}
		},
		
		savePageRegInfo: function(win,view){
			//	console.log(view);
		        //信息表单
		        var form = win.child("form").getForm();
		        var gridStore =win.child("form").child("grid").getStore();
				var selList = win.child("form").child("grid").getSelectionModel().getSelection();
			
				var removeJson = "";
				//删除记录
				var removeRecs = gridStore.getRemovedRecords();
									  	
				for(var i=0;i<removeRecs.length;i++){
					if(removeJson == ""){
						removeJson = Ext.JSON.encode(removeRecs[i].data);
					}else{
						removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
					}
				};
				
				 var comParams = "";
					if(removeJson != ""){
						comParams = '"delete":[' + removeJson + "]";
					}
					
					var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams+'}}';
				//	console.log(tzParams2);
					//保存数据
			        if(comParams!=""){
			            Ext.tzSubmit(tzParams2,function(){
			            	
			            },"",true,this);
			        }

			        
			        //表单数据
			        var	comParamsALL = '"update":[{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}]';
					
					var actType=win.actType;
					console.log(actType);
					
					 //表格数据
					var updateJson = "";
					var updateRecs = gridStore.getUpdatedRecords();
					console.log(updateRecs);
					
					for(var i=0;i<updateRecs.length;i++){
						if(updateJson == ""){
						//	updateJson = Ext.JSON.encode(updateRecs[i].data);
							updateJson = '{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
						//	editJson =   '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
							
						}else{
						//	updateJson = updateJson + ','+Ext.JSON.encode(updateRecs[i].data);
							updateJson = updateJson + ',{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
						//	editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
						}
					};
			console.log(updateJson);
					 var comParams3 = "";
						if(updateJson != ""){
						//	comParams3 = '"update":[' + updateJson + "]";
							comParamsALL = comParamsALL+',"update":[' + updateJson + "]";
						//	comParams = comParams + ',"update":[' + editJson + "]";
						}
			console.log(comParamsALL);
					//	var tzParams3 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams3+'}}';
					//	console.log("tzParams3:"+tzParams3);
						//保存数据
					/*	if(comParams3!=""){
				            Ext.tzSubmit(tzParams3,function(){
				            	gridStore.reload();
				            },"",true,this);
				        }
					*/
					
					//提交参数
				//	var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
					var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParamsALL+'}}';
					//	var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'"}}';
				
					console.log(tzParams);
					
				//	var tzStoreParams = '{"comID":"'+formParams["comID"]+'"}';
					
				//	var pageGrid = this.parent.getView().child("grid");
					var pageGrid = this.getView();
				//	view.parent.parent.down("maintoolbar").hide();
					
				//	var pageGrid = this.findParentByType("grid");
				//	console.log(pageGrid);
					Ext.tzSubmit(tzParams,function(resp){
						win.actType = "update";
					//	form.findField("pageID").setReadOnly(true);
					//	form.findField("pageID").setFieldStyle('background:#F4F4F4');
					//	pageGrid.store.tzStoreParams = tzStoreParams;
					//	pageGrid.store.reload();
				    },"",true,this);
				},
			
});