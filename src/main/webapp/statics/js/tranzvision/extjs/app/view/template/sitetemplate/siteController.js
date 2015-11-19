Ext.define('KitchenSink.view.template.sitetemplate.siteController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteBasicA', 
	requires: [
       'KitchenSink.view.template.sitetemplate.siteTemplateInfo',
       'KitchenSink.view.template.sitetemplate.skin.skinInfo'
    ],
	//添加站点模板集合；
    addSite: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存站点模板基本信息后，再新增站点模板集合。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDMB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDMB_STD，请检查配置。');
			return;
		}
    	
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.template.sitetemplate';
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
		//var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		//var siteId = comSiteParams["siteId"];
		
        cmp.on('afterrender',function(){
			//模板集合表单
			var form = cmp.child("form").getForm();
			
			form.reset();
			//form.setValues({siteId:siteId});
		});
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //编辑数据
    editSite: function() {
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
    editSiteInfo: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var siteId = selRec.get("siteId");
     	//显示皮肤设置编辑页面
     	this.editSiteIntoByID(siteId);
    },
    editSiteIntoByID:function(siteId){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_ZDMB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDMB_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.siteTemplateInfo';
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
			//页面注册信息列表
			var tabpanel = panel.child("form").child("tabpanel");
			var grid = tabpanel.getActiveTab();
			//参数
			var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDMB_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				//var roleList = responseData.listData;	

				var tzStoreParams = '{"siteId":"'+siteId+'","queryID":"1"}';
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
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_MBGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
	}
});