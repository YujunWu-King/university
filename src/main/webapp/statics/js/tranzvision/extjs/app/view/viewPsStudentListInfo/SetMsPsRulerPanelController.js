Ext.define('KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.setrulercontroller',


	//面试评审规则查看面试学生
	viewStudentInfo: function(btn) {
		//是否有访问权限   
		var setform = btn.findParentByType('form').getForm();
		var vauleform=setform.getValues();

		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_KS_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_KS_STD，请检查配置。');
			return;
		}

		var contentPanel, cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel';
		if (!Ext.ClassManager.isCreated(className)) {
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;


		cmp = new ViewClass();
		cmp.actType = "add";

		cmp.on('afterrender', function(panel) {
			var form = panel.child('form').getForm();
			var stugrid = panel.child('grid');
			var store = stugrid.getStore();
			store.tzStoreParams = '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.TZ_MSPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + vauleform.classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + vauleform.batchId + '"}}';
			store.load();
			form.setValues(vauleform);
            //form.setValues({classId:classId,batchId:batchId,ksNum:ksNum,reviewClpsKsNum:reviewClpsKsNum,reviewKsNum:reviewKsNum});
			//form.findField('classId').setValue(classId);
			form.findField("classId").setReadOnly(true);
			form.findField("classId").addCls("lanage_1");

			//form.findField('batchId').setValue(batchId);
			form.findField("batchId").setReadOnly(true);
			form.findField("batchId").addCls("lanage_1");

			//form.findField('ksNum').setValue(ksNum);
			form.findField("ksNum").setReadOnly(true);
			form.findField("ksNum").addCls("lanage_1");

			//form.findField('reviewClpsKsNum').setValue(reviewClpsKsNum);
			form.findField("reviewClpsKsNum").setReadOnly(true);
			form.findField("reviewClpsKsNum").addCls("lanage_1");

			//form.findField('reviewKsNum').setValue(reviewKsNum);
			form.findField("reviewKsNum").setReadOnly(true);
			form.findField("reviewKsNum").addCls("lanage_1");
		});


		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}

	},
	//添加评委
	addpwInfom: function(btn) {
		//var form=btn.finParentByType('setmspsruler').down('form').getForm();
		var form=this.getView().child('form').getForm();
		var classId=form.findField('classId').getValue();
		var batchId=form.findField('batchId').getValue();
		
		var me = this;

		var menuItems = [];
		var selectdata=[];

		//是否有访问权限   
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_JUDGES_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_JUDGES_STD，请检查配置。');
			return;
		}
		var win = me.lookupReference('addpwwindow');

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
			me.getView().add(win);
			win.on('afterrender', function(window) {
				var splitbut = window.down('splitbutton[reference=batchsetsplit]');
				var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"JUDGROUP","comParams":{"batchId":'+batchId+',"classId":'+classId+'}}';
				Ext.tzLoad(tzParams, function(responseData) {
					//资源集合信息数据
					var formData = responseData.judggroup;
					var groupid = formData.substring(0, formData.indexOf("|"));
					var groupname = formData.substring(formData.indexOf("|") + 1, formData.length + 1);
					console.log(groupid);
					console.log(groupname);
					var groupidarray = groupid.split(",");
					var groupnamearray = groupname.split(",");
					for (i = 0; i < groupnamearray.length; i++) {
						var menulist = {
							text: groupnamearray[i],
							value: groupidarray[i],
							handler: 'Batchsetuppwteam',

						};
						var fielddata = {
						"TZ_CLPS_GR_ID": groupidarray[i],
						"TZ_CLPS_GR_NAME": groupnamearray[i]
					};
						menuItems.push(menulist);
						selectdata.push(fielddata)

					}
					
				var states = Ext.create('Ext.data.Store', {
					fields: ['TZ_CLPS_GR_ID', 'TZ_CLPS_GR_NAME'],
					data: selectdata
				});
				//判断是否为空,若为空 则不执行
				if(window.down('grid').columns[2].editor!=null){
					window.down('grid').columns[2].editor.store = states;
				}

					console.log(menuItems);
					splitbut.setMenu(menuItems, true);

				});
			})

			win.show();
		}
	},


	//添加评委menu动态方法。
	Batchsetuppwteam: function(btn) {
		var strvalue = btn.getValue();
		console.log(strvalue);
		var attaList = "";
		var m = 0;
		var store = btn.findParentByType('grid').getStore();
		var selList = btn.findParentByType('grid').getSelectionModel().getSelection();

		//选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要操作的记录");
			return;
		} else {
			for (var i = 0; i < selList.length; i++) {
				selList[i].set('judgGroupId', strvalue);
			}
			store.commitChanges();
		}

	},
	//切换页签时触发
	beforeOnTabchange: function(tabs, newTab, oldTab) {
		var selectdata = [];
		var groupidarray = [];
		var groupnamearray = [];

		console.log("new:" + newTab.name);
		console.log("old:" + oldTab.name);
		if (newTab.title == "面试评委") {
			var gridstore = newTab.down('grid').getStore();
			var form = tabs.findParentByType('setmspsruler').down('form').getForm();
			var gridfield=tabs.findParentByType('setmspsruler').down('grid');
			 var kspwnum=tabs.findParentByType('setmspsruler').down('grid').down('numberfield[name=ksRevedpwnum]');
             var pwTeamnum=tabs.findParentByType('setmspsruler').down('grid').down('numberfield[name=countTeamnum]');
 
			var classId = form.findField('classId').getValue();
			var batchId = form.findField('batchId').getValue();
			if (classId != "" && batchId != "" && batchId != null && classId != null) {
				gridstore.tzStoreParams = '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.TZ_MSPS_PW_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
				gridstore.load();

                var tzParamsnum = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"NUMCOUNT","comParams":{"batchId":'+batchId+',"classId":'+classId+'}}';
                Ext.tzLoad(tzParamsnum,function(responsedata){
                	
                	kspwnum.setValue(responsedata.kspwnum);
                	pwTeamnum.setValue(responsedata.pwTeamnum);

               })
				var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"JUDGROUP","comParams":{"batchId":'+batchId+',"classId":'+classId+'}}';
				Ext.Ajax.request({
					url: Ext.tzGetGeneralURL(),
					params: {
						tzParams: tzParams
					},
					timeout: 60000,
					async: false,
					success: function(response, opts) {
						//返回值内容
						var jsonText = response.responseText;
						try {
							var jsonObject = Ext.util.JSON.decode(jsonText);

							/*判断服务器是否返回了正确的信息*/
							if (jsonObject.state.errcode == 0) {

								var formData = jsonObject.comContent.judggroup;
								var groupid = formData.substring(0, formData.indexOf("|"));
								var groupname = formData.substring(formData.indexOf("|") + 1, formData.length + 1);

								groupidarray = groupid.split(",");
								groupnamearray = groupname.split(",");

							} else {
								TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);

							}
						} catch (e) {
							if (typeof(failureFn) == "function") {
								failureFn(jsonText);
							}
							TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
						}

					}
				});
				for (i = 0; i < groupidarray.length; i++) {
					var fielddata = {
						"TZ_CLPS_GR_ID": groupidarray[i],
						"TZ_CLPS_GR_NAME": groupnamearray[i]
					};
					selectdata.push(fielddata)
				}
				var states = Ext.create('Ext.data.Store', {
					fields: ['TZ_CLPS_GR_ID', 'TZ_CLPS_GR_NAME'],
					data: selectdata
				});
				//判断是否为空,若为空 则不执行
				if(newTab.down('grid').columns[3].editor!=null){
					newTab.down('grid').columns[3].editor.store = states;
				}

				


			} else {
				Ext.Msg.alert("提示", "班级编号和批次编号都不能为空");
				return false;
			}
		} else {
			console.log("不是grid");


		}

	},
	readervalue: function(v) {
		console.log(v);
		var groupidarray = [];
		var groupnamearray = [];
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"JUDGROUPALL","comParams":{}}';

		Ext.Ajax.request({
			url: Ext.tzGetGeneralURL(),
			params: {
				tzParams: tzParams
			},
			timeout: 60000,
			async: false,
			success: function(response, opts) {
				//返回值内容
				var jsonText = response.responseText;
				try {
					var jsonObject = Ext.util.JSON.decode(jsonText);

					/*判断服务器是否返回了正确的信息*/
					if (jsonObject.state.errcode == 0) {

						var formData = jsonObject.comContent.judggroup;
						var groupid = formData.substring(0, formData.indexOf("|"));
						var groupname = formData.substring(formData.indexOf("|") + 1, formData.length + 1);
					
						groupidarray = groupid.split(",");
						groupnamearray = groupname.split(",");

					} else {
						TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);

					}
				} catch (e) {
					if (typeof(failureFn) == "function") {
						failureFn(jsonText);
					}
					TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
				}

			}
		});



		for (var i in groupidarray) {
			if (v == groupidarray[i]) {
				return groupnamearray[i];
				
			}
		}
	},
	exportPwinform:function(btn){
		var selksList = "";
		var m = 0;
		var store = btn.findParentByType('grid').getStore();
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
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"EXPORT","comParams":{"export":[' + selksList + ']}}';
		Ext.tzSubmit(tzParams, function(respDate) {
			var fileUrl = respDate.fileUrl;
			
				window.location.href = fileUrl;

		}, "导出成功!", true, this)

	
	},
	batcResetPassword:function(btn){
		var selksList = "";
		var m = 0;
		var store = btn.findParentByType('grid').getStore();
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
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"RESETPWD","comParams":{"export":[' + selksList + ']}}';
		Ext.tzSubmit(tzParams, function(respDate) {


		}, "设置成功!", true, this)
		
	},
	   //确定按钮
    ensurepwinfromSave:function(btn){
    var form = this.getView().child("form").getForm();

        if (form.isValid()) {
            //获得表单信息
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){

                comView.close();
            },"",true,this);
        }
},
	 
	onpwinfodescSave:function(btn){
		var form = this.getView().child("form").getForm();
		
        if (form.isValid()) {
            //获得表单信息
        	
        	//保存评委数，和组数
            var classId=form.findField('classId').getValue();
            var batchId=form.findField('batchId').getValue();
            var grid=btn.findParentByType('panel').down('grid');
            var kspwnum=grid.down('numberfield[name=ksRevedpwnum]').getValue();
            var pwTeamnum=grid.down('numberfield[name=countTeamnum]').getValue();
            var tzParams = this.getResSetInfoParams();
            var store = grid.getStore();
            var editRecs = store.getModifiedRecords();
             var comParamspw= '{"typeFlag":"PWTEAMNUM","data":{"classId":'+classId+',"batchId":'+batchId+',"kspwnum":'+kspwnum+',"pwTeamnum":'+pwTeamnum+'}}';

            for(var i=0;i<editRecs.length;i++) {
               comParamspw = comParamspw + ',' + '{"typeFlag":"JUDGE","classId":'+classId+',"batchId":'+batchId+',"data":' + Ext.JSON.encode(editRecs[i].data) + '}';
            }
            var tzParamspw = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"U","comParams":{"add":['+comParamspw+']}}';
   
            var num=btn.findParentByType('panel').down('grid');
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            	
            	Ext.tzLoad(tzParamspw,function(responseData){
            	
            	});
            	
                comView.actType = "update";
            },"",true,this);
        }
		
	},
	   getResSetInfoParams: function(){
        //表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        
            comParams = '"add":[{"typeFlag":"RULE","data":'+Ext.JSON.encode(formParams)+'}]';
        
        //修改json字符串
        var editJson = "";
       
        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
	    onPwinfoClose:function(){
        this.getView().close();

    },
    
    
    //删除评委
	deleteMsPw: function(view, rowIndex) {
         var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
         //资源集合ID
         var judgId = selRec.get("judgId");
         var form=view.findParentByType("setmspsruler").child("form").getForm();
		 var batchId= form.findField('batchId').getValue();
		 var classId= form.findField('classId').getValue();
		 
		 var comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},{"judgId":'+Ext.JSON.encode(judgId)+'}';

		 var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_JUDGES_STD","OperateType":"U","comParams":{"delete":[' + comparams + ']}}';
			
        
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
			if (btnId == 'yes') {
		   Ext.tzSubmit(tzParams,function(responseData){
            	
            	
            	
               store.removeAt(rowIndex);
            },"",true,this);
								
			}
		}, this);
	}





});