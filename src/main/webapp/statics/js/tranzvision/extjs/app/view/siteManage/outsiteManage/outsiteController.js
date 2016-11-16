Ext.define('KitchenSink.view.siteManage.outsiteManage.outsiteController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.outsiteBasicB', 

    //添加外部站点；
	addOutSite: function(btn) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWZDDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWZDDY_STD，请检查配置。');
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        //操作类型设置为更新
		cmp.actType = "add";
        cmp.on('afterrender',function(){
			//模板集合表单
			var form = cmp.child("form").getForm();
			form.reset();
		});
        tab = contentPanel.add(cmp);     
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    
    editContent: function(btn) {
		//是否有访问权限
//		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_MG_COM"]["TZ_ART_LIST_STD"];
//		if( pageResSet == "" || pageResSet == undefined){
//			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
//			return;
//		}
//		//该功能对应的JS类
//		var className = pageResSet["jsClassName"];
//		if(className == "" || className == undefined){
//			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ART_LIST_STD，请检查配置。');
//			return;
//		}
		var className="KitchenSink.view.content.artMg.artMg";
		
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        //操作类型设置为更新
//		cmp.actType = "add";
//        cmp.on('afterrender',function(){
//			//模板集合表单
//			var form = cmp.child("form").getForm();
//			form.reset();
//		});
        tab = contentPanel.add(cmp);     
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //保存站点
    onFormSave: function(btn) {
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//站点信息
		var comSiteParams = form.getValues();
		//站点id
		var siteId = comSiteParams["siteId"];

		//更新操作参数
		var comParams = "";
		var siteForm = "";
		siteForm = form.getValues();
		var fileName = form.findField("enabled").getValue();
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有选择启用状态，无法保存。');
			return;
		};
		fileName = form.findField("siteLanguage").getValue();
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有选择站点语言，无法保存。');
			return;
		};
		fileName = form.findField("siteName").getValue();
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有填写站点名称，无法保存。');
			return;
		};
		
		fileName = form.findField("siteType").getValue();
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有选择站点类型，无法保存。');
			return;
		};
		
		fileName = form.findField("sitePath").getValue();
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有填写站点路径，无法保存。');
			return;
		};
		
		if (fileName != null && fileName != undefined && fileName != "") { 
        	var fdStart = fileName.toLowerCase().indexOf("/");
    		if (fdStart != 0){
    			Ext.Msg.alert("提示", "站点路径格式错误，必须以/开头");
    			return;
    		}
        }
		
		
		//新增
		if(actType == "add"){
			if(comParams == ""){
				comParams = '"add":[' + Ext.JSON.encode(siteForm) + ']';
			}else{
				comParams = comParams + ',"add":[' + Ext.JSON.encode(siteForm) + ']';
			}
		}
		//修改json字符串
		if(actType == "update"){
			if(comParams == ""){
				comParams = '"update":[' + Ext.JSON.encode(siteForm) + ']';
			}else{
				comParams = comParams + ',"update":[' + Ext.JSON.encode(siteForm) + ']';
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWZDDY_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var siteId = responseData.siteId;
				form.findField('siteId').setValue(siteId);
			}
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
			contentPanel.child("outsiteManage").store.reload();
			
		},"",true,this);
    },
    onFormEnsure: function(btn){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	},
    
    //编辑站点
	editSelectField: function() {
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
	   	//组件ID
	   	var siteId = selList[0].get("siteId");
	   	//显示组件注册信息编辑页面
	   	this.editSiteIntoByID(siteId);
    },
    
	//编辑站点（列表）
    editField: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
   	 	var siteId = selRec.get("siteId");
     	this.editSiteIntoByID(siteId);
    },
    
    //编辑站点
    editSiteIntoByID:function(siteId){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWZDDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWZDDY_STD，请检查配置。');
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        cmp = new ViewClass();
        //操作类型设置为更新
		cmp.actType = "update";

		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//以下为不能修改的部分
			form.findField("siteId").setReadOnly(true);
			form.findField("siteId").addCls("lanage_1");
			
			form.findField("siteLanguage").setReadOnly(true);
			form.findField("siteLanguage").addCls("lanage_1");
			
			form.findField("siteType").setReadOnly(true);
			form.findField("siteType").addCls("lanage_1");
			
			form.findField("sitePath").setReadOnly(true);
			form.findField("sitePath").addCls("lanage_1");
			
			form.findField("picPrefix").setReadOnly(true);
			form.findField("picPrefix").addCls("lanage_1");
			
			form.findField("attPrefix").setReadOnly(true);
			form.findField("attPrefix").addCls("lanage_1");
			
			form.findField("viewPrefix").setReadOnly(true);
			form.findField("viewPrefix").addCls("lanage_1");
			
			//参数
			var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWZDDY_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
			});
		});
		
        tab = contentPanel.add(cmp);     
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    
    // 站点栏目管理
    editSiteColuById: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var siteId = selRec.get("siteId");
		this.editSiteColu(siteId);
	},
	editSiteColu: function(siteId){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWLMGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	        return;
	     }
	      //该功能对应的JS类
	    className = pageResSet["jsClassName"];
	    if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWLMGL_STD，请检查配置。');
	         return;
       }
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
           if (!clsProto.themeInfo) {
               Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                   themeName + '\'. Is this intentional?');
           }
       }
       
       cmp = new ViewClass({ siteId:siteId });
       //操作类型设置为更新
       cmp.actType = "update";

       cmp.on('close',function(panel){
    	   try{
    		   grid.store.reload();
    	   }catch(e){
    	   }	  
       });

       tab = contentPanel.add(cmp);
       contentPanel.setActiveTab(tab);
       Ext.resumeLayouts(true);
       if (cmp.floating) {
           cmp.show();
       }
   },
   
   closeComRegInfos: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	
	editSiteTempById: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var siteId = selRec.get("siteId");
		var siteName = selRec.get("sitetemplateName");
		this.editSiteTemp(siteId,siteName);
	},
	
	editSiteTemp: function(siteId,siteName) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWMBGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWMBGL_STD，请检查配置。');
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        
        cmp.actType = "update";
        
		cmp.on('afterrender', function(panel) {
			var form = panel.child('form').getForm();
			//form.setValues({siteId:siteId},{siteName:siteName});
			form.setValues({siteId:siteId,siteName:siteName});
			//form.findField("siteId").setValue(siteId);
			//form.findField("siteName").setValue(siteName);

			var grid = panel.child('grid');
			var tzStoreParams = '{"siteId":"' + siteId + '"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load();
			
//			var form = panel.child('form').getForm();
//
//			var grid = panel.child('grid');
//			//参数
//			var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWZDDY_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'"}}';
//			//加载数据
//			Ext.tzLoad(tzParams,function(responseData){
//				//组件注册信息数据
//				var formData = responseData.formData;
//				form.setValues(formData);
//				var tzStoreParams = '{"siteId":"' + siteId + '"}';
//				grid.store.tzStoreParams = tzStoreParams;
//				grid.store.load();					
//			});
		});
		
		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);
		
		if (cmp.floating) {
			cmp.show();
		}
    },
	//站点菜单管理
    editSiteMenuById: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var siteId = selRec.get("siteId");
		this.editSiteMenu(siteId);
	},
	editSiteMenu: function(siteId){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWCDGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	        return;
	     }
	      //该功能对应的JS类
	    className = pageResSet["jsClassName"];
	    if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWCDGL_STD，请检查配置。');
	         return;
       }
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
           if (!clsProto.themeInfo) {
               Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                   themeName + '\'. Is this intentional?');
           }
       }
       
       cmp = new ViewClass({ siteId:siteId });
       //操作类型设置为更新
       cmp.actType = "update";

       cmp.on('close',function(panel){
    	   try{
    		   grid.store.reload();
    	   }catch(e){
    	   }	  
       });

       tab = contentPanel.add(cmp);
       contentPanel.setActiveTab(tab);
       Ext.resumeLayouts(true);
       if (cmp.floating) {
           cmp.show();
       }
   }
	
	
    /*,
    //删除选中的数据
	deleteSite: function(){
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
				   var userStore = this.getView().store;
				   userStore.remove(selList);
				}												  
			},this);   
	   }
	},
	//删除站点模板
    deleteList: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//保存数据
	saveComRegInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ZDGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	closeComRegInfos: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	//确定
	ensureComRegInfos:function(btn) {
		this.saveComRegInfos(btn);
		this.closeComRegInfos(btn);
	} */
});