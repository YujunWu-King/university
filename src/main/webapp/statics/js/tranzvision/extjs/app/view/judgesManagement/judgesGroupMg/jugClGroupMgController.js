Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jugClMg',
    createClass: function () {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CLPS_GROP_COM"]["TZ_CLJUP_INFO_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLJUP_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel, cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        //contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        //contentPanel.body.addCls('kitchensink-example');

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

        return cmp;
    },
    addDataList: function (btn) {
        var roleGrid = this.getView();
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新增
        var cmp = this.createClass();
        //操作标志
        cmp.actType = "add";
        var jugGrid = btn.findParentByType('grid');
        cmp.jugGrid = jugGrid;

        //var grid = this.lookupReference('userRoleGrid');

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            form.findField('jugGroupId').setValue('NEXT');
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },

    editSelData: function (view, rowIndex) {

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);

        var jugGroupId = selRec.get("jugGroupId");
//   		console.log(jugTypeId);
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        var cmp = this.createClass();

        cmp.on('afterrender', function (panel) {
            var form = this.lookupReference('jugClGroupForm').getForm();
            //参数
//   			console.log(jugTypeId);
            var tzParams = '{"ComID":"TZ_CLPS_GROP_COM","PageID":"TZ_CLJUP_INFO_STD","OperateType":"QF","comParams":{"jugGroupId":"' + jugGroupId + '"}}';
            //加载数据
            Ext.tzLoad(tzParams, function (responseData) {
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

    deleteSelData: function (view, rowIndex) {
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
            if (btnId == 'yes') {
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        }, this);
    },
    saveList: function (btn) {
        //数据列表
        var grid = btn.findParentByType("grid");
        //信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();

        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if (removeJson != "") {
            comParams = '"delete":[' + removeJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_CLPS_GROP_COM","PageID":"TZ_CLJUP_LIST_STD","OperateType":"U","comParams":{' + comParams + '}}';
            //保存数据
            Ext.tzSubmit(tzParams, function () {
                store.reload();
                grid.commitChanges(grid);
            }, "", true, this);
        } else {
            Ext.Msg.alert("提示", "保存成功");
        }
    },
    closeList: function (btn) {
//        var grid = btn.findParentByType("grid");
//        grid.close();
        this.view.close();
    },
    ensureList: function (btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
//console.log(removeRecs);
        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if (removeJson != "") {
            comParams = '"delete":[' + removeJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_CLPS_GROP_COM","PageID":"TZ_CLJUP_LIST_STD","OperateType":"U","comParams":{' + comParams + '}}';
            //保存数据
            Ext.tzSubmit(tzParams, function () {

                grid.commitChanges(grid);
                grid.close();

            }, "", true, this);
        } else {
            grid.close();
        }


    },
    saveDataInfo: function () {

        var win = this.getView();

        //页面注册信息表单
        var form = this.getView().child('form').getForm();

        //表单数据
        var formParams = form.getValues();


        win.actType = "add";

        //提交参数
        var tzParams = '{"ComID":"TZ_CLPS_GROP_COM","PageID":"TZ_CLJUP_INFO_STD","OperateType":"U","comParams":{"' + win.actType + '":[{"data":' + Ext.JSON.encode(formParams) + '}]}}';

        Ext.tzSubmit(tzParams, function (responseData) {
//        	console.log(responseData)
        	if(responseData['jugGroupId']){
				form.setValues({jugGroupId:responseData.jugGroupId});

			};
            var interviewMgrPanel = Ext.ComponentQuery.query("panel[reference=jugClMgPanel]");
            interviewMgrPanel[0].getStore().reload();
        }, "", true, this);
    },
    onFormClose: function () {
        this.getView().close();
    },
    onFormSave: function () {
        this.saveDataInfo();
    },
    onFormEnsure: function () {
        this.saveDataInfo();

        this.getView().close();


    },
    searchDataList: function (btn) {
        Ext.tzShowCFGSearch({

            cfgSrhId: 'TZ_CLPS_GROP_COM.TZ_CLJUP_LIST_STD.TZ_CLPS_GR_TBL',
            condition: {
                "TZ_JG_ID": Ext.tzOrgID
            },
            callback: function (seachCfg) {
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});