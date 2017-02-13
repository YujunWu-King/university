Ext.define('KitchenSink.view.qklZsmb.zsmbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.zsmbController',
    searchCertTmpl:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId:'TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.TZ_CERTTMPL_V',
            callback:function(searchCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = searchCfg;
                store.load();
            }
        });
    },
    addCertTmpl: function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZHENGSHU_COM"]["TZ_MOBAN_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MOBAN_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel';
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

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    eidtCertTmpl:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen > 1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        var certTmpl = selList[0].get("certTmpl");
        var JgId = selList[0].get("JgId");
        this.editCertTmplByID(JgId,certTmpl);
    },
    editCurrTmpl:function(view,rowIndex){
        var store = view.findParentByType("grid").store;
        var selPlan = store.getAt(rowIndex) ;
        var certTmpl = selPlan.get("certTmpl");
        var JgId = selPlan.get("JgId");
        this.editCertTmplByID(JgId,certTmpl);
    },
    editCertTmplByID:function(JgId,certTmpl){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZHENGSHU_COM"]["TZ_MOBAN_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的js类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_MOBAN_INFO_STD，请检查配置。')
            return;
        }

        var contentPanel,cmp,className,ViewClass,clsProto;
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }
        cmp = new ViewClass();
        //操作类型设置为更新
        cmp.actType = "update";
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            form.findField("certTmpl").setReadOnly(true);
            form.findField("certTmpl").addCls("lanage_1");
            //参数
            var tzParams = '{"ComID":"TZ_ZHENGSHU_COM","PageID":"TZ_MOBAN_INFO_STD","OperateType":"QF","comParams":{"certTmpl":"'+certTmpl+'","JgId":"'+JgId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
            	var formData = responseData.formData;
                form.setValues(formData);
                panel.down('image[name=titleImage]').setSrc(TzUniversityContextPath +formData.imageAUrl+formData.titleImageName);	
            });
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteCurrTmpl: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    deleteCertTmpl: function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
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
    saveMbList:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var comParams="";
        var removeRecs = store.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_ZHENGSHU_COM","PageID":"TZ_MOBAN_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        var comView = this.getView();
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.commitChanges();
            },"",true,this);
        }
    },
    ensureMbList:function(btn){
        this.saveMbList(btn);
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
    closeMbList:function(btn){
        //关闭窗口
        this.getView().close();
    },
    saveTmplDfn: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()) {			
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_ZHENGSHU_COM","PageID":"TZ_MOBAN_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
					panel.actType = "update";	
					form.findField("certTmpl").setReadOnly(true);
                    form.findField("certTmpl").addCls('lanage_1');
			},"",true,this);
		}
	},
	ensureTmplDfn: function(btn){ 
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var actType = panel.actType; 
		var form = panel.child("form").getForm();
		if (form.isValid()) {
		
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_ZHENGSHU_COM","PageID":"TZ_MOBAN_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
					panel.actType = "update";
					form.reset();
					panel.close();
			},"",true,this);
		}
	},
	closeTmplDfn:function(btn){
			var panel = btn.findParentByType("panel");
			panel.close();
    }
});