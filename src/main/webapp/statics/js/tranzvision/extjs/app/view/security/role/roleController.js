Ext.define('KitchenSink.view.security.role.roleController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.roleMg',
    addRole: function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_ROLE_COM"]["TZ_ROLE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ROLE_INFO_STD，请检查配置。');
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

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editRole: function() {
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
        //角色名称
        var roleName = selList[0].get("roleName");
        //显示角色信息编辑页面
        this.editRoleInfoByName(roleName);
    },
    editCurrRole: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //角色名称
        var roleName = selRec.get("roleName");
        //显示角色信息编辑页面
        this.editRoleInfoByName(roleName);
    },
    editRoleInfoByName: function(roleName){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_ROLE_COM"]["TZ_ROLE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ROLE_INFO_STD，请检查配置。');
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
            //角色表单信息;
            var form = panel.child('form').getForm();
            form.findField("roleName").setReadOnly(true);
            form.findField("roleName").setFieldStyle('background:#F4F4F4');
            //许可权列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_ROLE_COM","PageID":"TZ_ROLE_INFO_STD","OperateType":"QF","comParams":{"roleName":"'+roleName+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //角色信息数据
                var formData = responseData.formData;
                form.setValues(formData);

                var tzStoreParams = '{"roleName":"'+roleName+'"}';
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
    deleteRoles: function(){
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
                    var roleStore = this.getView().store;
                    roleStore.remove(selList);
                }
            },this);
        }
    },
    deleteCurrRole: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveRoles: function(btn){
        //角色列表
        var grid = btn.findParentByType("grid");
        //角色信息数据
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
        var tzParams = '{"ComID":"TZ_AQ_ROLE_COM","PageID":"TZ_ROLE_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    onRoleInfoSave: function(btn){
        //角色信息表单
        var form = this.getView().child("form").getForm();

        var grid = this.getView().child("grid");
        //许可权列表数据
        var store = grid.getStore();

        if (form.isValid()) {
            //获取角色信息参数
            var tzParams = this.getRoleInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";

                if(tzParams.indexOf("PLST")>-1||tzParams.indexOf("delete")>-1){
                    var tzStoreParams = '{"roleName":"'+form.findField("roleName").getValue()+'"}';
                    store.tzStoreParams = tzStoreParams;
                    store.reload();
                }
                form.findField("roleName").setReadOnly(true);
            },"",true,this);
        }
    },
    onRoleInfoEnsure: function(btn){
        //角色信息表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取角色信息参数
            var tzParams = this.getRoleInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getRoleInfoParams: function(){
        //角色信息表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"ROLE","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"ROLE","data":'+Ext.JSON.encode(formParams)+'}';
        }

        //许可权列表
        var grid = this.getView().child("grid");
        //许可权列表数据
        var store = grid.getStore();
        //修改记录
        var gridEditJson = "";
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(gridEditJson == ""){
                gridEditJson = '{"typeFlag":"PLST","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                gridEditJson = gridEditJson + ',{"typeFlag":"PLST","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){

            if(gridEditJson != ""){
            editJson=editJson+','+gridEditJson;
            }
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                editJson=gridEditJson;
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
        var tzParams = '{"ComID":"TZ_AQ_ROLE_COM","PageID":"TZ_ROLE_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onRoleInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    addPermission:function(btn){
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存许可权信息后，再新增授权组件。");
            return;
        }
        var form = this.getView().child("form").getForm();
        var grid = this.getView().child("grid");
        var store = grid.store;
        Ext.tzShowPromptSearch({
            recname: 'PSCLASSDEFN',
            searchDesc: '选择许可权',
            maxRow:20,
            condition:{
                srhConFields:{
                    CLASSID:{
                        desc:'许可权编号',
                        operator:'07',
                        type:'01'
                    },
                    CLASSDEFNDESC:{
                        desc:'许可权描述',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                CLASSID: '许可权编号',
                CLASSDEFNDESC: '许可权描述'
            },
            multiselect: true,
            callback: function(selection){
                var roleName=form.findField("roleName").getValue();
                for(var i= 0;i<selection.length;i++){
                    var permID=selection[i].data.CLASSID;
                    var permDesc=selection[i].data.CLASSDEFNDESC;

                    var rolePlstModal = Ext.create('KitchenSink.view.security.role.rolePlstModel',{
                        roleName:roleName,
                        permID:permID,
                        permDesc:permDesc
                    });
                    if(store.findRecord("permID",selection[i].data.permID) == null){
                        store.add(rolePlstModal);
                    }
                }
            }
        });
    },
    editPermission: function(btn){
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要编辑的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要编辑的记录");
            return;
        }

        Ext.tzSetCompResourses("TZ_AQ_PLST_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_INFO_STD，请检查配置。');
            return;
        }
        //许可权编号
        var permID = selList[0].get("permID");

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
            //许可权表单信息;
            var form = panel.child('form').getForm();
            form.findField("permID").setReadOnly(true);
            form.findField("permID").setFieldStyle('background:#F4F4F4');
            //授权组件列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_INFO_STD","OperateType":"QF","comParams":{"permID":"'+permID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //角色信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                var tzStoreParams = '{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V","condition":{"CLASSID-operator": "01","CLASSID-value": "'+permID+'"}}';
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
    deletePermissions: function(btn){
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
    onAddPlstEnsure: function(btn){
        var win = btn.findParentByType("window");
        var grid = win.child('grid');
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择许可权");
            return;
        }else{
            var win_bd = win.findParentByType("panel");
            var form = win_bd.child('form').getForm();
            var formParams = form.getValues();
            var roleName = formParams["roleName"];
            var user_params;

            var plstgrid =  win_bd.child("grid");
            var plststore = plstgrid.store;
            //memstore.clearFilter(true);
            //var memmodal;

            for(var i=0;i<checkLen;i++){
                var plstmodal = Ext.create('KitchenSink.view.security.role.rolePlstModel',{
                    roleName:roleName,
                    permID:selList[i].get("permID"),
                    permDesc:selList[i].get("permDesc")
                });
                if(plststore.findRecord("permID",selList[i].get("permID")) == null){
                    plststore.add(plstmodal);
                }
            }

            var comView = this.getView();
            //关闭窗口
            comView.close();

        }
    },
    onAddPlstClose: function(){
        this.getView().close();
    },
    queryRole:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_AQ_ROLE_COM.TZ_ROLE_LIST_STD.PSROLEDEFN_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //确定
    ensureRoles:function(btn) {
        this.saveRoles(btn);
        this.closeRoles(btn);
    },
    //关闭
    closeRoles: function(btn){
        //关闭
        var grid = btn.findParentByType("grid");
        grid.close();
    },
    //roleInfoPanel.js 中grid 中的编辑
    editRoleInfoSave:  function(view,rowIndex) {

        Ext.tzSetCompResourses("TZ_AQ_PLST_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_INFO_STD，请检查配置。');
            return;
        }
        //许可权编号
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var permID = selRec.get("permID");

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
            //许可权表单信息;
            var form = panel.child('form').getForm();
            form.findField("permID").setReadOnly(true);
            form.findField("permID").setFieldStyle('background:#F4F4F4');
            //授权组件列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_INFO_STD","OperateType":"QF","comParams":{"permID":"'+permID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //角色信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                var tzStoreParams = '{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V","condition":{"CLASSID-operator": "01","CLASSID-value": "'+permID+'"}}';
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
    deleteRoleInfoSave: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    }
});

