Ext.define('KitchenSink.view.processDispatchMg.processDispatchController', {
    extend: 'Ext.app.ViewController',
    requires: ['Ext.ux.IFrame'],
    alias: 'controller.processDispatchCon',

    //可配置搜索
    cfgSearchAct: function (btn) {
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_JC_DISPATCH_COM.TZ_DISPATCH_MG.TZ_PROCESS_DF_VW',
            condition: {},
            callback: function (seachCfg) {
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //放大镜搜索ComID
    pmtSearchCycleTmp: function(btn){
        var form = btn.findParentByType("window").child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_XUNH_DEFN_T',
            searchDesc: '搜索循环信息',
            maxRow:20,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_XH_MC:{
                        desc:'循环名称',
                        operator:'07',
                        type:'01'
                    },
                    TZ_XH_QZBDS:{
                        desc:'循环表达式',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XH_MC: '循环名称',
                TZ_XH_QZBDS: '循环表达式'
            },
            multiselect: false,
            callback: function(selection){
                form.findField("cycleExpression").setValue(selection[0].data.TZ_XH_QZBDS);
                //form.findField("ComIDName").setValue(selection[0].data.TZ_COM_MC);
            }
        });
    },

    openProcessMonitor: function () {

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JC_DISPATCH_COM"]["TZ_MONITOR_LIST"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MONITOR_LIST，请检查配置。');
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
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

    onComRegClose: function (btn) {
        //关闭窗口
        this.getView().close();
    },
    openProcessBL:function (view, rowIndex) {
        var user = Ext.tzOrgID.toUpperCase();
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var processName = selRec.get("processName");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JC_DISPATCH_COM"]["TZ_DISPATCH_INFO"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DISPATCH_INFO，请检查配置。');
            return;
        }
        var win = this.lookupReference('processDispatchWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            var form = win.child('form').getForm();
            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_DISPATCH_INFO","OperateType":"QF","comParams":{"user":"'+user+'","processName":"'+processName+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        //操作类型设置为更新
        win.actType = "update";
        win.show();
    }
});
