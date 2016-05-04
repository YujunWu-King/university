Ext.define('KitchenSink.view.enrollmentManagement.examStats.studentsExamController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.studentsExamController',
	tplid: "",
	/*新增统计项目*/
	addStatsClass: function(btn) {
		if (this.getView().actType == "add") {
			Ext.MessageBox.alert("提示", "请先保存后，再添加待统计项目。");
			return;
		}

		var win = this.lookupReference('projectListWindow');
		if (!win) {
			win = new KitchenSink.view.enrollmentManagement.examStats.projectListWindow();
			win.statsId = this.getView().statsId;
			this.getView().add(win);
		}
		win.show();
	},
	/*删除当前统计班级*/
	delStatsClass: function() {
		//选中行
		var selList = this.getView().down("grid[id=classGrid]").getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.prompt", "提示"), Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.youSelectedNothing", "您没有选中任何记录"));
			return;
		} else {
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.confirm", "确认"), Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.deleteConfirm", "您确定要删除所选记录吗?"),
			function(btnId) {
				if (btnId == 'yes') {
					//统计项目列表
					var grid = this.getView().down("grid[id=classGrid]");
					//统计项目数据
					var store = grid.getStore();
					store.remove(selList);
				}
			},
			this);
		}
	},
	/*生成/重新生成报表*/
	generateReports: function(btn) {
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取统计信息参数
			var tzParams = this.getClassInfoParams();
			var comView = this.getView();
			//统计项目列表
			var grid = comView.down("grid[id=classGrid]");
			//统计项目数据
			var store = grid.getStore();
			me = this;
			Ext.tzSubmit(tzParams,
			function(responseData) {
				if (comView.actType == "add") {
					comView.statsId = responseData.statsId;
					comView.actType = "update";
					form.setValues({
						"statsId": responseData.statsId
					});
				}
				//if(store.isLoaded()){
				store.reload();
				//}
				me.createStuInfo(store);

			},
			"", true, this);
		}
	},
	/*生成学生报考多项目信息Grid*/
	createStuInfo: function(store) {
		stuColumns = [{
			text: '姓名',
			dataIndex: 'username',
			width: 120
		},
		{
			text: '身份证号',
			dataIndex: 'codeId',
			width: 170
		},
		{
			text: '报考多项目',
			dataIndex: 'isMulti',
			width: 100
		}];

		var comView = this.getView();

		for (var i = 0; i < store.getCount(); i++) {
			var dataIndex = 'dIndex_' + store.getAt(i).data.classId;
			var className = store.getAt(i).data.className;
			var columnWidth = className.length;

			var gridColumn = new Ext.grid.Column({
				text: className,
				width: columnWidth > 60 ? columnWidth: 60,
				flex: 1,
				sortable: false,
				menuDisabled: true,
				dataIndex: dataIndex,
				renderer: function(value, metaData) {
					value = (value == undefined ? "": value);
					if (value == '是') {
						metaData.style = 'color:blue';
					}
					return value;
				}
			});
			stuColumns.push(gridColumn);
		};

		var stuStore = new KitchenSink.view.enrollmentManagement.examStats.studentsInfoStore({
			statsId: comView.statsId,
			username: comView.username,
			isMulti: comView.isMulti
		});
		
		console.log("username     " + comView.username);
		console.log("isMulti      " + comView.isMulti);
		
		var grid = comView.down('grid[name=stuGrid]');
		var panel = comView.down('panel[name=stuInfo]');
		if (grid != undefined) panel.remove(grid);
		grid = {
			xtype: 'grid',
			name: 'stuGrid',
			username: '',
			isMulti: '',
			store: stuStore,
			columns: stuColumns,
			plugins: [{
				ptype: 'gridexporter'
			}],
			dockedItems: [{
				xtype: "toolbar",
				items: [{
					text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.query", "查询"),
					iconCls: "query",
					handler: "cfgSearchStuInfo"
				},
				{
					text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.exportExcel", "导出学生数据到Excel"),
					handler: function(btn) {
						var grid = btn.findParentByType('grid');
						grid.saveDocumentAs({
							type: 'excel',
							title: '学生报考多项目信息',
							fileName: '学生报考多项目信息' + parseInt(Math.random() * 1000000000, 10) + '.xls'
						});
					}
				}]
			}]
		};
		panel.insert(0, grid);

		var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
		activeTab.mask("loading...");
		stuStore.load({
			callback: function(records, operation, success) {
				activeTab.unmask();
			}
		});
	},
	
	/*查询(学生报考多项目信息)*/
	cfgSearchStuInfo: function(btn) {
		if (this.getView().actType == "add") {
			Ext.MessageBox.alert("提示", "请先保存后，再查询学生报考多项目信息。");
			return;
		}

		var win = this.lookupReference('searchStuInfoWindow');
		if (!win) {
			win = new KitchenSink.view.enrollmentManagement.examStats.searchStuInfoWindow();
			win.statsId = this.getView().statsId;
			win.username = this.getView().username;
			win.isMulti = this.getView().isMulti;

			this.getView().add(win);
		}
		win.show();
	},
	
	/*搜索*/
	searchCfg: function(btn) {
		var win = btn.findParentByType("window");
		var form = win.child("form").getForm();

		var comView = this.getView();
		//统计项目列表
		var grid = comView.down("grid[id=classGrid]");
		//统计项目数据
		var store = grid.getStore();
		
		win.username = form.findField('username').getValue();
		win.isMulti = form.findField('isMulti').getValue();

		if (win.username == null) {
			win.username = "";
		}
		if (win.isMulti == null) {
			win.isMulti = "";
		}
		comView.username = win.username;
		comView.isMulti = win.isMulti;

		this.createStuInfo(store);

		win.close();
	},
	deleteClass: function(view, rowIndex) {
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.confirm", "确认"), Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.deleteConfirm", "您确定要删除所选记录吗?"),
		function(btnId) {
			if (btnId == 'yes') {
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},
		this);
	},
	/*保存(考生统计)*/
	onStatsSave: function(btn) {
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取统计信息参数
			var tzParams = this.getClassInfoParams();
			var comView = this.getView();
			//统计项目列表
			var grid = comView.down("grid[id=classGrid]");
			//统计项目数据
			var store = grid.getStore();
			Ext.tzSubmit(tzParams,
			function(responseData) {
				if (comView.actType == "add") {
					comView.statsId = responseData.statsId;
					comView.actType = "update";
					form.setValues({
						"statsId": responseData.statsId
					});
				}
				if (store.isLoaded()) {
					store.reload();
				}
			},
			"", true, this);
		}
	},
	/*确定（考生统计）*/
	onStatsEnsure: function(btn) {
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getClassInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,
			function(responseData) {
				//关闭窗口						   
				comView.close();
			},
			"", true, this);
		}
	},
	getClassInfoParams: function() {
		//考生统计表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		var statsId = this.getView().statsId;
		//更新操作参数
		var comParams = "";
		//新增
		if (actType == "add") {
			comParams = '"add":[{"data":' + Ext.JSON.encode(form.getValues()) + '}]';
		}
		//修改json字符串
		var editJson = "";
		if (actType == "update") {
			editJson = '{"typeFlag":"STATS","data":' + Ext.JSON.encode(form.getValues()) + '}';
		}

		//统计项目信息列表
		var grid = this.getView().down("grid[id=classGrid]");
		//统计项目信息数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords();
		for (var i = 0; i < mfRecs.length; i++) {
			if (editJson == "") {
				editJson = '{"typeFlag":"CLS","data":' + Ext.JSON.encode(mfRecs[i].data) + '}';
			} else {
				editJson = editJson + ',{"typeFlag":"CLS","data":' + Ext.JSON.encode(mfRecs[i].data) + '}';
			}
		}
		if (editJson != "") {
			if (comParams == "") {
				comParams = '"update":[' + editJson + "]";
			} else {
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		for (var i = 0; i < removeRecs.length; i++) {
			if (removeJson == "") {
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			} else {
				removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
			}
		}
		if (removeJson != "") {
			if (comParams == "") {
				comParams = '"delete":[' + removeJson + "]";
			} else {
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_EXAM_COUNT_COM","PageID":"TZ_EXAM_COUNT_STD","OperateType":"U","comParams":{' + comParams + '}}';
		return tzParams;
	},
	/*关闭*/
	onStatsClose: function(btn) {
		//关闭窗口
		this.getView().close();
	},
	/*确定(添加统计项目--确定)*/
	onClassChooseEnsure: function(btn) {
		var win = btn.findParentByType('window');
		var multiSel = win.multiSel;
		var rowNum = win.rowNum;
		var grid = win.child('grid');
		var selList = grid.getView().getSelectionModel().getSelection();

		if (selList.length == 0) {
			Ext.Msg.alert(Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.prompt", "提示"), Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.qxzclass", "请选择班级记录"));
			return;
		} else {
			var isRept = false;

			var classGrid = this.getView().down("grid[id=classGrid]");

			for (var i = 0; i < selList.length; i++) {
				if (classGrid.getStore().find("classId", selList[i].data.classId, 0, false, true, true) != -1) {
					isRept = true;
					break;
				}
			}
			if (!isRept) {
				for (var i = 0; i < selList.length; i++) {
					var row = classGrid.getStore().getCount();
					var order = row + 1;
					var model = new KitchenSink.view.enrollmentManagement.examStats.studentsExamModel({
						statsId: win.statsId,
						classId: selList[i].data.classId,
						className: selList[i].data.className,
						classStatus: selList[i].data.classStatus,
						order: order
					});

					classGrid.getStore().insert(row + i, model);
				}
				win.close();
			} else {
				Ext.Msg.alert(Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.prompt", "提示"), Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.ishasClass", "选择添加的班级已存在"));
			}
		}
	},
	/*确定(添加统计项目--关闭)*/
	onClassWinClose: function(btn) {
		var win = btn.findParentByType('window');
		win.close();
	},
	/*搜索(项目列表可配置搜索)*/
	cfgSearchClass: function(btn) {
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.PS_TZ_JG_CLASS_V',
			condition: {
				TZ_JG_ID: Ext.tzOrgID
			},
			callback: function(seachCfg) {
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	}
});