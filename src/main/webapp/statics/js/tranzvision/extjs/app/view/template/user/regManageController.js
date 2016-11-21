Ext.define('KitchenSink.view.template.user.regManageController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.regManage',
	/*保存注册项模板*/
    onUserRegSave: function(btn) {
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var siteId = form.findField("siteId").getValue();
			
			var grid = this.getView().child("grid");
			//var grid = btn.findParentByType("panel").child("grid");
			//注册项数据
			var store = grid.getStore();

			//修改记录
			var editJson = "";
			var mfRecs = store.getModifiedRecords();

			for (var i = 0; i < mfRecs.length; i++) {
				if (editJson == "") {
					editJson = Ext.JSON.encode(mfRecs[i].data);
				} else {
					editJson = editJson + ',' + Ext.JSON.encode(mfRecs[i].data);
				}
			}
			var comParams = '"update": [';
			if (editJson != "") {
				comParams += '{"OperateType": "saveGrid","data": [' + editJson + ']},';
			}

			if (form.getValues() != "") {
				comParams += '{"OperateType": "saveForm","data": [' + Ext.JSON.encode(form.getValues()) + ']}';
			}
			comParams += "]";
			//提交参数
			var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"U","comParams":{' + comParams + '}}';

			Ext.tzSubmit(tzParams,
				function (responseData) {
					var tzParams1 = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"QF","comParams":{"siteId": "' + siteId + '"}}';

					Ext.tzLoad(tzParams1,
						function (responseData) {
							//组件注册信息数据
							var formData = responseData.formData;
							form.setValues(formData);
							//页面注册信息列表数据
							//var listData = responseData.listData;
							//grid.store.loadData(listData);
							var store = grid.getStore();
		        			var tzStoreParams = '{"siteId":"'+siteId+'"}';
		        			store.tzStoreParams = tzStoreParams;
							store.load();
						}
					);
				}
                ,"",true,this);
		}
	},
	/*窗口方式用户注册项管理*/
	onUserRegEdit: function(btn) {
		var me = this;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_USER_REG_COM"]["TZ_REGGL_WIN_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改数据的权限");
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert("提示", "未找到该功能页面对应的JS类，页面ID为：TZ_REGGL_WIN_STD，请检查配置。");
			return;
		}

		var win = this.lookupReference('regUserWindows');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		win.show();
	},
	/*发布注册项模板*/
	onUserRegPub: function(btn) {
		var form = this.getView().child("form").getForm();
		var grid = this.getView().child("grid");

		var comParams = '"update": [{"OperateType": "pub"}]';
		//提交参数
		var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"U","comParams":{' + comParams + '}}';

		Ext.tzSubmit(tzParams,
			function(responseData) {
				var tzParams1 = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"QF","comParams":{}}';

				Ext.tzLoad(tzParams1,
					function(responseData) {
						//组件注册信息数据
						var formData = responseData.formData;
						form.setValues(formData);
						//页面注册信息列表数据
						//var listData = responseData.listData;
						//grid.store.loadData(listData);
						grid.store.load();
					});
			},"",true,this);
	},
	/*预览注册项模板*/
	onUserRegPreView: function(btn) {
		var form = this.getView().child("form").getForm();
		var siteId = form.findField("siteId").getValue();
		var view = this.getView();
		var grid = this.getView().child("grid");
		var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_VIEW_STD","OperateType":"HTML","comParams":{"siteId":"'+siteId+'"}}';
		var tmpURL = Ext.tzGetGeneralURL() + "?tzParams=" + window.escape(tzParams);
		window.open(tmpURL);
	},
	/*设置注册项为下拉框的下拉框选项值*/
	onReSetDropVal: function(view,t,rowIndex) {
		var store = view.findParentByType("grid").store;
		var siteId = store.getAt(rowIndex).get("siteId");
		var regId = store.getAt(rowIndex).get("regId");
		var regFieldType = store.getAt(rowIndex).get("regFieldType");
		var isSysField = store.getAt(rowIndex).get("isSysField");

		//文本框类型没有下拉框值配置页面
		if(regFieldType == "" || regFieldType == "INP" || isSysField == "" || isSysField == "Y"){
			return;
		}

		var me = this;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_USER_REG_COM"]["TZ_DROP_BOX_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改数据的权限");
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert("提示", "未找到该功能页面对应的JS类，页面ID为：TZ_DROP_BOX_STD，请检查配置。");
			return;
		}

		var win = this.lookupReference('dropBoxSetWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		var tzStoreParams = "{\"regId\":\"" + regId + "\",\"siteId\":\"" + siteId +"\"}";
		/*下拉框值Grid.store*/
		var vinStroe = win.child('grid').store;

		var form = win.child("form").getForm();
		form.reset();
		form.setValues({
			"siteId": siteId,
			"regId": regId
		});

		vinStroe.tzStoreParams = tzStoreParams;
		vinStroe.load();
		win.show();

	},
	/*保存（下拉框window）*/
	onOptionsSave: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		//获取信息项选项参数
		var tzParams = this.getItmeOptionsParams(win);

		Ext.tzSubmit(tzParams,
			function(responseData) {
				var grid = win.down('grid[name=dropBoxSetGrid]');
				//信息项选项数据
				grid.getStore().load();
			},"",true,this);
	},
	/*确定（下拉框window）*/
	onOptionsEnsure: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		/*保存页面注册信息*/
		var tzParams = this.getItmeOptionsParams(win);

		Ext.tzSubmit(tzParams,
			function(responseData) {
				//关闭窗口
				win.close();
			},"",true,this);
	},
	/*关闭（下拉框window）*/
	onOptionsClose: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	/*保存下拉框选项值页面*/
	getItmeOptionsParams: function(win) {
		var regId = win.child("form").getForm().findField("regId").getValue();
		var siteId = win.child("form").getForm().findField("siteId").getValue();
		//更新操作参数
		var comParams = "";

		//信息项选项grid;
		var grid = win.down('grid[name=dropBoxSetGrid]');
		//信息项选项数据
		var store = grid.getStore();

		//修改记录
		var editJson = "";
		var mfRecs = store.getModifiedRecords();
		for (var i = 0; i < mfRecs.length; i++) {
			if (editJson == "") {
				editJson = '{"siteId":"' + siteId + '","regId":"' + regId + '","data":' + Ext.JSON.encode(mfRecs[i].data) + '}';
			} else {
				editJson = editJson + ',{"siteId":"' + siteId + '","regId":"' + regId + '","data":' + Ext.JSON.encode(mfRecs[i].data) + '}';
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
				removeJson = '{"siteId":"' + siteId + '","regId":"' + regId + '","data":' + Ext.JSON.encode(removeRecs[i].data) + '}';
			} else {
				removeJson = removeJson + ',{"siteId":"' + siteId + '","regId":"' + regId + '","data":' + Ext.JSON.encode(removeRecs[i].data) + '}';
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
		var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_DROP_BOX_STD","OperateType":"U","comParams":{' + comParams + '}}';

		return tzParams;
	},
	/*新增下拉框选项值*/
	addOption: function() {
		var dropBoxSetGrid = this.lookupReference('dropBoxSetGrid');
		var dropBoxSetCellediting = dropBoxSetGrid.getPlugin('dropBoxSetCellediting');
		var dropBoxSetStore = dropBoxSetGrid.getStore();
		var rowCount = dropBoxSetStore.getCount();

		var i = 0;
		var maxNum = 1;
		for (i = 0; i < rowCount; i++) {
			var num = dropBoxSetStore.getAt(i).get("order");
			if (num != "" && maxNum <= parseInt(num)) {
				maxNum = parseInt(num) + 1;
			}
		}

		dropBoxSetCellediting.cancelEdit();
		// Create a model instance
		var r = Ext.create('KitchenSink.view.template.user.dropBoxSetModel', {
			order: maxNum,
			optId: "",
			optName: "",
			isSelect:false
		});

		dropBoxSetStore.insert(rowCount, r);
		dropBoxSetCellediting.startEditByPosition({
			row: rowCount,
			column: 1
		});
	},
	/*删除多行（下拉框）*/
	deleteOption: function(view, rowIndex) {
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?',
			function(btnId) {
				if (btnId == 'yes') {
					var store = view.findParentByType("grid").store;
					store.removeAt(rowIndex);
				}
			},
			this);
	},
	/*删除下拉框选项值*/
	deleteOptions: function() {
		//选中行
		var dropBoxSetGrid = this.lookupReference('dropBoxSetGrid');
		var selList = dropBoxSetGrid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录");
			return;
		} else {
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?',
				function(btnId) {
					if (btnId == 'yes') {
						var store = dropBoxSetGrid.store;
						store.remove(selList);
					}
				},
				this);
		}
	},
	/*保存（用户注册管理WIN）*/
	onRegUserWinSave:function(btn){
		var form = this.getView().child("form").getForm();
		var grid = this.getView().child("grid");
		//var grid = btn.findParentByType("panel").child("grid");
		//注册项数据
		var store = grid.getStore();

		//修改记录
		var editJson = "";
		var mfRecs = store.getModifiedRecords();

		for (var i = 0; i < mfRecs.length; i++) {
			if (editJson == "") {
				editJson = Ext.JSON.encode(mfRecs[i].data);
			} else {
				editJson = editJson + ',' + Ext.JSON.encode(mfRecs[i].data);
			}
		}
		if (editJson != "") {
			comParams = '"update": [{"OperateType": "save","data": [' + editJson + ']}]';
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_WIN_STD","OperateType":"U","comParams":{' + comParams + '}}';

		Ext.tzSubmit(tzParams,
			function(responseData) {
				var tzParams1 = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_WIN_STD","OperateType":"QF","comParams":{}}';

				Ext.tzLoad(tzParams1,
					function(responseData) {
						//组件注册信息数据
						var formData = responseData.formData;
						form.setValues(formData);
						//页面注册信息列表数据
						grid.store.pageID = 'TZ_REGGL_WIN_STD';
						grid.store.load();
					});
			},"",true,this);
	},
	/*确定（用户注册管理WIN）*/
	onRegUserWinEnsure:function(btn){
		var form = this.getView().child("form").getForm();
		//var grid = this.getView().child("grid");
		var grid = btn.findParentByType("panel").child("grid");
		//注册项数据
		var store = grid.getStore();

		//修改记录
		var editJson = "";
		var mfRecs = store.getModifiedRecords();

		for (var i = 0; i < mfRecs.length; i++) {
			if (editJson == "") {
				editJson = Ext.JSON.encode(mfRecs[i].data);
			} else {
				editJson = editJson + ',' + Ext.JSON.encode(mfRecs[i].data);
			}
		}
		if (editJson != "") {
			comParams = '"update": [{"OperateType": "save","data": [' + editJson + ']}]';
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_WIN_STD","OperateType":"U","comParams":{' + comParams + '}}';

		Ext.tzSubmit(tzParams,
			function(responseData) {
				var tzParams1 = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_WIN_STD","OperateType":"QF","comParams":{}}';

				Ext.tzLoad(tzParams1,
					function(responseData) {
						//组件注册信息数据
						var formData = responseData.formData;
						form.setValues(formData);
						//页面注册信息列表数据
						grid.store.pageID = 'TZ_REGGL_WIN_STD';
						grid.store.load();
					});
			},"",true,this);
		var win = btn.findParentByType("window");
		win.close();
	},
	/*关闭（用户注册管理WIN）*/
	onRegUserWinClose: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	//关闭
	onPanelClose:function(btn){
		var grid=btn.up('regManage');
		grid.close();
	}
});