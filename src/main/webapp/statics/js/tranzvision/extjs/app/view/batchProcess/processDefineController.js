Ext.define('KitchenSink.view.batchProcess.processDefineController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.processDefineCon',

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_PROCESS_DF_COM.TZ_PROCESS_LIST.TZ_PROCESS_DF_VW',
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
	saveProcess:function(btn){
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
		var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_ADD","OperateType":"U","comParams":{'+comParams+'}}';
		//保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();
		},"",true,this);
	},
	ensureProcess:function(btn){
		this.saveProcess(btn);
		this.view.close();
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
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
	addProcess: function(){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_DF_COM"]["TZ_PROCESS_ADD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_ADD，请检查配置。');
			return;
		}
		var win = this.lookupReference('processDefineWindow');

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
	editProcess:function () {
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
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_DF_COM"]["TZ_PROCESS_EDIT"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_EDIT，请检查配置。');
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
		var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_ADD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
		});

		win.show();
	},
	deleteProcess:function () {
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

	//编辑当前进程
	editProcessBL:function (view,rowIndex) {

		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);

		var orgId = selRec.get("orgId");
		var processName = selRec.get("processName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PROCESS_DF_COM"]["TZ_PROCESS_EDIT"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_EDIT，请检查配置。');
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
		var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_ADD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","processName":"'+processName+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
		});

		win.show();
	},

	deleteProcessBL:function (view, rowIndex) {
		Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	
	onProcessWinSave: function(btn){
		
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		var tzParams = this.getProcessParams();
		
		var gridStore = btn.findParentByType("grid").store;
		if (form.isValid()) {
			Ext.tzSubmit(tzParams,function(){
				
				win.actType = "update";
				gridStore.load();
				form.reset();
			},"",true,this);
		}

	},
	
	onProcessWinEnsure: function(btn){
		
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		
		var gridStore = btn.findParentByType("grid").store;
		
		if (form.isValid()) {
			
			var tzParams = this.getProcessParams();
			Ext.tzSubmit(tzParams,function(){
				gridStore.load();
				win.close();
			},"",true,this);
		}
	},

	
	getProcessParams:function(){
		
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
		var tzParams = '{"ComID":"TZ_PROCESS_DF_COM","PageID":"TZ_PROCESS_ADD","OperateType":"U","comParams":{'+comParams+'}}';
		return tzParams;
	}
});
