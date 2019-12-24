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
		//console.log(selksList);
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
		//console.log(selksList);
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
		//console.log(attaList);
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
		//console.log("classId+batchId:"+classId+batchId);
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
			//console.log(win.actType);
	win.on('afterrender', function(window) {
								var attgrid = window.child('grid');
								//attgrid.getStore().tzStoreParams ='{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}' ;
								attgrid.getStore().tzStoreParams ='{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW","condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+Ext.tzOrgID+'"}}' ;
								
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
			//console.log(win.actType);
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
		////console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},{"appInsId":'+appInsId+'},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SETPW_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		//console.log(tzParams);
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
		var jgid = form.findField('jgid').getValue();
		var batchId = form.findField('batchId').getValue();
		//var batchname="第一批次";
        //console.log("classId"+classId);
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW',
			condition:{
				"TZ_JG_ID":Ext.tzOrgID,
				"TZ_IS_APP_OPEN":"Y"
			},
			callback: function(seachCfg) {
				var seachCfgjson=Ext.JSON.decode(seachCfg)
	
				//if (seachCfgjson.condition["TZ_APPLY_PC_ID-value"]==""){
					
				//   seachCfgjson.condition["TZ_APPLY_PC_ID-value"]=batchId;
				//}
				seachCfgjson=Ext.encode(seachCfgjson)
				//console.log(seachCfgjson);
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfgjson;
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
		////console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDKS_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		//console.log(tzParams);
		Ext.tzSubmit(tzParams, function(responseData) {
			//刷新查看考生的列表 
			panel.down('grid').getStore().reload();
         if(responseData.strReturn!=""){
        	 Ext.Msg.alert("提示", responseData.strReturn);
         }
         
        
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
	    var panel=btn.findParentByType('viewmspsxsList');

		
		
		if((typeof panel.getedSQL) == "undefined"){
			searchSql = "SELECT TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID,OPRID,TZ_REALNAME,TZ_CLPS_GR_NAME  FROM PS_TZ_MSPS_KS_VW WHERE TZ_CLASS_ID='"+ classId +"' AND TZ_APPLY_PC_ID='"+ batchId +"'";
		}else{
			searchSql = panel.getedSQL;
		}
		
		searchSql=Ext.JSON.encode(searchSql);
		
		
		//console.log(selksList);		
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
            var tzParams = '{"ComID":"TZ_BZCJ_SRC_COM","PageID":"TZ_BZCJ_YSF_STD","OperateType":"MATHSCORE","comParams":{"add":[{"searchSql":'+searchSql+'}]}}';
           // var examineeGrid = panel.down('grid');
                    //console.log(tzParams);
            Ext.tzLoad(tzParams,function(respData){
            	//console.log(respData);
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
		var panel = btn.findParentByType("viewmspsxsList");
		var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
        //console.log("classId："+classId+"batchId:"+batchId);
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
				var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_KS_STD","OperateType":"getSearchSql","comParams":'+seachCfg+'}';
				Ext.tzLoad(tzParams,function(responseData){
					var getedSQL = responseData.searchSql;
					panel.getedSQL = getedSQL;
				});
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
                var newdateArry=[];
                if(dataArray.length>0){
                    for(var i=0;i<dataArray.length;i++){
                    	var newdate=[];
                       newdate.push(dataArray[i][0]);
                       newdate.push(dataArray[i][1]); 
                       newdateArry.push(newdate);
                    }
                }
                
                
                windowgrid.store.loadData(newdateArry);

                var winform = win.child("form").getForm();
                var strtmp = "共 ";
                strtmp = strtmp+"<span style='color:red;font-size: 22px'>"+dataArray.length+"</span>";
                strtmp = strtmp+" 条数据";
                var formpara={classId:classId,batchId:batchId,ImpRecsCount:strtmp};
                winform.setValues(formpara);
                win.show();
				
				//console.log(dataArray);
			
			}
		});
	},
	addksDrSave: function(btn) {
		//得到上一级页面
		var selksList = "";
		var mainContent=Ext.getCmp('tranzvision-framework-content-panel');
		var panel = mainContent.child("panel[reference=viewmspsxsList_mspsview]");
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
		////console.log(selksList);
		comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDKS_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
		//console.log(tzParams);
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
		
	},
	   /**
     * 导出选中的考生评议数据
     */
    exportSelectedExcel: function(btn){
    	var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
    	
    	//var panel = btn.findParentByType('materialsReviewExaminee');
		var grid = btn.findParentByType("grid");
		//var classId = panel.classId;
		//var batchId = panel.batchID;
		
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要导出的考生记录");
			return;
		}
		
		var appInsIds = [];
	    for(var i=0;i<checkLen;i++){
	    	appInsIds.push(selList[i].data.appInsId);
		}
	    var comParamsObj = {
	    	ComID: "TZ_REVIEW_MS_COM",
			PageID: "TZ_MSPS_KS_STD",
			OperateType: "EXPORT",
			comParams: {
				classId: classId,
				batchId: batchId,
				appInsIds: appInsIds
			}
	    };
	   
	    var className = 'KitchenSink.view.viewPsStudentListInfo.export.msexportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	exportObj: comParamsObj
        });
       
        win.show();
    },
    
    /**
     * 导出搜索结果中考生的评议数据
     */
    exportSearchExcel: function(btn){
    	var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
    	var panel=btn.findParentByType('viewmspsxsList');
    	//var panel = btn.findParentByType('materialsReviewExaminee');
		var grid = btn.findParentByType("grid");
		//var classId = panel.classId;
	//	var batchId = panel.batchID;
		

		
		//构造搜索sql
		if((typeof panel.getedSQL) == "undefined"){
			searchSql = "SELECT TZ_APP_INS_ID FROM PS_TZ_MSPS_KS_VW WHERE TZ_CLASS_ID='"+ classId +"' AND TZ_APPLY_PC_ID='"+ batchId +"'";
		}else{
			searchSql = panel.getedSQL;
		}
		
		var comParamsObj = {
			ComID: "TZ_REVIEW_MS_COM",
			PageID: "TZ_MSPS_KS_STD",
			OperateType: "EXPORT",
			comParams: {
				classId: classId,
				batchId: batchId,
				searchSql: searchSql
			}
		};
		
		
		var className = 'KitchenSink.view.viewPsStudentListInfo.export.msexportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	exportObj: comParamsObj
        });
        
        win.show();
    },
    
    /**
     * 下载考生评议数据导出结果
     */
    downloadHisExcel: function(btn){
    	var form=btn.findParentByType('viewmspsxsList').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
    	
    	//var panel = btn.findParentByType('materialsReviewExaminee');
		//var grid = btn.findParentByType("grid");
		//var classId = panel.classId;
    	//var panel = btn.findParentByType('materialsReviewExaminee');
	//	var classId = panel.classId;
		//var batchId = panel.batchID;
		
		var className = 'KitchenSink.view.viewPsStudentListInfo.export.msexportExcelWindow';
    	
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId,
        	type: 'download'
        });
        
        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);
        
        win.show();
    },
    /**
     * 评委组分组保存-迁移
     * author:丁鹏
     * time:2019年11月22日15:45:42
     * */
    //评委组分组保存
    onMsGroupSave: function (btn) {
        var panelGrid = this.lookupReference('pageGrid');
        var panel = btn.findParentByType("stuInfoPanel");
        grid = panel.down('grid');
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        let chg = panelGrid.getStore().getModifiedRecords();
        //var grid= Ext.getCmp('pageGrid');
        if (!!chg.length) {
            /*保存资源信息*/
            var ret = this.savePlstMsComInfo(win, panelGrid, grid);
        }
    },
    /**
     * 评委组分组确定-迁移
     * author：丁鹏
     * time：2019年11月22日15:46:23
     * */
    //评委组分组确定
    onMsGroupEnsure: function (btn) {
        var panelGrid = this.lookupReference('pageGrid');
        var panel = btn.findParentByType("stuInfoPanel");
        grid = panel.down('grid');
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        let chg = panelGrid.getStore().getModifiedRecords();
        var form = win.child("form").getForm();
        if (!!chg.length) {
            /*保存资源信息*/
            this.savePlstMsComInfo(win, panelGrid, grid);
            //关闭窗口
            win.close();
        } else {
            win.close();
        }
    },
    /**
     * 组内排序保存-实际操作-迁移
     * author：丁鹏
     * time：2019年11月22日15:48:35
     * */
    savePlstMsComInfo: function (win, panelGrid, grid) {
        let me = this;
        //资源信息表单
        var form = win.child("form").getForm();
        var classId = form.findField("classID").getValue();
        var batchId = form.findField("batchID").getValue();
        var jugGroupId = Ext.ComponentQuery.query("combo[name='jugGroupId']")[0].getValue();
        var msGroupId = Ext.ComponentQuery.query("combo[name='msGroupId']")[0].getValue();
        //表单数据
        var formParams = form.getValues();

        //更新操作参数
        // var comParams = "";
        // var processComParams = "";
        //授权组件页面列表

        //var mspsksGrid = Ext.getCmp('mspsksGrid');
        //授权组件页面数据
        var store = panelGrid.getStore();
        var editRecs = store.getModifiedRecords();
        var tzParamspw = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_ZNPX_STD","OperateType":"saveOrder","comParams":{' +
            '"classId":' + classId + ',' +
            '"batchId":' + batchId + ',' +
            '"jugGroupId":' + jugGroupId + ',' +
            '"msGroupId":' + msGroupId + ',' +
            '"data":' + Ext.JSON.encode(editRecs.map(item => {
                return item.data
            })) + '}}';

        if (tzParamspw != "") {
            Ext.tzLoad(tzParamspw, function (result) {
                grid.getStore().reload();
            });
        } else {
            return false;
        }
    },
    changeGroupToMs(btn, newValue, oldValue, eOpts) {
        var panelGrid = this.lookupReference('pageGrid');
        var panel = btn.findParentByType("stuInfoPanel");
        var form = panel.down('form').getForm();
        var classID = form.findField('classId').getValue();
        var batchID = form.findField('batchId').getValue();
        var jugGroupId = newValue;
        var msGroupId = Ext.ComponentQuery.query("combo[name='msGroupId']")[0].getValue();
        if (!!jugGroupId && !!msGroupId) {
            var comParamsObj = {
                ComID: 'TZ_MSXCFZ_COM',
                PageID: 'TZ_MSGL_ZNPX_STD',
                OperateType: 'GETSTUDATA',
                comParams: {
                    classId: classID,
                    batchId: batchID,
                    jugGroupId: jugGroupId,
                    msGroupId: msGroupId
                }
            }
            this.queryStudent(comParamsObj, panelGrid);
        } else {
            var comParamsObj = {
                ComID: 'TZ_MSXCFZ_COM',
                PageID: 'TZ_MSGL_ZNPX_STD',
                OperateType: 'GETMSDATA',
                comParams: {
                    classId: classID,
                    batchId: batchID,
                    jugGroupId: jugGroupId
                }
            }
            var tzParams = Ext.JSON.encode(comParamsObj);
            Ext.tzLoad(tzParams, function (respData) {
                let MSDATA = respData.MSDATA;
                if (!!MSDATA) {
                    let mszData = Ext.ComponentQuery.query("combo[name='msGroupId']")[0];
                    mszData.setStore(Ext.create('Ext.data.Store', {
                        fields: ['TZ_GROUP_ID', 'TZ_GROUP_DESC'],
                        data: MSDATA
                    }));
                }
            });
        }
    },
    changeMsToGrid(btn, newValue, oldValue, eOpts) {
        var panelGrid = this.lookupReference('pageGrid');
        var panel = btn.findParentByType("stuInfoPanel");
        var form = panel.down('form').getForm();
        var classID = form.findField('classId').getValue();
        var batchID = form.findField('batchId').getValue();
        var jugGroupId = Ext.ComponentQuery.query("combo[name='jugGroupId']")[0].getValue();
        var msGroupId = newValue;
        var comParamsObj = {
            ComID: 'TZ_MSXCFZ_COM',
            PageID: 'TZ_MSGL_ZNPX_STD',
            OperateType: 'GETSTUDATA',
            comParams: {
                classId: classID,
                batchId: batchID,
                jugGroupId: jugGroupId,
                msGroupId: msGroupId
            }
        }
        if (!!jugGroupId && !!msGroupId) {
            this.queryStudent(comParamsObj, panelGrid);
        }
    },
    queryStudent(com, panelGrid, grid) {
        panelGrid.getStore().removeAll();
        var tzParams = Ext.JSON.encode(com);
        Ext.tzLoad(tzParams, function (respData) {
            let SDATA = respData.SDATA;
            if (!!SDATA) {
                panelGrid.setStore(Ext.create('Ext.data.Store', {
                    fields: [{name: 'ins'}, {name: 'sXh'}, {name: 'sName'}, {name: 'msh'}],
                    data: SDATA
                }));
                panelGrid.getStore().reload();
            }
        });
    }
	
});