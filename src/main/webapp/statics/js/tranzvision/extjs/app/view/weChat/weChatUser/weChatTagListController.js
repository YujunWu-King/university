Ext.define('KitchenSink.view.weChat.weChatUser.weChatTagListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatTagListController',
    //新增标签
    addTag:function(btn) {
        var currentWin = btn.findParentByType("window");
        var grid = btn.findParentByType("grid");
        var form = grid.findParentByType("form").getForm();
        var wxAppId =  form.findField("wxAppId").getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_USER_COM"]["TZ_WX_TAGXX_STD"];
        if(pageResSet=="" || pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有访问或修改数据的权限');
        }
        //改功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID：TZ_WX_TAGXX_STD，请检查配置。');
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

        cmp = new ViewClass(currentWin);
        //操作标志
        cmp.actType="add";

        cmp.on('afterrender',function(){
            //新增时删除按钮不可见
            cmp.down("toolbar").child("button").setHidden(true);
            var form = cmp.child('form').getForm();
            form.findField('jgId').setValue(Ext.tzOrgID);
            form.findField('wxAppId').setValue(wxAppId);
        });

        cmp.show();
    },
    //操作列-编辑
    editTagInfo:function(view,rowIndex) {
        var currentWin = view.findParentByType("window");
        var store = view.findParentByType("grid").getStore();
        var selRec = store.getAt(rowIndex);
        var jgId = selRec.get("jgId");
        var wxAppId = selRec.get("wxAppId");
        var tagId = selRec.get("tagId");
        var tagName = selRec.get("tagName");

        //系统标签ID
        var systemTagId = Ext.tzGetHardcodeValue("TZ_WX_SYSTEM_TAG_ID");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_USER_COM"]["TZ_WX_TAGXX_STD"];
        if(pageResSet=="" || pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有访问或修改数据的权限');
        }
        //改功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID：TZ_WX_TAGXX_STD，请检查配置。');
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

        cmp = new ViewClass(currentWin);

        cmp.on('afterrender',function(){
            var form = cmp.child('form').getForm();
            /*form.findField('jgId').setValue(jgId);
            form.findField('wxAppId').setValue(wxAppId);
            form.findField('tagId').setValue(tagId);
            form.findField('tagName').setValue(tagName);*/
            //使用下面这种方式赋值，没做任何修改点击关闭按钮，不会提示警告信息要求保存
            form.setValues({
                jgId:jgId,
                wxAppId:wxAppId,
                tagId:tagId,
                tagName:tagName
            });

            //系统标签ID编辑时删除按钮不可见
            if(tagId==systemTagId) {
                cmp.down("toolbar").child("button").setHidden(true);
            }


        });

        cmp.show();
    },
    //保存、确定
    setTagForUser:function(btn) {
        var me = this,
            view = me.getView();

        var grid = view.down("grid"),
            gridStore = grid.getStore(),
            gridLength = gridStore.getCount();

        if(gridLength!=0) {

            var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
                userGridStore = activeTab.getStore();

            var form = grid.findParentByType("form").getForm();
            var wxAppId =  form.findField("wxAppId").getValue();
            var openIdList = form.findField("openIdList").getValue();

            var selectTagId = "",NoSelectTagId = "";

            for(var i=0;i<gridLength;i++) {
                var selectFlag = gridStore.getAt(i).get("selectFlag");
                var tagId = gridStore.getAt(i).get("tagId");

                if(selectFlag==true) {
                    if(selectTagId=="") {
                        selectTagId = tagId;
                    } else {
                        selectTagId = selectTagId + "," + tagId;
                    }
                } else {
                    if(NoSelectTagId=="") {
                        NoSelectTagId = tagId;
                    } else {
                        NoSelectTagId = NoSelectTagId + "," + tagId;
                    }
                }
            }

            var editParams = {};
            editParams.jgId = Ext.tzOrgID;
            editParams.wxAppId = wxAppId;
            editParams.openIdList = openIdList;
            editParams.selectTagId = selectTagId;
            editParams.NoSelectTagId = NoSelectTagId;

            var comParams = '"update":['+Ext.JSON.encode(editParams)+']';
            var tzParams = '{"ComID":"TZ_WX_USER_COM","PageID":"TZ_WX_TAG_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(responseData) {
                var errcodeUnTag = responseData.errcodeUnTag;
                var errmsgUnTag = responseData.errmsgUnTag;
                var errcodeTag = responseData.errcodeTag;
                var errmsgTag = responseData.errmsgTag;
                if((errcodeUnTag!="0" && errmsgUnTag!="ok")||(errcodeTag!="0" && errmsgTag!="ok")) {
                    var errmsg = "";
                    if(errcodeUnTag!="0" && errmsgUnTag!="ok") {
                        errmsg = errcodeUnTag;
                    }
                    if(errcodeTag!="0" && errmsgTag!="ok") {
                        if(errmsg!="") {
                            errmsg = errmsg + ";" + errmsgTag;
                        } else {
                            errmsg = errmsgTag;
                        }
                    }
                    Ext.MessageBox.alert("提示",errmsg);
                    return;
                } else {
                    if (btn.name == "ensureTagBtn") {
                        view.close();
                    }
                    //刷新用户管理
                    userGridStore.load();
                    /*userGridStore.load({
                        callback: function () {
                            //默认展开rowexpander
                            var expander = activeTab.getPlugin();
                            var storeData = userGridStore.data;
                            for (var i = 0; i < storeData.length; i++) {
                                var record = storeData.items[i];
                                expander.toggleRow(i, record);
                            }
                        }
                    });*/
                }

            },"",true,this);

        }
    },
    //关闭
    closeTagList:function() {
        this.getView().close();
    }
});