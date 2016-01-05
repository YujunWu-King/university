Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.menuController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuTypeInfo', 
	requires: [
       'KitchenSink.view.template.sitetemplate.menu.menuManges.siteMenuTypeInfoPanel'
    ],
    saveMenuInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_CDLXGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		});
	},
    addType: function() {
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDLXSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDLXSZ_STD，请检查配置。');
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
        var siteId = this.getView().siteId;
        cmp.on('afterrender',function(){
			//区域类型集合
			var form = cmp.child("form").getForm();
			form.reset();
			form.setValues({siteId:siteId});
			
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
				result:'TZ_TEMP_ID,TZ_TEMP_NAME',
				listeners:{    

					//向已有数据中插入一条新的数据    
					load : function(store, records, options ){    

						var data ={ "TZ_TEMP_ID": "", "TZ_TEMP_NAME": "" };    

						var rs = [new Ext.data.Record(data)];    

						store.insert(0,rs);    

					}    

				}
			});
			
			form.findField("menuColuTmpl").setStore(lm_mbStore);
			form.findField("menuContTmpl").setStore(lm_mbStore);
			
			//栏目类型
			var lm_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZDLM_LX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuColuType").setStore(lm_lxStore);
			
			//内容类型
			var nr_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZD_NRLX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuContType").setStore(nr_lxStore);
			
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    editType: function() {
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
	   var menutypeid = selList[0].get("menutypeid");
	   //显示组件注册信息编辑页面
	   this.editIntoByID(siteId,menutypeid);
    },
    editIntoByID: function(siteId,menutypeid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDLXSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDLXSZ_STD，请检查配置。');
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
			form.findField("siteId").setReadOnly(true);
			form.findField("menutypeid").setReadOnly(true);
			
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
				result:'TZ_TEMP_ID,TZ_TEMP_NAME',
				listeners:{    

					//向已有数据中插入一条新的数据    
					load : function(store, records, options ){    

						var data ={ "TZ_TEMP_ID": "", "TZ_TEMP_NAME": "" };    

						var rs = [new Ext.data.Record(data)];    

						store.insert(0,rs);    

					}    

				}
			});
			
			form.findField("menuColuTmpl").setStore(lm_mbStore);
			form.findField("menuContTmpl").setStore(lm_mbStore);
			
			//栏目类型
			var lm_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZDLM_LX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuColuType").setStore(lm_lxStore);
			
			//内容类型
			var nr_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZD_NRLX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuContType").setStore(nr_lxStore);
			
			//页面注册信息列表
			var grid = panel.down('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_CDLXSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","menutypeid":"'+menutypeid+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				var src = formData.menutypeimg;
				/*
				panel.child('form').child('form').child("image").setSrc(TzUniversityContextPath + formData.menutypeimg);
				panel.child('form').down('form[name=imgForm2]').child("image").setSrc(TzUniversityContextPath + formData.menunowimg);
				*/
				form.setValues({siteId:siteId});
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				var tzStoreParams = '{"siteId":"'+siteId+'","menutypeid":"'+menutypeid+'"}';
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
	deleteTyle: function(){
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
	 editSiteMenu: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
	 	var siteId = selRec.get("siteId");
   	 	var menutypeid = selRec.get("menutypeid");
     	//显示皮肤设置编辑页面
     	this.editMenuSkinByID(siteId,menutypeid);
    },
    editMenuSkinByID: function(siteId,menutypeid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_ZD_CDLXSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZD_CDLXSZ_STD，请检查配置。');
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
			form.findField("siteId").setReadOnly(true);
			form.findField("menutypeid").setReadOnly(true);
			//页面注册信息列表
			var tabpanel = panel.child("form").child("tabpanel");
			var grid = tabpanel.getActiveTab();

//			var grid = panel.child('grid');
			
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
				result:'TZ_TEMP_ID,TZ_TEMP_NAME',
				listeners:{    

					//向已有数据中插入一条新的数据    
					load : function(store, records, options ){    

						var data ={ "TZ_TEMP_ID": "", "TZ_TEMP_NAME": "" };    

						var rs = [new Ext.data.Record(data)];    

						store.insert(0,rs);    

					}    

				}
			});
			
			form.findField("menuColuTmpl").setStore(lm_mbStore);
			form.findField("menuContTmpl").setStore(lm_mbStore);
			
			//栏目类型
			var lm_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZDLM_LX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuColuType").setStore(lm_lxStore);
			
			//内容类型
			var nr_lxStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PT_ZHZXX_V',
				condition:{
					TZ_ZHZJH_ID:{
						value:'TZ_ZD_NRLX',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_ZHZ_ID,TZ_ZHZ_DMS'
			});
			form.findField("menuContType").setStore(nr_lxStore);
			
			//参数
			var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_CDLXSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","menutypeid":"'+menutypeid+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				form.setValues({siteId:siteId});
				var src = formData.menutypeimg;
				/*
				panel.child('form').child('form').child("image").setSrc(TzUniversityContextPath + formData.menutypeimg);
				panel.child('form').down('form[name=imgForm2]').child("image").setSrc(TzUniversityContextPath + formData.menunowimg);
				*/
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				

			var tzStoreParams = '{"siteId":"'+siteId+'","queryID":"1","menutypeid":"'+menutypeid+'"}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();

				//var tzStoreParams = '{"siteId":"'+siteId+'","menutypeid":"'+menutypeid+'"}';
				//grid.store.tzStoreParams = tzStoreParams;
				//grid.store.load();	
			});
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	deleteSiteMenu: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	}
});