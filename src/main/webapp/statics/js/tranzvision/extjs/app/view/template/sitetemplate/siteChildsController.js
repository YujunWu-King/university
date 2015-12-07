Ext.define('KitchenSink.view.template.sitetemplate.siteChildsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteChildInfo',
	requires: [
       'KitchenSink.view.template.sitetemplate.skin.skinInfo',
       'KitchenSink.view.template.sitetemplate.template.sitetemplate',
       'KitchenSink.view.template.sitetemplate.area.siteareaInfoPanel',
       'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement',
       'KitchenSink.view.template.sitetemplate.menu.sitemenuInfoPanel',
       'KitchenSink.view.template.sitetemplate.menu.menuManges.menuTypeManagement',
	   'KitchenSink.view.template.sitetemplate.column.siteTemplate'
    ],
    //添加皮肤设置；
    addSkin: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增皮肤设置。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDPF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDPF_STD，请检查配置。');
			return;
		}
    	
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.skin.skinInfo';
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
		cmp.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		
        cmp.on('afterrender',function(){
	        //皮肤设置表单
			var form = cmp.child("form").getForm();
			form.reset();
			form.setValues({siteId:siteId});
		});
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //编辑站点皮肤
    editSiteSkin: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var skinId = selRec.get("skinId");
     	//显示皮肤设置编辑页面
     	this.editSiteSkinByID(skinId);
    },
    editSiteSkinByID: function(skinId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDPF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDPF_STD，请检查配置。');
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
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		
		cmp.on('afterrender',function(panel){
			//皮肤设置表单信息;
			var form = panel.child('form').getForm();
			//皮肤效果图片集
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDPF_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","skinId":"'+skinId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//皮肤设置数据
				var formData = responseData.formData;
				form.setValues(formData);
				form.setValues({siteId:siteId});
				//皮肤效果图片集
				//var roleList = responseData.listData;	

				var tzStoreParams = '{"siteId":"'+siteId+'","skinId":"'+skinId+'"}';
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
    //删除站点皮肤
    deleteSiteSkin: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //添加站点模板集合；
    addTemplate: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增站点模板集合。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_MBSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MBSZ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('sitetemp');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //模板信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({siteId:siteId});
        win.show();
    },
    //编辑站点模板集合
    editSiteTemplate: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//模板集合id
   	 	var templateId = selRec.get("templateId");
     	//显示模板设置编辑页面
     	this.editSiteTemplateByID(templateId);
    },
    editSiteTemplateByID: function(templateId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_MBSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MBSZ_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('sitetemp');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//模板集合表单信息;
		var form = win.child('form').getForm();
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_MBSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","templateId":"'+templateId+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//模板设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
        win.show();
	},
    //删除站点模板集合
    deleteSiteTemplate: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//添加站点栏目集合；
    addColumn: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增站点栏目集合。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDLM_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDLM_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('siteTemplate');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //栏目信息表单
		var form = win.child("form").getForm();
		
		//加载栏目模板
		var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_TEMP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_TEMP_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_TEMP_ID,TZ_TEMP_NAME'
		});
		form.findField("lm_mb").setStore(lm_mbStore);
		form.findField("lm_nrmb").setStore(lm_mbStore);
		form.reset();
		form.setValues({siteId:siteId});
        win.show();
    },
    //编辑站点栏目集合
    editSiteColumn: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//栏目
   	 	var lm_id = selRec.get("lm_id");
     	//显示栏目设置编辑页面
     	this.editColumnSkinByID(lm_id);
    },
    editColumnSkinByID: function(lm_id){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDLM_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDLM_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('siteTemplate');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//模板栏目表单信息;
		var form = win.child('form').getForm();
		
		//加载栏目模板
		var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_TEMP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_TEMP_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_TEMP_ID,TZ_TEMP_NAME'
		});
		form.findField("lm_mb").setStore(lm_mbStore);
		form.findField("lm_nrmb").setStore(lm_mbStore);
		
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDLM_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","templateId":"'+lm_id+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//栏目设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
        win.show();
	},
    //删除站点栏目集合
    deleteSiteColumn: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //添加站点区域集合；
    addArea: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增站点区域集合。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_QYSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_QYSZ_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('siteareaInfoPanel');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板表单信息;
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //站点区域集合表单;
		var form = win.child("form").getForm();
		
		//加载区域类型
		var storeArea = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_ATYP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_AREA_TYPE_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_AREA_TYPE_ID,TZ_AREA_TYPE_NAME'
		});
		form.findField("areatypeid").setStore(storeArea);
		//加载栏目类型
		var storeColumn = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_COLU_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_COLU_ID,TZ_COLU_NAME'
		});
		form.findField("arealm").setStore(storeColumn);

		form.reset();
		form.setValues({siteId:siteId});
        win.show();
    },
   	//编辑站点区域集合
    editSiteArea: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//区域
   	 	var areaid = selRec.get("areaid");
     	//显示区域设置编辑页面
     	this.editAreaSkinByID(areaid);
    },
    editAreaSkinByID: function(areaid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_QYSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_QYSZ_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('siteareaInfoPanel');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//站点区域集合表单信息;
		var form = win.child('form').getForm();
		
		//加载区域类型
		var storeArea = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_ATYP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_AREA_TYPE_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_AREA_TYPE_ID,TZ_AREA_TYPE_NAME'
		});
		form.findField("areatypeid").setStore(storeArea);
		//加载栏目类型
		var storeColumn = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_COLU_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_COLU_ID,TZ_COLU_NAME'
		});
		form.findField("arealm").setStore(storeColumn);
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_QYSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","areaid":"'+areaid+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues({siteId:siteId});
			form.setValues(formData);
		});
        win.show();
	},
    //删除站点区域集合
    deleteSiteArea: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //区域类型管理；
    addTypeArea: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增区域类型。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_QYLXGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_QYLXGL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement';
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
        var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

        cmp.on('afterrender',function(panel){
        	panel.siteId = siteId;
        	var tzStoreParams = '{"siteId":"'+siteId+'"}';
			panel.store.tzStoreParams = tzStoreParams;
			panel.store.load();
		});
        tab = contentPanel.add(cmp);
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //添加站点菜单集合；
    addMenu: function() {
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增站点区域集合。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDSZ_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('sitemenuInfoPanel');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板表单信息;
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //站点菜单集合表单;
		var form = win.child("form").getForm();
		
		//加载区域类型
		var storeMenu = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_MTYP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_TYPE_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}/*,
				TZ_IS_ADD:{
					value:'on',
					operator:"01",
					type:"01"
				}*/
			},
			result:'TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME'
		});
		form.findField("menutypeid").setStore(storeMenu);
		//加载栏目类型
		var storeColumn = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_COLU_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_COLU_ID,TZ_COLU_NAME',
			listeners:{    

					//向已有数据中插入一条新的数据    
					load : function(store, records, options ){    

						var data ={ "TZ_COLU_ID": "", "TZ_COLU_NAME": "" };    

						var rs = [new Ext.data.Record(data)];    

						store.insert(0,rs);    

					}    

				}
				
		});
		form.findField("menulm").setStore(storeColumn);
		
		form.reset();
		form.setValues({siteId:siteId});
		
        win.show();
    },
	//编辑站点菜单集合
    editSiteMenu: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var menuid = selRec.get("menuid");
     	//显示菜单设置编辑页面
     	this.editMenuSkinByID(menuid);
    },
    editMenuSkinByID: function(menuid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDSZ_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('sitemenuInfoPanel');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//站点菜单集合表单信息;
		var form = win.child('form').getForm();
		
		//加载区域类型
		var storeMenu = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_MTYP_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				},
				TZ_TYPE_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}/*,
				TZ_IS_ADD:{
					value:'on',
					operator:"01",
					type:"01"
				}*/
			},
			result:'TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME'
		});
		form.findField("menutypeid").setStore(storeMenu);
		//加载栏目类型
		var storeColumn = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_SITEM_COLU_T',
			condition:{
				TZ_SITEM_ID:{
					value:siteId,
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_COLU_ID,TZ_COLU_NAME',
			listeners:{    

					//向已有数据中插入一条新的数据    
					load : function(store, records, options ){    

						var data ={ "TZ_COLU_ID": "", "TZ_COLU_NAME": "" };    

						var rs = [new Ext.data.Record(data)];    

						store.insert(0,rs);    

					}    

				}
		});
		form.findField("menulm").setStore(storeColumn);
		
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_CDSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","menuid":"'+menuid+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//菜单设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
        win.show();
	},
    //删除站点菜单集合
    deleteSiteMenu: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //菜单类型管理；
    addTypeMenu: function() {
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增菜单类型。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDLXGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDLXGL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement';
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
        var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

        cmp.on('afterrender',function(panel){
        	panel.siteId = siteId;
        	var tzStoreParams = '{"siteId":"'+siteId+'"}';
			panel.store.tzStoreParams = tzStoreParams;
			panel.store.load();
		});
        tab = contentPanel.add(cmp);
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //保存表单
    onFormSave: function(btn) {
		//组件注册表单
		var from = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//站点模板基本信息
		var comSiteParams = from.getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[' + Ext.JSON.encode(from.getValues()) + ']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(from.getValues());
		}
		if(comParams == ""){
			comParams = '"update":[' + editJson + ']';
		}else{
			comParams = comParams + ',"update":[' + editJson + ']';
		}
		
		//列表信息
		var tabpanel = this.getView().child("form").child("tabpanel");
		var grid = tabpanel.getActiveTab();
		var queryID;
		if(grid.title == "皮肤设置"){
			queryID = "1";
		}
		if(grid.title == "站点模板集合"){
			queryID = "2";
		}
		if(grid.title == "站点栏目集合"){
			queryID = "3";
		}
		if(grid.title == "站点区域集合"){
			queryID = "4";
		}
		if(grid.title == "站点菜单集合"){
			queryID = "5";
		}
		//页面注册信息数据
		if (queryID == "1" || queryID == "2" || queryID == "3" || queryID == "4" || queryID == "5")
		{
		
		var store = grid.getStore();
		//修改记录
		/*
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = Ext.JSON.encode(mfRecs[i].data);
			}else{
				editJson = editJson + ','+Ext.JSON.encode(mfRecs[i].data);
			}
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}*/
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
			if(comParams == ""){
				comParams = '"delete":[{"siteId":"' + siteId + '","queryID":"' + queryID + '","deleteList":[' + removeJson + ']}]';
			}else{
				comParams = comParams + ',"delete":[{"siteId":"' + siteId + '","queryID":"' + queryID + '","deleteList":[' + removeJson + ']}]';
			}
		}
	}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDMB_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var siteId = responseData.siteId;
				from.findField('siteId').setValue(siteId);
			}

				if (queryID == "1" || queryID == "2" || queryID == "3" || queryID == "4" || queryID == "5")
		{
			if(btn != "but_ensure"){
				grid.store.reload();
			}
		
		
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			contentPanel.child("SiteManagement").store.reload();
		}
		},"",true,this);
		
    },
    onFormEnsure: function(btn){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});