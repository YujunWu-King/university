Ext.define('KitchenSink.view.artTypeManagement.artTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.artTypeController', 
    addArtType: function() {
			//是否有访问权限
			
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_TYPE_MG_COM"]["TZ_ARTTYPE_INF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ARTTYPE_INF_STD，请检查配置。');
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
			
			cmp.on('afterrender',function(panel){
			
				var	artTypeFieldInfoGrid = panel.down('grid[name=artTypeFieldGrid]');
				var tzStoreParamsParaJson = {
						"artTypeId": "NEXT"
				}
				var tzStoreParamsField = Ext.JSON.encode(tzStoreParamsParaJson);
				artTypeFieldInfoGrid.store.tzStoreParams = tzStoreParamsField;
				artTypeFieldInfoGrid.store.load();
				
			});
			
            tab = contentPanel.add(cmp);  
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
    editArtType: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //内容类型ID
	   	 var artTypeId = selRec.get("artTypeId");
	     //显示内容类型信息编辑页面
	     this.editArtTypeInfoByKey(artTypeId);
			
    },
	editSelArtType: function() {
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
        //内容类型编号
        var artTypeid = selList[0].get("artTypeId");
        this.editArtTypeInfoByKey(artTypeid);
    },
	editArtTypeInfoByKey: function(artTypeId) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_TYPE_MG_COM"]["TZ_ARTTYPE_INF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ARTTYPE_INF_STD，请检查配置。');
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
				form.findField("artTypeId").setReadOnly(true);
				form.findField("artTypeId").addCls("lanage_1");
				
				var tzParamsJson = {
						"ComID":"TZ_ART_TYPE_MG_COM",
						"PageID":"TZ_ARTTYPE_INF_STD",
						"OperateType":"QF",
						"comParams":{
							"artTypeId": artTypeId
						}
				} 
				//参数
				var tzParams = Ext.JSON.encode(tzParamsJson);
				
				Ext.tzLoad(tzParams,function(responseData){
					
					form.setValues(responseData);	
					//参数信息数据
					
					var	artTypeFieldInfoGrid = panel.down('grid[name=artTypeFieldGrid]');
					var tzStoreParamsParaJson = {
							"artTypeId": artTypeId
					}
					var tzStoreParamsField = Ext.JSON.encode(tzStoreParamsParaJson);
					artTypeFieldInfoGrid.store.tzStoreParams = tzStoreParamsField;
					artTypeFieldInfoGrid.store.load();
				});
			});
			
            tab = contentPanel.add(cmp);     
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	searchArtTypeList: function(btn){     //searchComList为各自搜索按钮的handler event;

        Ext.tzShowCFGSearch({            

            cfgSrhId: 'TZ_ART_TYPE_MG_COM.TZ_ART_TYPE_MG_STD.TZ_ART_TYPE_VW',           
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
	deleteSelArtType: function(){
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
				   var artTypeStore = this.getView().store;
                    artTypeStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteArtType: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	saveArtType: function(btn){
        //内容类型列表
        var grid = btn.findParentByType("grid");
        //内容类型信息数据
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
        var tzParams = '{"ComID":"TZ_ART_TYPE_MG_COM","PageID":"TZ_ART_TYPE_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
	ensureArtType: function(btn){
        //内容类型列表
        var grid = btn.findParentByType("grid");
        //内容类型信息数据
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
			//提交参数
			var tzParams = '{"ComID":"TZ_ART_TYPE_MG_COM","PageID":"TZ_ART_TYPE_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				grid.close();
			},"",true,this);
        }else{
            grid.close();
        }  
    },
	closeArtType: function(btn){
        
        var grid = btn.findParentByType("grid");
        
        grid.close();
		
    },
	getArtTypeInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		
		//修改json字符串
	
		var editJson = "";
		editJson = '{"typeFlag":"ARTTYPEINFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		
		var grid = this.getView().child("grid");
		var store = grid.getStore();
		
		var mfRecs = store.getRange(0,store.getCount());
		var gridJson = "";
		for (var i = 0; i < mfRecs.length; i++) {
			if (gridJson == "") {
				gridJson = Ext.JSON.encode(mfRecs[i].data);
			} else {
				gridJson = gridJson + ',' + Ext.JSON.encode(mfRecs[i].data);
			}
		}
		if(gridJson != ""){
			editJson += ',{"typeFlag": "saveGrid","data": [' + gridJson + ']}';
		}
		
		if(editJson != ""){
			if(actType == "update"){
				comParams = '"update":[' + editJson + "]";
			}
			if(actType == "add"){
				comParams = '"add":[' + editJson + "]";
			}
		}

		//提交参数
		var tzParams = '{"ComID":"TZ_ART_TYPE_MG_COM","PageID":"TZ_ARTTYPE_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onFormSave: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getArtTypeInfoParams();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				form.findField("artTypeId").setReadOnly(true);
				form.findField("artTypeId").addCls("lanage_1");
				form.setValues({"artTypeId":responseData.artTypeId});
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("artTypeList").store.reload();
			},"",true,this);
		}
	},
	onFormEnsure: function(){
		//获取窗口
		var comView = this.getView();
		//信息表单
		var form = comView.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			var tzParams = this.getArtTypeInfoParams();	
			Ext.tzSubmit(tzParams,function(responseData){
				//重置表单
				form.reset();
				//关闭窗口
				comView.close();
				comView.actType = "update";	
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("artTypeList").store.reload();
			},"",true,this);

		}	
	},
	onFormClose: function(){
		this.getView().close();
	}
});


