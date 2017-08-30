Ext.define('KitchenSink.view.weChat.weChatUser.weChatUserMgController',{
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatUserMgController',
    //查询
    queryUser:function(btn) {
        var jgId = btn.findParentByType("grid").jgId;
        var wxAppId = btn.findParentByType("grid").wxAppId;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW',
            condition:{
                TZ_JG_ID:jgId,
                TZ_WX_APPID:wxAppId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //预查询-今日新增用户
    queryTodayNew:function(btn) {
        //关注日期为当前日期
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = "00" + (nowDate.getMonth()+1);
        month = month.substr(month.length-2,2);
        var day = nowDate.getDate();

        var nowDateStr = year + "-" + month + "-" + day;

        var grid = btn.findParentByType("grid");

        var jgId = grid.jgId;
        var wxAppId = grid.wxAppId;

        var userStore = grid.getStore();
        var tzStoreParams = '{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW",' +
            '"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+jgId+'","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"'+wxAppId+'","TZ_SUBSRIBE_DATE-operator":"01","TZ_SUBSRIBE_DATE-value":"'+nowDateStr+'"}}';
        userStore.tzStoreParams = tzStoreParams;
        userStore.reload();
    },
    //预查询-已绑定用户
    queryBind:function(btn) {
        //关联联系人/用户ID不为空
        var grid = btn.findParentByType("grid");

        var wxAppId = grid.wxAppId;

        var userStore = grid.getStore();
        var tzStoreParams = '{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW",' +
            '"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+jgId+'","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"'+wxAppId+'","TZ_GL_CONTID-operator":"13","TZ_GL_CONTID-value":",NULL"}}';
        userStore.tzStoreParams = tzStoreParams;
        userStore.reload();
    },
    //预查询-按类别标签
    queryForTag:function(btn) {
        var grid = btn.findParentByType("grid");

        var jgId = grid.jgId;
        var wxAppId = grid.wxAppId;
        var tagId = btn.name;

        var userStore = grid.getStore();
        var tzStoreParams = '{"OperateType":"tzQueryByTag","jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","tagId":"'+tagId+'"}';
        userStore.tzStoreParams = tzStoreParams;
        userStore.reload();
    },
    //预查询-已取消关注
    queryUnfollow:function(btn) {
        //订阅标识为1
        var grid = btn.findParentByType("grid");

        var jgId = grid.jgId;
        var wxAppId = grid.wxAppId;

        var userStore = grid.getStore();
        var tzStoreParams = '{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW",' +
            '"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+jgId+'","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"'+wxAppId+'","TZ_SUBSCRIBE-operator":"01","TZ_SUBSCRIBE-value":"1"}}';
        userStore.tzStoreParams = tzStoreParams;
        userStore.reload();
    },
    //设置标签
    setTag:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();

        var jgId = grid.jgId;
        var wxAppId = grid.wxAppId;

        var selList = grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.prompt","提示"),Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.noSelectTip","您没有选中任何记录"));
            return;
        } else {
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_USER_COM"]["TZ_WX_TAG_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className == "" || className == undefined){
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TAG_STD，请检查配置。');
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

            var openIdList = "",openId = "";
            if(checkLen>1) {
                for(var i=0;i<checkLen;i++) {
                    var openIdTmp = selList[i].data.openId;
                    if(openIdList == "") {
                        openIdList = openIdTmp;
                    } else {
                        openIdList = openIdList + "," + openIdTmp;
                    }
                }
            } else {
                openId = selList[0].data.openId;
                openIdList = openId;
            }


            cmp = new ViewClass();
            cmp.on('afterrender',function(win){
                var form = win.child("form").getForm();
                form.findField("jgId").setValue(jgId);
                form.findField("wxAppId").setValue(wxAppId);
                form.findField("openIdList").setValue(openIdList);

                var gridStore = win.down("grid").getStore();
                var tzStoreParams='{"jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","openId":"'+openId+'"}';
                gridStore.tzStoreParams = tzStoreParams;
                gridStore.load();
            });
            cmp.show();
        }
    },
    //更多操作-给选中用户发送普通消息
    sendMessageForSelect:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();

        var selList = grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.prompt","提示"),Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.noSelectTip","您没有选中任何记录"));
            return;
        } else {
            var selList = grid.getSelectionModel().getSelection();
            var checkLen = selList.length;
            if(checkLen==0) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.prompt","提示"),Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.noSelectTip","您没有选中任何记录"));
                return;
            } else {
                for(var i=0;i<checkLen;i++) {

                }
            }
        }
    },
    //更多操作-按微信标签发送普通消息
    sendMessageForTag:function() {

    },
    //更多操作-给所有用户发送普通消息
    sendMessageForAll:function() {

    },
    //更多操作-给选中用户发送模板消息
    sendTplForSelect:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();

        var selList = grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.prompt","提示"),Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.noSelectTip","您没有选中任何记录"));
            return;
        } else {
            for(var i=0;i<checkLen;i++) {

            }
        }
    },
    //更多操作-给所有用户发送模板消息
    sendTplForAll:function() {

    },
    //操作列-设置标签
    setTagForOne:function(btn,rowIndex) {
        var me = this,
            view = me.getView();

        var store = view.getStore();

        var wxAppId = view.wxAppId;

        var openId = store.getAt(rowIndex).data.openId;


        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_USER_COM"]["TZ_WX_TAG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TAG_STD，请检查配置。');
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
        cmp.on('afterrender',function(win){
            var form = win.child("form").getForm();
            form.findField("wxAppId").setValue(wxAppId);
            form.findField("openIdList").setValue(openId);

            var gridStore = win.down("grid").getStore();
            var tzStoreParams='{"wxAppId":"'+wxAppId+'","openId":"'+openId+'"}';
            gridStore.tzStoreParams = tzStoreParams;
            gridStore.load();
        });
        cmp.show();
    },
    //操作列-关联/查看客户
    viewUser:function() {

    },
    //操作列-创建/查看销售线索
    viewClue:function() {

    },
    //操作列-发送微信普通消息
    sendMessageForOne:function() {

    },
    //关闭
    closeUser:function() {
        this.getView().close();
    }
});