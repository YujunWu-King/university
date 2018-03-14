Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bjhfyList',
	//新增回复语
    addHfy: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFYBJ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var _bj_id=this.getView().class_id;
		var _lc_id=this.getView().bmlc_id;
		var win = this.lookupReference('bjHfy');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var form = win.child('form').getForm();
			form.findField("bj_id").setValue(_bj_id);
			form.findField("bmlc_id").setValue(_lc_id);
            this.getView().add(win);
        }
        win.show();
    },
	//回复语编辑
    hfy_edit:function(grid, rowIndex, colIndex){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFYBJ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BMSH_HFYBJ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('bjHfy');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var record = grid.store.getAt(rowIndex);
			var classID = record.data.bj_id;
			var bmlc_id = record.data.bmlc_id;
			var dybh_id = record.data.dybh_id;
			win.actType = "update";
			win.on('afterrender',function(panel){
				var form = win.child('form').getForm();
				var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYBJ_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","hfy_id":"'+dybh_id+'"}}';
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					form.setValues(formData);
				});
			});
            this.getView().add(win);
        }
        win.show();
    },
	//回复语编辑(title)
    editHfy:function(btn){
		var selList = this.getView().getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFYBJ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('bjHfy');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var classID = selList[0].get("bj_id");
			var bmlc_id = selList[0].get("bmlc_id");
			var dybh_id = selList[0].get("dybh_id");
			win.actType = "update";
			win.on('afterrender',function(panel){
				var form = win.child('form').getForm();
				var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYBJ_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","hfy_id":"'+dybh_id+'"}}';
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					form.setValues(formData);
				});
			});
            this.getView().add(win);
        }
        win.show();
    },
	//回复语编辑页面确定
	SureHfyPage: function(btn){
		var form = this.getView().child("form").getForm();
		var actType = this.getView().actType;
		var comParams = "";
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(form.getValues());
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYBJ_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
			}
		},"",true,this);
		var win = btn.findParentByType("bjhfyList");
		win.store.reload();
		this.getView().close();
	},
	//回复语编辑页面保存
	SaveHfyPage: function(btn){
		var form = this.getView().child("form").getForm();
		var actType = this.getView().actType;
		var comParams = "";
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(form.getValues());
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYBJ_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
			}
		},"",true,this);
		var win = btn.findParentByType("bjhfyList");
		win.store.reload();
		//this.getView().close();
	},
	//发布结果
	bmlc_fbjg:function(grid, rowIndex, colIndex){
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_FB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        var record = grid.store.getAt(colIndex);
        var classID = record.data.bj_id;
		var lc_id = record.data.lc_id;
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_FB_STD","OperateType":"QG","comParams":{"bj_id":"'+classID+'","lc_id":"'+lc_id+'"}}';
            /*Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                form.setValues(formData);
            });*/
			var tzStoreParams = '{"bj_id":"'+classID+'","lc_id":"'+lc_id+'"}';
            panel.child('panel').child('grid').store.tzStoreParams = tzStoreParams;
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//删除回复语
	deleteHfy: function(btn){
	   var selList = this.getView().getSelectionModel().getSelection();
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qxzyscdjl","请选择要删除的记录"));
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.confirm","确认"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var userStore = this.getView().store;
				   userStore.remove(selList);
				}												  
			},this);   
	   }
	},
	//删除回复语(列表)
	delete_Hfy: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.confirm","确认"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//保存回复语
	save_hfy: function(btn){
		var grid = this.getView();
		var store = grid.getStore();
		var removeJson = "";
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"update":[' + removeJson + "]";
		}
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFY_STD","OperateType":"U","comParams":{'+comParams+'}}';
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	//确定回复语
	ensure_hfy: function(btn){
		var me = this;
		var grid = this.getView();
		var store = grid.getStore();
		var removeJson = "";
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFY_STD","OperateType":"U","comParams":{'+comParams+'}}';
		Ext.tzSubmit(tzParams,function(){
			store.reload();	
			me.getView().close();
		},"",true,this);
		
	},
	//关闭回复语
	close_hfy: function(){
		this.getView().close();
	},
	getActivityInfoParams: function(){
		//var grid = this.getView().child("grid").getStore();
		var grid = this.getView().getStore();
		var grid_edit=grid.getModifiedRecords();
		//修改信息
		var edit_str="";
		for(var i=0;i<grid_edit.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"sffb","data":'+Ext.JSON.encode(grid_edit[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"sffb","data":'+Ext.JSON.encode(grid_edit[i].data)+'}';
	  		}
		}
		var comParams = "";
		if(edit_str != ""){
			if(comParams == ""){
				comParams = '"update":[' + edit_str + "]";
			}else{
				comParams = comParams + ',"update":[' + edit_str + "]";
			}
		}
		//结束报名信息项;
		console.log(comParams);
		//提交参数
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_LCFB_STD","OperateType":"U","comParams":{'+comParams+'}}';
		//console.log(tzParams);
        return tzParams;
	},
	//动态结果显示 系统变量选择
	searchSysVar:function(btn){
		var form = btn.findParentByType('form').getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_SYSVAR_T',
			searchDesc: '搜索模版参数',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_EFFFLG:{
						//value: Ext.tzOrgID,
						value:"Y",
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_SYSVARID:{
						desc:'系统变量编号',
						operator:'01',
						type:'01'
					},	
					TZ_SYSVARNAME:{
						desc:'系统变量名称',
						operator:'07',
						type:'01'
					}
				}	
			},
			srhresult:{
				TZ_SYSVARID: '参数编号',
				TZ_SYSVARNAME:'参数名称'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("sysvar").setValue(selection[0].data.TZ_SYSVARID);
				form.findField("sysvarname").setValue(selection[0].data.TZ_SYSVARNAME);
				
			}
		});	
    },
	onFormEnsure: function(btn){
		this.onSaveData("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	},
	//关闭回复语编辑页面
	CloseHfyPage: function(){
		this.getView().close();
	}
});