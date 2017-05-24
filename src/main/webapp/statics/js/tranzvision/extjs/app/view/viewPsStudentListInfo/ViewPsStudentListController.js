Ext.define('KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.viewxscontrol',
	importScore: function(btn) {
		Ext.tzImport({ /*importType 导入类型：A-传Excel；B-粘贴Excel数据*/
			importType: 'A',
			/*导入模版编号*/
			tplResId: 'TZ_PLANBUG_DR_IMP',
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
	exportSelStuInfom:function(btn){
		var selksList = "";
		var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
	
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
		
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			for (var i = 0; i < selList.length; i++) {
				
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}

			}
		}
		console.log(selksList);
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"EXPORT","comParams":{"export":[' + selksList + '],"classId":' + classId + ',"batchId":' + batchId + '}}';
		Ext.tzSubmit(tzParams, function(respDate) {
			var fileUrl = respDate.fileUrl;
				window.location.href = fileUrl;

		}, "导出成功!", true, this)

		
		
		
	},


	//设置录取状态A
	setluztequleA: function(btn) {
		var ztvalue = "A";
		this.setluztequle(btn, ztvalue);
	},
	//批量设置录取状态
	setluztequle: function(btn, ztvalue) {

/*var xsappList = "";
		xsappList = this.selectColum(btn);
		if (xsappList != null && xsappList != undefined) {
			//参数
			var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"SETLUZQ","comParams":{"set":[' + xsappList + '],"lqztvalue":"A"}}';

		}*/
		var selksList = "";
		var m = 0;
		var form = btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var store = btn.findParentByType('grid').getStore();
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();

		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			for (var i = 0; i < selList.length; i++) {
				selList[i].set('passState', ztvalue);
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}

			}
			store.commitChanges();

		}
		console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"U","comParams":{"update":[' + comparams + ']}}';
		Ext.tzSubmit(tzParams, function() {

		}, "设置成功!", true, this)


	},
	//设置录取状态B
	setluztequleB: function(btn) {

		var ztvalue = "B";
		this.setluztequle(btn, ztvalue);

	},
	//设置录取状态C
	setluztequleC: function(btn) {
		var ztvalue = "C";
		this.setluztequle(btn, ztvalue);
	},

	//得到选中行的的数据 并生产json串
	//返回 attaList json
	selectColum: function(btn) {
		//选中行
		var attaList = "";
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			// var appinsId = selList[0].get("appInsId");       
			for (var i = 0; i < selList.length; i++) {
				if (attaList == "") {
					attaList = Ext.JSON.encode(selList[i].data);
				} else {
					attaList = attaList + ',' + Ext.JSON.encode(selList[i].data);
				}
			}
		}
		console.log(attaList);
		return attaList;

	},
	//导出选中的学生的信息
	exportSelectedInfo: function(btn) {
		var attaList = this.selectColum(btn);
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"EXPORT","comParams":{"add":[' + xsappList + '],}';



	},
	onAddMsPsXs: function(btn) {
		var me = this;
		
		var panel = btn.findParentByType("viewmspsxsList");
		var form = panel.down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		console.log("classId+batchId:"+classId+batchId);
		//是否有访问权限   
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_ADDKS_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_ADDKS_STD，请检查配置。');
			return;
		}
		var win = me.lookupReference('addpsStuinfo');

		if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
/*var config = {
								coulumdt: pw_value

							}*/
			win = new ViewClass();
			//操作类型设置为新增
			win.actType = "add";
			console.log(win.actType);
	win.on('afterrender', function(window) {
								var attgrid = window.child('grid');
								attgrid.getStore().tzStoreParams ='{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}' ;
								attgrid.getStore().load();
							})
			me.getView().add(win);
			win.show();
		}

	},
	//考生指定评委实际操作
	setStupwDateopenwin:function(appInsId,form){
		 var me = this;
		//是否有访问权限   
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_SETPW_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_SETPW_STD，请检查配置。');
			return;
		}
		var win = me.lookupReference('setStuPw');

		if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
/*var config = {
								coulumdt: pw_value

							}*/
			win = new ViewClass();
			//操作类型设置为新增
			win.actType = "add";
			console.log(win.actType);
			win.on('afterrender', function(window) {
				var attgrid = window.child('grid');
			var form=window.down('form[reference=cbaform]').getForm();
			form.setValues({classId:classId,batchId:batchId,appInsId:appInsId});

				attgrid.getStore().tzStoreParams = '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.TZ_MSPS_STPW_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
				attgrid.getStore().load();

			})
			me.getView().add(win);
			win.show();
		}
		
		
	},
	//行中为考生制定评委
	setStuOnepw: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //资源集合ID
        var appInsId = selRec.get("appInsId");
        //显示资源集合信息编辑页面
       
		var form = view.findParentByType("viewmspsxsList").down('form').getForm();
        this.setStupwDateopenwin(appInsId,form);
    },
	//为考生指定评委
	setStuPw: function(btn) {
		 //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请指定一个要指定评委的学生");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能为一个考生指定评委");
            return;
        }
        //资源集合ID
        //var zhjgID = selList[0].get("zhjgID");
        var viewGrid=btn.findParentByType("viewmspsxsList").down('grid[reference=mspsksGrid]').getSelectionModel().getSelection();
		var appInsId=viewGrid[0].get('appInsId');
		var form = btn.findParentByType("viewmspsxsList").down('form').getForm();
		
	    this.setStupwDateopenwin(appInsId,form);
        


	},
	
	//指定评委页面保存
	setpwksSave:function(btn){
		var selksList="";
		var form = btn.findParentByType("setStuPw").down('form').getForm();
		var panel = btn.findParentByType("viewmspsxsList");
		var appInsId = form.findField('appInsId').getValue();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var selList=btn.findParentByType('setStuPw').down('grid[reference=setpwksGrid]').getSelectionModel().getSelection();
			//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请指定一个评委");
			return;
		} else {
			// var appinsId = selList[0].get("appInsId");       
			for (var i = 0; i < selList.length; i++) {
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}
			}
		}
		//console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},{"appInsId":'+appInsId+'},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SETPW_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		console.log(tzParams);
		Ext.tzSubmit(tzParams, function(responseData) {
			//刷新查看考生的列表 
			panel.down('grid').getStore().reload();


		}, "保存成功！", true, this);
	},
	//指定评委页关闭
	setpwksClose:function(){
		this.getView().close();
	
	},

	//多行删除评审考生
	deleteResSets: function(btn) {
		//选中行
		var restore = btn.findParentByType('grid').store;
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录");
			return;
		} else {
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
				if (btnId == 'yes') {

					restore.remove(selList);
				}
			}, this);
		}
	},
	//单行删除评审考生
	deleteCurrResSet: function(view, rowIndex) {
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
			if (btnId == 'yes') {
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		}, this);
	},
	//查看考生列表页保存删除
	onSaveRemoveKs: function(btn) {
	

		var form = btn.findParentByType("viewmspsxsList").down('form').getForm();
		var grid = btn.findParentByType("viewmspsxsList").down('grid');
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var store = grid.getStore();
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
		var comParams = "";
		if (removeJson != "") {
			comParams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + removeJson + '';
		}
		//提交参数.
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"U","comParams":{"delete":[' + comParams + ']}}';
		//保存数据
		Ext.tzSubmit(tzParams, function() {

			store.reload();
		}, "保存成功！", true, this);
	},
	//关闭窗口
	closeviewList: function(btn) {

		//关闭窗口
		var comView = btn.findParentByType("viewmspsxsList");
		comView.close();
	},
	//新加学生页面的可配置搜索
	//可配置搜索
	searchksList: function(btn) {
		//var panel = btn.findParentByType("viewmspsxsList");
		var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
        console.log("classId"+classId);
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW',
			condition:{
				"TZ_CLASS_ID":classId,
				"TZ_BATCH_NAME":"第一批次"
			},
			callback: function(seachCfg) {
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},
	//添加 考生 页面 关闭
	addksClose: function(btn) {
		var win = this.getView();
		win.close();
	},
	//添加 考生页面 保存 
	addksSave: function(btn) {
		//得到上一级页面
		var selksList = "";
		var panel = btn.findParentByType("viewmspsxsList");
		var win=btn.findParentByType("addPsStu");
		var grid = btn.findParentByType("addPsStu").down('grid');
		var form = panel.down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			// var appinsId = selList[0].get("appInsId");       
			for (var i = 0; i < selList.length; i++) {
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}
			}
		}
		//console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDKS_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		console.log(tzParams);
		Ext.tzSubmit(tzParams, function(responseData) {
			//刷新查看考生的列表 
			panel.down('grid').getStore().reload();


		}, "保存成功！", true, this);
		//刷新查看考生的列表 
		panel.down('grid').getStore().reload();
         win.close();
	},
	
	
	matchStudenSocre:function(btn){	
		var selksList = "";
		var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();	
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();		
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			for (var i = 0; i < selList.length; i++) {
				
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}
			}
		}
		console.log(selksList);		
	  Ext.tzSetCompResourses("TZ_BZCJ_SRC_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BZCJ_SRC_COM"]["TZ_BZCJ_YSF_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BZCJ_YSF_STD，请检查配置。');
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
        cmp.on('afterrender',function(panel){
        	var grid = panel.child('grid[name=appFormApplicants]');     	
        	
        	
           // var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_BZCJ_SRC_COM","PageID":"TZ_BZCJ_YSF_STD","OperateType":"MATHSCORE","comParams":{"add":[' + selksList + ']}}';
           // var examineeGrid = panel.down('grid');
                    
            Ext.tzLoad(tzParams,function(respData){
            	console.log(respData);
            	grid.store.loadData(respData);               
            });
        });
        
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
	},
	searchMsksList: function(btn) {
		//var panel = btn.findParentByType("viewmspsxsList");
		var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
        console.log("classId："+classId+"batchId:"+batchId);
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.TZ_MSPS_KS_VW',
			condition:{
				"TZ_CLASS_ID":classId,
				"TZ_APPLY_PC_ID":batchId
			},
			callback: function(seachCfg) {
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},
	ensureonRemoveKs:function(btn) {
		this.onSaveRemoveKs(btn);
				//关闭窗口
		var comView = btn.findParentByType("viewmspsxsList");
		comView.close();
	
	
	},
    importMsStuInfom: function(btn) {
    	var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var store = btn.findParentByType("grid").store;
		Ext.tzImport({ /*importType 导入类型：A-传Excel；B-粘贴Excel数据*/
			importType: 'A',
			/*导入模版编号*/
			tplResId: 'TZ_MSPS_ADKS_TMP',
			/*businessHandler  预览导入的数据之后点击下一步执行的函数，根据业务的需求自由编写，columnArray为解析Excel后的标题行数组（如果未勾选首行是标题行columnArray=[]）
			 * dataArray为解析后的Excel二维数组数据（勾选了首行是标题行则dataArray不包含首行数据；）
			 */
			businessHandler: function(columnArray, dataArray) {
				//是否有访问权限
                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_DRKS_STD"];
                if( pageResSet == "" || pageResSet == undefined){
                    Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                    return;
                }

                //该功能对应的JS类
                var className = pageResSet["jsClassName"];
                if(className == "" || className == undefined){
                    Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_DRKS_STD，请检查配置。');
                    return;
                }

                var win = this.lookupReference('importPsStu');

                if (!win) {
                    //className = 'KitchenSink.view.activity.applyOptionsWindow';
                    Ext.syncRequire(className);
                    ViewClass = Ext.ClassManager.get(className);
                    //新建类
                    win = new ViewClass();
                    //this.getView().add(win);
                }

                var windowgrid=win.child('grid');

                if(dataArray.length>0){
                    for(var i=0;i<dataArray.length;i++){
                        dataArray[i][5]="-1";
                    }
                }
                windowgrid.store.loadData(dataArray);

                var winform = win.child("form").getForm();
                var strtmp = "共 ";
                strtmp = strtmp+"<span style='color:red;font-size: 22px'>"+dataArray.length+"</span>";
                strtmp = strtmp+" 条数据";
                var formpara={classId:classId,batchId:batchId,ImpRecsCount:strtmp};
                winform.setValues(formpara);
                win.show();
				
				console.log(dataArray);
			
			}
		});
	},
	addksDrSave: function(btn) {
		//得到上一级页面
		var selksList = "";
		
		var panel = Ext.getCmp("viewmspsxsList_mspsview");
		var win=btn.findParentByType("importPsStu");
		var grid = btn.findParentByType("importPsStu").down('grid');
		var form = win.down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择需要导入的学生");
			return;
		} else {
			// var appinsId = selList[0].get("appInsId");       
			for (var i = 0; i < selList.length; i++) {
				if (selksList == "") {
					selksList = Ext.JSON.encode(selList[i].data);
				} else {
					selksList = selksList + ',' + Ext.JSON.encode(selList[i].data);
				}
			}
		}
		//console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDKS_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		console.log(tzParams);
		Ext.tzSubmit(tzParams, function(responseData) {
			//刷新查看考生的列表 
			panel.down('grid').getStore().reload();

           win.close();
		}, "导入成功！", true, this);
		//刷新查看考生的列表 
		panel.down('grid').getStore().reload();
        
	},
	addksDrClose:function(btn){
			var win=btn.findParentByType("importPsStu");
			win.close();
		
	}
	
});