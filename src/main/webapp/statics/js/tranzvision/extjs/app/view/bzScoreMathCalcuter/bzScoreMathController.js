Ext.define('KitchenSink.view.bzScoreMathCalcuter.bzScoreMathController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.zsbfjgMgController',

	importScore: function(btn) {
		Ext.tzImport({ /*importType 导入类型：A-传Excel；B-粘贴Excel数据*/
			importType: 'A',
			/*导入模版编号*/
		    tplResId:'TZ_PLANBUG_DR_IMP',
			/*businessHandler  预览导入的数据之后点击下一步执行的函数，根据业务的需求自由编写，columnArray为解析Excel后的标题行数组（如果未勾选首行是标题行columnArray=[]）
			 * dataArray为解析后的Excel二维数组数据（勾选了首行是标题行则dataArray不包含首行数据；）
			 */
			businessHandler: function(columnArray, dataArray) {
				var win = Ext.getCmp('bzScoreMathCalcuter_bzscoreBapage');

				var windowgrid = win.child('grid');

				if (dataArray.length > 0) {
					for (var i = 0; i < dataArray.length; i++) { /*dataArray[i][12]="-1";*/
					}
				}
				windowgrid.store.loadData(dataArray);

				this.close();
			}
		});
	},
	// 原分数页面关闭按钮
	onSchoolClose: function() {
		this.getView().close();

	},


	//原分数页计算标准成绩按钮
	calculationScore: function(btn) {
		var me=this;
		var date_old = [];
		var attaGrid, attaAllRecs, attaDelRecs, attaNewAtta, attaList = "",
			attaDelLit = "",
			attaNewList = "",
			comParams = "";
		attaGrid = btn.findParentByType("grid[reference=attachmentGrid]");
		var pw_value = attaGrid.findParentByType('panel').down('textfield').getValue();
		attaAllRecs = attaGrid.store.getRange();
		for (var i = 0; i < attaAllRecs.length; i++) {
			date_old.push(Ext.JSON.encode(attaAllRecs[i].data));
			if (attaList == "") {
				attaList = Ext.JSON.encode(attaAllRecs[i].data);
			} else {
				attaList = attaList + ',' + Ext.JSON.encode(attaAllRecs[i].data);
			}
		}
		if (isNaN(Number(pw_value)) || pw_value == "") { //当输入不是数字的时候，Number后返回的值是NaN;然后用isNaN判断。
			Ext.MessageBox.alert('提示', '请设置每组最多评委数为不大于10的数字');
			return;
		} else {
			if (pw_value > 10) {
				Ext.MessageBox.alert('提示', '请设置每组最多评委数为不大于10的数字');
				return;
			} else {

               jsonData = '{"ComID":"TZ_BZCJ_SRC_COM","PageID":"TZ_BZCJ_JIS_STD","OperateType":"MAXCOULUM","comParams":{"add":[' + attaList + ']}}';

				Ext.tzLoad(jsonData, function(respDate) {
					var maxnum = respDate.maxnum;
					if (pw_value < maxnum) {
						Ext.MessageBox.alert('提示', '设置每组最多评委数过小,最小为' + maxnum);
						return;
					} else {
						
						//是否有访问权限   
						var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BZCJ_SRC_COM"]["TZ_BZCJ_JIS_STD"];
						if (pageResSet == "" || pageResSet == undefined) {
							Ext.MessageBox.alert('提示', '您没有修改数据的权限');
							return;
						}
						//该功能对应的JS类
						var className = pageResSet["jsClassName"];
						if (className == "" || className == undefined) {
							Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BZCJ_JIS_STD，请检查配置。');
							return;
						}
						var win = me.lookupReference('bzscoreDelinfo');

						if (!win) {
							//className = 'KitchenSink.view.security.com.pageRegWindow';
							Ext.syncRequire(className);
							ViewClass = Ext.ClassManager.get(className);
							//新建类
							var config = {
								coulumdt: pw_value

							}
							win = new ViewClass(config);
							//操作类型设置为新增
							win.actType = "add";
							console.log(win.actType);
							win.on('afterrender', function(window) {
								var attgrid = window.child('grid');
								attgrid.getStore().tzStoreParams = '{"add":[' + attaList + ']}';

								attgrid.getStore().load();

							})
							me.getView().add(win);
							win.show();
						}



					}
				});


			}

		}

	},
	//计算标准分数页 关闭
	DelinfoClose: function(btn) {
		var win = btn.findParentByType("window");
		win.close();

	},
	//计算标准分数标准按钮
	CalculationBzScore: function(btn) {
		var win = btn.findParentByType("window");
		var fieldlist = win.query('textfield');
		var maxscore = fieldlist[0].getValue();
		var minscore = fieldlist[1].getValue();
		var ismathflag = fieldlist[2].getValue();
		if (minscore == "" || minscore.trim() == "" || maxscore == "" || maxscore.trim() == "") {

			Ext.MessageBox.alert('提示', '请输入最高分和最低分!');
			return;
		} else {


			var hbfsgrid = win.child('grid');
			var coulumdt = win.coulumdt;
			var attaAllRecs, attaDelRecs, attaNewAtta, bzfsList = "",
				attaDelLit = "",
				attaNewList = "",
				comParams = "";
			hbfsAllRecs = hbfsgrid.store.getRange();
			for (var i = 0; i < hbfsAllRecs.length; i++) {
				if (bzfsList == "") {
					bzfsList = Ext.JSON.encode(hbfsAllRecs[i].data);
				} else {
					bzfsList = bzfsList + ',' + Ext.JSON.encode(hbfsAllRecs[i].data);
				}
			}
			console.log(bzfsList);
			hbfsgrid.getStore().pageID = 'TZ_BZCJ_JIS_STD', hbfsgrid.getStore().tzStoreParams = '{"add":[' + bzfsList + '],"maxscore":' + maxscore + ',"minscore":' + minscore + ',"coulumdt":' + coulumdt + '}';
			console.log(hbfsgrid.getStore().tzStoreParams);
			hbfsgrid.getStore().load()
			fieldlist[2].setValue("Y");

		}



	},
	//检查人员识别编号是否合法
	checkPerson: function(btn) {

		var win = btn.findParentByType("window");


		var hbfsgrid = win.child('grid');
		var coulumdt = win.coulumdt;
		var attaAllRecs, attaDelRecs, attaNewAtta, bzfsList = "",
			attaDelLit = "",
			attaNewList = "",
			comParams = "";
		hbfsAllRecs = hbfsgrid.store.getRange();
		for (var i = 0; i < hbfsAllRecs.length; i++) {
			if (bzfsList == "") {
				bzfsList = Ext.JSON.encode(hbfsAllRecs[i].data);
			} else {
				bzfsList = bzfsList + ',' + Ext.JSON.encode(hbfsAllRecs[i].data);
				
			}
		}
		//参数
		var tzParams = '{"ComID":"TZ_BZCJ_SRC_COM","PageID":"TZ_BZCJ_JIS_STD","OperateType":"U","comParams":{"add":[' + bzfsList + ']}}';
		//加载数据
		Ext.tzLoad(tzParams, function(responseData) {
			//资源集合信息数据
			var formData = responseData.personid;
			if (formData.length == 0) {
				Ext.MessageBox.alert('提示', '考生全合法!');

			} else {
				Ext.MessageBox.alert('提示', '考生' + formData + '不合法!');
			}

		});



	},
	//导出学生面试信息
	exportStudentInform: function(btn) {
		var jsonData = "";
		var win = btn.findParentByType("window");
		var fieldlist = win.query('textfield');

		var ismathflag = fieldlist[2].getValue();
		if (ismathflag != "Y") {
			Ext.MessageBox.alert('提示', '导出之前先计算标准分!');
			return;
		} else {
			var hbfsgrid = win.child('grid');
			var coulumdt = win.coulumdt;
			var attaAllRecs, attaDelRecs, attaNewAtta, bzfsList = "",
				attaDelLit = "",
				attaNewList = "",
				comParams = "";
			hbfsAllRecs = hbfsgrid.store.getRange();
			for (var i = 0; i < hbfsAllRecs.length; i++) {
				if (bzfsList == "") {
					bzfsList = Ext.JSON.encode(hbfsAllRecs[i].data);
				} else {
					bzfsList = bzfsList + ',' + Ext.JSON.encode(hbfsAllRecs[i].data);
				}
			}

			jsonData = '{"ComID":"TZ_BZCJ_SRC_COM","PageID":"TZ_BZCJ_JIS_STD","OperateType":"EXPORT","comParams":{"add":[' + bzfsList + '],"coulumdt":' + coulumdt + '}}'



			Ext.tzSubmit(jsonData, function(respDate) {
				var fileUrl = respDate.fileUrl;
				window.location.href = fileUrl;
			}, "导出报名人信息成功", true, this);
		}
	},
	importlastexcel: function(btn) {
		Ext.tzImport({ /*importType 导入类型：A-传Excel；B-粘贴Excel数据*/
			importType: 'A',
			/*导入模版编号*/
			tplResId:'TZ_PLANBUG_DR_IMP',
			/*businessHandler  预览导入的数据之后点击下一步执行的函数，根据业务的需求自由编写，columnArray为解析Excel后的标题行数组（如果未勾选首行是标题行columnArray=[]）
			 * dataArray为解析后的Excel二维数组数据（勾选了首行是标题行则dataArray不包含首行数据；）
			 */
			businessHandler: function(columnArray, dataArray) {
				var win = Ext.getCmp('bzscoreDelinfo_Delinfo');

				var windowgrid = win.child('grid');

				if (dataArray.length > 0) {
					for (var i = 0; i < dataArray.length; i++) { /*dataArray[i][12]="-1";*/
					}
				}

				windowgrid.store.loadData(dataArray, true);

				this.close();
			}
		});
	},
	// 原分数页面关闭按钮
	onSchoolClose: function() {
		this.getView().close();


	}


})