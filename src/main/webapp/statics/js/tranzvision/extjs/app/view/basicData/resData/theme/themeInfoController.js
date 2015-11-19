Ext.define('KitchenSink.view.basicData.resData.theme.themeInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.themeInfo', 
	/*requires: [
       'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel'
    ],*/
    addResSet: function() {
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存主题信息后，再新增资源集合。");
            return;
        }
        var form = this.getView().child("form").getForm();
        var grid = this.getView().child("grid");
        var store = grid.store;
        Ext.tzShowPromptSearch({
            recname: 'TZ_PT_ZYJH_TBL',
            searchDesc: '新增资源集合',
            maxRow:20,
            condition:{
                srhConFields:{
                    TZ_ZYJH_ID:{
                        desc:'资源集合编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_ZYJH_MC:{
                        desc:'资源集合名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_ZYJH_ID: '资源集合编号',
                TZ_ZYJH_MC: '资源集合名称'
            },
            multiselect: false,
            callback: function(selection){
            	var themeID=form.findField("themeID").getValue();
            	for(var i= 0;i<selection.length;i++){
	                var resSetID=selection[0].data.TZ_ZYJH_ID;
	                var resSetDesc=selection[0].data.TZ_ZYJH_MC;
	
	                var themeInfoModel = Ext.create('KitchenSink.view.basicData.resData.theme.themeInfoModel',{
	                    themeID:themeID,
	                    resSetID:resSetID,
	                    resSetDesc:resSetDesc
	                });
	                if(store.findRecord("resSetID",selection[i].data.resSetID) == null){
                        store.add(themeInfoModel);
                    }
               }
            }
        });
    },
    editResSet: function(btn){
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
        Ext.tzSetCompResourses("TZ_ZY_RESSET_COM");
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
		//资源集合编号
        var resSetID = selList[0].get("resSetID");
        
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
    deleteResSets: function(btn){
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
    onThemeResInfoSave: function(btn){
        //主题信息表单
        var form = this.getView().child("form").getForm();
        var grid = this.getView().child("grid");
         //资源集合列表数据
        var store = grid.getStore();
        
        if (form.isValid()) {
            //获取主题信息参数
            var tzParams = this.getThemeResInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                
                if(tzParams.indexOf("PLST")>-1||tzParams.indexOf("delete")>-1){
                    var tzStoreParams = '{"themeID":"'+form.findField("themeID").getValue()+'"}';
                    store.tzStoreParams = tzStoreParams;
                    store.reload();
                }
                form.findField("themeID").setReadOnly(true);
            },"",true,this);
        }
    },
    onThemeResInfoEnsure: function(btn){
        //主题信息表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取资源集合信息参数
            var tzParams = this.getThemeResInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            	var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.child("themeSetMg").store.reload();
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
	getThemeResInfoParams: function(){
        //主题信息表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"THEME","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"THEME","data":'+Ext.JSON.encode(formParams)+'}';
        }
        //资源集合信息列表
        var grid = this.getView().child("grid");
        //资源集合信息数据
        var store = grid.getStore();
        //修改记录
        var gridEditJson = "";
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(gridEditJson == ""){
                gridEditJson = '{"typeFlag":"RESSET","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                gridEditJson = gridEditJson + ',{"typeFlag":"RESSET","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        
        if(editJson != ""){
        	if(gridEditJson != ""){
            editJson=editJson+','+gridEditJson;
            }
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

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
        var tzParams = '{"ComID":"TZ_GD_THEMEGL_COM","PageID":"TZ_GD_THEME_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
	
	onThemeResInfoClose: function(){
        var contentPanel;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.child("themeSetMg").store.reload();
		this.getView().close();
	},
    //编辑
    editThemeInfoBL: function(view, rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //皮肤
        var resSetID = selRec.get("resSetID");
        //显示编辑页面
        this.editThemeInfoByID(resSetID);
    },
    //删除
    deleteThemeInfoBL: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    editThemeInfoByID: function(resSetID){

        Ext.tzSetCompResourses("TZ_ZY_RESSET_COM");
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
    }
});