Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emailServerMg', 
	/*
	requires: [
       'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo'
    ],
	*/
    addEmailServer: function() {
			//是否有访问权限
			
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLSER_MG_COM"]["TZ_EMLSER_SET_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLSER_SET_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            //className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
			
			cmp.actType = "add";
			
            tab = contentPanel.add(cmp);    

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	editEmailServer: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var emailservid = selRec.get("emailservid");
	     //显示组件注册信息编辑页面
	     this.editEmailServerByKey(emailservid);
	},
	editSelEmailServer: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        //邮箱服务器编号
        var emailservid = selList[0].get("emailservid");
        //显示邮箱服务器编辑页面
        this.editEmailServerByKey(emailservid);
    },
    editEmailServerByKey: function(emailservid) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLSER_MG_COM"]["TZ_EMLSER_SET_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLSER_SET_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

         //   className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
				//邮箱信息配置数据表单
				//var form = this.lookupReference('emailServerForm').getForm();
				
				//组件注册表单信息;
				var form = panel.child('form').getForm();
				form.findField("emailservid").setReadOnly(true);
				form.findField("emailorg").setReadOnly(true);
				form.findField("emailorg").addCls("lanage_1");
				//当前登录人机构id;
				var orgId = Ext.tzOrgID;
				if(orgId.toUpperCase() != "ADMIN")
				{
					form.findField("emailorg").value = Ext.tzOrgID;
					form.findField("emailorg").readOnly = true;
				}
				//参数
				var tzParams = '{"ComID":"TZ_EMLSER_MG_COM","PageID":"TZ_EMLSER_SET_STD","OperateType":"QF","comParams":{"emailservid":"'+emailservid+'"}}';
				
				Ext.tzLoad(tzParams,function(responseData){
					//组件注册信息数据
					//console.log(responseData);
					//var formData = responseData.formData;
					form.setValues(responseData);							
				});
			});
			
            tab = contentPanel.add(cmp);   

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));			
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;

        Ext.tzShowCFGSearch({            

            cfgSrhId: 'TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.TZ_EMLS_DEF_VW',    
			condition:
            {
                "TZ_JG_ID_1": Ext.tzOrgID          //设置搜索字段的默认值，没有可以不设置condition;
            },			
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
	deleteSelEmailServer: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var emailServerStore = this.getView().store;
                    emailServerStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteEmailServer: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	saveEmailServer: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        //资源集合信息数据
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_EMLSER_MG_COM","PageID":"TZ_EMLSER_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
	ensureEmailServer: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        //资源集合信息数据
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
			var tzParams = '{"ComID":"TZ_EMLSER_MG_COM","PageID":"TZ_EMLSER_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				//关闭窗口
				grid.close();
			},"",true,this);
        }else{
			//关闭窗口
			grid.close();
        }        
    },
	closeEmailServer: function(btn){
        var grid = btn.findParentByType("grid");
        
		//关闭窗口
		grid.close();
    }

});