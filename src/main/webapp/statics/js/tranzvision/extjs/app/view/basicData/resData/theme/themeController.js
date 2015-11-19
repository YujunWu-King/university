Ext.define('KitchenSink.view.basicData.resData.theme.themeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.themeSetMg', 
	requires: [
       'KitchenSink.view.basicData.resData.theme.themeInfoPanel'
    ],
    //查询
    queryTheme:function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_GD_THEMEGL_COM.TZ_GD_THEMEGL_STD.TZ_PT_ZTXX_V',
			condition:{},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
    },
    saveThemeInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_THEMEGL_COM","PageID":"TZ_GD_THEMEGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	closeThemeInfo: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	ensureThemeInfo: function(btn){
		//确定
		this.saveThemeInfos(btn);
		this.closeThemeInfo(btn);
	},
    addTheme: function() {
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_THEMEGL_COM"]["TZ_GD_THEME_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_THEME_STD，请检查配置。');
			return;
		}
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.menu.menuManges.siteMenuTypeInfoPanel';
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
        var themeID = this.getView().themeID;
        cmp.on('afterrender',function(){
			//区域类型集合
			var form = cmp.child("form").getForm();
			form.reset();
			form.setValues({themeID:themeID});
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    editTheme: function() {
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
	   var themeID = selList[0].get("themeID");
	   //显示组件注册信息编辑页面
	   this.editThemeByID(themeID);
    },
	deleteTheme: function(){
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
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	//编辑
    editThemeBL: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
	 	var themeID = selRec.get("themeID");
     	//显示编辑页面
     	this.editThemeByID(themeID);
    },
    editThemeByID: function(themeID){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_THEMEGL_COM"]["TZ_GD_THEME_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_THEME_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.security.com.comInfoPanel';
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
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("themeID").setReadOnly(true);
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_THEMEGL_COM","PageID":"TZ_GD_THEME_STD","OperateType":"QF","comParams":{"themeID":"'+themeID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				var tzStoreParams = '{"themeID":"'+themeID+'"}';
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
	deleteThemeBL: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	}
});