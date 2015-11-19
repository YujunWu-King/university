Ext.define('KitchenSink.view.template.bmb.myBmbController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.myBmb',
	tplid: "",
	/*新增报名表模板*/
	addBmbTpl: function() {
		var me = this;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_ADD_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_ADD_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('myBmbRegWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}else{
			var activeTab = win.items.items[0].getActiveTab();
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
		}

		win.show();

		if (!window.mybmb_cj) {
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
		}
	},
   //选中后编辑
	editBmbTpl: function(btn) {
		//选中行
		var grid =btn.up('grid');
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

		var tplid = selList[0].get("tplid");
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		var tzParams='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"' + tplid + '"}}'
		var url = Ext.tzGetGeneralURL() + tzParams;
		window.open(url, '_blank');
	},
	/*编辑报名表模板*/
	onBmbTplEdit: function(view, rowIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//模板ID
		var tplid = selRec.get("tplid");
		var tzParams='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"' + tplid + '"}}'
		var url = Ext.tzGetGeneralURL() + tzParams;
		window.open(url, '_blank');
	},

	/*复制报名表模板*/
	onBmbTplCopy: function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		this.tplid = selRec.get("tplid");
		Ext.MessageBox.prompt('复制模板', '请输入另存模板的名称:', this.showResultText, this);
	},

	/*预览报名表模板*/
	onBmbTplPreview: function(gridViewObject, rowIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_FORM_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有预览报名表模板的权限');
			return;
		}

		var tplid = gridViewObject.store.getAt(rowIndex).get("tplid");
		var tzParams='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_FORM_STD","OperateType":"HTML","comParams":{"mode":"Y","TZ_APP_TPL_ID":"' + tplid + '"}}'
		var url = Ext.tzGetGeneralURL() + tzParams;
		window.open(url, '_blank');


		/*
		var tplname = gridViewObject.store.getAt(rowIndex).get("tplname");
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		var tab = contentPanel.add({
			title: tplname,
			loader: {
				url: externalURL + '?mode=Y&TZ_QSTN_TPL_ID=' + tplid,
				contentType: 'html',
				loadMask: true
			}
		});
		var items = contentPanel.items.items;
		items[items.length - 1].loader.load();
		contentPanel.setActiveTab(tab);
		*/
	},

	showResultText: function(id, text) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}

		if (id == "ok") {
			if (text) {
				//组件注册信息数据
				var store = this.getView().getStore(),
					lan = "";
				var tzStoreParams = '{"add":[{"id":"' + this.tplid + '","name":"' + text + '","language":"'+lan+'"}]}';
				var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
				Ext.tzSubmit(tzParams,
					function(jsonObject) {
						store.reload();
						var tzPar='?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"' + jsonObject.id + '"}}'
						var url = Ext.tzGetGeneralURL() + tzPar;
						window.open(url, '_blank');
					},"",true,this);
			} else {
				/*模板名称不能为空*/
				Ext.MessageBox.alert('提示', '新的模板名称不能为空！');
				return;
			}
		}
		return;
	},

	/*新增报名表模板页面，确定*/
	onBmbRegEnsure: function(btn) {
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_EDIT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
		var win = this.lookupReference('myBmbRegWindow');
		var activeTab = win.items.items[0].getActiveTab();
		var form = activeTab.getForm();
		if (form.isValid()) {
			//组件注册信息列表
			var grid = btn.findParentByType("myBmb");
			//组件注册信息数据
			var store = grid.getStore();

			var win = this.lookupReference('myBmbRegWindow');
			var activeTab = win.items.items[0].getActiveTab(),
				id = '';
			var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
				tplId = "", lan = "";

			//add By ZhangLang @20150709
			if (activeTab.itemId == "add") {
				var form = activeTab.getForm();
				lan = form.findField('language').getValue();
			}

			if (activeTab.itemId == "predefine") {
				Ext.each(Ext.query(".tplitem"),
					function (i) {
						if (this.style.backgroundColor == "rgb(173, 216, 230)") {
							tplId = this.getAttribute("data-id");
							return false;
						}
					});
			} else {
				tplId = "";
			}

			if (tplName) {
				var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"' + tplName + '","language":"' + lan + '"}]}'
				var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ADD_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
				Ext.tzSubmit(tzParams,
					function (jsonObject) {
						Ext.get(activeTab.id).select('input').elements[0].value = "";
						store.reload();
						var tzPar = '?tzParams={"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_EDIT_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":"' + jsonObject.id + '"}}'
						var url = Ext.tzGetGeneralURL() + tzPar;
						window.open(url, '_blank');
						win.close();
					}, "", true, this);
			}
		}
    },
	/*新增报名表模板页面，确定*/
	onBmbRegClose: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	/*设置管理权限*/
	onBmbTplSet: function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//模板ID
		var tplid = selRec.get("tplid");

		var me = this;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_ROLE_STD"];

		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert("提示", "您没有修改报名表模板配置数据的权限");
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert("提示", "未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_ROLE_STD，请检查配置。");
			return;
		}

		var win = this.lookupReference('myRoleSetWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		var tzStoreParams = "{'tplid':'" + tplid + "'}";
		/*下拉框值Grid.store*/
		var vinStroe = win.child('grid').store;
		vinStroe.tzStoreParams = tzStoreParams;
		vinStroe.load();
		win.show();
	},
	/*保存（设置管理权限）*/
	onRoleSetSave: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		//获取信息项选项参数
		var tzParams = this.getUpdateParams(win);
		console.log(tzParams);
		Ext.tzSubmit(tzParams,
			function(responseData) {
				console.log(responseData)
				var grid = win.down('grid');
				//信息项选项数据
				grid.getStore().load();
			},"",true,this);
	},
	/*确定（设置管理权限）*/
	onRoleSetEnsure: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		/*保存页面注册信息*/
		var tzParams = this.getUpdateParams(win);

		Ext.tzSubmit(tzParams,
			function(responseData) {
				//关闭窗口
				win.close();
			},"",true,this);
	},
	/*获取修改的数据*/
	getUpdateParams: function(win) {
		//更新操作参数
		var comParams = "";

		//信息项选项grid;
		var grid = win.down('grid');
		//信息项选项数据
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
			comParams = '"update":[' + editJson + "]";
		}

		//提交参数
		var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_ROLE_STD","OperateType":"U","comParams":{' + comParams + '}}';

		return tzParams;
	},
	/*关闭（设置管理权限）*/
	onRoleSetClose: function(btn) {
		//获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
	//关闭
	onPanelClose:function(btn){
		//alert("onPanelClose");
		var grid=btn.up('grid');
		grid.close();
	},
    //查询报名表，可配置搜索
    queryBmbTpl:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ONLINE_REG_COM.TZ_ONREG_MNG_STD.TZ_APPTPL_V',
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