Ext.define('KitchenSink.view.judgesManagement.secretaryAccount.secretaryAccController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.secretaryAccController',
    createClass: function () {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SEC_ACC_COM"]["TZ_SECINFO_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SECINFO_STD，请检查配置。');
            return;
        }

        var contentPanel, cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        //contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        //contentPanel.body.addCls('kitchensink-example');

        if (!Ext.ClassManager.isCreated(className)) {
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
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        cmp = new ViewClass();

        return cmp;
    },
    //选择系所
    selectJudgeDepart:function(trigger){
        Ext.tzShowPromptSearch({
            recname: 'TZ_GSM_DEPT_VW',
            searchDesc: '选择系所',
            maxRow:20,
            condition:{
                presetFields:
                {
                    TZ_MAJOR_ID:{
                        type:'01'
                    },
                    TZ_MAJOR_NAME:{
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_MAJOR_ID:{
                        desc:'所属系所ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_MAJOR_NAME:{
                        desc:'所属系所',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_MAJOR_ID: '所属系所ID',
                TZ_MAJOR_NAME: '所属系所'
            },
            multiselect: false,
            callback: function(selection){
                var oprID = selection[0].data.TZ_MAJOR_ID;
                var oprName = selection[0].data.TZ_MAJOR_NAME;
                var form = trigger.findParentByType('form');
                //form.getForm().findField("judgeID").setValue(oprID);
                form.getForm().findField("judgeDepart").setValue(oprName);
            }
        })
    },
    addNewSecretaryAcc1: function (btn) {
        var roleGrid = this.getView();
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新增
        var cmp = this.createClass();
        //操作标志
        cmp.actType = "add";
        var jugGrid = btn.findParentByType('grid');
        cmp.jugGrid = jugGrid;

        //var grid = this.lookupReference('userRoleGrid');

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
//            form.findField('jugGroupId').setValue('NEXT');
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }


    },
	//添加
	addNewSecretaryAcc: function(btn){		
		this.secretaryAccInfoHandler("","","add");
	},
	
	//删除当前行
	deleteCurrRow: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	
	//批量删除
	deleteSecretaryBat: function(btn){
		 //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			//Ext.Msg.alert("提示","请选择要删除的记录");
			  Ext.Msg.show({
					title: '提示信息',
					msg: "请选择要删除的记录！",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING,
					buttonText: {
						ok: "关闭"
					}
				}); 
			return;
	   }else{
		  Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);    
	   }
	},
	//编辑
	editCurrRow: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var accNum = rec.data.accountNo;
		var oprid = rec.data.oprId;
//		this.judgeAccInfoHandler(accNum,oprid,"update");
		this.secretaryAccInfoHandler(accNum,"","update");
	},
	//编辑选中行秘书账号
	editSelSecretaryAcc: function(btn){
		//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			//Ext.Msg.alert("提示","请选择要编辑的记录！");   
			Ext.Msg.show({
					title: '提示信息',
					msg: "请选择要编辑的记录！",
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.WARNING,
					buttonText: {
						ok: "关闭"
					}
				}); 
			return;
	   }else if(checkLen > 1){
		   //	Ext.Msg.alert("提示","只能选择一条数据编辑！");   
		   	Ext.Msg.show({
				title: '提示信息',
				msg: "只能选择一条数据编辑！",
				buttons: Ext.Msg.OK,
				icon: Ext.Msg.WARNING,
				buttonText: {
					ok: "关闭"
				}
			}); 
			return;
	   }else{
		    var accNum = selList[0].data.accountNo;
			var oprid = selList[0].data.oprId;
		   	this.secretaryAccInfoHandler(accNum,oprid,"update");
	   }
	},
	//处理秘书信息表单
	secretaryAccInfoHandler: function(accNum, oprid, type){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SEC_ACC_COM"]["TZ_SECINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SECINFO_STD，请检查配置。');
			return;
		}
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
		
		var win = this.lookupReference('secretaryAccInfoWindow');
		var panel = this.getView();
		
		var params = '{"ComID":"TZ_SEC_ACC_COM","PageID":"TZ_SECINFO_STD","OperateType":"QU","comParams":{}}';
//		var params = '{"ComID":"TZ_JUDGE_ACC_COM","PageID":"TZ_JUDINFO_STD","OperateType":"QG","comParams":{"orgId":"'+Ext.tzOrgID+'"}}';
//		console.log(params);
		Ext.tzLoad(params, function(resp){
			if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
				//新建类
				win = new ViewClass(resp.judgeTypeArr);
				//this.getView().add(win);
				panel.add(win);
			}
			//设置操作类型
			win.actType = type;
			win.pGrid = panel;
			var form = win.child("form").getForm();
			//var grid = win.child("form").child("grid");
			if (type == "add"){
				form.reset();
				form.findField("accountNo").setReadOnly(false);
				form.findField("orgId").setValue(Ext.tzOrgID);
				form.findField("orgDesc").setValue(resp.orgDesc);
			} else if (type == "update"){
				//参数
//				var tzParams = '{"ComID":"TZ_JUDGE_ACC_COM","PageID":"TZ_JUDACC_GL_STD","OperateType":"QF","comParams":{"accountNo":"'+accNum+'"}}';
				var tzParams = '{"ComID":"TZ_SEC_ACC_COM","PageID":"TZ_SECACC_GL_STD","OperateType":"QF","comParams":{"accountNo":"'+accNum+'","orgId":"'+Ext.tzOrgID+'"}}';
				Ext.tzLoad(tzParams,function(responseData){
//					console.log(responseData);
					form.setValues(responseData);
//					form.setValues(responseData.formData);
					form.findField("accountNo").setReadOnly(true);
				});
			}

            tab = contentPanel.add(win);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (win.floating) {
                win.show();
            }
		});
	},
	
	
	//保存修改
	onSave: function(btn){
		var delJson;
		var grid = this.getView();
		
		var tzParams = '{"ComID":"TZ_SEC_ACC_COM","PageID":"TZ_SECACC_GL_STD","OperateType":"U","comParams":{';
		//删除数据行
		var removeRecs = grid.store.getRemovedRecords();
		for (var i=0; i<removeRecs.length; i++){
			//删除行JSON报文
			if (i == 0){
				delJson = Ext.JSON.encode(removeRecs[i].data);
			} else {
				delJson += ',' + Ext.JSON.encode(removeRecs[i].data);
			}	
		}
		if (removeRecs.length > 0){
			delJson = '"delete": ['+ delJson +']';
			tzParams += delJson + '}}';
			
			Ext.tzSubmit(tzParams,function(respData){
				grid.store.reload();
			},"保存成功",true,this);
		}
	},
	onClose:function(btn){
//		btn.up('grid').close();
		this.view.close();
	},
	onSure: function(btn){
		var delJson;
		var grid = this.getView();
		
		var tzParams = '{"ComID":"TZ_SEC_ACC_COM","PageID":"TZ_SECACC_GL_STD","OperateType":"U","comParams":{';
		//删除数据行
		var removeRecs = grid.store.getRemovedRecords();
		for (var i=0; i<removeRecs.length; i++){
			//删除行JSON报文
			if (i == 0){
				delJson = Ext.JSON.encode(removeRecs[i].data);
			} else {
				delJson += ',' + Ext.JSON.encode(removeRecs[i].data);
			}	
		}
		if (removeRecs.length > 0){
			delJson = '"delete": ['+ delJson +']';
			tzParams += delJson + '}}';
			
			Ext.tzSubmit(tzParams,function(respData){
//				grid.store.reload();
				grid.close();
			},"",true,this);
		}
	},

	//关闭
	/*==========================================================*/
	/*    关闭班级列表页面  LYY  2015-10-26                     */
	/*==========================================================*/
	onClose1:function(btn){
		btn.up('grid').close();
	},
	
	onWinClose: function(btn){
		var win = btn.findParentByType('panel');
		win.close();
//		this.getView().getEl().unmask();;
	},
	
	onSecretaryAccSave: function(btn){
		var win = btn.findParentByType('panel');
		this.saveSecretaryAccInfoHandler(win,"save");
        var tentPanel = Ext.getCmp('tranzvision-framework-content-panel');

           var targetStore = tentPanel.child("grid").getStore();

//        console.log(targetStore);
        targetStore.reload();
	},
	
	onSecretaryAccEnsure: function(btn){
		var win = btn.findParentByType('panel');
		this.saveSecretaryAccInfoHandler(win,'ensure');
        var tentPanel = Ext.getCmp('tranzvision-framework-content-panel');

        var targetStore = tentPanel.child("grid").getStore();

//        console.log(targetStore);
        targetStore.reload();
//        this.getView().close();
	},

	saveSecretaryAccInfoHandler: function(win,sType){
		var form = win.child('form').getForm();
		var accNum = form.findField("accountNo").getValue();
		
		
		if (form.isValid()){
			var actType = win.actType;
			var psd1 = form.findField("password").getValue();
			var psd2 = form.findField("rePassword").getValue();
			
//            var checked =   win.child('form').getComponent("judgeTypeCheckboxGroup").getChecked();
			
			//var checked = Ext.getCmp('judgeTypeCheckboxGroup').getChecked();
//			if (checked.length > 0){
				if (psd1 == psd2){
					
					var comParams = "";
					var formParams = form.getValues();
					if (formParams['userType'] == undefined){
						formParams['userType'] = "0";
					}
					if (formParams['clockFlag'] == undefined){
						formParams['clockFlag'] = "N";
					}
					if (formParams['orgId'] == undefined){
						formParams['orgId'] = form.findField('orgId').getValue();	
					}
	
					if (actType == "add"){
						comParams = '"add":[{"typeFlag":"JUDINFO","data":'+Ext.JSON.encode(formParams)+'}]';
					}
					
					//修改json字符串
					var editJson = "";
					if(actType == "update"){
						editJson = '{"typeFlag":"JUDINFO","data":'+Ext.JSON.encode(formParams)+'}';
					}

					if(editJson != ""){
						if(comParams == ""){
							comParams = '"update":[' + editJson + "]";
						}else{
							comParams = comParams + ',"update":[' + editJson + "]";
						}
					}
					
					var tzParams = '{"ComID":"TZ_SEC_ACC_COM","PageID":"TZ_SECINFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
					Ext.tzSubmit(tzParams,function(respData){
						if (actType == "add"){
							win.actType = "update";
							form.findField("accountNo").setReadOnly(true    );
						}
						if (sType == "ensure"){
							win.close();	
						}
					},"",true,this);
				} else {
					//Ext.Msg.alert("提示","两次密码输入不一致！");
					Ext.Msg.show({
						title: '提示信息',
						msg: "两次密码输入不一致！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			/*}else{
				Ext.Msg.show({
						title: '提示信息',
						msg: "请选择评委类型！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
			}*/
		}
	},
	//放大镜搜索角色名称
	searchRoleName: function(btn){
//		var form = btn.findParentByType("panel").child("form");
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'PSROLEDEFN_VW',
			searchDesc: '搜索角色名称',
			maxRow:50,
			condition:{
				presetFields:{
					
				},
				srhConFields:{
					ROLENAME:{
						desc:'角色名称',
						operator:'07',
						type:'01'	
					},
					DESCR:{
						desc:'描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				ROLENAME: '角色名称',
				DESCR: '描述'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("roleName").setValue(selection[0].data.ROLENAME);
				form.findField("roleNameDesc").setValue(selection[0].data.DESCR);
			}
		});	
	},
	//可配置搜索
	searchSecretaryAcc: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_SEC_ACC_COM.TZ_SECACC_GL_STD.TZ_MSZHGL_VW',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值
            },          
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	}
   
});