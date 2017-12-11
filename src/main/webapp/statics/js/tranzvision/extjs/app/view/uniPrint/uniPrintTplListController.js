Ext.define('KitchenSink.view.uniPrint.uniPrintTplListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uniPrintTplListController',
	//查询
	searchPrintTpl: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_DYMB_COM.TZ_DYMB_LIST_STD.TZ_DYMB_VW',
			condition:{
				TZ_JG_ID:Ext.tzOrgID
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	//新增
	addPrintTpl:function(btn) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DYMB_COM"]["TZ_DYMB_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DYMB_INF_STD，请检查配置。');
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
		cmp.on('afterrender',function(panel){
			var deletePdfBtn = panel.child('form').down("button[name=pdfDeleteBtn]");
			deletePdfBtn.hide();
		});
		var tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}
	},
	//编辑
	editPrintTpl:function(btn) {
		var me = this,
			view = me.getView();

		var selList = view.getSelectionModel().getSelection();
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");
			return;
		}else if(checkLen >1){
			Ext.Msg.alert("提示","只能选择一条要修改的记录");
			return;
		}

		var dymbId = selList[0].get("TZ_DYMB_ID");
		var jgId = selList[0].get("TZ_JG_ID");

		me.editPrintTplById(dymbId,jgId);
	},
	//根据模板编号编辑
	editPrintTplById:function(dymbId,jgId) {
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DYMB_COM"]["TZ_DYMB_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DYMB_INF_STD，请检查配置。');
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

			var ifEffectiveStore = new KitchenSink.view.common.store.appTransStore("TZ_IF_EFFECTIVE");
			ifEffectiveStore.reload();

			var form = panel.child('form').getForm();
			//参数
			var tzParams = '{"ComID":"TZ_DYMB_COM","PageID":"TZ_DYMB_INF_STD","OperateType":"QF","comParams":{"dymbId":"'+dymbId+'","jgId":"'+jgId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var deletePdfBtn = panel.child('form').down("button[name=pdfDeleteBtn]");
				if(responseData.TZ_DYMB_PDF_URL!="" && responseData.TZ_DYMB_PDF_URL!=undefined) {
					deletePdfBtn.show();
					form.findField("pdfuploadfile").setVisible(false);

					var url = TzUniversityContextPath + "/DownPdfPServlet?templateID="+responseData.TZ_DYMB_ID;
					form.findField("downfileName").setValue("<a href='"+url+"' target='_blank'>"+responseData.TZ_DYMB_PDF_NAME+"</a>");
				} else {
					deletePdfBtn.hide();
					form.findField("pdfuploadfile").setVisible(true);
				}

				form.setValues(responseData);
			});

			var grid = panel.down("grid[name=fieldGrid]");
			var gridStore = grid.getStore();
			var gridStoreParams = Ext.encode({
				cfgSrhId:"TZ_DYMB_COM.TZ_DYMB_INF_STD.TZ_DYMB_YS_VW",
				condition:{
					"TZ_JG_ID-operator":"01",
					"TZ_JG_ID-value":jgId,
					"TZ_DYMB_ID-operator":"01",
					"TZ_DYMB_ID-value":dymbId
				}
			});

			gridStore.tzStoreParams = gridStoreParams;
			gridStore.load();

		});

		var tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}

	},
	//删除
	deletePrintTpl:function(btn) {
		var me = this,
			view = me.getView(),
			store = view.store;

		var selList = view.getSelectionModel().getSelection();
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");
			return;
		}else {
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){
					store.remove(selList);
				}
			},this);
		}
	},
	//grid列编辑
	editOnePrintTpl:function(view,rowIndex) {
		var store = view.findParentByType("grid").store;

		var dymbId=store.getAt(rowIndex).data.TZ_DYMB_ID;
		var jgId=store.getAt(rowIndex).data.TZ_JG_ID;

		this.editPrintTplById(dymbId,jgId);
	},
	//grid列删除
	deleteOnePrintTpl: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	//保存和确定
	savePrintTpl: function(btn){
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
			var tzParams = '{"ComID":"TZ_DYMB_COM","PageID":"TZ_DYMB_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
	//关闭
	closePrintTpl:function(btn) {
		var grid = btn.findParentByType("grid");
		grid.close();
	}
});
