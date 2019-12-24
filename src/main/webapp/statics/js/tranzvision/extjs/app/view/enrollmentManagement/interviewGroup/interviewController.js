//面试现场分组    控制层，所有的页面公用控制层
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.appFormInterview',
    
    //关闭 默认首页
    onComRegClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    
    //关闭预约列表
	closeviewList: function(btn) {
		var comView = btn.findParentByType("yyInfoPanel");
		comView.close();
	},
	
	//关闭预考生表
	closeviewListStu: function(btn) {
		//var comView = btn.findParentByType("viewmspsxsList_mspsview");
		//comView.close();
		this.getView().close();
	},
	
	//考生页面保存
	onSaveKsFz: function(btn) {
		
	},
	
	//考生页面确定
	ensureonKsFz: function(btn) {
		
	},
    
	//评委组分组保存
    onGroupSave:function(btn){
    	//获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        var grid= Ext.getCmp('pageGrid');
        if (form.isValid()) {
            /*保存资源信息*/
            var ret=this.savePlstComInfo(win);
        }
        var panelGrid=win.down("grid");
        panelGrid.getStore().reload();
    },

    //评委组分组确定
    onGroupEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存资源信息*/
            this.savePlstComInfo(win);
            //关闭窗口
            win.close();
        }
    },
    
    
    
    //评委组分组 保存 实际操作
    savePlstComInfo: function(win){
        //资源信息表单
        var form = win.child("form").getForm();
        var appInsId =form.findField("appInsId").getValue();
	   	var classId = form.findField("classID").getValue();
	   	var batchId = form.findField("batchID").getValue();
	   	var jugGroupId = form.findField("jugGroupId").getValue();
//	   	var grids = win.child("tabpanel").child("grid");
//	   	var groupName = grids.getSelectionModel().getSelection()[0].data.groupName;
	   	//console.log(groupName);

	   	
	   	console.log(appInsId);
	   	console.log(classId);
	   	console.log(batchId);
	   	console.log(jugGroupId);
	   	//console.log(groupName);
        //表单数据
        var formParams = form.getValues();

        //更新操作参数
        var comParams = "";
        var processComParams = "";
        //授权组件页面列表
        var grid = Ext.getCmp('pageGrid');
        //授权组件页面数据
        var store = grid.getStore();
                
        var store = grid.getStore();
        var editRecs=[];
        for(var i=0;i<store.getCount();i++){
            var recored = store.getAt(i);    
            editRecs.push(recored);
        } 
        //var editRecs = store.getModifiedRecords();
         var comParamspw= '';
         
         var flag=0;
        for(var i=0;i<editRecs.length;i++) {
        	//console.log(editRecs[i].data);
           if (editRecs[i].data.check ==true || editRecs[i].data.check =='Y') {
        	   flag=flag+1;
           }
           if (comParamspw=='') {
        	   comParamspw='{"classId":'+classId+',"batchId":'+batchId+',"appinsId":"'+appInsId+'","jugGroupId":"' + jugGroupId + '","data":' + Ext.JSON.encode(editRecs[i].data) + '}'; 
           } else {
        	   comParamspw = comParamspw + ',' +'{"classId":'+classId+',"batchId":'+batchId+',"appinsId":"'+appInsId+'","jugGroupId":"' + jugGroupId + '","data":' + Ext.JSON.encode(editRecs[i].data) + '}'; 
               
           }
           
           if (flag>1) {
        	   Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"),"只能选择一个分组");
               return;
           }     	                       
        }
        if (comParamspw=='') {
        	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"),"请选择一个分组");
            return;
        }
        var tzParamspw = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_MSFZ_STD","OperateType":"U","comParams":{"update":['+comParamspw+']}}';


        //提交参数
       // var panel = win.findParentByType("panel");
        //var permForm = panel.child("form").getForm();
        var panelGrid=win.findParentByType("stuInfoPanel").down("grid");
//        var panelGrid = win.findParentByType("mspsksGrid").down("grid");
//        if(tzParamspw!=""){
//            Ext.tzSubmit(tzParamspw,function(){
//                 //pageGrid.store.reload();
//            },"",true,this);
//            return true;
//       }
        if (tzParamspw != "") {
            Ext.tzSubmit(tzParamspw, function (result) {
                panelGrid.getStore().reload();
                Ext.MessageBox.show(
                    {
                        msg: result.MSG,
                        buttons: Ext.Msg.OK
                    });
            }, '', true, this);
            return true;
        }else{
            return false;
        }
        
    },
    
    //评委组分组页面，评委下拉菜单select 变更
	changeResTmpl:function(combo,records,eOpts){
		var form = this.getView().child("form").getForm();
		var appInsId =form.findField("appInsId").getValue();
	   	var classID = form.findField("classID").getValue();
	   	var batchID = form.findField("batchID").getValue();
	   	console.log(appInsId);
	   	console.log(classID);
	   	console.log(batchID);
	   	var jugGroupId = combo.value;
	   	console.log(jugGroupId);
	   	var comParamsObj = {
     				classId: classID,
     				batchId: batchID,
     				appInsId:appInsId,
     				pwGroupId:jugGroupId
     		}
	   	var tzStoreParams = Ext.JSON.encode(comParamsObj);
	   	var panelGrid=this.lookupReference('pageGrid');
    	panelGrid.store.tzStoreParams = tzStoreParams;

        panelGrid.store.load();
		
	},
	
	//批量分组
	PYFZ:function(btn){
		var panel = btn.findParentByType("stuInfoPanel");
		var form=panel.down('form').getForm();
		var classID = form.findField('classId').getValue();
		var batchID = form.findField('batchId').getValue();
		
		var grid = panel.down('grid');
		var selList = grid.getSelectionModel().getSelection();
		// 选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要分组的记录");
			return;
		} 
		var appInsIds="";
		for ( var i = 0; i <checkLen; i++){
			if (appInsIds=="") {
				appInsIds = selList[i].get("appInsId");
			} else  {
				appInsIds = appInsIds+","+selList[i].get("appInsId");
			}
		}
		
		 //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSXCFZ_COM"]["TZ_MSGL_MSFZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
        	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        //console.log(className);
        var win = this.lookupReference('interviewGroupWindow');
        if (!win) {
        	Ext.syncRequire(className);
        	ViewClass = Ext.ClassManager.get(className);
        	//新建类
        	//win = new ViewClass();
        	win = new ViewClass({
                classID:classID,
                batchID:batchID
            }
            );
        	this.getView().add(win);
        }
        

        //操作类型设置为更新
        win.on('afterrender',function(panel){
        	var wform = panel.child('form').getForm();
            var comParamsObj = {
        			ComID: 'TZ_MSXCFZ_COM',
        			PageID: 'TZ_MSGL_MSFZ_STD',
        			OperateType: 'GETGROUP',
        			comParams:{
        				classId: classID,
        				batchId: batchID,
        				appInsId:appInsIds
        			}
        		}
            var panelGrid = panel.down('grid');  
            var tzParams = Ext.JSON.encode(comParamsObj);
            Ext.tzLoad(tzParams,function(respData){
                var pw = respData.pwGroupId;
                if(pw!="" && pw!=undefined) {
                	  comParamsObj = {
                 				classId: classID,
                 				batchId: batchID,
                 				appInsId:appInsIds,
                 				pwGroupId:pw
                 		}
                	 var tzStoreParams = Ext.JSON.encode(comParamsObj);
                	  wform.setValues({appInsId:appInsIds,classID:classID,batchID:batchID,jugGroupId:pw});
                	panelGrid.store.tzStoreParams = tzStoreParams;
                    
                    panelGrid.store.load();
                } else {
                	pw="";
                	wform.setValues({appInsId:appInsIds,classID:classID,batchID:batchID,jugGroupId:pw});
                }
            });
			
		});
        win.show();
		
		
	},
    
	//可配置查询 查询考生列表
    queryStudents:function(btn){
    	var panel = btn.findParentByType("stuInfoPanel");
		var form=btn.findParentByType('stuInfoPanel').down('form').getForm();
		var classId = form.findField('classId').getValue();
		var batchId = form.findField('batchId').getValue();
		var msJxNo = form.findField('msJxNo').getValue();

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.TZ_MSYY_KS_VW',
            condition:{
                TZ_CLASS_ID:classId,
                TZ_APPLY_PC_ID:batchId,
                TZ_MS_PLAN_SEQ:msJxNo
            },
            callback: function(seachCfg){
            	var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;				
				store.load();
            }
        });
    },
    /**
     * 面试现场分组-默认页面可配置查询-修改视图为TZ_BMBSH_ECUST_VW0
     * author：丁鹏
     * 时间：2019年11月25日16:33:13
     * */
    // 默认页面可配置查询
    queryClass:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MSXCFZ_COM.TZ_MSGL_CLASS_STD.TZ_BMBSH_ECUST_VW0',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },


    
    //显示该班级批次下 的 预约面试列表
    yyShow:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSXCFZ_COM"]["TZ_MSPS_KSMD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        //console.log(className);
        ViewClass = Ext.ClassManager.get(className);
        //console.log(ViewClass);
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

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;


        cmp = new ViewClass({
                    classID:classID,
                    batchID:batchID
                }
           );
        //cmp = new ViewClass();
            cmp.on('afterrender',function(panel){
            	console.log("1111");
            	var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_STU_STD",' + '"OperateType":"QF","comParams":{"classId":"'+classID+'","batchId":"'+batchID+'"}}';
                var panelGrid = panel.down('grid');
                
                var tzStoreParams = '{"classID": "' + classID + '","batchID": "' + batchID + '"}';
                panelGrid.store.tzStoreParams = tzStoreParams;

                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    if(formData!="" && formData!=undefined) {
                        form.setValues(formData);
                        panelGrid.store.load();
                    } 
                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }

    },
    
    
    //显示该班级批次预约下 的 考生列表
    openStu:function(grid, rowIndex, colIndex){
    	
    	//是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSXCFZ_COM"]["TZ_MSGL_STU_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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

        
        
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var msJxNo = record.data.msJxNo;


        cmp = new ViewClass({
                    classID:classID,
                    batchID:batchID
                }
           );
            cmp.on('afterrender',function(panel){
            	
            	var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_STU_STD",' + '"OperateType":"QF","comParams":{"classId":"'+classID+'","batchId":"'+batchID+'"}}';
                var panelGrid = panel.down('grid');

                var tzStoreParams = '{"cfgSrhId":"TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.TZ_MSYY_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classID + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchID + '","TZ_MS_PLAN_SEQ-operator": "01","TZ_MS_PLAN_SEQ-value": "' + msJxNo + '"}}';
                panelGrid.store.tzStoreParams = tzStoreParams;

                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                   // console.log(formData);
                    if(formData!="" && formData!=undefined) {
                        panel.actType="update";
                        form.setValues(formData);
                        form.findField("msJxNo").setValue(msJxNo);
                        panelGrid.store.load();
                    } 
                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }

    },
        
    //打开 某一报名表，评委组分组页面
    openInterviewGroupWindow:function(view, rowIndex){
    	
    	var panel = view.findParentByType("stuInfoPanel");
		var form = panel.down('form').getForm();
        var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
	   	var appInsID = selRec.get("appInsId");

	   	var batchID =form.findField("batchId").getValue();
	   	var classID = form.findField("classId").getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSXCFZ_COM"]["TZ_MSGL_MSFZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
        	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        //console.log(className);
        var win = this.lookupReference('interviewGroupWindow');
        if (!win) {
        	Ext.syncRequire(className);
        	ViewClass = Ext.ClassManager.get(className);
        	//新建类
        	//win = new ViewClass();
        	win = new ViewClass({
                classID:classID,
                batchID:batchID
            }
            );
        	this.getView().add(win);
        }
        

        //操作类型设置为更新
        win.on('afterrender',function(panel){
        	var wform = panel.child('form').getForm();
            var comParamsObj = {
        			ComID: 'TZ_MSXCFZ_COM',
        			PageID: 'TZ_MSGL_MSFZ_STD',
        			OperateType: 'GETGROUP',
        			comParams:{
        				classId: classID,
        				batchId: batchID,
        				appInsId:appInsID
        			}
        		}
            var panelGrid = panel.down('grid');  
            var tzParams = Ext.JSON.encode(comParamsObj);
            Ext.tzLoad(tzParams,function(respData){
                var pw = respData.pwGroupId;
                if(pw!="" && pw!=undefined) {
                	  comParamsObj = {
                 				classId: classID,
                 				batchId: batchID,
                 				appInsId:appInsID,
                 				pwGroupId:pw
                 		}
                	 var tzStoreParams = Ext.JSON.encode(comParamsObj);
                	  wform.setValues({appInsId:appInsID,classID:classID,batchID:batchID,jugGroupId:pw});
                	panelGrid.store.tzStoreParams = tzStoreParams;
                    
                    panelGrid.store.load();
                } else {
                	pw="";
                	wform.setValues({appInsId:appInsID,classID:classID,batchID:batchID,jugGroupId:pw});
                }
            });
			
		});
        win.show();
    },
    /**
     * 评委组清空分组 保存 实际操作迁移
     * author：丁鹏
     * time：2019年11月22日14:10:40
     * */
    //评委组清空分组 保存 实际操作
    clearGroupInfo(btn) {
        var panel = btn.findParentByType("stuInfoPanel");
        let panelGrid = panel.down('grid');
        let selectData = panelGrid.getSelectionModel().getSelection();

        if (!selectData.length) {
            Ext.MessageBox.alert("提示", "请您选择需要清空分组信息的学生！");

        } else {
            let ins = selectData.map(item => {
                return item.data.appInsId
            }).join(",");
            Ext.MessageBox.confirm("提示", "您是否确认清空分组信息，确认则进行清空!", (result) => {
                if ("yes" === result || "ok" === result) {
                    var form = panel.down('form').getForm();
                    var classID = form.findField('classId').getValue();
                    var batchID = form.findField('batchId').getValue();
                    var comParamsObj = {
                        ComID: 'TZ_MSXCFZ_COM',
                        PageID: 'TZ_MSGL_MSFZ_STD',
                        OperateType: 'clearGroupInfo',
                        comParams: {
                            classId: classID,
                            batchId: batchID,
                            insId: ins
                        }
                    }
                    var tzParams = Ext.JSON.encode(comParamsObj);
                    Ext.tzLoad(tzParams, function (respData) {
                        var panelGrid = panel.down("grid");
                        panelGrid.getStore().reload();
                        Ext.MessageBox.alert("提示", "清除分组信息成功!");
                    });
                } else {

                }
            });
        }
    },
    /**
     * 组内排序按钮-迁移
     * author：丁鹏
     * time：2019年11月22日14:13:03
     * */
    groupOrder(btn) {
        var panel = btn.findParentByType("stuInfoPanel");
        var form = panel.down('form').getForm();
        var classID = form.findField('classId').getValue();
        var batchID = form.findField('batchId').getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MSXCFZ_COM"]["TZ_MSGL_ZNPX_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx", "您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs", "未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        //console.log(className);
        var win = this.lookupReference('interviewGroupXhWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            //win = new ViewClass();
            win = new ViewClass({
                    classID: classID,
                    batchID: batchID
                }
            );
            this.getView().add(win);
        }
        //操作类型设置为更新
        win.on('afterrender', function (panel) {
            var wform = panel.child('form').getForm();
            wform.setValues({classID: classID, batchID: batchID});
        });
        win.show();

    }

});

