Ext.define('KitchenSink.view.template.kjgl.kjController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.kj',
    queryKj:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_KJGL_COM.TZ_KJGL_LIST_STD.TZ_KJ_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addKj:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_KJGL_COM"]["TZ_KJGL_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_KJGL_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
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

        // var auditKjYtQyStore=new KitchenSink.view.common.store.appTransStore("TZ_QY_BZ");
        //var auditKjYtIdStore=new KitchenSink.view.common.store.appTransStore("TZ_COM_YT_ID");
        // var auditKjlxStore=new KitchenSink.view.common.store.appTransStore("TZ_COM_BMB_LX");
        //alert(auditKjlxStore.data.items.length);

        // {
        //    auditKjYtQyStore:auditKjYtQyStore,
        //        auditKjYtIdStore:auditKjYtIdStore,
        //    auditKjlxStore:auditKjlxStore
        // }

        cmp = new ViewClass();
        //加载控件用途信息
        cmp.on('afterrender',function(panel){
            var panelgrid = panel.child('tabpanel').down('grid[name=kjYtGrid]');
            var tzStoreParams = '{"kjID":"NEXT","queryType":"YT"}';
            panelgrid.store.tzStoreParams = tzStoreParams;
            panelgrid.store.load();
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editKj:function(obj,rowIndex){
        var kjID;
        if(obj.name=="toolbarEdit"){
            var selList = this.getView().getSelectionModel().getSelection();
            var checkLen = selList.length;
            if(checkLen == 0){
                Ext.MessageBox.alert("提示","请选择一条要修改的记录");
                return;
            }else if(checkLen >1){
                Ext.MessageBox.alert("提示","只能选择一条要修改的记录");
                return;
            };
            kjID = selList[0].get("kjID");
        }else{
            var store = obj.findParentByType("grid").store;
            var selRec = store.getAt(rowIndex);
            kjID = selRec.get("kjID");
        }

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_KJGL_COM"]["TZ_KJGL_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_KJGL_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
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
            form.findField("kjID").setReadOnly(true);
            form.findField("kjID").addCls("lanage_1");
            //var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJGL_INFO_STD","OperateType":"QF","comParams":{"kjID":"'+kjID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
            });
            //加载控件用途信息
            var panelgrid = panel.child('tabpanel').down('grid[name=kjYtGrid]');
            //var tzStoreParams = '{"kjID":"'+kjID+'"}';
            var tzStoreParams = '{"kjID":"'+kjID+'","queryType":"YT"}';
            panelgrid.store.tzStoreParams = tzStoreParams;
            panelgrid.store.load();
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteKj:function(obj,rowIndex){
        //选中行
        var store = obj.findParentByType('grid').getStore();
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            var selList = obj.findParentByType('grid').getSelectionModel().getSelection();
            //选中行长度
            var checkLen = selList.length;
            if(checkLen == 0){
                Ext.Msg.alert("提示","请选择要删除的记录");
                return;
            }else{
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        store.remove(selList);
                    }
                },this);
            }
            }

        },
    deleteKjJygz:function(obj,rowIndex){
        //选中行
       // var form = tabPanel.ownerCt.child('form').getForm();
       // var store = obj.findParentByType('grid').getStore();
       // var kjJygzStore =  tabPanel.down('grid[name=kjJygzGrid]').store;
        //var grid = this.getView().child("grid");
        //var grid = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').store;;

        var store = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').store;
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            var selList = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').getSelectionModel().getSelection();
            //选中行长度
            var checkLen = selList.length;
            if(checkLen == 0){
                Ext.Msg.alert("提示","请选择要删除的记录");
                return;
            }else{
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        store.remove(selList);
                    }
                },this);
            }
        }

    },
    addKjJygz: function(btn) {
        var formkj = this.getView().child("form").getForm();
        var kjID = formkj.getValues()['kjID'];
        var kjName = formkj.getValues()['kjName'];
        if (formkj.isValid()) {
            // Ext.tzSetCompResourses("TZ_KJGL_COM");
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_KJGL_COM"]["TZ_KJJYGZ_INFO_STD"];
            if (pageResSet == "" || pageResSet == undefined) {
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            var className = pageResSet["jsClassName"];
            if (className == "" || className == undefined) {
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_KJJYGZ_INFO_STD，请检查配置。');
                return;
            }
            //var _bj_id=this.getView().class_id;
            //var _lc_id=this.getView().bmlc_id;
            var win = this.lookupReference('kjJygzWindow');
            if (!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);
                win = new ViewClass();
                var form = win.child('form').getForm();
                form.findField('kjID').setValue(kjID);
                form.findField('kjName').setValue(kjName);
                this.getView().add(win);
            }
            win.show();
        }
    },
    editKjJygz: function() {
        //选中行
        //var store = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').store;
        //var selList = this.getView().getSelectionModel().getSelection();
        var selList = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        var kjID = selList[0].get("kjID");
        var kjJygzID = selList[0].get("kjJygzID");
       this.editKjJygzByID(kjID,kjJygzID);
    },
    currKjJygzEdit: function(view,rowIndex) {
        var store = this.getView().child("tabpanel").down('grid[name=kjJygzGrid]').store;
        var selRec = store.getAt(rowIndex);
        //ID
        var kjID = selRec.get("kjID");
        var kjJygzID = selRec.get("kjJygzID");
        this.editKjJygzByID(kjID,kjJygzID);
    },
    editKjJygzByID:function(kjID,kjJygzID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_KJGL_COM"]["TZ_KJJYGZ_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_KJJYGZ_INFO_STD，请检查配置。');
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
        var win = this.lookupReference('kjJygzWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJJYGZ_INFO_STD","OperateType":"QF","comParams":{"kjID":"'+kjID+'","kjJygzID":"'+kjJygzID+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();

    },
    onKjInfoClose:function(){
        this.getView().close();
    },
    onKjInfoSave:function(btn){
        var form = this.getView().child("form").getForm();
        var tabpanel = this.getView().child("tabpanel");
        if (form.isValid()) {
            var tzParams = this.getKjInfoParams(btn);
            if(tzParams!=""){
                var comView = this.getView();
                Ext.tzSubmit(tzParams,function(responseData){
                    comView.actType = "update";
                    //控件用途
                    var kjYtStore = tabpanel.down('grid[name=kjYtGrid]').getStore();
                    if(kjYtStore.isLoaded()){
                        form.findField("kjID").setReadOnly(true);
                        var tzStoreParams = '{"kjID":"'+form.getValues()['kjID']+'","queryType":"YT"}';
                        kjYtStore.tzStoreParams = tzStoreParams;
                        kjYtStore.reload();
                    }
                    //控件校验规则
                    var kjJygzStore = tabpanel.down('grid[name=kjJygzGrid]').getStore();
                    if(kjJygzStore.isLoaded()){
                        form.findField("kjID").setReadOnly(true);
                        //tzStoreParams = '{"kjID":"'+kjID+'","queryType":"JYGZ"}';
                        var tzStoreParams = '{"kjID":"'+form.getValues()['kjID']+'","queryType":"JYGZ"}';
                        kjJygzStore.tzStoreParams = tzStoreParams;
                       kjJygzStore.reload();
                    }
                },"",true,this);
            }
        }
    },
    onKjInfoEnsure:function(btn){
        var form = this.getView().child("form").getForm();
        var tabpanel = this.getView().child("tabpanel");
        if (form.isValid()) {
            var tzParams = this.getKjInfoParams(btn);
            if(tzParams!=""){
                var comView = this.getView();
                Ext.tzSubmit(tzParams,function(responseData){
                    //关闭窗口
                    comView.close();
                },"",true,this);
            }
        }
    },
    getKjInfoParams: function(btn){
        //主要表单
        var form = this.getView().child('form').getForm();
        var kjid = form.findField('kjID').getValue();
        //用途信息
        var tabpanel = this.getView().child("tabpanel");
        //表单数据
        var formParams = form.getValues();

        //更新操作参数
        var comParams = "";

        //修改json字符串
        var  editJson = '{"typeFlag":"KJ","data":'+Ext.JSON.encode(formParams)+'}';

        /*用途列表修改数据*/
        var ytGrid = tabpanel.down('grid[name=kjYtGrid]');
        var ytStore = ytGrid.getStore();
        //var ytGridModifiedRecs = ytStore.getModifiedRecords();
        //取所有
        var ytGridModifiedRecs = ytStore.getRange();
        for(var i=0;i<ytGridModifiedRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"YT","kjID":"'+kjid+'","data":'+Ext.JSON.encode(ytGridModifiedRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"YT","kjID":"'+kjid+'","data":'+Ext.JSON.encode(ytGridModifiedRecs[i].data)+'}';
            }
        }
        //控件校验规则
        var jygzGrid = tabpanel.down('grid[name=kjJygzGrid]');
        var jygzStore = jygzGrid.getStore();
        var jygzGridModifiedRecs = jygzStore.getModifiedRecords();
        for(var i=0;i<jygzGridModifiedRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"KJJYGZ","kjID":"'+kjid+'","data":'+Ext.JSON.encode(jygzGridModifiedRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"KJJYGZ","kjID":"'+kjid+'","data":'+Ext.JSON.encode(jygzGridModifiedRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = jygzStore.getRemovedRecords();
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
        var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJGL_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
	/*控件管理页面"保存"操作*/
    onKjSave:function(btn){
        var grid = btn.findParentByType("grid");
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
		var comParams="";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
			var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJGL_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(){
				store.reload();
			},"",true,this);
		}else{
			return;
		}
    },
	/*控件管理页面"确认"操作*/
    onKjEnsure:function(btn){
        var grid = btn.findParentByType("grid");
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

		var comParams="";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
			var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJGL_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';

			Ext.tzSubmit(tzParams,function(){
			grid.close();
			},"",true,this);
		}else{
			grid.close();
		}
    }
});

