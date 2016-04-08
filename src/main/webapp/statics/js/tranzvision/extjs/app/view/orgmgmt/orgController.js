Ext.define('KitchenSink.view.orgmgmt.orgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.orgMg',
	createOrgInfoClass: function(){
		var cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ORGGL_COM"]["TZ_GD_ORGDEF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ORGDEF_STD，请检查配置。');
			return;
		}


        //className = 'KitchenSink.view.orgmgmt.orgInfoPanel';
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
	createOrgUserCmpClass: function(){
		var cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;
        Ext.tzSetCompResourses("TZ_AQ_YHZHGL_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_YHZHGL_COM"]["TZ_AQ_YHZHXX_STD"];
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
  onFormPublish: function(){
    	var orgView = this.getView();
    	//机构信息表单
        var form = this.getView().child("form").getForm();
        var orgLoginInf = form.findField("orgLoginInf").getValue();
        if(orgLoginInf == ""){
        	 Ext.Msg.alert("提示","请填写登录系统名称");
        	 return;
        }

        var orgLoginBjImgUrl = form.findField("orgLoginBjImgUrl").getValue();
        if(orgLoginBjImgUrl == ""){
        	 Ext.Msg.alert("提示","请上传背景图片");
        	 return;
        }

        var orgLoginCopr = form.findField("orgLoginCopr").getValue();
        if(orgLoginCopr == ""){
        	 Ext.Msg.alert("提示","登录页面版权信息");
        	 return;
        }

	      if (form.isValid()) {
	            //获取机构信息参数
	            var tzParams = this.getJgdOrgInfoParams();
	            var orgView = this.getView();
	            Ext.tzSubmit(tzParams,function(responseData){
	                orgView.actType = "update";
	                form.findField("orgId").setReadOnly(true);

	                var orgId = form.findField("orgId").getValue();
					    		var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"JTH","comParams":{ "orgId":"'+orgId+'"}}';
					    		Ext.tzLoad(tzParams,function(responseData){

					    			var success = responseData.success;
					    			if(success == "true"){
					    				Ext.Msg.alert("提示","生成机构登陆页成功");
					    			}else{
					    				Ext.Msg.alert("提示",responseData.message);
					    			}

	    						});
	            },"",true,this);
	      }

  },
  onPTFormPublish: function(){
    	//机构信息表单
			var form = this.getView().child("form").getForm();
    	var orgLoginInf = form.findField("orgLoginInf").getValue();
        if(orgLoginInf == ""){
        	 Ext.Msg.alert("提示","请填写登录系统名称");
        	 return;
        }
        //如果有效状态为无效，不允许生成机构登录页面！;
        var orgYxState = form.findField("orgYxState").getValue();
        if(orgYxState == "N"){
            Ext.Msg.alert("提示","机构无效，不能生成登录页面！");
            return;
        }

        var orgLoginBjImgUrl = form.findField("orgLoginBjImgUrl").getValue();
        if(orgLoginBjImgUrl == ""){
        	 Ext.Msg.alert("提示","请上传背景图片");
        	 return;
        }

        var orgLoginCopr = form.findField("orgLoginCopr").getValue();
        if(orgLoginCopr == ""){
        	 Ext.Msg.alert("提示","登录页面版权信息");
        	 return;
        }

        var tabpanel = this.getView().child("form").child("tabpanel");
        //机构管理员信息列表、数据
		var memGrid = this.lookupReference('userRoleGrid');
        var memStore = memGrid.getStore();

        //机构角色信息列表、数据
		var roleGrid = this.lookupReference('orgRoleGrid');
        var roleStore = roleGrid.getStore();

        var formParams = form.getValues();

        var orgId = formParams["orgId"];

		if (form.isValid()) {
			//获取机构信息参数
			var tzParams = this.getOrgInfoParams(memStore,roleStore);
			var orgView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				orgView.actType = "update";
				form.findField("orgId").setReadOnly(true);

                var tzStoreParams1 = '{"orgId":"'+orgId+'","queryType":"USER","cfgSrhId":"TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.TZ_JG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ orgId+'"}}';
                var tzStoreParams2 = '{"orgId":"'+orgId+'","queryType":"ROLE"}';
                memStore.tzStoreParams = tzStoreParams1;
                roleStore.tzStoreParams = tzStoreParams2;
                var queryType="USER";
                memStore.load();
                roleStore.load();

                var orgId = form.findField("orgId").getValue();
				    		var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"JTH","comParams":{ "orgId":"'+orgId+'"}}';
				    		Ext.tzLoad(tzParams,function(responseData){

				    			var success = responseData.success;
				    			if(success == "true"){
				    				Ext.Msg.alert("提示","生成机构登陆页成功");
				    			}else{
				    				Ext.Msg.alert("提示",responseData.message);
				    			}

    						});
			},"",true,this);
		}

  },
	onFormSave: function(){
		//机构信息表单
		var form = this.getView().child("form").getForm();

        var tabpanel = this.getView().child("form").child("tabpanel");
        //机构管理员信息列表、数据
		var memGrid = this.lookupReference('userRoleGrid');
        var memStore = memGrid.getStore();

        //机构角色信息列表、数据
		var roleGrid = this.lookupReference('orgRoleGrid');
        var roleStore = roleGrid.getStore();

        var formParams = form.getValues();

        var orgId = formParams["orgId"];


        var orgYxState = formParams["orgYxState"];

        if (orgId == "ADMIN" && orgYxState == "N") {
            Ext.Msg.alert("提示", "平台管理机构不允许失效操作！");
        } else {
            if (form.isValid()) {
                //获取机构信息参数
                var tzParams = this.getOrgInfoParams(memStore, roleStore);
                var orgView = this.getView();
                Ext.tzSubmit(tzParams, function (responseData) {
                    orgView.actType = "update";
                    form.findField("orgId").setValue(responseData.orgid).setReadOnly(true);
					orgId = responseData.orgid;
                    var tzStoreParams1 = '{"orgId":"' + orgId + '","queryType":"USER","cfgSrhId":"TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.TZ_JG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "' + orgId + '"}}';
                    var tzStoreParams2 = '{"orgId":"' + orgId + '","queryType":"ROLE"}';
                    memStore.tzStoreParams = tzStoreParams1;
                    roleStore.tzStoreParams = tzStoreParams2;
                    var queryType = "USER";
                    memStore.load();
                    roleStore.load();
                }, "", true, this);
            }
        }
    },
    onFormEnsure: function(){
        //机构信息表单
        var form = this.getView().child("form").getForm();

        var tabpanel = this.getView().child("form").child("tabpanel");
        //机构管理员信息列表、数据
        //var memGrid = tabpanel.getComponent("orgaUser");
        var memGrid = this.lookupReference('userRoleGrid');
        var memStore = memGrid.getStore();

        //机构角色信息列表、数据
        //var roleGrid = tabpanel.getComponent("orgaRole");
        var roleGrid = this.lookupReference('orgRoleGrid');
        var roleStore = roleGrid.getStore();

        var formParams = form.getValues();
        var orgId = formParams["orgId"];
        var orgYxState = formParams["orgYxState"];

        if (orgId == "ADMIN" && orgYxState == "N") {
            Ext.Msg.alert("提示", "平台管理机构不允许失效操作！");
        } else {
            if (form.isValid()) {
                //获取机构信息参数
                var tzParams = this.getOrgInfoParams(memStore, roleStore);
                var comView = this.getView();
                Ext.tzSubmit(tzParams, function (responseData) {
                    //关闭窗口
                    comView.close();
                }, "", true, this);
            }
        }
    },
    onFormClose: function(){
        this.getView().close();
    },
    closeUserInfos: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        win.close();
    },
    getOrgInfoParams: function(memStore,roleStore){
        //机构信息表单
        var form = this.getView().child("form").getForm();
        //机构信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}';
        }

		//新增记录
		var memNewRecs = memStore.getNewRecords();
        var roleNewRecs = roleStore.getNewRecords();
        for(var i=0;i<memNewRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"USER","data":'+Ext.JSON.encode(memNewRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"USER","data":'+Ext.JSON.encode(memNewRecs[i].data)+'}';
            }
        };
        for(var i=0;i<roleNewRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleNewRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleNewRecs[i].data)+'}';
            }
        };

		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		//删除json字符串
		var removeJson = "";
		//删除记录
        var memRemoveRecs = memStore.getRemovedRecords();
        var roleRemoveRecs = roleStore.getRemovedRecords();

		for(var i=0;i<memRemoveRecs.length;i++){
            if(removeJson == ""){
                removeJson = '{"typeFlag":"USER","data":'+Ext.JSON.encode(memRemoveRecs[i].data)+'}';
            }else{
                removeJson = removeJson  + ',{"typeFlag":"USER","data":'+Ext.JSON.encode(memRemoveRecs[i].data)+'}';
            }
        }
        for(var i=0;i<roleRemoveRecs.length;i++){
            if(removeJson == ""){
                removeJson = '{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleRemoveRecs[i].data)+'}';
            }else{
                removeJson = removeJson  + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleRemoveRecs[i].data)+'}';
            }
        }

		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	addUserAccount: function(btn) {
		if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存机构信息后，再新增用户信息。");
            return;
        }
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
   		contentPanel.body.addCls('kitchensink-example');

		//新建用户账号信息类
		var cmp = this.createOrgUserCmpClass();


        var formOrg =  this.getView().child("form").getForm();
        var formOrgParams = formOrg.getValues();
        var orgId = formOrgParams["orgId"];

        //操作标志
        cmp.actType = "add";
        //用户Grid
        var userGrid = btn.findParentByType("grid");
        cmp.userGridStore = userGrid.getStore();

        cmp.on('afterrender',function(){
            //设置机构编号值，是从机构信息里新建账户进去的，创建后添加到机构管理员表，同时刷新grid
            var form = this.lookupReference('userAccountForm').getForm();
            var formParams = form.getValues();
            formParams["orgNo"] = orgId;
            formParams["orgId"] = orgId;
			//后台管理用户
			formParams["rylx"] = "NBYH";
            form.setValues(formParams);
            form.findField("orgNo").setReadOnly(true);

            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
			//roleType 为B 是普通用户
            var tzStoreParams = '{"usAccNum":"","orgId":"'+orgId+'","roleType":"B"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteUserAccountOrRole: function(btn){
        var grid = btn.findParentByType("grid");
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{

            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
	},
    addOrgAccount: function() {
			var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
       		contentPanel.body.addCls('kitchensink-example');

            //新建机构信息类
			var cmp = this.createOrgInfoClass();
			//操作标志
			cmp.actType = "add";

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
    editOrgAccount: function(btn) {
        var grid =btn.up('grid');
        //选中行
		   var selList = grid.getSelectionModel().getSelection();
		   //选中行长度
		   var checkLen = selList.length;
		   if(checkLen == 0){
				Ext.Msg.alert("提示","请选择一条要修改的记录");
				return;
		   }else if(checkLen >1){
			   Ext.Msg.alert("提示","只能选择一条要修改的记录");
			   return;
		   }

		   var orgId = selList[0].get("orgId");
		   /*if(orgId == "ADMIN"){
				Ext.Msg.alert("提示","平台管理机构为系统预留机构账号，不能修改");
				return;
		   }*/
		   this.editOrgAccountByOrgId(orgId);
    },
	editOrgAccountByOrgId: function(orgId){
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
       		contentPanel.body.addCls('kitchensink-example');

            //更新机构信息类
			var cmp = this.createOrgInfoClass();
			//操作标志
			cmp.actType = "update";

		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("orgId").setReadOnly(true);
            form.findField("orgId").setFieldStyle('background:#F4F4F4');
			//页面注册信息列表
			var tabPanel = panel.child('form').child('tabpanel');
			var grid = tabPanel.getActiveTab();
			//参数
			var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGGL_STD","OperateType":"QF","comParams":{"orgId":"'+ encodeURIComponent(orgId) +'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;
				if(formData.orgLoginBjImgUrl!=""){
					panel.down("image").setSrc(TzUniversityContextPath + formData.orgLoginBjImgUrl);
				}

				var queryType="USER";
				var tzStoreParams = '{"queryType":"' + queryType + '","cfgSrhId":"TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.TZ_JG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ orgId+'"}}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();
				panel.commitChanges(panel);
			});

		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
    deleteOrgAccount: function(){
	   //选中行
        var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");
			return;
	   }else{
		    for(var i=0;i<checkLen;i++){
				if(selList[i].get("orgId") == "ADMIN"){
					Ext.Msg.alert("提示","平台管理机构ADMIN为系统预留机构账号，不能删除");
					return;
				}
			}
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){
					//参数
					var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGGL_STD","OperateType":"U","comParams":{"delete":[';
                    var org_params = "";
                    for(i=0;i<checkLen;i++){
                        if(i == 0){
                            org_params = '{"orgId":"'+ encodeURIComponent(selList[i].get("orgId")) +'"}';
                        }else{
                            org_params = org_params + ',{"orgId":"'+ encodeURIComponent(selList[i].get("orgId")) +'"}';
                        }
                    }
                    tzParams = tzParams + org_params + "]}}";

                    var store = this.getView().store;
                    store.remove(selList);
					//删除服务器端数据
					Ext.tzLoad(tzParams,function(responseData){

					});
				}
			},this);
	   }
    },
    addOrgAdminMemAccount: function() {
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存机构信息后，再新增用户信息。");
            return;
        }
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
   		contentPanel.body.addCls('kitchensink-example');

		//新建用户账号信息类
		var cmp = this.createOrgUserCmpClass();


        var formOrg =  this.getView().child("form").getForm();
        var formOrgParams = formOrg.getValues();
        var orgId = formOrgParams["orgId"];

        //操作标志
        cmp.actType = "add";

        cmp.on('afterrender',function(){
            //设置机构编号值，是从机构信息里新建账户进去的，创建后添加到机构管理员表，同时刷新grid
            var form = this.lookupReference('userAccountForm').getForm();
            var formParams = form.getValues();
            formParams["orgNo"] = orgId;
            formParams["orgId"] = orgId;
			//后台管理用户
			formParams["rylx"] = "NBYH";
            form.setValues(formParams);
            form.findField("orgNo").setReadOnly(true);

            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
			//roleType 为A 是管理员用户
            var tzStoreParams = '{"usAccNum":"","orgId":"'+orgId+'","roleType":"A"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    initOrg: function() {
        return;
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建机构信息类
        var cmp = this.createOrgInfoClass();
        //操作标志
        cmp.actType = "add";

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    getJgdOrgInfoParams: function(){
        //机构信息表单
        var form = this.getView().child("form").getForm();
        //机构信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}';
        }

        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onJgdFormSave: function(){
        //机构信息表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取机构信息参数
            var tzParams = this.getJgdOrgInfoParams();
            var orgView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                orgView.actType = "update";
                form.findField("orgId").setReadOnly(true);
            },"",true,this);
        }
    },
    onJgdFormEnsure: function(){
        //机构信息表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取机构信息参数
            var tzParams = this.getJgdOrgInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    addOrgRole:function(btn){
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存机构信息后，再新增角色信息。");
            return;
        }
        var form =btn.findParentByType("form").getForm();
        var grid = btn.findParentByType("form").child('tabpanel').getActiveTab();;

        var store = grid.store;
        Ext.tzShowPromptSearch({
            recname: 'PSROLEDEFN',
            searchDesc: '选择角色',
            maxRow:20,
            condition:{
                srhConFields:{
                    ROLENAME:{
                        desc:'角色名称',
                        operator:'07',
                        type:'01'
                    },
                    DESCR:{
                        desc:'描述',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                ROLENAME: '角色名称',
                DESCR: '描述'
            },
            multiselect: true,
            callback: function(selection){
                var orgID=form.findField("orgId").getValue();
                for(var i= 0;i<selection.length;i++){
                    var roleName=selection[i].data.ROLENAME;
                    var roleDesc=selection[i].data.DESCR;

                    var orgRoleModel = Ext.create('KitchenSink.view.orgmgmt.orgRoleModel',{
                        orgId:orgID,
                        roleName:roleName,
                        roleDesc:roleDesc,
                        roleType:"",
                        roleTypeDesc:""
                    });
                    if(store.findRecord("roleName",roleName) == null){
                        store.add(orgRoleModel);
                    }
                }
            }
        });
    },
    editRole: function(btn) {
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        //角色名称
        var roleName = selList[0].get("roleName");
        //显示角色信息编辑页面

        Ext.tzSetCompResourses("TZ_AQ_ROLE_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_ROLE_COM"]["TZ_ROLE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ROLE_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

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
        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('afterrender',function(panel){
            //角色表单信息;
            var form = panel.child('form').getForm();
            form.findField("roleName").setReadOnly(true);
            //许可权列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_ROLE_COM","PageID":"TZ_ROLE_INFO_STD","OperateType":"QF","comParams":{"roleName":"'+roleName+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //角色信息数据
                var formData = responseData.formData;
                form.setValues(formData);

                var tzStoreParams = '{"roleName":"'+roleName+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
	queryOrgAccount: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({
           cfgSrhId: 'TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.TZ_GD_JG_VW',
           condition:
            {
                //"TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
	queryUser: function(btn){     //searchComList为各自搜索按钮的handler event;
		var form =btn.findParentByType("form").getForm();
		var orgID=form.findField("orgId").getValue();
        Ext.tzShowCFGSearch({
           cfgSrhId: 'TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.TZ_JG_USER_VW',
		   queryType:'USER',
           condition:
            {
                "TZ_JG_ID": orgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
				var query = ',"queryType":"USER",';
				//加入参数
				var newsearchCfg = seachCfg.replace(",", query);
                store.tzStoreParams = newsearchCfg;
                store.load();
            }
        });
    },
	editUserAccount: function(btn) {

    	var roleGrid = btn.findParentByType("grid");

	   //选中行
	   var selList = roleGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");
			return;
	   }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");
		   return;
	   }
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
   		contentPanel.body.addCls('kitchensink-example');

		//新建用户账号信息类
		var cmp = this.createOrgUserCmpClass();

    	//操作标志
        cmp.actType = "update";

        //用户Grid
        var userGrid = btn.findParentByType("grid");
        cmp.userGridStore = userGrid.getStore();

        //登录账号
		var usAccNum = selList[0].get("usAccNum");
		//机构编号
		var orgID = selList[0].get("orgId");

		cmp.on('afterrender',function(){
			//用户账号信息表单
			var form = this.lookupReference('userAccountForm').getForm();
			//用户角色信息列表
			var grid = this.lookupReference('userRoleGrid');
			form.findField("usAccNum").setReadOnly(true);
			//参数
			var tzParams = '{"ComID":"TZ_AQ_YHZHGL_COM","PageID":"TZ_AQ_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
				form.setValues(responseData);

				var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();
			});

		});


		cmp.on('close',function(panel){
		         	 roleGrid.store.reload();
		});

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editOrgRole:function(btn){
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }

        var win = this.lookupReference('orgRoleWindow');

        if (!win) {
            Ext.syncRequire("KitchenSink.view.orgmgmt.orgRoleWindow");
            ViewClass = Ext.ClassManager.get("KitchenSink.view.orgmgmt.orgRoleWindow");
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //机构编号
        var orgId = selList[0].get("orgId");
        //角色名称
        var roleName = selList[0].get("roleName");
        //角色类型
        var roleType = selList[0].get("roleType");
        //角色描述
        var roleDesc = selList[0].get("roleDesc");

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'orgId', value:orgId},
                {id:'roleName', value:roleName},
                {id:'roleType', value:roleType},
                {id:'roleDesc', value:roleDesc}
            ]
        );
        win.show();
    },
    copyOrgRole:function(btn){
        var form =btn.findParentByType("form").getForm();
        var orgID=form.findField("orgId").getValue();

        if(orgID != null && orgID != "") {
            var roleGrid = this.lookupReference('orgRoleGrid');
            var roleStore = roleGrid.getStore();
            //获取机构信息参数
            var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"U","comParams":{"update":[{"typeFlag":"COPYROLE","data":{"orgId":"' + orgID + '"}}]}}';
            var orgView = this.getView();
            Ext.tzSubmit(tzParams, function (responseData) {
                var tzStoreParams2 = '{"orgId":"' + orgID + '","queryType":"ROLE"}';
                roleStore.tzStoreParams = tzStoreParams2;
                roleStore.load();
            },"",true,this);
        }
    },
    /*保存机构角色修改信息*/
    onOrgRoleFormSave:function(btn){
        var me = this;
        var win = btn.findParentByType('window');
        var form  = win.child('form').getForm();
        if(form.isValid()){
            var formParams = form.getValues();
            var roleGrid  = me.getView().lookupReference('orgRoleGrid');
            var modifiedRecords = roleGrid.getStore().getModifiedRecords();
            var record = roleGrid.getSelectionModel().getSelection()[0];
            var editJson = '{"typeFlag":"ROLE","data":'+Ext.JSON.encode(formParams)+'}';
            for(var i=0;i<modifiedRecords.length;i++){
                if(modifiedRecords[i]!=record){
                    editJson = editJson + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(modifiedRecords[i].data)+'}';
                }
            };

            var comParams = '"update":[' + editJson + "]";
            var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(responseData){
                record.set('roleType',formParams['roleType']);
                record.set('roleTypeDesc',form.findField('roleType').getRawValue());
                roleGrid.store.commitChanges();
                if(btn.name=='ensure')win.close();
            },"",true,this);
        }
    },
    //orgAcount.js 中的 grid 中的编辑
    editOrgAccountOne: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //组件ID
        var orgId = selRec.get("orgId");
        /*if(orgId == "ADMIN"){
         Ext.Msg.alert("提示","平台管理机构为系统预留机构账号，不能修改");
         return;
         }*/
        this.editOrgAccountByOrgId(orgId);
    },
    //orgInfoPanel.js 中的机构信息中的 机构用户 中的grid 中的编辑
    editUserAccountLine: function(view, rowIndex){

       var roleGrid= view.findParentByType("grid");
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建用户账号信息类
        var cmp = this.createOrgUserCmpClass();

        //操作标志
        cmp.actType = "update";

        var store = view.findParentByType("grid").store;
        //用户Grid
        cmp.userGridStore = store;
	
        var selRec = store.getAt(rowIndex);
        //登录账号
        var usAccNum = selRec.get("usAccNum");
        //机构编号
        var orgID = selRec.get("orgId");

        cmp.on('afterrender',function(){
            //用户账号信息表单
            var form = this.lookupReference('userAccountForm').getForm();
            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
            form.findField("usAccNum").setReadOnly(true);
            //参数
            var tzParams = '{"ComID":"TZ_AQ_YHZHGL_COM","PageID":"TZ_AQ_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                form.setValues(responseData);

                var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });


        cmp.on('close',function(panel){
            roleGrid.store.reload();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    //orgInfoPanel.js 中的机构信息中的 机构用户 中的grid 中的 删除
    deleteUserAccountLine: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //orgInfoPanel.js 中的机构信息中的 机构角色 中的grid 中的编辑
    editOrgRoleLine:function(view, rowIndex){

        var win = this.lookupReference('orgRoleWindow');

        if (!win) {
            Ext.syncRequire("KitchenSink.view.orgmgmt.orgRoleWindow");
            ViewClass = Ext.ClassManager.get("KitchenSink.view.orgmgmt.orgRoleWindow");
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //机构编号
        var orgId = selRec.get("orgId");
        //角色名称
        var roleName = selRec.get("roleName");
        //角色类型
        var roleType = selRec.get("roleType");
        //角色描述
        var roleDesc = selRec.get("roleDesc");

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'orgId', value:orgId},
                {id:'roleName', value:roleName},
                {id:'roleType', value:roleType},
                {id:'roleDesc', value:roleDesc}
            ]
        );
        win.show();
    },
    //orgInfoPanel.js 中的机构信息中的 机构角色 中的grid 中的 删除
    deleteOrgRoleLine: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //关闭
    onPanelClose:function(){
        //alert("onPanelClose");
        this.getView().close();
    }
});