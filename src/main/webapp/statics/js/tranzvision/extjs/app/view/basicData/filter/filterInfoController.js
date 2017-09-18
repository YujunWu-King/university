Ext.define('KitchenSink.view.basicData.filter.filterInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.filterInfo', 
	/*requires: [
       'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel'
    ],*/
    addFld: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存组信息后，再新增字段信息。");
			return;
		}
        //是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_GD_FLDTZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_FLDTZ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterFldWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = this.getView().child("form").getForm();
		var formParams = form.getValues();
		var ComID = formParams['ComID'];
   	 	var PageID = formParams['PageID'];
   	 	var ViewMc = formParams['ViewMc'];
   	 	//默认搜索可配置搜索主视图中的字段
   	 	var FieldView= formParams['ViewMc'];
   	 	formParams['FieldView']=formParams['ViewMc'];
   	 	var winform = win.child("form").getForm();
   	 	var grid = win.child("form").child("grid");
   	 	winform.setValues(formParams);
   	 	var tzStoreParams = '{"ComID":"' + ComID + '","PageID":"' + PageID + '","ViewMc":"' + ViewMc +'","FieldView":"' + FieldView + '","FieldMc":""}';
        grid.store.tzStoreParams = tzStoreParams;

        win.show();
    },
    //添加DeepQuery字段
    addDqFld: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存组信息后，再新增字段信息。");
			return;
		}
        //是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_GD_FLDTZDQ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_FLDTZDQ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterFldWindowDeepQuery');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = this.getView().child("form").getForm();
		var formParams = form.getValues();
		var ComID = formParams['ComID'];
   	 	var PageID = formParams['PageID'];
   	 	var ViewMc = formParams['ViewMc'];
   	 	//默认搜索可配置搜索主视图中的字段
   	 	var FieldView= formParams['ViewMc'];
   	 	formParams['FieldView']=formParams['ViewMc'];
   	 	var winform = win.child("form").getForm();
   	 	var grid = win.child("form").child("grid");
   	 	winform.setValues(formParams);
   	 	var tzStoreParams = '{"ComID":"' + ComID + '","PageID":"' + PageID + '","ViewMc":"' + ViewMc +'","FieldView":"' + FieldView + '","FieldMc":""}';
        grid.store.tzStoreParams = tzStoreParams;

        win.show();
    },
    addOneFld:function(view,t,rowIndex){
    	var record = view.findParentByType("grid").store.getAt(rowIndex);
    	var comParams = "";
    	comParams = 'add:[' + Ext.JSON.encode(record.data) + ']';
    	//提交参数
		var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_GD_FLDTZ_STD","OperateType":"U","comParams":{' + comParams + '}}';
        console.log(tzParams);
		//保存数据
		var tabpanel = this.getView().child("tabpanel");
		var grid = tabpanel.down('grid[name=filterGrid]');
		Ext.tzSubmit(tzParams,function(){
			grid.store.reload();
			var win = view.findParentByType("window");
			win.close();
		},"",true,this);
    },
    editSelFld: function(){
    	//var store = view.findParentByType("grid").store;
	 		//var selRec = store.getAt(rowIndex);
		 	//选中行
		   var tabpanel = this.getView().child("tabpanel");
		   var grid = tabpanel.down('grid[name=filterGrid]');
		   var selList = grid.getSelectionModel().getSelection();
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
   	 	var FieldMc = selRec.get("FieldMc");
   	 	
     	//显示编辑页面
     	this.editFilterFldByID(ComID,PageID,ViewMc,FieldMc);
    },
    editFld: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	
   	 	var ComID = selRec.get("ComID");
   	 	var PageID = selRec.get("PageID");
   	 	var ViewMc = selRec.get("ViewMc");
   	 	var FieldMc = selRec.get("FieldMc");
   	 	
     	//显示编辑页面
     	this.editFilterFldByID(ComID,PageID,ViewMc,FieldMc);
    },
    editFilterFldByID: function(ComID,PageID,ViewMc,FieldMc){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FILTER_FLD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FILTER_FLD_STD，请检查配置。');
            return;
        }
        
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
		//className = 'KitchenSink.view.basicData.filter.filterFldInfoPanel';
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
            //资源集合表单信息;
            var form = panel.child('form').getForm();
            form.findField("FieldMc").setReadOnly(true);
            //资源信息列表
            var grid = panel.child('form').child('tabpanel').getActiveTab();	
            //参数
            var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_FLD_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                
                var strDeepQueryFlg=formData.deepQueryFlg;
                form.findField("deepQueryFlg").hide();
                if (strDeepQueryFlg=="Y"){
                	  form.findField("deepQueryView").show();
                	  form.findField("deepQueryFld").show();
                }else{
                	 form.findField("deepQueryView").hide();
               	  	 form.findField("deepQueryFld").hide();
                }
                
                
                //资源集合信息列表数据
                var roleList = responseData.listData;
            });
            var queryID;
            /*
						if(grid.name == "searchFld"){
							queryID = "1";
						}
						if(grid.name == "promptFld"){
							queryID = "2";
						}
						*/
		
						queryID = "1";
						var grid1 = panel.down('grid[name=searchFld]');
						var tzStoreParams1 = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}';
            grid1.store.tzStoreParams = tzStoreParams1;
            grid1.store.load();
            
           
            queryID = "2";
            var grid2 = panel.down('grid[name=promptFld]');
						var tzStoreParams2 = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}';
            grid2.store.tzStoreParams = tzStoreParams2;
            grid2.store.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteFlds: function(btn){
        //资源信息列表
        var tabpanel = this.getView().child("tabpanel");
		var grid = tabpanel.down('grid[name=filterGrid]');
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    onFilterInfoSave: function(){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getFilterInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("ComID").setReadOnly(true);
				form.findField("PageID").setReadOnly(true);
				form.findField("ViewMc").setReadOnly(true);
			},"",true,this);
		}
    },
    onFilterInfoEnsure: function(){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getFilterInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				comView.close();	
			},"",true,this);
		}
    },
    onFilterInfoClose: function(){
		this.getView().close();
	},
	getFilterInfoParams: function(){
        //资源集合表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        
        if (formParams['advModel'] == undefined || formParams['advModel'] == ""){
				formParams['advModel'] = "0";
		}else{
			formParams['advModel'] = "1";
		};
		
		if (formParams['baseSchEdit'] == undefined || formParams['baseSchEdit'] == ""){
				formParams['baseSchEdit'] = "0";
		}else{
			formParams['baseSchEdit'] = "1";
		};
		
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"FILTER","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        var editfldJson="";
        //资源信息列表
        var tabpanel = this.getView().child("tabpanel");
		var grid = tabpanel.down('grid[name=filterGrid]');
        //资源信息数据
        var store = grid.getStore();
        
        //列表中修改的记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editfldJson == ""){
				editfldJson = Ext.JSON.encode(mfRecs[i].data);
			}else{
				editfldJson = editfldJson + ','+Ext.JSON.encode(mfRecs[i].data);
			}
		}
		
        if(actType == "update"){
            editJson = '{"typeFlag":"FILTER","data":'+Ext.JSON.encode(formParams)+',"updateList":[' + editfldJson + ']}';
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        
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
		
		var gridDataSet = tabpanel.down('grid[name=dataSetGrid]');
        //DataSet信息数据
        var storeDataSet = gridDataSet.getStore();
		//删除json字符串
        var removeJsonDataSet = "";
        //删除记录
        var removeRecsDataSet = storeDataSet.getRemovedRecords();
        for(var i=0;i<removeRecsDataSet.length;i++){
            if(removeJsonDataSet == ""){
                removeJsonDataSet = Ext.JSON.encode(removeRecsDataSet[i].data);
            }else{
                removeJsonDataSet = removeJsonDataSet + ','+Ext.JSON.encode(removeRecsDataSet[i].data);
            }
    	}
		
		var removeJsonData = '{"typeFlag":"FILTER","removeList":[' + removeJson + ']}';
		removeJsonData = removeJsonData + "," + '{"typeFlag":"DataSet","removeList":[' + removeJsonDataSet + ']}';
		
        if(removeJsonData != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJsonData + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJsonData + "]";
            }
        }
        
        //提交参数
        var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_DEFN_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    deleteFLd: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	onFilterFldWinClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	addDataSetFld:function(btn){
		/*新增dataset字段*/
		var form = this.getView().child("form").getForm();
		
		var comId = form.findField("ComID").getValue();
		var pageId = form.findField("PageID").getValue();
		var viewMc = form.findField("ViewMc").getValue();
		
		 //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FLDDST_FLD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FILTER_FLD_STD，请检查配置。');
            return;
        }
        
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
		//className = 'KitchenSink.view.basicData.filter.filterFldInfoPanel';
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
            //资源集合表单信息;
            var formDateSet = panel.child('form').getForm();
			formDateSet.findField("ComID").setValue(comId);
			formDateSet.findField("PageID").setValue(pageId);
			formDateSet.findField("ViewMc").setValue(viewMc);
            //资源信息列表
            var grid = panel.child('grid');
            //参数
			/*
            var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_FLD_STD","OperateType":"QF","comParams":{"ComID":"'+s+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+"1"+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
            });
			
            var queryID;
			queryID = "1";
			var grid1 = panel.down('grid[name=searchFld]');
			var tzStoreParams1 = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","FieldMc":"'+FieldMc+'"}';
            grid1.store.tzStoreParams = tzStoreParams1;
            grid1.store.load();*/
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
	},
	editDataSetSelFld: function(btn){
		//选中行
	    var tabpanel = this.getView().child("tabpanel");
	    var grid = tabpanel.down('grid[name=dataSetGrid]');
	    var selList = grid.getSelectionModel().getSelection();
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
		var fieldOrder = selRec.get("fieldOrder");
   	 	
     	//显示编辑页面
     	this.editFltDstFldByID(ComID,PageID,ViewMc,fieldOrder);
    },
    editDataSetFld: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	
   	 	var ComID = selRec.get("ComID");
   	 	var PageID = selRec.get("PageID");
   	 	var ViewMc = selRec.get("ViewMc");
   	 	var fieldOrder = selRec.get("fieldOrder");
   	 	
     	//显示编辑页面
     	this.editFltDstFldByID(ComID,PageID,ViewMc,fieldOrder);
    },
    editFltDstFldByID: function(ComID,PageID,ViewMc,fieldOrder){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FLDDST_FLD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FLDDST_FLD_STD，请检查配置。');
            return;
        }
        
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
		//className = 'KitchenSink.view.basicData.filter.filterFldInfoPanel';
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
            //资源集合表单信息;
            var form = panel.child('form').getForm();
            //form.findField("fieldOrder").setReadOnly(true);
            //资源信息列表
            //参数
            var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_FLD_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","fieldOrder":"'+fieldOrder+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                
            });

//            var grid = panel.down('grid[name=filterCondGrid]');
//			var tzStoreParams = '{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","fieldOrder":"'+fieldOrder+'"}';
//            grid.store.tzStoreParams = tzStoreParams;
//            grid.store.load();
            
            var queryID;

			queryID = "1";
			var grid1 = panel.down('grid[name=filterCondGrid]');
			var tzStoreParams1 = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","fieldOrder":"'+fieldOrder+'"}';
			grid1.store.tzStoreParams = tzStoreParams1;
			//grid1.store.load();
			
			queryID = "2";
			var grid2 = panel.down('grid[name=filterRoleGrid]');
			var tzStoreParams2 = '{"queryID":"' + queryID + '","ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","fieldOrder":"'+fieldOrder+'"}';
			grid2.store.tzStoreParams = tzStoreParams2;
            
            
            
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
	deleteDataSetFlds: function(btn){
        //资源信息列表
        var tabpanel = this.getView().child("tabpanel");
		var grid = tabpanel.down('grid[name=dataSetGrid]');
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
	deleteDataSetFLd: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    }
    
});