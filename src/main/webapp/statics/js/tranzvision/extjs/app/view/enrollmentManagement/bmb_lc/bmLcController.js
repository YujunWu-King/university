Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmLcController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bmFlow',
	//回复语设置
	bmlc_hfySet:function(grid, rowIndex, colIndex){
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFY_STD"];
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
		cmp.class_id=classID;
		cmp.bmlc_id=lc_id;
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFY_STD","OperateType":"QG","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+lc_id+'"}}';
            /*Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                form.setValues(formData);
            });*/
			var tzStoreParams = '{"bj_id":"'+classID+'","bmlc_id":"'+lc_id+'"}';
            //panel.child('panel').child('grid').store.tzStoreParams = tzStoreParams;
			panel.store.tzStoreParams = tzStoreParams;
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
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
            
			var tzStoreParams = '{"bj_id":"'+classID+'","lc_id":"'+lc_id+'"}';
			panel.store.tzStoreParams = tzStoreParams;
			
			var view = cmp.getView();
			var tip = Ext.create('Ext.tip.ToolTip', {
				// The overall target element.
				target: view.el,
				//target:'company',
				// Each grid row causes its own separate show and hide.
				delegate: view.itemSelector,
				// Moving within the row should not hide the tip.
				trackMouse: true,
				// Render immediately so that tip.body can be referenced prior to the first show.
				renderTo: Ext.getBody(),
				listeners: {
					// Change content dynamically depending on which element triggered the show.
					beforeshow: function updateTipBody(tip) {
						tip.update(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.csjg","初筛结果")+'："' + view.getRecord(tip.triggerElement).get('result_desc') + '"');
					}
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
	//保存
	onSaveData: function(){
		//var form = this.getView().child("grid").getStore();
		var form = this.getView().getStore();
		if (form) {
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			//var activityId = form.findField("bj_id").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
				Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bccc","保存出错"));
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";
					}
				},"",true,this);
			}
		}else{
			Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qtxbtx","请填写必填项"));
		}
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
	onFormEnsure: function(btn){
		this.onSaveData("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});