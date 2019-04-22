Ext.define('KitchenSink.view.viewByPro.proListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.proList', 
	
	
	//查看项目下的班级************************************************************
	editClassInfo: function(grid, rowIndex, colIndex){
	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BY_PRO_COM"]["TZ_CLASS_LIST_PAGE"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLASS_LIST_PAGE，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
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
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}
		cmp = new ViewClass();
		
		var record = grid.store.getAt(rowIndex);
        var projectId = record.data.projectId;
        var projectType = record.data.projectType;
        
        //console.log("*******projectId***********"+projectId);
		
		//操作类型设置为更新
		cmp.on('afterrender',function(panel){
			//全日制度不需要面试,隐藏面试列
			if(projectType == "全日制"){
				panel.columns[8].setVisible(false);
			}
           // var tzParams = '{"ComID":"TZ_BY_PRO_COM","PageID":"TZ_CLASS_LIST_PAGE","OperateType":"QF","comParams":{"projectId":"'+projectId+'"}}';
            //Ext.tzLoad(tzParams,function(respData){
               
            	var tzStoreParams = '{"cfgSrhId": "TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.TZ_BMBSH_ECUST_VW","condition":{"TZ_PRJ_ID-operator":"01","TZ_PRJ_ID-value":"'+projectId+'","TZ_DLZH_ID-operator":"01","TZ_DLZH_ID-value":"'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}';
            	panel.store.projectId=projectId;
            	panel.store.tzStoreParams = tzStoreParams;
            	panel.store.load();
           // });
        
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}	  
	},
    viewApplicants:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_STU_STD"];
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
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        //alert(ViewClass)
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
        window.classID2=record.data.classID;
        window.batchID2=record.data.batchID;
        var render = function(initialData){
        	
            cmp = new ViewClass({
                    initialData:initialData,
                    classID:classID,
                    batchID:batchID
                }
            );
            cmp.on('afterrender',function(panel){
                var form = panel.child('form').getForm();
                var panelGrid = panel.child('grid');
                panelGrid.getView().on('expandbody', function (rowNode, record, expandRow, eOpts){
                    if(!record.get('moreInfo')){
                        var appInsID = record.get('appInsID');
                        var tzExpandParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzLoadExpandData","comParams":{"classID":"'+classID+'","appInsID":"'+appInsID+'"}}';
                        Ext.tzLoad(tzExpandParams,function(respData){
                                if(panelGrid.getStore().getModifiedRecords().length>0){
                                    record.set('moreInfo',respData);
                                }else{
                                    record.set('moreInfo',respData);
                                    panelGrid.getStore().commitChanges( );
                                }
                            },panelGrid
                        );
                    }
                });
                var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    form.setValues(formData);

                    var tzStoreParams = {
                        "classID":classID,
                        "batchID":batchID,
                        "cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW",
                        "condition":{
                            "TZ_CLASS_ID-operator": "01",
                            "TZ_CLASS_ID-value": classID,
                            "TZ_BATCH_ID-operator": "01",
                            "TZ_BATCH_ID-value": batchID
                        }
                    };
                    panelGrid.store.classID=classID;
                    panelGrid.store.batchID=batchID;
                    panelGrid.store.tzStoreParams = Ext.encode(tzStoreParams);
                    panelGrid.store.load();
                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        };


        var submitStateStore = new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
            auditStateStore = new KitchenSink.view.common.store.appTransStore("TZ_AUDIT_STATE"),
            interviewResultStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_RESULT"),
            orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_ORG_COLOR_V',
                condition:{
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        operator:'01',
                        type:'01'
                    }},
                result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
            });

        //下拉项过滤器数据
        var colorSortFilterOptions=[],
            submitStateFilterOptions=[],
            auditStateFilterOptions=[],
            interviewResultFilterOptions=[];

        //颜色类别初始化数据-学生颜色类别列渲染数据
        var initialColorSortData=[];

        //4个下拉控件Store加载完毕之后打开页面
        var times = 4;
        var beforeRender = function(){
            times--;
            if(times==0){
                render({
                    submitStateStore:submitStateStore,
                    auditStateStore:auditStateStore,
                    interviewResultStore:interviewResultStore,
                    orgColorSortStore:orgColorSortStore,
                    colorSortFilterOptions:colorSortFilterOptions,
                    submitStateFilterOptions:submitStateFilterOptions,
                    auditStateFilterOptions:auditStateFilterOptions,
                    interviewResultFilterOptions:interviewResultFilterOptions,
                    initialColorSortData:initialColorSortData
                });
            }
        };

        orgColorSortStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                initialColorSortData.push(records[i].data);
                colorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
            }
            beforeRender();
        });
        submitStateStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                submitStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });
        auditStateStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                auditStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });
        interviewResultStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                interviewResultFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });

    },
	//确定
	ensureHardCodeInfos: function(btn){
		//保存信息
		this.saveHardCodeInfos(btn);
		//关闭窗口
		this.view.close();
	},
	//关闭
	closeHardCodeInfos:function(btn){
		this.view.close();
	},
	onHardCodeClose: function(btn){
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		panel.close();
	},
    queryHardCode:function(btn){     //searchComList为各自搜索按钮的handler event;
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_BY_PRO_COM.TZ_PRO_LIST_PAGE.TZ_PRJ_PROMG_VW',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID,          //设置搜索字段的默认值，没有可以不设置condition;
				TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	}
});