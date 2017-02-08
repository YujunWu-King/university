Ext.define('KitchenSink.view.scholarShipManage.scholarController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.scholarController',

    queryScholar:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_SCHLR_VW',
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
    ScholarPanelClose:function(btn){
        var grid=btn.findParentByType("grid");
        grid.close();
    },
    addScholar:function(btn){
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCHOLAR_COM"]["TZ_SCHOLAR_NEW_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SCHOLAR_NEW_STD，请检查配置。');
            return;
        }
        var win =this.lookupReference('addScholarWin');
        if (!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
        win.show(); 
        window.wjdc_pre=null;//目的是让他每次都执行下面的操作
        if (!window.wjdc_pre) {
            window.wjdc_pre = function(el) {
                Ext.each(Ext.query(".tplitem"),
                    function(i) {
                        this.style.backgroundColor = null
                    });
                el.style.backgroundColor = "rgb(173, 216, 230)";
                // var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
                //问卷名称和模板名称保持一致，不加后面的数字
                var newName = el.getElementsByClassName("tplname")[0].getAttribute("title");
                win.child("form").getForm().setValues({"predefine":newName}); 
            }
        }
    },
    scholarNewEnsure:function(btn){

        var win=btn.findParentByType("window");
        var shcolarName = win.child("form").getValues().predefine;
        var from=win.child("form").getForm();
        var tplId='';
        Ext.each(Ext.query(".tplitem"),
            function(i) {
                if (this.style.backgroundColor == "rgb(173, 216, 230)") {
                    tplId = this.getAttribute("data-id");
                }
            });
        if (shcolarName){
            var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"' + shcolarName + '"}]}';
            var tzParams = '{"ComID":"TZ_SCHOLAR_COM","PageID":"TZ_SCHLR_LIST_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
            Ext.tzSubmit(tzParams,
                function(responseData) {
                    var scholarId=responseData.TZ_SCHLR_ID;
                    win.close();
                    //进入到奖学金详情页面
                    var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCHOLAR_COM"]["TZ_SCHLR_SET_STD"];
                    if( pageResSet == "" || pageResSet == undefined){
                        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.nmyqx","您没有权限"));
                        return;
                    }
                    //该功能对应的JS类
                    var className = pageResSet["jsClassName"];
                    if(className == "" || className == undefined){
                        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
                        //组件注册表单信息;
                        var form = panel.child('form').getForm();
                        //参数
                        var tzParams = '{"ComID":"TZ_SCHOLAR_COM","PageID":"TZ_SCHLR_LIST_STD","OperateType":"QF","comParams":{"TZ_SCHLR_ID":"'+scholarId+'"}}';
                        //加载数据
                        Ext.tzLoad(tzParams,function(responseData){
                            //组件注册信息数据
                            var formData = responseData.formData;
                            form.setValues(formData);
                        });
                    });
                    tab = contentPanel.add(cmp);
                    contentPanel.setActiveTab(tab);
                    Ext.resumeLayouts(true);
                    if (cmp.floating) {
                        cmp.show();
                    }
                },"",true,this);
        }
    },
    scholarNewClose:function(btn){
    	var win=btn.findParentByType("window");
    	win.close();
    },
    editScholar:function(btn){
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.prompt","提示"),  Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.prompt","提示"),  Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var TZ_SCHLR_ID= selList[0].get("TZ_SCHLR_ID");
        this.editSchLrByID(TZ_SCHLR_ID);
    },
    editScholarRow: function(view, rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var TZ_SCHLR_ID = selRec.get("TZ_SCHLR_ID");
        this.editSchLrByID(TZ_SCHLR_ID);
    },
    editSchLrByID:function(TZ_SCHLR_ID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCHOLAR_COM"]["TZ_SCHLR_SET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_SET_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            //参数
            var tzParams = '{"ComID":"TZ_SCHOLAR_COM","PageID":"TZ_SCHLR_LIST_STD","OperateType":"QF","comParams":{"TZ_SCHLR_ID":"'+TZ_SCHLR_ID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
            });
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    onSchlrDetailSave:function(btn){
        var panel=btn.findParentByType("panel");
        var form =panel .child("form").getForm();
        if(!form.isValid()){
            return false;
        }else{
            var formParams = form.getValues();
            var tzParams = '{"ComID":"TZ_SCHOLAR_COM","PageID":"TZ_SCHLR_LIST_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
            },"",true,this);
        }
    },
    onSchlrDetailSure:function(btn){
        var panel=btn.findParentByType("panel");
        var form =panel .child("form").getForm();
        var comView = this.getView();
        if(!form.isValid()){
            return false;
        }else{
            var formParams = form.getValues();
            var tzParams = '{"ComID":"TZ_SCHOLAR_COM","PageID":"TZ_SCHLR_LIST_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
            	comView.close();
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=scholarList]");
                interviewMgrPanel[0].getStore().reload();
            },"",true,this);
        }
    },
    onSchlrDetailClose:function(){
    	this.getView().close(); 
    },
    setWjdc:function(btn){
        var form = btn.findParentByType("form").getForm();
        var wjId = form.findField("TZ_DC_WJ_ID").getValue();
        //显示资源集合信息编辑页面
        if(wjId==''){
            Ext.MessageBox.alert('提示', '问卷调查不存在');
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
                //问卷调查听众赋值
                var audIDList=formData.AudID;
                var audNameList=formData.AudName;
                var oprIdArray=new Array();
                var i=0,j=0;
                for(j=0;j<audIDList.length;j++){
                    var TagModel=new KitchenSink.view.template.survey.question.tagModel();
                    var audId = audIDList[j];
                    var audName=audNameList[j];
                    TagModel.set('tagId',audId);
                    TagModel.set('tagName',audName);
                    oprIdArray[i]=TagModel;
                    i++;
                }
                form.findField("AudList").setValue(oprIdArray); 
            });

        });
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //参与人管理
    viewScholarCyr:function(view,rowIndex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var wjId = selRec.get("TZ_DC_WJ_ID");
        var schLrId=selRec.get("TZ_SCHLR_ID");
 
        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_PERSON_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_PERSON_STD，请检查配置。');
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
        cmp.on('afterrender',function(grid){
            var tzStoreParams = '{"cfgSrhId":"TZ_ZXDC_WJGL_COM.TZ_ZXDC_PERSON_STD.TZ_ZXDC_CYR_VW","condition":{"TZ_DC_WJ_ID-operator": "01","TZ_DC_WJ_ID-value":"'+wjId+'","TZ_SCHLR_ID":"'+schLrId+'"}}'; 
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.reload();
            grid.wjId=wjId;
            grid.schLrId=schLrId;
        });
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    } 


})