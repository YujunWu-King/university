Ext.define('KitchenSink.view.basicData.filter.filterController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.filter', 
	requires: [
       //'KitchenSink.view.basicData.filter.filterWindow'
    ],
    //图片上传
    picClipping:function(){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_PICCLIPPING_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PICCLIPPING_STD，请检查配置。');
			return;
		}

		Ext.syncRequire(className);
		ViewClass = Ext.ClassManager.get(className);
	    //新建类
        win = new ViewClass();
        this.getView().add(win);
       	win.show();
       	win.on('beforeclose',function(w){
       		//图片名称
       		alert("文件名："+w.filename);
       		//系统文件名
       		alert("唯一文件名："+w.sysFileName);
       		//服务器存储路径
       		alert("服务器存储路径："+w.path);
       		//访问路径
			alert("访问路径："+w.accessPath);
		});
        
    },
    //导入Excel
    importExcel: function() {
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_IMPORTEXCEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_IMPORTEXCEL_STD，请检查配置。');
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
        cmp.actType = "update";
        var ProcessID = '123';
        cmp.on('afterrender',function(panel){
        	
        	var form = panel.child('form').getForm();
			form.findField("ProcessID").setReadOnly(true);
			var grid = panel.child('grid');
			var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_IMPORTEXCEL_STD","OperateType":"QF","comParams":{"ProcessID":"'+ProcessID+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			
			//grid.store.load();
			});
			//var tzStoreParams = '{"ProcessID":"'+ProcessID+'"}';
			//grid.store.tzStoreParams = tzStoreParams;
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //查询
    queryFilter:function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_GD_FILTER_COM.TZ_GD_FILTERGL_STD.TZ_FILTERGL_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
    },
    saveFilterInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_GD_FILTERGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
    addFilter: function(btn) {
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_GD_FILTERTZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_FILTERTZ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
        }

		//操作类型设置为新增
		win.actType = "add";
        //btn.findParentByType("grid").disable();
        //btn.getView().getViewport().child('[region=center]').add(win);
        win.show();
        //btn.findParentByType("grid").enable();
    },
    addFilterInfo: function(ComID,PageID,ViewMc) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FILTER_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FILTER_DEFN_STD，请检查配置。');
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
		cmp.actType = "add";
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("ComID").setReadOnly(true);
			form.findField("PageID").setReadOnly(true);
			form.findField("ViewMc").setReadOnly(true);
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_DEFN_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				
				//grid.store.load();						
			});
			var tzStoreParams = '{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'"}';
			grid.store.tzStoreParams = tzStoreParams;
		});
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
    },
    
	deleteFilter: function(){
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
  editFilterBL: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	
	 	var ComID = selRec.get("ComID");
	    var PageID = selRec.get("PageID");
	    var ViewMc = selRec.get("ViewMc");
     	this.editFilterByID(ComID,PageID,ViewMc);
  },
 editFilterByID: function(ComID,PageID,ViewMc){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FILTER_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FILTER_DEFN_STD，请检查配置。');
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
			form.findField("ComID").setReadOnly(true);
			form.findField("PageID").setReadOnly(true);
			form.findField("ViewMc").setReadOnly(true);
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_DEFN_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				
				//grid.store.load();						
			});
			var tzStoreParams = '{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'"}';
			grid.store.tzStoreParams = tzStoreParams;
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
		
	},
	//编辑
  editFilter: function(){
    //var store = view.findParentByType("grid").store;
	 	//var selRec = store.getAt(rowIndex);
	 	
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
	   selRec = selList[0];
	 	
	 	var ComID = selRec.get("ComID");
	    var PageID = selRec.get("PageID");
	    var ViewMc = selRec.get("ViewMc");
     	this.editFilterByID(ComID,PageID,ViewMc);
    },
	deleteFilterBL: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	onFilterWinEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		var formParams = form.getValues();
		if (form.isValid()) {
			var tu = this;
			var tzChParams = '{"ComID":"' + formParams["ComID"] + '","PageID":"' + formParams["PageID"] + '","ViewMc":"' + formParams["ViewMc"] + '"}';
			var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_GD_FILTERTZ_STD","OperateType":"EJSON","comParams":'+tzChParams+'}';
			Ext.Ajax.request({
			    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_FILTE.TZ_GD_FILTER_D.FieldFormula.Iscript_ensureFilter',
			    url:Ext.tzGetGeneralURL(),
			    params: {
			        tzParams: tzParams
			    },
			    success: function(response){
			    	//var responseText = eval( "(" + response.responseText + ")" );
			    	var responseText =eval( "(" + response.responseText+ ")" ).comContent;
			        if(responseText.success == 0){
			        	//重置表单
						form.reset();
						//关闭窗口
						win.close();
						tu.addFilterInfo(formParams["ComID"],formParams["PageID"],formParams["ViewMc"]);
						
			        }else{
			        	Ext.MessageBox.alert("错误", responseText.message);        
			        }
			    }
			});
		}	
	},
	
	onFilterWinClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	//放大镜搜索ComID
	pmtSearchComIDTmp: function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_AQ_COMZC_TBL',
			searchDesc: '搜索组件信息',
			maxRow:20,
			condition:{
				presetFields:{
					
				},
				srhConFields:{
					TZ_COM_ID:{
						desc:'组件ID',
						operator:'07',
						type:'01'	
					},
					TZ_COM_MC:{
						desc:'组件名称',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_COM_ID: '组件ID',
				TZ_COM_MC: '组件名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("ComID").setValue(selection[0].data.TZ_COM_ID);
				//form.findField("ComIDName").setValue(selection[0].data.TZ_COM_MC);
			}
		});	
	},
	//放大镜搜索PageID
	pmtSearchPageIDTmp: function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		var comSiteParams = form.getValues();
		var ComID = comSiteParams['ComID'];
		Ext.tzShowPromptSearch({
			recname: 'TZ_AQ_PAGZC_TBL',
			searchDesc: '搜索页面信息',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_COM_ID:{
						//value: Ext.tzOrgID,
						value:ComID,
						type: '01'	
					}
				},
				srhConFields:{
					TZ_PAGE_ID:{
						desc:'页面ID',
						operator:'07',
						type:'01'		
					},
					TZ_PAGE_MC:{
						desc:'页面名称',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_PAGE_ID: '页面ID',
				TZ_PAGE_MC: '页面名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("PageID").setValue(selection[0].data.TZ_PAGE_ID);
				//form.findField("PageIDName").setValue(selection[0].data.TZ_PAGE_MC);
			}
		});	
	}
});