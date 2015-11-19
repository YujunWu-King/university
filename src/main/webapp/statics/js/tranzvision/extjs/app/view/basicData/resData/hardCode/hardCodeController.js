Ext.define('KitchenSink.view.basicData.resData.hardCode.hardCodeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hardCode', 

    addHardCode: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_HARDCODE_COM"]["TZ_GD_HARDCODE_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_HARDCODE_STD，请检查配置。');
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

	deleteHardCode: function(){
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
	editHardCode: function() {
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
	   var hardCodeName = selList[0].get("hardCodeName");
	   this.editHardCodeInfoByID(hardCodeName);
    },
	
	editSelHardCode: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
	   	 var hardCodeName = selRec.get("hardCodeName");
	     this.editHardCodeInfoByID(hardCodeName);
    },
	
	editHardCodeInfoByID: function(hardCodeName){
	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_HARDCODE_COM"]["TZ_GD_HARDCODE_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_HARDCODE_STD，请检查配置。');
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
		cmp.actType = "edit";
		cmp.on('afterrender',function(panel){
			
			var form = panel.child('form').getForm();
            form.findField("hardCodeName").setReadOnly(true);
			form.findField("hardCodeName").addCls("lanage_1");
			//参数
			var tzParams = '{"ComID":"TZ_GD_HARDCODE_COM","PageID":"TZ_GD_HARDCODE_STD","OperateType":"QF","comParams":{"hardCodeName":"'+hardCodeName+'"}}';
			//加载数据
			console.log(tzParams)
			Ext.tzLoad(tzParams,function(responseData){
				console.log(responseData);
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
	
	deleteSelHardCode: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this); 
	},
	
    saveHardCodeInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_HARDCODE_COM","PageID":"TZ_GD_HARDCODE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	//确定
	ensureHardCodeInfos: function(btn){
		//保存信息
		this.saveHardCodeInfos(btn);
		//关闭窗口
		this.view.close();
	},
	//关闭
	closeHardCodeInfos:function(btn){
		this.view.close();
	},
	onHardCodeClose: function(btn){
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		panel.close();
	},
	
	onHardCodeSave: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var _actType = panel.actType;
		var form = panel.child("form").getForm();
		var tzParams = '{"ComID":"TZ_GD_HARDCODE_COM","PageID":"TZ_GD_HARDCODE_STD","OperateType":"U","comParams":{';
		
		if(_actType=="add"){
			tzParams = tzParams + '"add":[{';
		}else if(_actType=="edit"){
			tzParams = tzParams + '"update":[{'
		}
		
		var hardCodeName = form.findField("hardCodeName").getValue();
		var hardCodeDesc = form.findField("hardCodeDesc").getValue();
		var hardCodeValue = form.findField("hardCodeValue").getValue();
		var hardCodeDetailDesc = form.findField("hardCodeDetailDesc").getValue();
	
		
		tzParams += '"hardCodeName":"'+hardCodeName+'","hardCodeDesc":"'+hardCodeDesc+'","hardCodeValue":"'+hardCodeValue+'","hardCodeDetailDesc":"'+hardCodeDetailDesc+'"}]}}';
		panel.actType = "edit";
		Ext.tzSubmit(tzParams,function(response){
			var formData =	response.formData;
			form.setValues(formData);
			form.findField("hardCodeName").setReadOnly(true);
			form.findField("hardCodeName").addCls('lanage_1');
		},"",true,this);
	},
	
	onHardCodesure: function(btn){ 
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		if (form.isValid()) {
			this.onHardCodeSave(btn);
			panel.close();
		}	
	},
    queryHardCode:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_HARDCODE_COM.TZ_GD_HARDCODELIST.TZ_HARDCODE_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});