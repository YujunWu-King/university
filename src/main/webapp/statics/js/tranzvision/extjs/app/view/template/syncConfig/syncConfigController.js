Ext.define('KitchenSink.view.template.syncConfig.syncConfigController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.syncConfigController',

    //新增同步配置
    addNewSyncConfig: function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_APPSYNC_COM"]["TZ_SYNCCONFIG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.prompt","提示"), Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.prompt","提示"),Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.syncConfig.syncConfigInfoPanel';
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
        cmp.actType="add";
        var proGrid = btn.findParentByType('grid');
        cmp.proGrid = proGrid;

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            form.findField('TZ_APPSYNC_ID').setValue('NEXT');
        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },


    //保存修改
    onSaveData: function(btn){
        var grid = this.getView();
        var isValid = true;
        var nameRepeat = false;

        var modifRecs = grid.store.getModifiedRecords();
        for (var i=0; i<modifRecs.length; i++){
            //分类名称不能为空
            if (modifRecs[i].data.proTypeName == ""){
                isValid = false;
                break;
            }else {
                var loop = 0, sNum = 0, rowNum = 0;
                //用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
                while (loop < 2){
                    if(grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) != -1){
                        rowNum = grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) + 1;
                        sNum++;//相同数据行数量加1
                    }
                    loop++;
                }
                if (sNum == 2){
                    nameRepeat = true;
                    break;
                }
            }
        }

        if (isValid){
            if (nameRepeat){
                Ext.Msg.alert(Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_SYNCCONFIG_STD.prompt","提示"),Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbncf","项目类型名称不能重复！"));
            } else {
                var tzParams = this.getProClassifyParams();
                Ext.tzSubmit(tzParams,function(respData){
                    grid.store.reload();
                },"",true,this);
            }
        } else {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_SYNCCONFIG_STD.prompt","提示"),Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbnwk","项目类型名称不能为空！"));
        }
    },



    //可配置搜索
    searchList: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_CONFIG',
            condition:
                {
                    //"TZ_JG_ID": Ext.tzOrgID  //设置搜索字段的默认值，没有可以不设置condition;
                },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //保存
    onSyncConfigSave: function(btn){
        var panel = btn.findParentByType("panel");
        var grid =panel.up('grid');

        var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=syncConfigInfoPanel]");

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
            var tzParams = '{"ComID":"TZ_APPSYNC_COM","PageID":"TZ_APPSYNC_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(responseData){
                if(btn.name=="ensure"){
                    panel.close();
                }else{
                    panel.actType = "update";
                    form.findField("TZ_APPSYNC_ID").setReadOnly(true);
                    form.findField("TZ_APPSYNC_ID").addCls('lanage_1');
                    if(responseData['TZ_APPSYNC_ID']){
                        form.setValues({TZ_APPSYNC_ID:responseData.TZ_APPSYNC_ID});

                    };
                    for(var i=0;i<msArrInfoPanelArr.length;i++){
                        msArrInfoPanelArr[i].store.load();
                    }
                }
            },"",true,this);
        }
    },

    onSyncConfigClose: function(btn){
        //关闭定义页面
        var proClose = btn.findParentByType("syncConfigInfoPanel");
        proClose.close();
    },
    onSyncConfigEnsure: function(btn){
        //确定按钮
        btn.name="ensure";
        this.onSyncConfigSave(btn);
        //this.onProjectClose(btn);
    },
    //编辑
    editSyncConfigDfn: function(btn) {
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
        var TZ_APPSYNC_ID = selList[0].get("TZ_APPSYNC_ID");
        this.editSyncConfigInfoByID(TZ_APPSYNC_ID);
    },
    editSyncConfigInfoByID: function(TZ_APPSYNC_ID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_APPSYNC_COM"]["TZ_SYNCCONFIG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SYNCCONFIG_STD，请检查配置。');
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
        //操作类型设置为更新
        cmp.actType = "update";
        cmp.on('afterrender',function(panel){

            var form = panel.child('form').getForm();

            var proTypeIdField = form.findField("TZ_APPSYNC_ID");
            proTypeIdField.setReadOnly(true);
            proTypeIdField.addCls('lanage_1');/*灰掉应用程序类ID输入框*/
            //参数
            var tzParams = '{"ComID":"TZ_APPSYNC_COM","PageID":"TZ_APPSYNC_STD","OperateType":"QF","comParams":{"TZ_APPSYNC_ID":"'+TZ_APPSYNC_ID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                form.setValues(responseData);

            });

        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //编辑
    onEditCurrRow: function(view, rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var TZ_APPSYNC_ID = selRec.get("TZ_APPSYNC_ID");
        this.editSyncConfigInfoByID(TZ_APPSYNC_ID);
    },
    //表格内部的删除操作
    deleteSelSmtDtTmp: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.confirm","确认"), Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //表格上方的删除（可以同时删除多条记录）
    deleteProTmp:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    //保存按钮
    onPanelSave:function(btn){
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //  console.log(comParams);
        var tzParams = '{"ComID":"TZ_APPSYNC_COM","PageID":"TZ_APPSYNC_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    //确定按钮
    onPanelEnsure:function(btn){

        var grid = btn.findParentByType("grid");
        var view=this.getView();
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
            var tzParams = '{"ComID":"TZ_APPSYNC_COM","PageID":"TZ_APPSYNC_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
                grid.close();
            },"",true,this);
        }else{
            grid.close();
        }
    }
});