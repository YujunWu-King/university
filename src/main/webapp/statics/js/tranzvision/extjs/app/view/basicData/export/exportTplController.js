Ext.define('KitchenSink.view.basicData.export.exportTplController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.exportTplController', 

	listSearch: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_EXP_TPL_COM.TZ_TPL_LST_STD.TZ_EXP_TPL_DFN_T',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
    deleteTpl: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteTpls: function(){
	   var selList = this.getView().getSelectionModel().getSelection();
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
	//列表模板保存和确定
	listSave: function(btn){
		var grid = btn.findParentByType("grid");
		var store = grid.getStore();

		var removeJson = "";
		var removeRecs = store.getRemovedRecords();

        var comParams="";

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
			//提交参数
			var tzParams = '{"ComID":"TZ_EXP_TPL_COM","PageID":"TZ_TPL_LST_STD","OperateType":"U","comParams":{'+comParams+'}}';
	        //保存数据
			Ext.tzSubmit(tzParams,function(){
				if(btn.name=="save"){
					store.reload();	
				}else{
					grid.close();
				}
			},"",true,this);
		}else{
			if(btn.name=="save"){
				TranzvisionMeikecityAdvanced.Boot.showToast("没有需要保存的数据");
			}else{
				grid.close();
			}
		}
	},
	addTpl: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EXP_TPL_COM"]["TZ_TPL_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TPL_INF_STD，请检查配置。');
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
			cmp.actType = "add";
            tab = contentPanel.add(cmp);     
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
    },
    
    editTpl: function(obj,rowIndex) {
    	var tplId;
    	
    	if(obj.isXType("button",true)){
    		var selList = this.getView().getSelectionModel().getSelection();

    		   var checkLen = selList.length;
    		   if(checkLen == 0){
    				Ext.Msg.alert("提示","请选择一条要修改的记录");   
    				return;
    		   }else if(checkLen >1){
    			   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
    			   return;
    		   }
    		   tplId = selList[0].get("tplId");
    	}else{
    		var store = this.getView().store;
    		var selRec = store.getAt(rowIndex);
    		tplId = selRec.get("tplId");
    	}
	   
	   this.editTplByTplId(tplId);
    },
	
    editTplByTplId: function(tplId){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EXP_TPL_COM"]["TZ_TPL_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TPL_INF_STD，请检查配置。');
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
			
			var form = panel.child('form').getForm();
			var tplIdField = form.findField("tplId");
			tplIdField.setReadOnly(true);
			tplIdField.addCls('lanage_1');/*置灰*/
			//参数
			var tzParams = '{"ComID":"TZ_EXP_TPL_COM","PageID":"TZ_TPL_INF_STD","OperateType":"QF","comParams":{"tplId":"'+tplId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData);
			});			
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}	  
	},
	
	infoSave: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，update-更新
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()) {
			var comParams = "",
				modifiedJson,
				formData = form.getValues();
			
			modifiedJson = [{type:"TPL",data:formData}];
			
			
			if(actType=="add"){
				comParams="add:"+Ext.encode(modifiedJson);
			}else{
				comParams="update:"+Ext.encode(modifiedJson);
			}
			
			var tzParams = '{"ComID":"TZ_EXP_TPL_COM","PageID":"TZ_TPL_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				if(btn.name=="ensure"){
					panel.close();
				}else{
					panel.actType = "update";
					form.findField("tplId").setReadOnly(true);
                    form.findField("tplId").addCls('lanage_1');
				}
			},"",true,this);
		}
	}
});