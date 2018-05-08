Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.appFormInterview',
    
    update:function(btn){
    	
    	var panel = this.getView();
        var form = panel.child("form").getForm();
        var classID = form.findField('classID').getValue();
        var tz_app_ins_id = form.findField('tz_app_ins_id').getValue();
        var inteGroup_id = form.findField('jugGroupName').getValue();
        
    	//选中行
 	    // var selList = this.getView().findParentByType("grid").getSelectionModel().getSelections();
    	var grid = this.getView().child("grid");
    	var clickone =grid.getSelection();//获取选取行的数组集合
    	
    	//判断是否选择评委组
    	if(inteGroup_id == null){
    		Ext.Msg.alert("提示","请选择评委组");   
 			return;
    	}
    	
 	    //判断是否有选中面试组
 	    if(clickone.length == 0){
 			Ext.Msg.alert("提示","请选择面试分组");   
 			return;
 	    }
 	    var tz_group_name = clickone[0].get('tz_group_name');
    	var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSPS_KSMD_STD","OperateType":"updateStu","comParams":{"tz_group_name":"'+tz_group_name+'","classID":"'+classID+'","tz_app_ins_id":"'+tz_app_ins_id+'","inteGroup_id":"'+inteGroup_id+'"}}';
    	
    	var panel = this.getView();
        if(tzParams!=""){
            Ext.tzSubmit(tzParams,function(responseData){
            	
            	panel.close();	
            },"",true,this);
        }
    },
    
    queryStudents:function(btn){
        var store = btn.findParentByType("grid").store;

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.TZ_APP_LIST_VW',
            condition:{
                TZ_CLASS_ID:store.classID,
                TZ_BATCH_ID:store.batchID
            },
            callback: function(seachCfg){
                var tzStoreParams = Ext.decode(seachCfg);
                tzStoreParams.classID = store.classID;
                tzStoreParams.batchID = store.batchID;
                store.tzStoreParams = Ext.encode(tzStoreParams);
                store.load();
            }
        });
    },
    queryClass:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MSXCFZ_COM.TZ_MSGL_CLASS_STD.TZ_BMBSH_ECUST_VW',
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
    onStuInfoSave: function(btn){
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams();
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.commitChanges();
                },"",true,this);
            }
        }
    },
    onStuInfoEnsure: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams();
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    comView.close();
                },"",true,this);
            }else{
                comView.close();
            }
        }
    },
    refresh: function(btn){
    	var grid = this.getView().child("grid");
    	console.log(grid)
    	grid.store.reload();
    },
    getStuInfoParams: function(){
        //更新操作参数
        var comParams = "";
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        //修改json字符串
        var editJson = "";
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
        return tzParams;
    },
    onStuInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    
    viewApplicants:function(grid, rowIndex, colIndex){
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
                        var tzExpandParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_STU_STD","OperateType":"tzLoadExpandData","comParams":{"classID":"'+classID+'","appInsID":"'+appInsID+'"}}';
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
                var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_STU_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    form.setValues(formData);

                    var tzStoreParams = {
                        "classID":classID,
                        "batchID":batchID,
                        "cfgSrhId": "TZ_MSXCFZ_COM.TZ_MSGL_STU_STD.TZ_APP_LIST_VW",
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
    
    //打开面试分组窗口
    openInterviewGroupWindow:function(view, rowIndex){
    	
        var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
	   	var appInsID = selRec.get("appInsID");
	   	var group_name = selRec.get("group_name");
	   	var clpsGrName = selRec.get("clpsGrName");

        var classID = this.getView().classID;
        var tzParams = '{"ComID":"TZ_MSXCFZ_COM","PageID":"TZ_MSGL_MSFZ_STD","OperateType":"","comParams":{"classID":"' + classID + '","appInsID":"'+appInsID+'"}}';
        Ext.tzLoadAsync(tzParams,function(responseData){
        });
        
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
        var win = this.lookupReference('classForm');
        if (!win) {
        	Ext.syncRequire(className);
        	ViewClass = Ext.ClassManager.get(className);
        	//新建类
        	win = new ViewClass();
        	this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        win.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//form.setValues(responseData);
			form.findField("tz_app_ins_id").setValue(appInsID);
			form.findField("classID").setValue(classID);
			form.findField("group_name").setValue(group_name);
			
			var combo = panel.child('form').child('combobox');
	        //设置combobox默认值
			combo.setValue(clpsGrName);  
			
			var grid = panel.child('grid');
			var rowCount = grid.store.getCount();
	    	//设置grid默认值
	    	for(var i=0;i<rowCount;i++){
	            if(grid.store.getAt(i).get("tz_group_name") == group_name) {   
	                //选中默认行
	            	grid.on('boxready', function(){
	            		grid.getSelectionModel().select(i, true);
	            	})
	            	return;
	            }
	        }
			
		});
        //var comRegParams = this.getView().child("form").getForm().getValues();
        //var tabPanel = win.lookupReference("packageTabPanel");
        //tabPanel.setActiveTab(1);
        //var form = win.child("form").getForm();
        //form.reset();
        //form.setValues({appInsID:"'"+classID+"'"});
        win.show();
    },

});

