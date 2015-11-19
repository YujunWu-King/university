/**
 * Created by luyan on 2015/11/16.
 */

Ext.define('KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.dbrwListController',
    createDbrwListClass: function(){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DBRW_LIST_COM"]["TZ_DBRW_LIST_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DBRW_LIST_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

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
    //查看
    viewDbrwInfo: function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要查看的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要查看的记录");
            return;
        }
        var wflinsId = selList[0].get("wflinsId");
        this.viewDbrwById(wflinsId);

/*
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建待办任务列表类
        var cmp = this.createDbrwListClass();

        //任务创建人
        var oprid = selList[0].get("taskCreateOprid");

        cmp.on('afterrender',function(){

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
        */
    },
    //列表中操作之查看
    viewSelDbrwInfo:function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var wflinsid = selRec.get("wflinsId");
        this.viewDbrwById(wflinsid);
    },
    viewDbrwById:function(wflinsid) {
        var contentPanel,cmp,className,viewClass,clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if(!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        viewClass = Ext.ClassManager.get(className);
        clsProto = viewClass.prototype;

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

        cmp = this.createDbrwListClass();

        cmp.on('afterrender',function(){
        //查看任务信息

        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }

    },
    //可配置搜索
    searchDbrwList: function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_DBRW_LIST_COM.TZ_DBRW_LIST_STD.TZ_WF_DBRW_T',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                //基于可配置搜索，清除过滤条件
                btn.findParentByType('grid').getStore().clearFilter();
                btn.findParentByType('grid').clearFilters();
                store.tzStoreParams = seachCfg;
                store.load();
            }
        })
    },
    //关闭
    closeDbrwList:function(btn) {
        var grid = btn.findParentByType("grid");
        grid.close();
    }
});