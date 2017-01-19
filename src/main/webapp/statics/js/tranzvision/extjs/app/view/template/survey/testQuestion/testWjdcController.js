Ext.define('KitchenSink.view.template.survey.testQuestion.testWjdcController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.testWjdcController',

    findCsWjdc:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_CSWJ_VW',
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }, 
    //关闭
    CsWjdcClose:function(btn){
        btn.findParentByType("grid").close();
    },
    //新增测试问卷
    addCsWjdc:function(btn){ 
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CSWJ_LIST_COM"]["TZ_CSWJ_DETAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CSWJ_DETAIL_STD，请检查配置。');
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
        cmp.actType = "add";
        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            var csWjId=form.findField("TZ_CS_WJ_ID").getValue();
            if (csWjId.length==0){
                form.findField("TZ_CS_WJ_ID").setValue("NEXT");
            }
        });
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //选择问卷模板
    cswj_mbChoice:function(btn){
        var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        searchDesc="问卷模板";
        modal="TZ_APP_TPL_ID";
        modal_desc="TZ_APP_TPL_MC";
        // var form = btn.findParentByType('window').child("form").getForm();
        var form=this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'PS_TZ_DC_DY_T',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_EFFEXP_ZT:{
                        value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_APP_TPL_ID:{
                        desc:'问卷模板ID',
                        operator:'01',
                        type:'01'
                    },
                    TZ_APP_TPL_MC:{
                        desc:'问卷模板名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_APP_TPL_ID: '问卷模板ID',
                TZ_APP_TPL_MC: '问卷模板名称'
            },
            multiselect: false,
            callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_APP_TPL_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_APP_TPL_MC);
            }
        });
    },
    onCsWjdcSave:function(btn){
        var form=btn.findParentByType("panel").child("form").getForm();
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        var actType = this.getView().actType;
        //记录查重
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');
        for(var i=0;i<mfRecs.length;i++) {
            //记录查重
            var TZ_XXX_BH = mfRecs[i].get("TZ_XXX_BH");
            var tagNameCount = 0;
            var recIndex = store.findBy(function (record, id) {
                if (record.get("TZ_XXX_BH") == TZ_XXX_BH) {
                    tagNameCount++;
                }}, 0);
           }
        if (tagNameCount > 1) {
            Ext.MessageBox.alert('提示','信息项编号不能重复');
        }

            var form = this.getView().child("form").getForm();
            if (form.isValid()) {
                var tzParams = this.getOrgInfoParams();
                var comView = this.getView();
                Ext.tzSubmit(tzParams, function (responseData) {
                    form.setValues({"TZ_CS_WJ_ID": responseData.id});
                    var tzStoreParams = '{"TZ_CS_WJ_ID":"' + form.findField("TZ_CS_WJ_ID").getValue() + '","FLAG":"A"}';
                    store.tzStoreParams = tzStoreParams;
                    store.reload();
                    comView.actType = "update";
                    var interviewMgrPanel = Ext.ComponentQuery.query("panel[reference=testWjdcInfo]");
                    interviewMgrPanel[0].getStore().reload();
                }, "", true, this);

            }

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
        var tzParams = '{"ComID":"TZ_CSWJ_LIST_COM","PageID":"TZ_CSWJ_DETAIL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onCsWjdcEnsure:function(btn){
        var form=btn.findParentByType("panel").child("form").getForm();
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        var actType = this.getView().actType;
        //记录查重
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getOrgInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                form.setValues(responseData.formData);
                var tzStoreParams = '{"TZ_CS_WJ_ID":"'+form.findField("TZ_CS_WJ_ID").getValue()+'"}';
                store.tzStoreParams = tzStoreParams;
                store.reload();
                comView.actType = "update";
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=testWjdcInfo]");
                interviewMgrPanel[0].getStore().reload();
                comView.close();
            },"",true,this);
        }
    },
    onCsWjdcClose:function(btn){
        this.getView().close();
    },
    editCsWjdc:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.prompt","提示"),  Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.prompt","提示"),  Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var cswjId= selList[0].get("TZ_CS_WJ_ID");
        this.editCswjByID(cswjId);
    },
    editTestWjdc: function(view, rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var cswjId = selRec.get("TZ_CS_WJ_ID");
        this.editCswjByID(cswjId);
    },
    editCswjByID:function(cswjId){
        // 是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CSWJ_LIST_COM"]["TZ_CSWJ_DETAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.prompt","提示"), Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.prompt","提示"),Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            //页面注册信息列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_CSWJ_LIST_COM","PageID":"TZ_CSWJ_DETAIL_STD","OperateType":"QF","comParams":{"TZ_CS_WJ_ID":"'+cswjId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                var wjID=form.findField("TZ_DC_WJ_ID").getValue();
                if (wjID==''){
                }else{
                    //开通在线调查只读
                }
                //页面注册信息列表数据
                var tzStoreParams = '{"FLAG":"A","TZ_CS_WJ_ID":"'+cswjId+'"}';
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
    viewCsWjdcDetail:function(view,rowindex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowindex);
        var wjId = selRec.get("TZ_DC_WJ_ID");
        this.viewWjdcByID(wjId);
    },
    viewWjdcByID:function(wjId) {
        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_WJXQ_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_WJXQ_STD，请检查配置。');
            return; 
        }

        var contentPanel, cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if (!Ext.ClassManager.isCreated(className)) {
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
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        cmp = new ViewClass();
        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            //页面注册信息列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJXQ_STD","OperateType":"QF","comParams":{"wjId":"'+wjId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //页面注册信息列表数据
                //var tzStoreParams = '{"wjId":"'+wjId+'"}';
                // grid.store.tzStoreParams = tzStoreParams;
                // grid.store.load();
            });

        });

        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    csWjdcSumStatic:function(view,rowIndex){
        var selRec = view.getStore().getAt(rowIndex);
        var wjId = selRec.get("TZ_DC_WJ_ID");
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_SURVEY_ANS_STD","OperateType":"HTML","comParams":{"SURVEY_ID":"' + wjId +'"}}';
        var newTab=window.open('about:blank');
        newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+tzParams;
    },
    /*设置问卷调查*/
    setWjdc:function(btn){
        var form = btn.findParentByType("form").getForm();
        var wjId = form.findField("TZ_DC_WJ_ID").getValue();
        //显示资源集合信息编辑页面
        if(wjId==''){
          Ext.MessageBox.alert('提示', '该测试问卷没有开通问卷调查！');
        }else{
          this.editWjdcByID(wjId);
        }
    },
    /*根据问卷id设置问卷*/
    editWjdcByID:function(wjId){
        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_WJSZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_WJSZ_STD，请检查配置。');
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
        cmp=new ViewClass();
        cmp.actType = "update";
        //cmp.parentGridStore = store;
        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            //参数
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJSZ_STD","OperateType":"QF","comParams":{"wjId":"'+wjId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                if(formData.TZ_DC_WJ_DLZT=='Y'){
                    form.findField("TZ_DC_WJ_DLZT").setValue(true)  ;
                }else{
                    form.findField("TZ_DC_WJ_DLZT").setValue(false)  ;
                }
                if(formData.TZ_DC_WJ_NEEDPWD=='Y'){
                    form.findField("TZ_DC_WJ_NEEDPWD").setValue(true);
                }else{
                    form.findField("TZ_DC_WJ_NEEDPWD").setValue(false);
                }
            });

        });
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    setCswjXXXInfo:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CSWJ_LIST_COM"]["TZ_CSWJ_XXX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.prompt","提示"), Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.prompt","提示"),Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('backXXXWin');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        win.cswjId = rec.get('TZ_CS_WJ_ID');
        win.wjId = rec.get('TZ_DC_WJ_ID');
        win.xxxBh = rec.get('TZ_XXX_BH');
        if(win.cswjId=="" || win.wjId==""||win.xxxBh==""){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.prompt","提示"),Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_XXX_STD.qxbcdjzlxx","请先保存信息项,在进行信息项设置！"));
            return;
        }

        //操作类型设置为更新
        win.actType = "update";
        var grid = win.child('grid');
        //参数
        var tzParams = '{"ComID":"TZ_CSWJ_LIST_COM","PageID":"TZ_CSWJ_LIST_STD","OperateType":"QF","comParams":{"cswjId":"'+win.cswjId+'","wjId":"'+win.wjId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var roleList = responseData.listData;
            var tzStoreParams = '{"cswjId":"'+win.cswjId+'","wjId":"'+win.wjId+'","xxxBh":"'+win.xxxBh+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
        });
        win.show();
    },
    //删除一个信息项
    deleteCswjXXX:function(view,rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.confirm","确认"), Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //添加一个信息项
    addCswjXXX:function(view,rowIndex){
        var store = view.findParentByType("grid").store;
        store.insert(rowIndex+1,new KitchenSink.view.template.survey.testQuestion.testWjXxxModal());
    },
    //信息项设置保存
    onGridSave:function(btn){
        var win = this.lookupReference('backXXXWin');
        var cswjId = win.cswjId;
        var wjId = win.wjId;
        var xxxBh = win.xxxBh;
        var grid = win.child("grid");
        var store = grid.getStore();
        var tzParams = this.getBackMsgParams(btn);
        var comView = this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            var tzStoreParams = '{"cswjId":"'+cswjId+'","wjId":"'+wjId+'","xxxBh":"'+xxxBh+'"}';
            store.tzStoreParams = tzStoreParams;
            comView.actType = "update";
            store.reload();
        },"",true,this);
    },
    onGridSure:function(btn){
        var win = this.lookupReference('backXXXWin');
        var cswjId = win.cswjId;
        var wjId = win.wjId;
        var xxxBh = win.xxxBh;
        var grid = win.child("grid");
        var store = grid.getStore();
        var tzParams = this.getBackMsgParams(btn);
        var comView = this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            var tzStoreParams = '{"cswjId":"'+cswjId+'","wjId":"'+wjId+'","xxxBh":"'+xxxBh+'"}';
            store.tzStoreParams = tzStoreParams;
            comView.actType = "update";
            store.reload();
            win.close();
        },"",true,this);
    },
    onGridClose:function(btn){
        var win = btn.findParentByType('window');
        win.close(); 
    },
    getBackMsgParams: function(btn){
        var win = btn.findParentByType('window');
        var cswjId = win.cswjId;
        var wjId = win.wjId;
        var xxxBh = win.xxxBh;
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
        var tzParams = '{"ComID":"TZ_CSWJ_LIST_COM","PageID":"TZ_CSWJ_XXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    //信息项编号选择
    Cswj_XxxChoice:function(btn){
    
        var searchDesc,modal,modal_desc;
        searchDesc="信息项选择";
        modal="TZ_XXX_BH";
        modal_desc="TZ_XXX_MC";
        var grid=this.getView().child("grid");
        var selList = grid.getSelectionModel().getSelectionMode();
        var form=grid.findParentByType("panel").child("form").getForm();
        var wjID=form.findField("TZ_DC_WJ_ID").getValue();
        console.log(grid,wjID,selList);
        Ext.tzShowPromptSearch({
            recname: 'PS_TZ_DCWJ_XXX_VW',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_DC_WJ_ID:{
                        value: wjID,
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_XXX_BH:{
                        desc:'信息项编号',
                        operator:'01',
                        type:'01'
                    },
                    TZ_XXX_MC:{
                        desc:'信息项名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XXX_BH: '信息项编号',
                TZ_XXX_MC: '信息项名称'
            },
            multiselect: false, 
            callback: function(selection){
                console.log(selection[0].data.TZ_XXX_BH,selection[0].data.TZ_XXX_MC);
                selList.get("TZ_XXX_BH").setValue(selection[0].data.TZ_XXX_BH);
                selList.get("TZ_XXX_MC").setValue(selection[0].data.TZ_XXX_MC);
                //grid.findField(modal_desc).setValue(selection[0].data.TZ_XXX_MC);
            }
        });
    }

})
