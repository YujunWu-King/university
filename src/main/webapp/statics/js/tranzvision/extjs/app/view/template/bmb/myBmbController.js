Ext.define('KitchenSink.view.template.bmb.myBmbController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.myBmb',
	tplid : "",
	/* 新增报名表模板 */
	addBmbTpl : function() {
		var me = this;
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_ADD_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示',
					'未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_ADD_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('myBmbRegWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			// 新建类
			win = new ViewClass();
			this.getView().add(win);
		} else {
			var activeTab = win.items.items[0].getActiveTab();
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
		}

		win.show();

		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"), function(i) {
						this.style.backgroundColor = null
					});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			console.log(win.items);

			var activeTab = win.items.items[0].getActiveTab();

			var newName = el.getElementsByClassName("tplname")[0]
					.getAttribute("title")
					+ "_" + (+new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
	},
	// 选中后编辑
	editBmbTpl : function(btn) {
		// 选中行
		var grid = btn.up('grid');
		var selList = grid.getSelectionModel().getSelection();
		// 选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要修改的记录");
			return;
		} else if (checkLen > 1) {
			Ext.Msg.alert("提示", "只能选择一条要修改的记录");
			return;
		}

		var tplid = selList[0].get("tplid");
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		// var
		// tzParams='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"'
		// + tplid + '"}}'
		// var url = Ext.tzGetGeneralURL() + tzParams;
		var url = TzUniversityContextPath + "/admission/diyform/" + tplid;
		window.open(url, '_blank');
	},
	/* 编辑报名表模板 */
	onBmbTplEdit : function(view, rowIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		// 模板ID
		var tplid = selRec.get("tplid");
		// var
		// tzParams='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"'
		// + tplid + '"}}'
		// var url = Ext.tzGetGeneralURL() + tzParams;
		var url = TzUniversityContextPath + "/admission/diyform/" + tplid;
		window.open(url, '_blank');
	},
	
	/* 测试 */
//	onDownTest : function(view, rowIndex) {
//		var url = TzUniversityContextPath + "/DownPdfServlet?instanceID=259&fileName=你好.pdf";
//		window.open(url, '_blank');
//	},

	/* 复制报名表模板 */
	onBmbTplCopy : function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		this.tplid = selRec.get("tplid");
		Ext.MessageBox.prompt('复制模板', '请输入另存模板的名称:', this.showResultText, this);
	},

	/* 预览报名表模板 */
	onBmbTplPreview : function(gridViewObject, rowIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_FORM_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有预览报名表模板的权限');
			return;
		}

		var tplid = gridViewObject.store.getAt(rowIndex).get("tplid");
		var tzParams = '?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_FORM_STD","OperateType":"HTML","comParams":{"mode":"Y","TZ_APP_TPL_ID":"'
				+ tplid + '"}}'
		var url = Ext.tzGetGeneralURL() + tzParams;
		window.open(url, '_blank');

		/*
		 * var tplname = gridViewObject.store.getAt(rowIndex).get("tplname");
		 * var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		 * var tab = contentPanel.add({ title: tplname, loader: { url:
		 * externalURL + '?mode=Y&TZ_QSTN_TPL_ID=' + tplid, contentType: 'html',
		 * loadMask: true } }); var items = contentPanel.items.items;
		 * items[items.length - 1].loader.load();
		 * contentPanel.setActiveTab(tab);
		 */
	},

	showResultText : function(id, text) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		if (id == "ok") {
			if (text) {
				// 组件注册信息数据
				var store = this.getView().getStore(), lan = "";
				var tzStoreParams = '{"add":[{"id":"' + this.tplid
						+ '","name":"' + text + '","language":"' + lan + '"}]}';
				var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":'
						+ tzStoreParams + '}';
				Ext.tzSubmit(tzParams, function(jsonObject) {
							store.reload();
							// var
							// tzPar='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"'
							// + jsonObject.id + '"}}'
							// var url = Ext.tzGetGeneralURL() + tzPar;
							var url = TzUniversityContextPath
									+ "/admission/diyform/" + jsonObject.id;
							window.open(url, '_blank');
						}, "", true, this);
			} else {
				/* 模板名称不能为空 */
				Ext.MessageBox.alert('提示', '新的模板名称不能为空！');
				return;
			}
		}
		return;
	},

	/* 新增报名表模板页面，确定 */
	onBmbRegEnsure : function(btn) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		var win = this.lookupReference('myBmbRegWindow');
		var activeTab = win.items.items[0].getActiveTab();
		var form = activeTab.getForm();
		if (form.isValid()) {
			// 组件注册信息列表
			var grid = btn.findParentByType("myBmb");
			// 组件注册信息数据
			var store = grid.getStore();

			var win = this.lookupReference('myBmbRegWindow');
			var activeTab = win.items.items[0].getActiveTab(), id = '';
			var tplName = Ext.get(activeTab.id).select('input').elements[0].value, tplId = "", lan = "";

			// add By ZhangLang @20150709
			if (activeTab.itemId == "add") {
				var form = activeTab.getForm();
				lan = form.findField('language').getValue();
			}

			if (activeTab.itemId == "predefine") {
				Ext.each(Ext.query(".tplitem"), function(i) {
							if (this.style.backgroundColor == "rgb(173, 216, 230)") {
								tplId = this.getAttribute("data-id");
								return false;
							}
						});
			} else {
				tplId = "";
			}

			if (tplName) {
				var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"'
						+ tplName + '","language":"' + lan + '"}]}'
				var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":'
						+ tzStoreParams + '}';
				Ext.tzSubmit(tzParams, function(jsonObject) {
							// Ext.get(activeTab.id).select('input').elements[0].value
							// = "";
							store.reload();
							// var tzPar =
							// '?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"'
							// + jsonObject.id + '"}}'
							// var url = Ext.tzGetGeneralURL() + tzPar;
							var url = TzUniversityContextPath
									+ "/admission/diyform/" + jsonObject.id;
							window.open(url, '_blank');
							win.close();
						}, "", true, this);
			}
		}
	},
	/* 新增报名表模板页面，确定 */
	onBmbRegClose : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	/* 设置管理权限 */
	onBmbTplSet : function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		// 模板ID
		var tplid = selRec.get("tplid");

		var me = this;
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_ROLE_STD"];

		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改报名表模板配置数据的权限");
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert("提示",
					"未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_ROLE_STD，请检查配置。");
			return;
		}

		var win = this.lookupReference('myRoleSetWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			// 新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		// var tzStoreParams = "{'tplid':'" + tplid + "'}";
		var tzStoreParams = '{"tplid":"' + tplid + '"}';
		/* 下拉框值Grid.store */
		var vinStroe = win.child('grid').store;
		vinStroe.tzStoreParams = tzStoreParams;
		vinStroe.load();
		win.show();
	},
	/* 保存（设置管理权限） */
	onRoleSetSave : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		// 获取信息项选项参数
		var tzParams = this.getUpdateParams(win);
		console.log(tzParams);
		Ext.tzSubmit(tzParams, function(responseData) {
					console.log(responseData)
					var grid = win.down('grid');
					// 信息项选项数据
					grid.getStore().load();
				}, "", true, this);
	},
	/* 确定（设置管理权限） */
	onRoleSetEnsure : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		/* 保存页面注册信息 */
		var tzParams = this.getUpdateParams(win);

		Ext.tzSubmit(tzParams, function(responseData) {
					// 关闭窗口
					win.close();
				}, "", true, this);
	},
	/* 获取修改的数据 */
	getUpdateParams : function(win) {
		// 更新操作参数
		var comParams = "";

		// 信息项选项grid;
		var grid = win.down('grid');
		// 信息项选项数据
		var store = grid.getStore();

		// 修改记录
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
			comParams = '"update":[' + editJson + "]";
		}

		// 提交参数
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ROLE_STD","OperateType":"U","comParams":{'
				+ comParams + '}}';

		return tzParams;
	},
	/* 关闭（设置管理权限） */
	onRoleSetClose : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	// 关闭
	onPanelClose : function(btn) {
		// alert("onPanelClose");
		var grid = btn.up('grid');
		grid.close();
	},
	// 查询报名表，可配置搜索
	queryBmbTpl : function(btn) {
		Ext.tzShowCFGSearch({
					cfgSrhId : 'TZ_ONLINE_REG_COM.TZ_ONREG_MNG_STD.TZ_APPTPL_V',
					condition : {
						"TZ_JG_ID" : Ext.tzOrgID
						// 设置搜索字段的默认值，没有可以不设置condition;
					},
					callback : function(seachCfg) {
						var store = btn.findParentByType("grid").store;
						store.tzStoreParams = seachCfg;
						store.load();
					}
				});
	},
	/* 报名表打印设置 */
	onBmbPrintSet : function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		// 模板ID
		var tplid = selRec.get("tplid");
		var me = this;
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_PRINT_STD"];

		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改报名表模板打印配置的权限");
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert("提示",
					"未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_PRINT_STD，请检查配置。");
			return;
		}

		var win = this.lookupReference('myPrintSetWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			// 新建类
			win = new ViewClass();
			this.getView().add(win);
		}

		var form = win.child("form").getForm();
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PRINT_STD","OperateType":"QF","comParams":{"tplid":"'
				+ tplid + '"}}';
		// 加载数据
		Ext.tzLoad(tzParams, function(responseData) {
					// 组件注册信息数据
					var formData = responseData.formData;
					form.setValues(formData);
					win.show();
				});

	},
	/* PDF打印模板设置，进入页面 */
	onPdfPrintSet : function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var tplID = selRec.get("tplid");

		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_PDF_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示',
					'未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_PDF_STD，请检查配置。');
			return;
		}

		var contentPanel, cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		if (!Ext.ClassManager.isCreated(className)) {
			Ext.syncRequire(className);
		}

		ViewClass = Ext.ClassManager.get(className.toString());

		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
							clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so
			// issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log
						.warn('Example \''
								+ className
								+ '\' lacks a theme specification for the selected theme: \''
								+ themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();

		// 操作类型设置为更新
		cmp.actType = "update";

		cmp.on('afterrender', function(panel) {

			var form = panel.child('form').getForm();
			form.findField("jgName").setReadOnly(true);
			form.findField("tplName").setReadOnly(true);
			form.findField("jgName").addCls("lanage_1");
			form.findField("tplName").addCls("lanage_1");
			var grid = panel.child('grid');
			// 参数
			var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PDF_STD","OperateType":"QF","comParams":{"tplID":"'
					+ tplID + '"}}';
			// 加载数据
			Ext.tzLoad(tzParams, function(responseData) {
						var formData = responseData.formData;
						form.setValues(formData);
						var tzStoreParams = '{"tplID":"' + tplID + '"}';
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
	/* 加载报名表模板字段 */
	loadAppFormModalFields : function(btn) {
		var actType = this.getView().actType;
		var form = this.getView().child('form').getForm();
		var exportTplID = form.findField("tplID").getValue();

		if (actType == "add") {
			Ext.Msg.alert("提示", "请先保存导出模板信息再加载报名表模板字段。");
			return;
		};

		if (exportTplID == "" || exportTplID.length < 1) {
			Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法新增模板字段。');
			return;
		}

		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		if (store.getCount() > 0) {
			Ext.MessageBox.confirm('确认', '字段定义列表中已有数据，确定重新加载报名表模板字段吗？',
					function(btnId) {
						if (btnId == 'yes') {
							store.removeAll();
							this.loadFieldsStore(exportTplID, store);
						}
					}, this);
		} else {
			this.loadFieldsStore(exportTplID, store);
		}
	},
	/* 加载报名表模板字段 实际操作 */
	loadFieldsStore : function(exportTplID, store, btn) {
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PDF_STD","OperateType":"loadAppFormFields","comParams":{"tplID":"'
				+ exportTplID + '"}}';
		// 加载数据
		Ext.tzLoad(tzParams, function(responseData) {
					var fieldList = responseData.root;
					if (fieldList == null || fieldList.length == 0) {
						Ext.Msg.alert("提示", "选择的报名表模板中没有字段。");
						return;
					}
					var seq = 0;
					for (var i = 0; i < fieldList.length; i++) {
						seq += 10;
						var model = new KitchenSink.view.template.bmb.myBmbPdfModel(
								{
									tplID : exportTplID,
									fieldID : fieldList[i].fieldID,
									fieldName : fieldList[i].fieldName,
									pdffield1 : '',
									pdffield2 : '',
									pdffield3 : ''
								});
						store.add(model);
					}
				});
	},
	/* 加载PDF模板信息项 */
	addPdfField : function(btn) {
		var actType = this.getView().actType;
		var form = this.getView().child('form').getForm();
		var exportTplID = form.findField("tplID").getValue();
		var fileName = form.findField("fileName").getValue();
		// Ext.Msg.alert("提示", fileName);
		// return;

		if (actType == "add") {
			Ext.Msg.alert("提示", "请先保存导出模板信息再加载报名表模板字段。");
			return;
		};

		if (exportTplID == "" || exportTplID.length < 1) {
			Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法新增模板字段。');
			return;
		}
		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有上传pdf文档，无法加载PDF模板信息项。');
			return;
		}

		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		if (store.getCount() > 0) {
			Ext.MessageBox.confirm('确认', '字段定义列表中已有数据，确定重新加载报名表模板字段吗？',
					function(btnId) {
						if (btnId == 'yes') {
							this.loadFieldsStoreByPdf(exportTplID, fileName,
									store);
						}
					}, this);
		} else {
			// this.loadFieldsStore(exportTplID, fileName,store);
			Ext.MessageBox.alert('提示', '字段定义列表中没有数据，无法加载PDF模板信息项。');
			return;
		}
	},
	/* 加载PDF模板信息项 实际操作 */
	loadFieldsStoreByPdf : function(exportTplID, fileName, store, btn) {
		var storDates = this.getGridInfoParams(store);
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PDF_STD","OperateType":"loadAppFormPdf","comParams":{"tplID":"'
				+ exportTplID
				+ '","fileName":"'
				+ fileName
				+ '","storDates":['
				+ storDates + ']}}';
		 //Ext.MessageBox.alert('tzParams', tzParams);
		 //return;

		// 加载数据

		Ext.tzLoad(tzParams, function(responseData) {
					var fieldList = responseData.root;
					if (fieldList == null || fieldList.length == 0) {
						Ext.Msg.alert("提示", "PDF模版文件里面没有可以匹配的项。");
						return;
					}
					var seq = 0;
					for (var i = 0; i < fieldList.length; i++) {
						seq += 10;
						var model = new KitchenSink.view.template.bmb.myBmbPdfModel(
								{
									tplID : exportTplID,
									fieldID : fieldList[i].fieldID,
									fieldName : fieldList[i].fieldName,
									pdffield1 : fieldList[i].pdffield1,
									pdffield2 : fieldList[i].pdffield2,
									pdffield3 : fieldList[i].pdffield3
								});
						store.add(model);
					}
				});

	},
	/* 加载grid的信息 构造提交后台的参数 */
	getGridInfoParams : function() {
		// 修改json字符串
		var editJson = "";
		// 字段列表
		var grid = this.getView().child("grid");
		var store = grid.getStore();
		var count = store.getCount();

		// Ext.Msg.alert("1111", editRecs);
		// return;
		for (var i = 0; i < count; i++) {
			if (editJson == "") {
				editJson = '{"data":' + Ext.JSON.encode(store.getAt(i).data)
						+ '}';
			} else {
				editJson = editJson + ',' + '{"data":'
						+ Ext.JSON.encode(store.getAt(i).data) + '}';
			}
		}
		store.removeAll();
		store.commitChanges();
		return editJson;
	},
	/* 添加字段 */
	addField : function(btn) {
		var actType = this.getView().actType;
		var form = this.getView().child('form').getForm();
		var tplID = form.findField("tplID").getValue();

		if (actType == "add") {
			Ext.Msg.alert("提示", "请先保存导出模板信息再加载报名表模板字段。");
			return;
		};

		if (tplID == "" || tplID.length < 1) {
			Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法新增模板字段。');
			return;
		}
		Ext.tzShowPromptSearch({
			recname : 'TZ_FORM_FIELD_V',
			searchDesc : '新增模板字段',
			maxRow : 20,
			TZ_EFFEXP_ZT : "",
			condition : {
				presetFields : {
					TZ_APP_TPL_ID : {
						value : tplID,
						type : '01'
					}
				},
				srhConFields : {
					TZ_XXX_BH : {
						desc : '信息项编号',
						operator : '07',
						type : '01'
					},
					TZ_XXX_MC : {
						desc : '信息项名称',
						operator : '07',
						type : '01'
					},
					TZ_COM_LMC : {
						desc : '控件类名称',
						operator : '07',
						type : '01'
					}
				}
			},
			srhresult : {
				TZ_XXX_BH : '信息项编号',
				TZ_XXX_MC : '信息项名称',
				TZ_COM_LMC : '控件类名称'
			},
			multiselect : true,
			callback : function(selection) {
				var store = btn.findParentByType('grid').store;
				for (var i = 0; i < selection.length; i++) {
					var fieldID = selection[i].data.TZ_XXX_BH;
					var fieldName = selection[i].data.TZ_XXX_MC;

					if (store.find('fieldID', fieldID, 0, false, false, true) == -1) {
						var model = new KitchenSink.view.template.bmb.myBmbPdfModel(
								{
									tplID : tplID,
									fieldID : fieldID,
									fieldName : fieldName,
									pdffield1 : '',
									pdffield2 : '',
									pdffield3 : ''
								});
						store.add(model);
					}
				};
				store.commitChanges();
			}
		})
	},
	/* 删除多行字段 */
	removeSelectField : function(obj, rowIndex) {
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_PDF_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有删除数据的权限');
			return;
		}
		// 选中行
		var store = obj.findParentByType('grid').getStore();
		if (rowIndex.toString().match(/^\d+$/)) {
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
						if (btnId == 'yes') {
							store.removeAt(rowIndex);
						}
					}, this);
		} else {
			var selList = obj.findParentByType('grid').getSelectionModel()
					.getSelection();
			// 选中行长度
			var checkLen = selList.length;
			if (checkLen == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录");
				return;
			} else {
				Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
							if (btnId == 'yes') {
								store.remove(selList);
							}
						}, this);
			}

		}
	},
	/* 删除指定行字段 */
	removeField : function(view, rowIndex) {
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
					if (btnId == 'yes') {
						var store = view.findParentByType("grid").store;
						store.removeAt(rowIndex);
					}
				}, this);
	},
	/* PDF模板设置保存 */
	onTplInfoSave : function(btn) {
		var form = this.getView().child("form").getForm();

		var fileName = form.findField("fileName").getValue();

		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有上传pdf文档，无法保存。');
			return;
		};

		if (form.isValid()) {
			var tzParams = this.getTplInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams, function(responseData) {
						comView.actType = "update";
						form.findField("jgName").setReadOnly(true);
						form.findField("tplName").setReadOnly(true);
						form.findField("jgName").addCls("lanage_1");
						form.findField("tplName").addCls("lanage_1");
					}, "", true, this);
		}
	},
	/* PDF模板设置保存 并且关闭窗口 */
	onTplInfoEnsure : function(btn) {
		var form = this.getView().child("form").getForm();

		var fileName = form.findField("fileName").getValue();

		if (fileName == "" || fileName.length < 1) {
			Ext.MessageBox.alert('提示', '您没有上传pdf文档，无法保存。');
			return;
		};
		if (form.isValid()) {
			var tzParams = this.getTplInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams, function(responseData) {
						// 关闭窗口
						comView.close();
					}, "", true, this);
		}
	},
	/* PDF模板设置保存 构造提交后台的JSON */
	getTplInfoParams : function() {
		var form = this.getView().child("form").getForm();
		// 表单数据
		var formParams = form.getValues();

		// 组件信息标志
		var actType = this.getView().actType;
		// 更新操作参数
		var comParams = "";

		var exportTplID = form.findField("tplID").getValue();

		// 修改json字符串
		var editJson = '{"typeFlag":"TPL","data":'
				+ Ext.JSON.encode(formParams) + '}';

		// 字段列表
		var grid = this.getView().child("grid");
		var store = grid.getStore();

		// var updateRecsFiled = "";

		var count = store.getCount();
		for (var i = 0; i < count; i++) {
			if (editJson == "") {
				editJson = '{"typeFlag":"FIELD","data":'
						+ Ext.JSON.encode(store.getAt(i).data) + '}';
			} else {
				editJson = editJson + ',' + '{"typeFlag":"FIELD","data":'
						+ Ext.JSON.encode(store.getAt(i).data) + '}';
			}
		}

		if (editJson != "") {
			if (comParams == "") {
				comParams = '"update":[' + editJson + "]";
			} else {
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		// 提交参数
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PDF_STD","OperateType":"U","comParams":{'
				+ comParams + '}}';
		store.commitChanges();
		return tzParams;
	},
	/* 保存（打印设置） */
	onPrintSave : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		var form = win.child("form").getForm();
		var formParams = form.getValues();

		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PRINT_STD","OperateType":"U","comParams":{"update":['
				+ Ext.JSON.encode(formParams) + ']}}';
		Ext.tzSubmit(tzParams, function(response) {
				}, "", true, this);
	},
	/* 确定（打印设置） */
	onPrintEnsure : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		var form = win.child("form").getForm();
		var formParams = form.getValues();

		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_PRINT_STD","OperateType":"U","comParams":{"update":['
				+ Ext.JSON.encode(formParams) + ']}}';
		Ext.tzSubmit(tzParams, function(response) {
					win.close();
				}, "", true, this);
	},
	/* 关闭（打印设置） */
	onPrintClose : function(btn) {
		// 获取窗口
		var win = btn.findParentByType("window");
		win.close();
	}

});