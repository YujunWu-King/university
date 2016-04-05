Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.submitDataModelMg' ,

    addDataModel_bk: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTSET_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
	//刘哲添加，新增递交资料选择页面------开始
	addSmtDataModel: function() {
		var me = this;
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('myBmbRegWindow1137');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			this.getView().add(win);
		}else{
			var activeTab = win.items.items[0].getActiveTab();
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
		}
		win.show();
		/*
		if (!window.mybmb_cj) {
		}*/
		
		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					this.style.backgroundColor = null
				});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			var activeTab = win.items.items[0].getActiveTab();

			var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
	},
	/*新增报名表模板页面，确定*/
	onNewEnsure_1: function(btn) {
		//组件注册信息列表
		var grid = btn.findParentByType("submitDataModelMg");
		//组件注册信息数据
		var store = grid.getStore();

		var win = this.lookupReference('myBmbRegWindow1137');
		var activeTab = win.items.items[0].getActiveTab(),
			id = '';
		var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
			tplId = "";
		if (activeTab.itemId == "predefine") {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					if (this.style.backgroundColor == "rgb(173, 216, 230)") {
						tplId = this.getAttribute("data-id");
						return false;
					}
				});
		} else {
			tplId = "";
		}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTSET_STD"];
		var className = pageResSet["jsClassName"];
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
		win.close();
		if (tplId&&tplName){
			cmp = new ViewClass();
			cmp.actType = "update";
			cmp.on('afterrender',function(panel){
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var form = cmp.child('form').getForm();
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	
				var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}';
				var grid_1 = cmp.child('grid');
				grid_1.store.tzStoreParams = tzStoreParams;
				grid_1.store.load();
			});
			});
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}else if (tplName) {
			cmp = new ViewClass();
			cmp.actType = "add";
			var form = cmp.child('form').getForm();
			form.findField('smtDtName').setValue(tplName);
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}


		
		/*if (tplName) {
			var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"' + tplName + '"}]}'
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
			Ext.tzSubmit(tzParams,
				function(jsonObject) {
					Ext.get(activeTab.id).select('input').elements[0].value = "";
					store.reload();
					var href = "http://202.120.24.169:9550/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_REG.TZ_TPL_SET.FieldFormula.Iscript_TplDesign?TZ_APP_TPL_ID=" + jsonObject.id;
					window.open(href, '_blank');
				},"",true,this);
		}*/
		//win.close();
	},
	onNewClose: function(btn) {
		var win = btn.findParentByType("window");
		win.close();
	},
	onNewCloseZlgl: function(btn) {
		var win = btn.findParentByType("window");
		win.close();
	},
	//刘哲添加，新增递交资料选择页面------结束


	editSelSmtDtTmp: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var smtDtTmpID = selRec.get("smtDtTmpID");
	     //显示组件注册信息编辑页面
	     this.editSmtDtTmpByID(smtDtTmpID);
	},

	editSmtDtTmpByID: function(smtDtTmpID){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTSET_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
			form.findField("smtDtTmpID").setReadOnly(true);
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+smtDtTmpID+'","addOne":"old"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	
				var tzStoreParams = '{"smtDtTmpID":"'+smtDtTmpID+'","addOne":"old"}';
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

	deleteSelSmtDtTmp: function(view, rowIndex){
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.confirm","确认"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this); 
	},
	
	
	saveSmtDtTmp: function(btn){
		//组件注册信息列表
		var grid = btn.findParentByType("grid");
		//组件注册信息数据
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
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
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTLST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	
 
	onDtMdlSetSave: function(btn){
		
		var grid = this.getView().child("grid");
		var store = grid.getStore();
		var actType = this.getView().actType;

        //记录查重
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');
        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var tagLength=mfRecs[i].get("content").length;
            if(tagLength>50){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nrjjbndy50gzf","内容简介不能大于50个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }else{
                /* if(mfRecs[i].get("tagID")=="NEXT"){
                 if(tagLength<1){
                 continue;
                 }
                 }
                 */
                 if(tagLength<1) {
                     Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nrjjbnwk","内容简介不能为空"),
                         function(e){
                             if(e == "ok"|| e == "OK" || e == "确定"){
                                 tagCellEditing.startEdit(mfRecs[i], 1);
                             }
                         }
                     )
                     return;

                 }
            }

            //记录查重
            var content = mfRecs[i].get("content");
            var tagNameCount =0;
            var recIndex = store.findBy(function(record,id){
                if(record.get("content")==content){
                    tagNameCount++;
                    if(tagNameCount>1){
                        return true;
                    }
                }
            },0);

            if(tagNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nrjjcxcf","内容简介出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }
        }
		
		var form = this.getView().child("form").getForm();
		
		if (form.isValid()) {

			var tzParams = this.getTranslateParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				form.setValues(responseData.formData);
				  var tzStoreParams = '{"smtDtTmpID":"'+form.findField("smtDtTmpID").getValue()+'"}';
		          store.tzStoreParams = tzStoreParams;
				  store.reload();
				  comView.actType = "update";
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=smtDtlist]");
                interviewMgrPanel[0].getStore().reload();
			},"",true,this);

		}
	},
	onDtMdlSetEnsure: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getTranslateParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=smtDtlist]");
                interviewMgrPanel[0].getStore().reload();
				comView.close();	
			},"",true,this);
		}
	},	
	onDtMdlSetClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	getTranslateParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		
		//表单数据
		var formParams = form.getValues();
		
		//启用状态
		if(formParams["smtDtStatus"] == undefined){
			formParams["smtDtStatus"] = "N";
		}
		
		
			//提交资料模板ID
		if(formParams["smtDtTmpID"] == undefined){
			formParams["smtDtTmpID"] = "999999999";
		}
		
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"COM","data":'+Ext.JSON.encode(formParams)+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"COM","data":'+Ext.JSON.encode(formParams)+'}';
		}
		
		//页面注册信息列表
		var grid = this.getView().child("grid");
		//页面注册信息数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
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
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onSmtDataInfoSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveDataInfo(win);
		}
	},
	onDataInfoEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveDataInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	saveDataInfo: function(win){
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
		var formParams = form.getValues();
	
		//提交参数

		var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTXX_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"smtDtTmpID":"'+formParams["smtDtTmpID"]+'"}';
		var pageGrid = this.getView().child("grid");
		Ext.tzSubmit(tzParams,function(responseData){
			win.actType = "update";
            form.setValues(responseData);
			pageGrid.store.tzStoreParams = tzStoreParams;
			pageGrid.store.reload();
	    },"",true,this);
	},
	onDataInfoClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
    //递增资料删除一行
	deleteData: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.confirm","确认"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},

    addSmtDataInfo:function(btn){

        if(this.getView().actType == "add"){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxbcymxx","请先保存页面信息"));
            return;
        }
        //是否有访问权限
        	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTXX_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nmyqx","您没有权限"));
         return;
         }
         //该功能对应的JS类
        var className = pageResSet["jsClassName"];
       var className="KitchenSink.view.enrollProject.submitDtMdlMg.smtDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win ;

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //组件注册信息
        var comRegParams = this.getView().child("form").getForm().getValues();
        //组件IDTZ_APPPRO_TMP_ID
        var smtDtTmpID = comRegParams["smtDtTmpID"];
        //页面注册信息表单
        var form = win.child("form").getForm();

        var formParams = form.getValues();
        formParams["smtDtTmpID"] = smtDtTmpID;
        //formParams["TZ_APPPRO_ID"] = TZ_APPPRO_ID;
        form.setValues(formParams);
        form.reset();
        win.show();

    },

	//递增资料，添加最后一行
	addDataInfo: function(){
		
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxbcdjzlmxxx","请先保存递交资料模型信息后，再新增递交资料信息。"));
			return;
		}
		
		var profeGrid = this.lookupReference('smtDtMdlSetGrid');
		var cellEditing = profeGrid.getPlugin('dataCellediting');
		var profeStore = profeGrid.getStore();
		var rowCount = profeStore.getCount();
		
	    var comRegParams = this.getView().child("form").getForm().getValues();
		var smtDtTmpIDVal = comRegParams["smtDtTmpID"];
		var model = new KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlSetModel({
            smtDtTmpID: smtDtTmpIDVal,
            smtDtID: '',
            order: rowCount+1,
			content:'',
			remark:''
        });


		profeStore.insert(rowCount, model);
		cellEditing.startEditByPosition({
            row: rowCount,
            column: 0
        });
	},
	editBackMsg:function(grid, rowIndex, colIndex){
		 var rec = grid.getStore().getAt(rowIndex);
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_BAKEMSG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}	
	
		var win = this.lookupReference('backMsgWindow');
        
        if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
			//win=new KitchenSink.view.enrollProject.submitDtMdlMg.backMsgWin();
            this.getView().add(win);
        }
		
		  win.smtDtTmpID = rec.get('smtDtTmpID');
	      win.smtDtID = rec.get('smtDtID');
		  
		  	
		if(win.smtDtID==""){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxbcdjzlxx","请先保存递交资料信息，再给递交资料设置常用回复短语！"));
			return;
		}
		
		    //操作类型设置为更新
		    win.actType = "update";
			var grid = win.child('grid');
			console.log(grid);
			//参数
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_BAKEMSG_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+win.smtDtTmpID+'","smtDtID":"'+win.smtDtID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var roleList = responseData.listData;	
				var tzStoreParams = '{"smtDtTmpID":"'+win.smtDtTmpID+'","smtDtID":"'+win.smtDtID+'"}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();					
			});
			
		 win.show();
		
	},
    //常用回复短语删除一行
	deleteBackMsg: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.confirm","确认"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	//常用回复短语，添加最后一行
	addLastBackMsg: function(){
		var win = this.lookupReference('backMsgWindow');
		var smtDtTmpIDVal = win.smtDtTmpID;
	    var smtDtIDVal = win.smtDtID;
		
		var profeGrid = this.lookupReference('backMsgGrid');
		var cellEditing = profeGrid.getPlugin('dataCellediting');
		var profeStore = profeGrid.getStore();
		var rowCount = profeStore.getCount();
	
		var model = new KitchenSink.view.enrollProject.submitDtMdlMg.backMsgModel({
            smtDtTmpID: smtDtTmpIDVal,
            smtDtID: smtDtIDVal,
            msgId: '',
			msgContent:'',
			order:rowCount+1
        });

		profeStore.insert(rowCount, model);
		cellEditing.startEditByPosition({
            row: rowCount,
            column: 0
        });
	},

    onBackMsgSure1:function(btn){
        var win = this.lookupReference('backMsgWindow');
        //var win=this.getView();
        var smtDtTmpIDVal = win.smtDtTmpID;
        var smtDtIDVal = win.smtDtID;
        var grid = win.child("grid");
        var store = grid.getStore();

        var tzParams = this.getBackMsgParams(btn);
        var  comView=this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            var tzStoreParams = '{"smtDtTmpID":"'+smtDtTmpIDVal+'","smtDtID":"'+smtDtIDVal+'"}';
            store.tzStoreParams = tzStoreParams;
            comView.actType = "update";
            store.reload();

        },"",true,this);
       // this.onBackMsgSave(btn);
        //var win11 = btn.findParentByType('window');
        win.close();
    },
	onBackMsgSave: function(btn){
		var win = this.lookupReference('backMsgWindow');
		var smtDtTmpIDVal = win.smtDtTmpID;
	    var smtDtIDVal = win.smtDtID;
		
		var grid = win.child("grid");
		var store = grid.getStore();
	
		var tzParams = this.getBackMsgParams(btn);
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			var tzStoreParams = '{"smtDtTmpID":"'+smtDtTmpIDVal+'","smtDtID":"'+smtDtIDVal+'"}';
			store.tzStoreParams = tzStoreParams;
			comView.actType = "update";	
			store.reload();
		},"",true,this);
	},
	onBackMsgClose: function(btn){
		var win = btn.findParentByType('window');
		win.close();
	},
	onNewCloseAdd: function(btn){
		var win = btn.findParentByType('window');
		win.close();
	},
	getBackMsgParams: function(btn){
		var win = btn.findParentByType('window');
		var smtDtTmpIDVal = win.smtDtTmpID;
	    var smtDtIDVal = win.smtDtID;
		var rowNum = win.rowNum;
		var grid = win.child('grid');
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords();
		var editJson = ""; 
		var comParams = "";
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
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
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_BAKEMSG_STD","OperateType":"U","comParams":{'+comParams+'}}';
		//alert(tzParams);
        return tzParams;
	},
    //可配置查询
    querySmtDataModel:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.TZ_SBMINF_TMP_V',
            condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //关闭
    closeSmtDtTmp:function(btn){
        var grid=btn.up("grid");
        grid.close();
    },
    //确定
    ensureSmtDtTmp:function(btn){
        var comView = this.getView();
        //组件注册信息列表
        var grid = btn.findParentByType("grid");
        //组件注册信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
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
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTLST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
            comView.close();
        },"",true,this);
    },
    //编辑
    editSmtDataModel:function(btn){
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.MessageBox.alert("提示", "请选择一条要修改的记录");
            return;
        } else if (checkLen > 1) {
            Ext.MessageBox.alert("提示", "只能选择一条要修改的记录");
            return;
        }
        var smtDtTmpID = selList[0].get("smtDtTmpID");
        //显示组件注册信息编辑页面
        this.editSmtDtTmpByID(smtDtTmpID);
    },
    //删除
    deleteSmtDataModel:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    //顶部的编辑
    editSmtDataInfo:function(btn){
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.MessageBox.alert("提示", "请选择一条要修改的记录");
            return;
        } else if (checkLen > 1) {
            Ext.MessageBox.alert("提示", "只能选择一条要修改的记录");
            return;
        }
        var smtDtTmpID = selList[0].get("smtDtTmpID");
        var smtDtID=selList[0].get("smtDtID");
        var className="KitchenSink.view.enrollProject.submitDtMdlMg.smtDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        if(smtDtID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxbcbmlcxx","请先保存资料模型信息"));
            return;
        }
        var win = this.lookupReference('smtDtMdlSet');
        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        //操作类型设置为更新
        win.actType = "update";
        //参数
        var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTXX_STD","OperateType":"QF","comParams":{"TZ_SBMINF_TMP_ID":"'+smtDtTmpID+'","TZ_SBMINF_ID":"'+smtDtID+'"}}';
        //页面注册信息表单
        var form = win.child("form").getForm();
        Ext.tzLoad(tzParams,function(responseData){
            form.findField("smtDtTmpID").setReadOnly(true);
            form.findField("smtDtID").setReadOnly(true);
            form.setValues(responseData);
        });
        win.show();
    },
    //grid列表中的编辑
    editData:function(view,rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var smtDtTmpID = selRec.get("smtDtTmpID");
        var smtDtID = selRec.get("smtDtID");
        var className="KitchenSink.view.enrollProject.submitDtMdlMg.smtDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        if(smtDtID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxbcbmlcxx","请先保存资料模型信息"));
            return;
        }
        var win = this.lookupReference('smtDtMdlSet');
        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        //操作类型设置为更新
        win.actType = "update";
        //参数
        var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTXX_STD","OperateType":"QF","comParams":{"TZ_SBMINF_TMP_ID":"'+smtDtTmpID+'","TZ_SBMINF_ID":"'+smtDtID+'"}}';
        //页面注册信息表单
        var form = win.child("form").getForm();
        Ext.tzLoad(tzParams,function(responseData){
            form.findField("smtDtTmpID").setReadOnly(true);
            form.findField("smtDtID").setReadOnly(true);
            form.setValues(responseData);
        });
        win.show();
    },
    //左上角新增删除；批量删除
   removeSmtDataInfo:function(btn){
       //选中行
       var applyItemOptionsGrid = btn.findParentByType("grid");
       var selList = applyItemOptionsGrid.getSelectionModel().getSelection();
       //选中行长度
       var checkLen = selList.length;
       if(checkLen == 0){
           Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.prompt","提示"),  Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qxzyscdjl","请选择要删除的记录"));
           return;
       }else{
           Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.confirm","确认"),  Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.qryscsxjlm","确认要删除所选记录吗？"), function(btnId){
               if(btnId == 'yes'){
                   var store = applyItemOptionsGrid.store;
                   store.remove(selList);
               }
           },this);
       }
   }
});