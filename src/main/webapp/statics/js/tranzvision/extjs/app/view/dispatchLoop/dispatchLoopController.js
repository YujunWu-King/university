Ext.define('KitchenSink.view.dispatchLoop.dispatchLoopController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.dispatchLoopCon',

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_DD_LOOP_COM.TZ_DD_LOOP_LIST.TZ_DD_LOOP_VW',
			condition:
			{
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},

	addDispatchLoop: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	editDispatchLoop:function () {
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
		//机构ID
		var orgId = selList[0].get("orgId");
		var loopName = selList[0].get("loopName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("orgId").setReadOnly(true);
			form.findField("loopName").setReadOnly(true);

			//参数
			var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_INFO","OperateType":"QF","comParams":{"orgId":"'+orgId+'","loopName":"'+ loopName +'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
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
	saveDispatchLoop:function (btn) {

		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var removeJson = "";
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
		var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_LIST","OperateType":"U","comParams":{'+comParams+'}}';
		//保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();
		},"",true,this);
	},
	ensureDispatchLoop:function (btn) {
		this.saveDispatchLoop(btn);
		this.view.close();
	},
	deleteDispatchLoop:function () {
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.MessageBox.alert("提示", "您没有选中任何记录");
			return;
		}else{
			Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
				if(btnId == 'yes'){
					var tagStore = this.getView().store;
					tagStore.remove(selList);
				}
			},this);
		}
	},

	editDispatchLoopBL:function (view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var orgId = selRec.get("orgId");
		var loopName = selRec.get("loopName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("orgId").setReadOnly(true);
			form.findField("loopName").setReadOnly(true);

			//参数
			var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_INFO","OperateType":"QF","comParams":{"orgId":"'+orgId+'","loopName":"'+ loopName +'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
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
	deleteDispatchLoopBL:function (view, rowIndex) {
		Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	onDispatchLoopClose: function(btn){
		//关闭窗口
		this.getView().close();
	},

	//新增窗口保存方法
	onDispatchLoopInfoSave:function (btn) {

		var form = this.getView().child("form").getForm();
		var tzParams = this.getDispatchLoopInfoParams();
		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			
			comView.actType = "update";
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			contentPanel.child("dispatchLoopCon").store.reload();
		},"",true,this);
	},
	//新增窗口确定方法
	onDispatchLoopInfoEnsure:function (btn) {
		
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getDispatchLoopInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.child("dispatchLoopCon").store.reload();
				comView.close();
			},"",true,this);
		}
	},

	onDispatchLoopInfoClose: function(btn){
		//关闭窗口
		this.getView().close();
	},

	//判断表单为新增还是编辑
	getDispatchLoopInfoParams:function () {

		var form = this.getView().child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		//组件信息标志
		var actType = this.getView().actType;
		console.log("actType======" + actType)

		//年份处理
		var yearForm = this.getView().down('tabpanel').down('form[name=yearForm]').getForm();
		var yearFormValues = yearForm.getValues();

		if(Ext.getCmp("yearOne").checked == true){

		}else if(Ext.getCmp("yearTwo").checked == true){

		}else if(Ext.getCmp("yearThree").checked == true){

		}else{

		};
		//更新操作参数
		var comParams = "";

		//新增
		if(actType == "add"){
			comParams = '"add":[{"data":'+Ext.JSON.encode(formParams)+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"data":'+Ext.JSON.encode(formParams)+'}';
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}

		//提交参数
		var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_LIST","OperateType":"U","comParams":{'+comParams+'}}';
		return tzParams;
	}
});
