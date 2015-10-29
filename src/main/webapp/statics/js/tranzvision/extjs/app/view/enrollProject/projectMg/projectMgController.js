Ext.define('KitchenSink.view.enrollProject.projectMg.projectMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.projectMgController', 
	//新增项目
	addNewProject: function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PRJ_PROMG_COM"]["TZ_PRJ_PROINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var contentPanel, cmp, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       	contentPanel.body.addCls('kitchensink-example');
			
		//className = 'KitchenSink.view.enrollProject.projectMg.projectInfoPanel';
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
		cmp.actType="add";
		var proGrid = btn.findParentByType('grid');
		cmp.proGrid = proGrid;
		
		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			form.findField('projectId').setValue('NEXT');
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}	
	},
	//编辑项目
	editProjectInfo: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var proId = rec.data.projectId;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PRJ_PROMG_COM"]["TZ_PRJ_PROINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel, cmp, ViewClass, clsProto;
			
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
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}
	
		cmp = new ViewClass();
		cmp.actType="edit";
		
		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			var tzParams = '{"ComID":"TZ_PRJ_PROMG_COM","PageID":"TZ_PRJ_PROMG_STD","OperateType":"QF","comParams":{"projectId":"'+proId+'"}}';
			Ext.tzLoad(tzParams,function(respData){
				var formData = respData.formData;
				form.setValues(formData);
			});
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
	
		if (cmp.floating) {
			cmp.show();
		}		
	},
	
	getProjectDefnParams: function(){
		var comParams = "";
		//项目定义表单
		var form = this.getView().child("form").getForm();	
		//操作标识，add-新增，update-更新
		var actType = this.getView().actType;
		//项目ID
		var projectId = form.findField("projectId").getValue();
		
		var formParams = form.getValues();
		if (formParams['isOpen'] == undefined){
			formParams['isOpen'] = "N";
		}
		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"PROINFO","data":'+Ext.JSON.encode(formParams)+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "edit"){
			editJson = '{"typeFlag":"PROINFO","data":'+Ext.JSON.encode(formParams)+'}';
		}
		//专业方向grid
		var professionGrid = this.getView().down('grid[name=professionGrid]');
		//var professionStore = professionGrid.getStore();
		var professionStore = professionGrid.store;
		//专业方向修改记录
		var profeModifRecs = professionStore.getModifiedRecords();
		for (var i=0; i<profeModifRecs.length; i++){
			var RecsJsonParams = profeModifRecs[i].data;
			if(editJson == ""){
				editJson = '{"typeFlag":"PROFESSION","data":'+Ext.JSON.encode(RecsJsonParams)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"PROFESSION","data":'+Ext.JSON.encode(RecsJsonParams)+'}';
			}
		}
		
		//删除json字符串
		var removeJson = "";
		//专业方向删除记录
		var profeRemoveRecs = professionStore.getRemovedRecords();
		for(var i=0;i<profeRemoveRecs.length;i++){
			if(removeJson == ""){
				removeJson = '{"typeFlag":"PROFESSION","projectId":"'+projectId+'","data":'+Ext.JSON.encode(profeRemoveRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"PROFESSION","projectId":"'+projectId+'","data":'+Ext.JSON.encode(profeRemoveRecs[i].data)+'}';
	  		}
		}
		
		
		//管理人员grid
		var managerGrid = this.getView().down('grid[name=managerGrid]');
		var managerStore = managerGrid.getStore();
		//管理人员修改记录
		var managerModifRecs = managerStore.getModifiedRecords();
		for (var i=0; i<managerModifRecs.length; i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"MANAGER","data":'+Ext.JSON.encode(managerModifRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"MANAGER","data":'+Ext.JSON.encode(managerModifRecs[i].data)+'}';
			}
		}
		
		//管理人员删除记录
		var managerRemoveRecs = managerStore.getRemovedRecords();
		for(var i=0;i<managerRemoveRecs.length;i++){
			if(removeJson == ""){
				removeJson = '{"typeFlag":"MANAGER","projectId":"'+projectId+'","data":'+Ext.JSON.encode(managerRemoveRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"MANAGER","projectId":"'+projectId+'","data":'+Ext.JSON.encode(managerRemoveRecs[i].data)+'}';
	  		}
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		
		var tzParams = '{"ComID":"TZ_PRJ_PROMG_COM","PageID":"TZ_PRJ_PROMG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
		
	},
	
	//验证专业方向，主要验证专业方向编号不能为空和重复数据
	professionValid: function(){
		//新增数据行
		var result = [];
		var isValid = true;
		var Desc;
		
		var professionGrid = this.getView().down('grid[name=professionGrid]');
		var profeModifiedRecs =   professionGrid.store.getModifiedRecords(); 
		var IDLabelDesc = Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.professionId","专业方向ID");
		var NameLabelDesc = Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.professionName","专业方向名称");
		for (var i=0; i<profeModifiedRecs.length; i++){
			var loop, sNum, rowNum;
			if (profeModifiedRecs[i].data.professionId == ""){
				isValid = false;
				Desc = IDLabelDesc + Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bnwk","不能为空！");
				break;
			} else {
				loop = 0, sNum = 0, rowNum = 0;
				//用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
				while (loop < 2){
					if(professionGrid.getStore().find("professionId",profeModifiedRecs[i].data.professionId,rowNum,false,false,true) != -1){
						rowNum = professionGrid.getStore().find("professionId",profeModifiedRecs[i].data.professionId,rowNum,false,false,true) + 1;
						sNum++;//相同数据行数量加1
					}
					loop++;
				}
				if (sNum == 2){
					isValid = false;
					Desc = IDLabelDesc + Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bncf","不能重复！");
					break;	
				}
			}
			if (profeModifiedRecs[i].data.professionName == ""){
				isValid = false;
				Desc = NameLabelDesc + Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bnwk","不能为空！");
				break;
			} else {
				loop = 0, sNum = 0, rowNum = 0;
				//用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
				while (loop < 2){
					if(professionGrid.getStore().find("professionName",profeModifiedRecs[i].data.professionName,rowNum,false,false,true) != -1){
						rowNum = professionGrid.getStore().find("professionName",profeModifiedRecs[i].data.professionName,rowNum,false,false,true) + 1;
						sNum++;//相同数据行数量加1
					}
					loop++;
				}
				if (sNum == 2){
					isValid = false;
					Desc = NameLabelDesc + Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bncf","不能重复！");
					break;	
				}
			}	
		}
		result.push(isValid);
		result.push(Desc);	
		
		return result;
	},
	
	//验证管理人员，主要验证管理人员ID不能为空和重复数据
	managerValid: function(){
		//新增数据行
		var result = [];
		var isValid = true;
		var Desc;
		
		var managerGrid = this.getView().down('grid[name=managerGrid]');
		var managerNewRecs =   managerGrid.store.getNewRecords(); 
		for (var i=0; i<managerNewRecs.length; i++){
			if (managerNewRecs[i].data.managerOprid == ""){
				isValid = false;
				Desc = Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.glrybhbnwk","管理人员编号不能为空");
				break;
			} else {
				var loop = 0, sNum = 0, rowNum = 0;
				//用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
				while (loop < 2){
					if(managerGrid.getStore().find("managerOprid",managerNewRecs[i].data.managerOprid,rowNum,false,true,true) != -1){
						rowNum = managerGrid.getStore().find("managerOprid",managerNewRecs[i].data.managerOprid,rowNum,false,true,true) + 1;
						sNum++;//相同数据行数量加1
					}
					loop++;
				}
				if (sNum == 2){
					isValid = false;
					Desc = Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.glrytjcf","管理人员添加重复");
					break;	
				}
			}	
		}
		result.push(isValid);
		result.push(Desc);	
		
		return result;
	},
	
	
	onProjectSave: function(btn){
		var form = this.getView().child("form").getForm();
		var proView = this.getView();
		var actType = proView.actType;
		
		if (form.isValid()) {
			//专业方向grid验证
			var profeRsut = this.professionValid();
			var managerRst = this.managerValid();
			
			if(profeRsut[0]){
				if(managerRst[0]){
					var tzParams = this.getProjectDefnParams();
					Ext.tzSubmit(tzParams,function(responseData){
						var tabPanel = btn.findParentByType('panel').down('tabpanel');
						var actTab = tabPanel.getActiveTab();
						if(actType=="add"){
							proView.actType = "edit";	
							//form.findField("projectId").setValue(responseData.projectId);//保存后关闭会提示未保存
							form.setValues({projectId:responseData.projectId});
							if (actTab.xtype == 'grid'){
								var queryType = actTab.tabType;
								
								var tzStoreParams = '{"projectId":"'+responseData.projectId+'","queryType":"' + queryType + '"}';
								actTab.store.tzStoreParams = tzStoreParams;
								actTab.store.load();	
							}
							if(proView.proGrid.getStore() != null){ 
								proView.proGrid.getStore().reload();
							}
						} else {
							if (actTab.xtype == 'grid'){
								actTab.store.reload();	
							}
						}
						for(var i=0; i<tabPanel.items.length; i++){
							if(tabPanel.items.get(i).xtype == 'grid'){
								tabPanel.items.get(i).firstLoad = true;	
							}	
						}
					},"",true,this);
				}else{
					Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),managerRst[1]);
				}
			}else{
				Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),profeRsut[1]);
			}
		}
	},
	
	onProjectEnsure: function(btn){
		var form = this.getView().child("form").getForm();
		var proView = this.getView();
		var actType = proView.actType;
		
		if (form.isValid()) {
			//专业方向grid验证
			var profeRsut = this.professionValid();
			var managerRst = this.managerValid();
			
			if(profeRsut[0]){
				if(managerRst[0]){
					var tzParams = this.getProjectDefnParams();
					Ext.tzSubmit(tzParams,function(responseData){
						if(actType=="add" && proView.proGrid.getStore() != null){
							proView.proGrid.getStore().reload();	
						}
						proView.close();
					},"",true,this);
				}else{
					Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),managerRst[1]);
				}
			}else{
				Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),profeRsut[1]);
			}
		}
	},
	
	onProjectClose: function(){
		this.getView().close();
	},
	
	addProfessionAtLast: function(){
		var profeGrid = this.lookupReference('professionGrid');
		var cellEditing = profeGrid.getPlugin('professionCellediting');
		var profeStore = profeGrid.getStore();
		var rowCount = profeStore.getCount();
		
		var model = new KitchenSink.view.enrollProject.projectMg.professionModel({
            professionId: '',
            professionName: '',
            sortNum: rowCount+1,
			isSaved: 'N'
        });
		
		profeStore.insert(rowCount, model);
		cellEditing.startEditByPosition({
            row: rowCount,
            column: 1
        });
	},
	/*
	//添加专业方向	
	addProfession: function(view, rowIndex){
		var rec = new KitchenSink.view.enrollProject.projectMg.professionModel({
            professionId: 'NEXT',
            professionName: '',
            sortNum: rowIndex+2,
			isSaved: 'N'
        });
		var grid = this.lookupReference('professionGrid');
		var cellEditing = grid.getPlugin('professionCellediting');
		
        view.getStore().insert(rowIndex+1, rec);
		
		var items = view.store.data.items;
		for(var i = rowIndex+2;i< items.length;i++){
			items[i].set('sortNum',i+1);
		}
        cellEditing.startEditByPosition({
            row: rowIndex+1,
            column: 1
        });
	},*/
	//删除专业方向
	deleteCurrentRow: function(grid, rowIndex){
		//console.log(grid.store);
		var store = grid.store;
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.confirm","确认"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
            var removeRec = store.getAt(rowIndex);
			if(btnId == 'yes'){					      
			   store.remove(removeRec);			   
			   var items = store.data.items;
				for(var i = rowIndex; i< items.length; i++){
					items[i].set('sortNum',i+1);
				}
			}												  
		},this);  
	},
	
	
	addManagerAtLast: function(btn){
		var win = this.lookupReference('userWindow');
        
        if (!win) {
            win = new KitchenSink.view.enrollProject.projectMg.userWindow();
            this.getView().add(win);
        }
        win.show();
	},
	/*
	addManeger: function(view, rowIndex){
		var win = this.lookupReference('userWindow');
        if (!win) {
            win = new KitchenSink.view.enrollProject.projectMg.userWindow();
			win.multiSel = 'SINGLE';
			win.rowNum = rowIndex;
            this.getView().add(win);
        }
        win.show();	
	},
	*/
	//确认添加人员，需要验证不能添加重复的人员到人员管理中
	onUserChooseEnsure: function(btn){
		var win = btn.findParentByType('window');
		var multiSel = win.multiSel;
		var rowNum = win.rowNum;
		var grid = win.child('grid');
		var selList = grid.getView().getSelectionModel().getSelection();
		
		if (selList.length == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.qxzyhjl","请选择用户记录"));
			return;	
		} else {
			if (multiSel == 'SINGLE' && selList.length > 1){
				Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.znxzytjl","只能选择一条记录"));
				return;			
			} else {
				var isRept = false;
				var ManagerGrid = this.getView().down('tabpanel').getActiveTab();
				for(var i=0;i<selList.length; i++){
					if (ManagerGrid.getStore().find("managerOprid",selList[i].data.userOprid,0,false,true,true) != -1){
						isRept = true;
						break;
					}
				}
				if (!isRept){
					for(var i=0;i<selList.length; i++){
						if (multiSel == 'SINGLE'){
							var row = rowNum + i + 1;
						} else {
							var row = ManagerGrid.getStore().getCount();
						}
						
						var model = new KitchenSink.view.enrollProject.projectMg.userModel({
							managerOprid: selList[i].data.userOprid, 
							managerName: selList[i].data.userName,
							managerPhone: selList[i].data.userPhone,
							managerEmail: selList[i].data.userEmail,
							managerAccNo: selList[i].data.accountNum,
							orgId: selList[i].data.orgId
						});	
	
						ManagerGrid.getStore().insert(row, model);
					}
					win.close();
				} else {
					Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.xztjdryycz","选择添加的人员已存在"));
				}
			}
		}
	},
	
	onUserWinClose: function(btn){
		var win = btn.findParentByType('window');
		win.close();
	},
	//放大镜搜索报名表模板
	pmtSearchAppFormTmp: function(btn){
		var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        if(fieldName=='appOnFormModel'){
            searchDesc=Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.sszxbmbmb","搜索在线报名表模板");
            modal="appOnFormModel";
            modal_desc="appFormName";
        }else if(fieldName=="ps_appf_modal"){
            searchDesc=Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.sspsbmbmb","搜索评审报名表模板");
            modal="ps_appf_modal";
            modal_desc="ps_appf_modal_desc";
        }
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_APPTPL_DY_T',
			searchDesc: searchDesc,
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_EFFEXP_ZT:{
						value: 'Y',
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_APP_TPL_ID:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmbmbid","报名表模板ID"),
						operator:'01',
						type:'01'	
					},
					TZ_APP_TPL_MC:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmbmbmc","报名表模板名称"),
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_APP_TPL_ID:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmbmbid","报名表模板ID") ,
				TZ_APP_TPL_MC: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmbmbmc","报名表模板名称")
			},
			multiselect: false,
			callback: function(selection){
				form.findField(modal).setValue(selection[0].data.TZ_APP_TPL_ID);
				form.findField(modal_desc).setValue(selection[0].data.TZ_APP_TPL_MC);
			}
		});	
	},
	//放大镜搜索报名流程模板
	pmtSearchScheduModel: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_APPPRO_TMP_T',
			searchDesc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ssbmlcmb","搜索报名流程模板") ,
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_APPPRO_STATUS:{
						value: 'Y',
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_APPPRO_TMP_ID:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmlxmbid","报名流程模板ID"),
						operator:'01',
						type:'01'	
					},
					TZ_APPPRO_TMP_NAME:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmlxmbmc","报名流程模板名称"),
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_APPPRO_TMP_ID: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmlxmbid","报名流程模板ID"),
				TZ_APPPRO_TMP_NAME: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.bmlxmbmc","报名流程模板名称")
			},
			multiselect: false,
			callback: function(selection){
				form.findField("applyScheduModel").setValue(selection[0].data.TZ_APPPRO_TMP_ID);
				form.findField("appScheduModName").setValue(selection[0].data.TZ_APPPRO_TMP_NAME);
			}
		});	
	},
	//放大镜搜索递交资料模型
	pmtSearchSmtDtTmp: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_SBMINF_TMP_T',
			searchDesc: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ssdjzlmx","搜索递交资料模型"),
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_SBMINF_STATUS:{
						value: 'Y',
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_SBMINF_TMP_ID:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.djzlmxid","递交资料模型ID"),
						operator:'01',
						type:'01'	
					},
					TZ_SBMINF_TMP_NAME:{
						desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.djzlmxmc","递交资料模型名称"),
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_SBMINF_TMP_ID: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.djzlmxid","递交资料模型ID"),
				TZ_SBMINF_TMP_NAME:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.djzlmxmc","递交资料模型名称")
			},
			multiselect: false,
			callback: function(selection){
				form.findField("smtDtTmpId").setValue(selection[0].data.TZ_SBMINF_TMP_ID);
				form.findField("smtDtName").setValue(selection[0].data.TZ_SBMINF_TMP_NAME);
			}
		});	
	},
	
	//放大镜选择材料/面试评审成绩模型
    choiceScoreModal: function(btn){
        var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        if(fieldName=='clps_cj_modal'){
            searchDesc=Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.sscspscjmx","搜索材料评审成绩模型");
            modal="clps_cj_modal";
            modal_desc="clps_cj_modal_desc";
        }else if(fieldName=="msps_cj_modal"){
            searchDesc=Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ssmspscjmx","搜索面试评审成绩模型");
            modal="msps_cj_modal";
            modal_desc="msps_cj_modal_desc";
        }
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_RS_MODAL_TBL',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_MODAL_FLAG:{
                        value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_SCORE_MODAL_ID:{
                        desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.cjmxid","成绩模型ID"),
                        operator:'07',
                        type:'01'
                    },
                    TZ_MODAL_NAME:{
                        desc:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.cjmxms","成绩模型描述") ,
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_SCORE_MODAL_ID: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.cjmxid","成绩模型ID"),
                TZ_MODAL_NAME:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.cjmxms","成绩模型描述")
            },
            multiselect: false,
            callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_SCORE_MODAL_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_MODAL_NAME);
            }
        });
    },
	
	//查看管理人员
	viewUserInfo: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var accNum = rec.data.managerAccNo;/*用户账号*/
		var orgid = rec.data.orgId;/*机构ID*/
		console.log(accNum);
		console.log(orgid);
		//组件间跳转
		Ext.tzSetCompResourses("TZ_AQ_YHZHGL_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_YHZHGL_COM"]["TZ_AQ_YHZHXX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        cmp = new ViewClass();
		
		//登录账号
		var usAccNum = accNum;
		//机构编号
		var orgID = orgid;
		cmp.on('afterrender',function(){
			//用户账号信息表单
			var form = this.lookupReference('userAccountForm').getForm();
			//用户角色信息列表
			var grid = this.lookupReference('userRoleGrid');			
			//参数
			var tzParams = '{"ComID":"TZ_AQ_YHZHGL_COM","PageID":"TZ_AQ_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
				form.setValues(responseData);

				var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();						
			});
			
			//隐藏保存按钮
			cmp.getDockedItems()[0].items.get(0).hide();
			//隐藏确定按钮
			cmp.getDockedItems()[0].items.get(1).hide();
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);
		
		Ext.resumeLayouts(true);
		
		if (cmp.floating) {
		    cmp.show();
		}
	},
	
	
	//查看班级详情
	viewClassInfo: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var bj_id = rec.data.classId;
		//组件间跳转
		Ext.tzSetCompResourses("TZ_GD_BJGL_COM");
			//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_GD_BJJB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel,cmp, className, ViewClass, clsProto;
		
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
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}
		
		cmp = new ViewClass(bj_id);
        //操作类型设置为更新
		cmp.actType = "update";
		//cmp.class_id = bj_id;
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//页面注册信息列表
			var tabpanel = panel.child("form").child("tabpanel");
			var grid = tabpanel.getActiveTab();
			//加载报名流程模板
			var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_APPPRO_TMP_T',
				condition:{
					TZ_APPPRO_STATUS:{
						value:'Y',
						operator:"01",
						type:"01"
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_APPPRO_TMP_ID,TZ_APPPRO_TMP_NAME'
			});
			var _d=panel.down('combobox[name=bmlc_mb]');
			_d.setStore(lm_mbStore);
			//加载报名流程模板
			var lm_mbStore1 = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_SBMINF_TMP_T',
				condition:{
					TZ_SBMINF_STATUS:{
						value:'Y',
						operator:"01",
						type:"01"
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_SBMINF_TMP_ID,TZ_SBMINF_TMP_NAME'
			});
			var _dd=panel.down('combobox[name=djzl_mx]');
			_dd.setStore(lm_mbStore1);
			//参数
			var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_GD_BJJB_STD","OperateType":"QF","comParams":{"bj_id":"'+bj_id+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);

				//加载班级信息中的grid数据
				//专业方向页面重新加载
				var zy_grid = panel.down('grid[name=zyfx_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"2"}';
				zy_grid.store.tzStoreParams = Params;
				zy_grid.store.reload();
				//批次管理页面重新加载
				var pc_grid = panel.down('grid[name=pcgl_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"3"}';
				pc_grid.store.tzStoreParams = Params;
				pc_grid.store.reload();
				//管理人员页面重新加载
				var gl_grid = panel.down('grid[name=glry_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"4"}';
				gl_grid.store.tzStoreParams = Params;
				gl_grid.store.reload();
				//报名流程页面重新加载
				var _bm="";
				var bm_grid = panel.down('grid[name=applyItemGrid]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"5","lc_id":"'+_bm+'"}';
				bm_grid.store.tzStoreParams = Params;
				bm_grid.store.reload();
				//递交资料页面重新加载
				var _zl="";
				var zl_grid = panel.down('grid[name=applyItemGrid1]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"6","zl_id":"'+_zl+'"}';
				zl_grid.store.tzStoreParams = Params;
				zl_grid.store.reload();
			});
		});
        tab = contentPanel.add(cmp);     
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
	},
	
	//可配置搜索
	cfgSearchProject: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.TZ_PRJ_PROMG_VW',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
	//管理人员选择可配置搜索
	cfgSearchAdmin: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.TZ_ADMIN_YHXX_V',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    }
	
	
	
});