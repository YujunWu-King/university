Ext.define('KitchenSink.view.template.proTemplate.bmlcmbMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.proTemplate',
    createBmlcInfoClass: function(){
        var cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;
        className = 'KitchenSink.view.template.proTemplate.bmlcmbInfoPanel';
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

        return cmp;
    },
    onProFormSave: function(){


        var grid = this.getView().child("grid");
        var store = grid.getStore();
        var actType = this.getView().actType;
        //记录查重
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');
        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var tagLength=mfRecs[i].get("TZ_APPPRO_NAME").length;
            if(tagLength>30){
                Ext.MessageBox.alert( Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.lcmcbndy30gzf","流程名称不能大于30个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }else{
            }

            //记录查重
            var TZ_APPPRO_NAME = mfRecs[i].get("TZ_APPPRO_NAME");
            var tagNameCount =0;
            var recIndex = store.findBy(function(record,id){
                if(record.get("TZ_APPPRO_NAME")==TZ_APPPRO_NAME){
                    tagNameCount++;
                    if(tagNameCount>1){
                        return true;
                    }
                }
            },0);

            if(tagNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.lcmccxcf","流程名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }
        }
        var form = this.getView().child("form").getForm();

        if (form.isValid()) {

            var tzParams = this.getOrgInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                form.setValues(responseData.formData);
                var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+form.findField("TZ_APPPRO_TMP_ID").getValue()+'"}';
                store.tzStoreParams = tzStoreParams;
                store.reload();
                comView.actType = "update";
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=protmpDtlist]");
                interviewMgrPanel[0].getStore().reload();


            },"",true,this);

        }

    },
    onFormEnsure: function(){
        //机构信息表单
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        var actType = this.getView().actType;

        var form = this.getView().child("form").getForm();

        if (form.isValid()) {

            var tzParams = this.getOrgInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                form.setValues(responseData.formData);
                var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+form.findField("TZ_APPPRO_TMP_ID").getValue()+'"}';
                store.tzStoreParams = tzStoreParams;
                store.reload();
                comView.actType = "update";

                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=protmpDtlist]");
                interviewMgrPanel[0].getStore().reload();
                comView.close();
            },"",true,this);

        }
    },
    onFormClose: function(){
        this.getView().close();
    },
    //常用回复短语添加一行

    editDataInfo4:function(){

        //选中行
        var backmsgGrid = this.lookupReference('protmpDetSetGrid');
        var selList = backmsgGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        //回复语模板信息ID
        var TZ_APPPRO_TMP_ID= selList[0].get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID= selList[0].get("TZ_APPPRO_ID");


        //this.editBackMsgDataInfoByID(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID,TZ_APPPRO_HF_BH);
        this.bmlcbackmsgeditByID(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID);

    },

    //批量删除报名步骤
    deleteDataInfo3: function(){

        //选中行
        var applyItemOptionsGrid = this.lookupReference('protmpDetSetGrid');
        var selList = applyItemOptionsGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzyscdjl","请选择要删除的记录"));
            return;
        }else{
            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.confirm","确认"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qryscsxjlm","确认要删除所选记录吗？"), function(btnId){
                if(btnId == 'yes'){
                    var store = applyItemOptionsGrid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    //递增资料，添加最后一行
    addDataInfo3: function(){

        if(this.getView().actType == "add"){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcbmlcmbxx","请先保存报名流程模板信息"));
            return;
        }

        var profeGrid = this.lookupReference('protmpDetSetGrid');
        var cellEditing = profeGrid.getPlugin('dataCellediting');
        var profeStore = profeGrid.getStore();
        var rowCount = profeStore.getCount();

        var comRegParams = this.getView().child("form").getForm().getValues();
        var smtDtTmpIDVal = comRegParams["TZ_APPPRO_TMP_ID"];
        var model = new KitchenSink.view.template.proTemplate.bmlcmbdetailModel({
            TZ_APPPRO_TMP_ID: smtDtTmpIDVal,
            TZ_APPPRO_ID: '',
            TZ_SORT_NUM: rowCount+1,
            TZ_APPPRO_NAME:''

        });
        profeStore.insert(rowCount, model);
        cellEditing.startEditByPosition({
            row: rowCount,
            column: 0
        });
    },

    getOrgInfoParams: function(){
        //报名流程信息表单
        var form = this.getView().child("form").getForm();
        //机构信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"ORG","data":'+Ext.JSON.encode(form.getValues())+'}';
        }

        //机构管理员信息列表
        var grid = this.getView().child("grid");
        //机构管理员信息数据
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"MEM","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"MEM","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMB_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    addUserAccount: function() {
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcjgxx","请先保存机构信息"));
            return;
        }

        var win = this.lookupReference('pageUserWindow');

        if (!win) {
            className = 'KitchenSink.view.orgmgmt.pageUserWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var grid = win.child('grid');
        //参数
        var tzParams = '{"ComID":"TZ_AQ_YHZHGL_COM","PageID":"TZ_AQ_YHZHGL_STD","OperateType":"QG","comParams":{}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){

            //用户信息列表数据
            var roleList = responseData.listData;

            var tzStoreParams = '{}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });

        win.show();
    },
    deleteUserAccount: function(btn){
        //选中行
        var pannel = btn.findParentByType("panel");
        //var form = pannel.child("form").getForm();
        var form =  this.getView().child("form").getForm();
        //var grid = pannel.child("grid");
        var grid = this.getView().child("grid");
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzyscdjl","请选择要删除的记录"));
            return;
        }else{

            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.confirm","确认"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qryscsxjlm","确认要删除所选记录吗？"), function(btnId){
                if(btnId == 'yes'){
                    //参数
                    //var form =  this.getView().child("form").getForm();

                    var formParams = form.getValues();
                    var orgId = formParams["orgId"];

                    var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"U","comParams":{"delete":[';
                    var org_params = "";
                    for(i=0;i<checkLen;i++){
                        if(i == 0){
                            org_params = '{"orgId":"' + orgId + '","usAccNum":"'+selList[i].get("usAccNum")+'"}';
                        }else{
                            org_params = org_params + ',{"orgId":"' + orgId + '","usAccNum":"'+selList[i].get("usAccNum")+'"}';
                        }
                    }
                    tzParams = tzParams + org_params + "]}}";
                    //alert(tzParams);

                    var store = grid.store;
                    store.remove(selList);
                    //删除服务器端数据
                    /*Ext.tzLoad(tzParams,function(responseData){

                     });*/
                }
            },this);
        }
    },
    addOrgAccount: function() {
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        //Ext.Msg.alert("提示","ZGW");
        //新建机构信息类
        var cmp = this.createBmlcInfoClass();
        //操作标志
        cmp.actType = "add";

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },

    editSelbackmsg: function(view, rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //组件ID
        var TZ_APPPRO_TMP_ID = selRec.get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID = selRec.get("TZ_APPPRO_ID");
        //显示组件注册信息编辑页面
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        className = 'KitchenSink.view.template.proTemplate.backMsgProWin';
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
        cmp.TZ_APPPR_O_TMP_ID=TZ_APPPRO_TMP_ID;
        cmp.TZ_APPPRO_ID=TZ_APPPRO_ID;
        /*
         cmp.on('afterrender',function(panel){
         //组件注册表单信息;
         //var form = panel.child('form').getForm();
         //form.findField("smtDtTmpID").setReadOnly(true);
         //页面注册信息列表
         var grid = panel.child('grid');
         //参数
         // var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMBGL_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+smtDtTmpID+'"}}';
         var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}}';
         //加载数据
         Ext.tzLoad(tzParams,function(responseData){
         //组件注册信息数据
         //var formData = responseData.formData;
         //form.setValues(formData);
         //页面注册信息列表数据

         var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';
         grid.store.tzStoreParams = tzStoreParams;
         grid.store.load();
         });

         });
         */
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },

    editProTmp:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        //模板ID
        var protmId= selList[0].get("TZ_APPPRO_TMP_ID");

        //显示活动信息编辑页面
        this.editSmtDtTmpByID(protmId);
    },
    editSelSmtDtTmp: function(view, rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //组件ID
        var smtDtTmpID = selRec.get("TZ_APPPRO_TMP_ID");
        //显示组件注册信息编辑页面
        this.editSmtDtTmpByID(smtDtTmpID);

    },
    editSmtDtTmpByID: function(smtDtTmpID){
        // 是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PM_BMLCMB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        // className = 'KitchenSink.view.template.proTemplate.bmlcmbInfoPanel';
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
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            // form.findField("smtDtTmpID").setReadOnly(true);
            //页面注册信息列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMBGL_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+smtDtTmpID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //页面注册信息列表数据
                var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+smtDtTmpID+'"}';
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
    //赵广武添加，新增报名流程模板选择页面------开始
    addDataModelPro: function() {
        var me = this;
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_LCMBCHSE_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        //Ext.MessageBox.alert(className);

        //var className="KitchenSink.view.template.proTemplate.newBmlcmb";
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('myBmbRegWindow1');
        //var win= new KitchenSink.view.template.proTemplate.newBmlcmb();

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }else{
            var activeTab = win.items.items[0].getActiveTab();
            document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
        }

        win.show();
        
		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					this.style.backgroundColor = null
				});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			var activeTab = win.items.items[0].getActiveTab();

			var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
        
    },

    /*新增报名表模板页面，确定*/
    proonNewEnsure: function(btn) {
        //组件注册信息列表
        var grid = btn.findParentByType("proTemplate");
        //组件注册信息数据
        var store = grid.getStore();

        // var win = this.lookupReference('myBmbRegWindow1');
        var win=btn.findParentByType("window");
        var activeTab = win.items.items[0].getActiveTab(),
            id = '';
        var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
            tplId = "";
        if (activeTab.itemId == "predefine") {
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    if (this.style.backgroundColor == "rgb(173, 216, 230)") {
                        tplId = this.getAttribute("data-id");
                        return false;
                    }
                });
        } else {
            tplId = "";
        }


        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PM_BMLCMB_STD"];
        var className = pageResSet["jsClassName"];
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
        win.close();
        if (tplId&&tplName){
            cmp = new ViewClass();
            cmp.actType = "update";
            cmp.on('afterrender',function(panel){
                //var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMB_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+tplId+'"}';

                var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMB_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+tplId+'","TZ_APPPRO_TMP_NAME":"'+tplName+'","addOne":"new"}}';
                //加载数据
                Ext.tzLoad(tzParams,function(responseData){
                    //组件注册信息数据
                    var form = cmp.child('form').getForm();
                    var formData = responseData.formData;
                    form.setValues(formData);
                    //页面注册信息列表数据
                    var roleList = responseData.listData;
                    var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+tplId+'","TZ_APPPRO_TMP_NAME":"'+tplName+'","addOne":"new"}';
                    var grid_1 = cmp.child('grid');
                    grid_1.store.tzStoreParams = tzStoreParams;
                    grid_1.store.load();
                });
            });
            tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
        }else if (tplName) {
            cmp = new ViewClass();
            cmp.actType = "add";
            var form = cmp.child('form').getForm();
            form.findField('TZ_APPPRO_TMP_NAME').setValue(tplName);
            tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
        }
    },
    onNewClose: function(btn) {
        var win = btn.findParentByType("window");
        win.close();
    },
    //赵广武添加，新增报名流程模版选择页面------结束
    addProDataInfo: function(btn){
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存模板信息后，再新增报名流程。");
            return;
        }
        //是否有访问权限
        /*	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTXX_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
         return;
         }
         //该功能对应的JS类
         var className = pageResSet["jsClassName"];*/
        var className="KitchenSink.view.template.proTemplate.protmDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win ;

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //组件注册信息
        var comRegParams = this.getView().child("form").getForm().getValues();
        //组件IDTZ_APPPRO_TMP_ID
        var TZ_APPPRO_TMP_ID = comRegParams["TZ_APPPRO_TMP_ID"];
        //页面注册信息表单
        var form = win.child("form").getForm();

        var formParams = form.getValues();
        formParams["TZ_APPPRO_TMP_ID"] = TZ_APPPRO_TMP_ID;
        //formParams["TZ_APPPRO_ID"] = TZ_APPPRO_ID;
        form.setValues(formParams);
        form.reset();

        win.show();
    },
    deleteDataInfo: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.confirm","确认"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    onDataInfoSave: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.saveDataInfo(win);
        }
    },
    onDataInfoEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.saveDataInfo(win);
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        }
    },

    onDataInfoClose: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        form.reset();
        win.close();
    },
    saveDataInfo: function(win){
        //页面注册信息表单
        var form = win.child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        var TZ_APPPRO_TMP_ID=formParams["TZ_APPPRO_TMP_ID"];
        //提交参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BMLCDTXX_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
        var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'"}';
        //var pageGrid = this.getView().child("grid");
        var pageGrid = this.lookupReference('protmpDetSetGrid');
        Ext.tzSubmit(tzParams,function(responseData){
            win.actType = "update";
            //form.findField("smtDtID").setReadOnly(true);
            form.setValues(responseData.formData);
            pageGrid.store.tzStoreParams = tzStoreParams;
            pageGrid.store.reload();
        },"",true,this);
    },
    addData: function(view, rowIndex){
        var comRegParams = this.getView().child("form").getForm().getValues();
        var smtDtTmpIDVal = comRegParams["TZ_APPPRO_TMP_ID"];
        var rec = new KitchenSink.view.template.proTemplate.bmlcmbdetailModel({
            TZ_APPPRO_TMP_ID: smtDtTmpIDVal,
            TZ_APPPRO_ID: '',
            TZ_SORT_NUM: rowIndex+2,
            TZ_APPPRO_NAME:''
        });
        var grid = this.lookupReference('protmpDetSetGrid');
        var cellEditing = grid.getPlugin('dataCellediting');

        view.getStore().insert(rowIndex+1, rec);
        cellEditing.startEditByPosition({
            row: rowIndex+1,
            column: 0
        });

        //重新赋值显示顺序
        var rowCount = grid.getStore().getCount( );
        var records = grid.getStore().getRange(rowIndex+2, rowCount);
        var num=rowIndex+2;

        for (var i = 0; i < records.length; i++) {
            var record = records[i];
            record.set('order',num+1);
            num+=1;
        }

    },
    editDataInfo: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var smtDtTmpID = selRec.get("TZ_APPPRO_TMP_ID");
        var smtDtID = selRec.get("TZ_APPPRO_ID");
        //Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_BMLCDTXX_STD，请检查配置。'+smtDtTmpID+'==='+smtDtID);
        //return;

        //是否有访问权限
        /*  var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PM_BMLCMBGL_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
         return;
         }
         //该功能对应的JS类
         var className = pageResSet["jsClassName"];*/
        var className="KitchenSink.view.template.proTemplate.protmDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        if(smtDtID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcbmlcxx","请先保存报名流程信息"));
            return;
        }
        var win = this.lookupReference('bmlcmbInfoWindow');

        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        //参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BMLCDTXX_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+smtDtTmpID+'","TZ_APPPRO_ID":"'+smtDtID+'"}}';
        //页面注册信息表单
        var form = win.child("form").getForm();

        Ext.tzLoad(tzParams,function(responseData){
            form.findField("TZ_APPPRO_TMP_ID").setReadOnly(true);
            form.findField("TZ_APPPRO_ID").setReadOnly(true);
            form.setValues(responseData);
            //store.reload();
        });

        win.show();

    },
    editBackMsgDataInfoByID:function(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID,TZ_APPPRO_HF_BH){

        var TZ_APPPRO_TMP_ID = TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = TZ_APPPRO_ID;
        var TZ_APPPRO_HF_BH=TZ_APPPRO_HF_BH;

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_BACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('protmBackMsgWin');
        //alert(className);
        if (!win) {
            className = 'KitchenSink.view.template.proTemplate.protmBackMsgWin';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        //操作类型设置为更新
        win.actType = "update";
        //参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PROBACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'","TZ_APPPRO_HF_BH":"'+TZ_APPPRO_HF_BH+'"}}';
        //页面注册信息表单
        var form = win.child("form").getForm();

        Ext.tzLoad(tzParams,function(responseData){
            form.findField("TZ_APPPRO_TMP_ID").setReadOnly(true);
            form.findField("TZ_APPPRO_ID").setReadOnly(true);
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();

    },

    editBackMsgDataInfo: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);

        var TZ_APPPRO_TMP_ID = selRec.get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID = selRec.get("TZ_APPPRO_ID");
        var TZ_APPPRO_HF_BH=selRec.get("TZ_APPPRO_HF_BH");

        this.editBackMsgDataInfoByID(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID,TZ_APPPRO_HF_BH);

    },
    editDataInfo3: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var rec = grid.getStore().getAt(rowIndex);
        var TZ_APPPRO_TMP_ID = selRec.get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID = selRec.get("TZ_APPPRO_ID");
        var className="KitchenSink.view.template.proTemplate.backMsgWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        if(TZ_APPPRO_ID==""){
            Ext.MessageBox.alert("提示","请先保存报名流程信息，在设置其默认信息！");
            return;
        }
        var win = this.lookupReference('bmlcmbbackMsgWindow');

        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        //参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}}';
        //页面注册信息表单
        var form = win.child("grid");

        Ext.tzLoad(tzParams,function(responseData){
            var roleList = responseData.listData;
            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+win.TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+win.TZ_APPPRO_ID+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();

        });

        win.show();

    },
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
				form.findField("TZ_SYSVAR").setValue(selection[0].data.TZ_SYSVARID);
				form.findField("TZ_SYSVAR_NAME").setValue(selection[0].data.TZ_SYSVARNAME);
				
			}
		});	
    },
    onProBackMsgSave: function(btn){
        //获取窗口
        // var win= this.lookupReference('protmBackMsgWin');
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.saveBackMsgInfo(win);
        }
    },
    onProBackMsgEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.saveBackMsgInfo(win);
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        }
    },
    onProBackMsgClose: function(btn){
        var win = btn.findParentByType("window");
        win.close();
    },
    saveBackMsgInfo: function(win){
        //页面注册信息表单
        var form = win.child("form").getForm();

        var actType = win.actType;
        //表单数据
        var formParams = form.getValues();
        //是否默认
        if(formParams["TZ_WFB_DEFALT_BZ"] == undefined){
            formParams["TZ_WFB_DEFALT_BZ"] = "N";
        }
        var TZ_APPPRO_TMP_ID=formParams["TZ_APPPRO_TMP_ID"];
        var TZ_APPPRO_ID=formParams["TZ_APPPRO_ID"];
        //alert(TZ_APPPRO_TMP_ID);
        //提交参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PROBACKMSG_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
        //var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+formParams["TZ_APPPRO_TMP_ID"]+',"TZ_APPPRO_ID":"'+formParams["TZ_APPPRO_ID"]+'"}';
        var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';

        var pageGrid = this.lookupReference('probackMsgGrid');
        Ext.tzSubmit(tzParams,function(responseData){
			form.setValues(responseData.formData);
            win.actType = "update";
            //form.findField("TZ_APPPRO_ID").setReadOnly(true);
            pageGrid.store.tzStoreParams = tzStoreParams;
            pageGrid.store.reload();
        },"",true,this);
    },


    onBackMsgSure:function(btn){

        var win = this.getView();
        var TZ_APPPRO_TMP_ID = win.TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = win.TZ_APPPRO_ID;

        var grid = win.child("grid");
        var store = grid.getStore();
        var tzParams = this.getBackMsgParams(btn);
        var comView = this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';
            store.tzStoreParams = tzStoreParams;
            comView.actType = "update";
            store.reload();

        },"",true,this);
        comView.close();
    },

    onBackMsgSave: function(btn){
        //var win = this.lookupReference('bmlcmbbackMsgWindow');
        var win = this.getView();
        var TZ_APPPRO_TMP_ID = win.TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = win.TZ_APPPRO_ID;

        var grid = win.child("grid");
        var store = grid.getStore();

        var tzParams = this.getBackMsgParams(btn);
        var comView = this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';
            store.tzStoreParams = tzStoreParams;
            comView.actType = "update";
            store.reload();
        },"",true,this);
    },
    onBackMsgClose: function(btn){
        // var win = btn.findParentByType('window');
        // win.close();
        this.getView().close();
    },
    getBackMsgParams: function(btn){
        //var win = btn.findParentByType('window');
        var win = this.getView();
        var TZ_APPPRO_TMP_ID = win.TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = win.TZ_APPPRO_ID;
        var rowNum = win.rowNum;
        var grid = win.child('grid');
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams = "";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PROBACKMSG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //alert(tzParams);
        return tzParams;
    },
    deleteBackMsg: function(grid, rowIndex){
        var store = grid.getStore();
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.confirm","确认"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
            if(btnId == 'yes'){
                store.removeAt(rowIndex);
            }
        },this);
    },

    bmlcbackmsgeditByID2:function(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID){
        var TZ_APPPRO_TMP_ID = TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = TZ_APPPRO_ID;

        if(TZ_APPPRO_ID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcbmlcxx","请先保存报名流程信息"));
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_BACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        // className = 'KitchenSink.view.template.proTemplate.bmlcmbInfoPanel';
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
        cmp.TZ_APPPRO_TMP_ID=TZ_APPPRO_TMP_ID;
        cmp.TZ_APPPRO_ID=TZ_APPPRO_ID;
        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID ":"'+TZ_APPPRO_ID +'"}}';

            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';
            var grid=panel.child("grid");
            grid.store.tzStoreParams = tzStoreParams;
            //grid.store.tzStoreParams=
            grid.store.load();
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    bmlcbackmsgeditByID:function(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID){

        var TZ_APPPRO_TMP_ID = TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID = TZ_APPPRO_ID;

        if(TZ_APPPRO_ID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcbmlcxx","请先保存报名流程信息"));
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_BACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        var className ="KitchenSink.view.template.proTemplate.backMsgProWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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

        cmp.TZ_APPPRO_TMP_ID=TZ_APPPRO_TMP_ID;
        cmp.TZ_APPPRO_ID=TZ_APPPRO_ID;
        //alert(cmp.TZ_APPPRO_TMP_ID);
        //alert(cmp);
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID ":"'+TZ_APPPRO_ID +'"}}';

            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+TZ_APPPRO_ID+'"}';
            var grid=panel.child("grid");
            grid.store.tzStoreParams = tzStoreParams;
            //grid.store.tzStoreParams=
            grid.store.load();
        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }

    },
    /*设置回复短语信息*/
    bmlcbackmsgedit:function(view, rowIndex){
        //Ext.tzSetCompResourses("TZ_PM_BMLCMBGL_COM");
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //组件ID
        var TZ_APPPRO_TMP_ID = selRec.get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID = selRec.get("TZ_APPPRO_ID");
        this.bmlcbackmsgeditByID(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID);
    },
    /*编辑回复语模板设置信息*/
    editDatabBackInfo:function(){
        //选中行
        var backmsgGrid = this.lookupReference('probackMsgGrid');
        var selList = backmsgGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        //回复语模板信息ID
        var TZ_APPPRO_TMP_ID= selList[0].get("TZ_APPPRO_TMP_ID");
        var TZ_APPPRO_ID= selList[0].get("TZ_APPPRO_ID");
        var TZ_APPPRO_HF_BH= selList[0].get("TZ_APPPRO_HF_BH");

        this.editBackMsgDataInfoByID(TZ_APPPRO_TMP_ID,TZ_APPPRO_ID,TZ_APPPRO_HF_BH);

    },
    deleteDatabBackInfo:function(){
        //选中行
        var applyItemOptionsGrid = this.lookupReference('probackMsgGrid');
        var selList = applyItemOptionsGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzyscdjl","请选择要删除的记录"));
            return;
        }else{
            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.confirm","确认"),  Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qryscsxjlm","确认要删除所选记录吗？"), function(btnId){
                if(btnId == 'yes'){
                    var store = applyItemOptionsGrid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    addDatabBackInfo: function(btn){
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcymxx","请先保存页面信息"));
            return;
        }

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PROBACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //var className="KitchenSink.view.template.proTemplate.protmDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('protmBackMsgWin');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //组件注册信息
        // var comRegParams = this.getView().child("form").getForm().getValues();
        //组件IDsmtDtTmpID
        // var TZ_APPPRO_TMP_ID = comRegParams["TZ_APPPRO_TMP_ID"];

        var win11 = this.getView();
        var TZ_APPPRO_TMP_ID= win11.TZ_APPPRO_TMP_ID;
        var TZ_APPPRO_ID=win11.TZ_APPPRO_ID;
        //页面注册信息表单
        var form = win.child("form").getForm();
        //form.reset();

        var formParams = form.getValues();
        formParams["TZ_APPPRO_TMP_ID"] = TZ_APPPRO_TMP_ID;
        formParams["TZ_APPPRO_ID"] = TZ_APPPRO_ID;
        form.setValues(formParams);
        win.show();
    },
    editDataInfo2:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_BACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('bmlcmbbackMsgWindow');

        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            //win=new KitchenSink.view.enrollProject.submitDtMdlMg.backMsgWin();
            this.getView().add(win);
        }

        win.TZ_APPPRO_TMP_ID = rec.get('TZ_APPPRO_TMP_ID');
        win.TZ_APPPRO_ID = rec.get('TZ_APPPRO_ID');


        if(win.TZ_APPPRO_ID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcdjzlxx","请先保存递交资料信息"));
            return;
        }

        //操作类型设置为更新
        win.actType = "update";
        var grid = win.child('grid');
        // console.log(grid);
        //参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BACKMSG_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+win.TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+win.TZ_APPPRO_ID+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var roleList = responseData.listData;
            var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+win.TZ_APPPRO_TMP_ID+'","TZ_APPPRO_ID":"'+win.TZ_APPPRO_ID+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });

        win.show();

    },

    onUserFormEnsure: function(btn){
        var win = btn.findParentByType("window");
        var grid = win.child('grid');
        var selList = grid.getSelectionModel().getSelection();

        //alert(form + " / " + this.getView().child("form") + " / " + this.getView().child("grid") + " / " + btn.findParentByType("form") + "---" + formParams + "----" + orgId);

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"), Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxzyh","请选择用户"));
            return;
        }else{
            var win_bd = win.findParentByType("panel");
            var form = win_bd.child('form').getForm();
            var formParams = form.getValues();
            var orgId = formParams["orgId"];
            var user_params;
            for(var i=0;i<checkLen;i++){
                if(i == 0){
                    user_params = '{"orgId":"'+orgId+'","usAccNum":"'+selList[i].get("usAccNum")+'"}';
                }else{
                    user_params = user_params + ',{"orgId":"'+orgId+'","usAccNum":"'+selList[i].get("usAccNum")+'"}';
                }
            }
            var memParams = '"add":[';
            memParams = memParams + user_params + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGMEM_STD","OperateType":"U","comParams":{'+memParams+'}}';
            //alert(tzParams);
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                var memgrid =  win_bd.child("grid");
                var tzStoreParams = '{"orgId":"'+orgId+'"}';
                memgrid.store.tzStoreParams = tzStoreParams;
                memgrid.store.load();
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    onUserFormClose: function(){
        this.getView().close();
    },

    /*以下代码为李丹丹添加*/
    //报名流程模板 可配置搜索
    queryProTmp:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMBGL_STD.TZ_APPPRO_TMP_V',
            condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //表格内部的删除操作
    deleteSelSmtDtTmp: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.confirm","确认"), Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
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
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMBGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
            var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMBGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
                grid.close();
            },"",true,this);
        }else{
            grid.close();
        }
    },
    //关闭按钮
    onPanelClose:function(btn){
        var grid=btn.up('grid');
        grid.close();
    },
    //编辑
    editDataInfo5:function(btn) {
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.MessageBox.alert("提示", "请选择一条要修改的记录");
            return;
        } else if (checkLen > 1) {
            Ext.MessageBox.alert("提示", "只能选择一条要修改的记录");
            return;
        }
        var smtDtTmpID = selList[0].get("TZ_APPPRO_TMP_ID");
        var smtDtID=selList[0].get("TZ_APPPRO_ID");
        var className="KitchenSink.view.template.proTemplate.protmDataWin";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        if(smtDtID==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.prompt","提示"),Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.qxbcbmlcxx","请先保存报名流程信息"));
            return;
        }
        var win = this.lookupReference('bmlcmbInfoWindow');

        if (!win) {
            //className = 'KitchenSink.view.security.com.pageRegWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        //参数
        var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_GD_BMLCDTXX_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+smtDtTmpID+'","TZ_APPPRO_ID":"'+smtDtID+'"}}';
        //页面注册信息表单
        var form = win.child("form").getForm();

        Ext.tzLoad(tzParams,function(responseData){
            form.findField("TZ_APPPRO_TMP_ID").setReadOnly(true);
            form.findField("TZ_APPPRO_ID").setReadOnly(true);
            form.setValues(responseData);

        });

        win.show();

    }
});

