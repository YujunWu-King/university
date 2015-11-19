Ext.define('KitchenSink.view.basicData.resData.resource.resourceSetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.resSet',
    addResSet: function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZY_RESSET_COM"]["TZ_RESSET_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESSET_INFO_STD，请检查配置。');
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
    editResSet: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        //资源集合ID
        var resSetID = selList[0].get("resSetID");
        //显示资源集合信息编辑页面
        this.editResSetInfoByID(resSetID);
    },
    editCurrResSet: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //资源集合ID
        var resSetID = selRec.get("resSetID");
        //显示资源集合信息编辑页面
        this.editResSetInfoByID(resSetID);
    },
    editResSetInfoByID: function(resSetID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZY_RESSET_COM"]["TZ_RESSET_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESSET_INFO_STD，请检查配置。');
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
            //资源集合表单信息;
            var form = panel.child('form').getForm();
            form.findField("resSetID").setReadOnly(true);
            form.findField("resSetID").addCls("lanage_1");
            //资源信息列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_INFO_STD","OperateType":"QF","comParams":{"resSetID":"'+resSetID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //资源集合信息列表数据
                var roleList = responseData.listData;

                var tzStoreParams = '{"resSetID":"'+resSetID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteResSets: function(){
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
				   var resSetStore = this.getView().store;
                    resSetStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteCurrResSet: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveResSets: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        //资源集合信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
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
        var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_MANG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    ensureResSets:function(btn){
    	 this.saveResSets(btn);
    	 //关闭窗口
    	 var comView = this.getView();
         comView.close();
    },
    closeResSets:function(btn){
    	//关闭窗口
    	 var comView = this.getView();
         comView.close();
    },
    onResSetInfoSave: function(btn){
        //资源集合表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取资源集合信息参数
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                form.findField("resSetID").setReadOnly(true);
                form.findField("resSetID").addCls("lanage_1");
            },"",true,this);
        }
    },
    onResSetInfoEnsure: function(btn){
        //资源集合表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取资源集合信息参数
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getResSetInfoParams: function(){
        //资源集合表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //公共资源        
        if(formParams["publicRes"] == undefined){
            formParams["publicRes"] = "N";
        }
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"SET","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"SET","data":'+Ext.JSON.encode(formParams)+'}';
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

        //资源信息列表
        var grid = this.getView().child("grid");
        //资源信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
    }
        if(removeJson != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onResSetInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    editCurrResource: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //资源集合ID
        var resSetID = selRec.get("resSetID");
        //资源ID
        var resourceID = selRec.get("resourceID");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZY_RESSET_COM"]["TZ_RESSET_RES_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESSET_RES_STD，请检查配置。');
            return;
        }
        //展示资源信息窗口
        var win = this.lookupReference('resourceInfoWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //参数
        var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_RES_STD","OperateType":"QF","comParams":{"resSetID":"'+resSetID+'","resourceID":"'+resourceID+'"}}';
        //资源信息表单
        var form = win.child("form").getForm();
        Ext.tzLoad(tzParams,function(responseData){
            form.setValues(responseData);
            form.findField("resourceID").setReadOnly(true);
            form.findField("resourceID").addCls("lanage_1");
        });
        win.show();
    },
    deleteCurrResource: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    addResource:function(btn){
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存资源集合信息后，再新增资源信息。");
            return;
        }

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZY_RESSET_COM"]["TZ_RESSET_RES_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESSET_RES_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('resourceInfoWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //资源集合信息
        var resourceInfoParams = this.getView().child("form").getForm().getValues();
        //资源集合ID
        var resSetID = resourceInfoParams["resSetID"];
        //资源信息表单
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({resSetID:resSetID});
        form.findField("resourceID").setReadOnly(false);
        win.show();
    },
    editResource: function(btn){
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZY_RESSET_COM"]["TZ_RESSET_RES_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESSET_RES_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('resourceInfoWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //资源集合ID
        var resSetID = selList[0].get("resSetID");
        //资源ID
        var resourceID = selList[0].get("resourceID");
        //参数
        var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_RES_STD","OperateType":"QF","comParams":{"resSetID":"'+resSetID+'","resourceID":"'+resourceID+'"}}';
        //资源信息表单
        var form = win.child("form").getForm();
        Ext.tzLoad(tzParams,function(responseData){
            form.setValues(responseData);
            form.findField("resourceID").setReadOnly(true);
            form.findField("resourceID").addCls("lanage_1");
        });
        win.show();
    },
    deleteResources: function(btn){
        //资源信息列表
        var grid = this.getView().child("grid");
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    onResourceInfoSave: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存资源信息*/
            this.saveResourceInfo(win);
        }
    },
    onResourceInfoEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存资源信息*/
            this.saveResourceInfo(win);
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        }
    },
    saveResourceInfo: function(win){
        //资源信息表单
        var form = win.child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //提交参数
        var tzParams = '{"ComID":"TZ_ZY_RESSET_COM","PageID":"TZ_RESSET_RES_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
        var tzStoreParams = '{"resSetID":"'+form.findField("resSetID").getValue()+'"}';
        var pageGrid = this.getView().child("grid");
        Ext.tzSubmit(tzParams,function(){
            win.actType = "update";
            form.findField("resourceID").setReadOnly(true);
            form.findField("resourceID").addCls("lanage_1");
            pageGrid.store.tzStoreParams = tzStoreParams;
            pageGrid.store.reload();
        },"",true,this);
    },
    onResourceInfoClose: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        win.close();
    },
    queryResSet:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZY_RESSET_COM.TZ_RESSET_MANG_STD.TZ_PT_ZYJH_TBL',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});

