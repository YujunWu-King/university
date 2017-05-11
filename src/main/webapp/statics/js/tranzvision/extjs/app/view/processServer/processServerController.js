Ext.define('KitchenSink.view.processServer.processServerController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.processServerCon',

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_PROCESS_FW_COM.TZ_PROCESS_FW_LIST.TZ_JC_SERVER_VW',
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

	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	
	//保存
	onProcessServerWinSave: function(btn){
		
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		var tzParams = this.getProcessServerParams();
		
		var gridStore = btn.findParentByType("grid").store;
		
		Ext.tzSubmit(tzParams,function(){
			
			win.actType = "update";
			gridStore.load();
			form.reset();
		},"",true,this);
		
	},
	
	//确定
	onProcessServerWinEnsure: function(btn){

		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		
		var gridStore = btn.findParentByType("grid").store;
		
		if (form.isValid()) {
			
			var tzParams = this.getProcessServerParams();
			Ext.tzSubmit(tzParams,function(){
				gridStore.load();
				win.close();
			},"",true,this);
		}
	},
	
	getProcessServerParams:function(){
		
		var form = this.getView().child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		
		//新增
		if(actType == "add"){
			comParams = '"add":['+ Ext.JSON.encode(formParams)+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			comParams = '"update":[' + Ext.JSON.encode(formParams) + ']';
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_ADD","OperateType":"U","comParams":{'+comParams+'}}';
		return tzParams;
	},
	
	saveProcessServer:function (btn) {

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
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_LIST","OperateType":"U","comParams":{'+comParams+'}}';
		//保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();
		},"",true,this);
	},
	ensureProcessServer:function (btn) {
		this.saveProcessServer(btn);
		this.view.close();
	},
	addProcessServer:function () {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_FW_COM"]["TZ_PROCESS_FW_ADD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_FW_ADD，请检查配置。');
			return;
		}
		var win = this.lookupReference('processServerWindow');

		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}

		//操作类型设置为新增
		win.actType = "add";
		win.show();
	},
	editProcessServer:function () {
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
		var orgId = selList[0].get("orgId");
		var processName = selList[0].get("processName");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_FW_COM"]["TZ_PROCESS_FW_EDIT"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_FW_EDIT，请检查配置。');
			return;
		}
		var win = this.lookupReference('processDefineEditWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			var form = win.child('form').getForm();

			this.getView().add(win);
		}
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"QF","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
		});

		win.show();
	},
	deleteProcessServer:function () {
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
	startProcessServerBL:function (view,rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var orgId = selRec.get("orgId");
		var processName = selRec.get("processName");
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"startProcess","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			store.reload();
			Ext.MessageBox.alert("提示", "进程服务器已启动！");
		});
	},
	stopProcessServerBL:function (view,rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);

		var orgId = selRec.get("orgId");
		var processName = selRec.get("processName");
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"stopProcess","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			store.reload();
			Ext.MessageBox.alert("提示", "进程服务器已停止！");
		});
	},

	//编辑当前进程服务器
	editProcessServerBL:function (view,rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);

		var orgId = selRec.get("orgId");
		var processName = selRec.get("processName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_FW_COM"]["TZ_PROCESS_FW_EDIT"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_FW_EDIT，请检查配置。');
			return;
		}
		var win = this.lookupReference('processServerEditWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			var form = win.child('form').getForm();

			this.getView().add(win);
		}
		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"QF","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
		});

		win.show();
	},
	deleteProcessServerBL:function () {
		Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	startProcessWin:function (btn) {
		var win = btn.findParentByType("window");
		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var form = win.child("form").getForm();
		var formValues = form.getValues();
		var orgId = formValues['orgId'];
		var processName = formValues['processName'];
		console.log(orgId)
		console.log(processName)
		var processStore = new KitchenSink.view.processServer.processServerStore();

		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"startProcess","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			store.reload();
			Ext.MessageBox.alert("提示", "进程服务器已启动！");
		});
	},
	stopProcessWin:function (btn) {
		var win = btn.findParentByType("window");
		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var form = win.child("form").getForm();
		var formValues = form.getValues();
		var orgId = formValues['orgId'];
		var processName = formValues['processName'];
		var processStore = new KitchenSink.view.processServer.processServerStore();

		var tzParams = '{"ComID":"TZ_PROCESS_FW_COM","PageID":"TZ_PROCESS_FW_EDIT","OperateType":"stopProcess","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			store.reload();
			Ext.MessageBox.alert("提示", "进程服务器已停止！");
		});
	}
});
