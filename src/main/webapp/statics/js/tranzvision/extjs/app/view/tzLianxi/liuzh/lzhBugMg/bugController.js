﻿Ext.define("KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugController',
    deleteBug: function(btn){
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
    addNewBug:function(btn){
        /**/
        var baseBtn = btn;
        var grid =baseBtn.findParentByType("grid");
        var store = grid.store;
        var contentPanel,className,ViewClass,cmp;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        className = 'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.updateBug';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass();
        cmp.title = "新增BUG";
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }

    },
    editBug:function(btn,rowdex) {
        var response = Ext.Ajax.request({
                url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                params: {
                    params: '{"type":"getRole","manager":"lzh"}'
                },
                async: false
            }),
            role = response.responseText.match(/role=(\d+)/i)[1];
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if (selList.length === 0 && !rowdex.toString().match(/^\d+$/)) {
            Ext.Msg.alert("提示", "请选择要查看的记录");
            return;
        } else if (selList.length !== 1 && !rowdex.toString().match(/^\d+$/)) {
            Ext.Msg.alert("提示", "请您仅选择一条记录进行查看");
            return;
        } else {
            var contentPanel, className, ViewClass, cmp,BugID;
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
            BugID = rowdex.toString().match(/^\d+$/) ? btn.findParentByType("grid").getStore().getAt(rowdex).data.BugID : selList[0].data.BugID;

            if (role == 1) {
                className = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LZH_BUGMGUSER_COM"]["LZH_BUGMG_USERVIEW"]["jsClassName"];
            } else {
                className = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LX_LZH_BUGMG2_COM"]["LX_BUG_VIEW_STD"]["jsClassName"];
            }
            if (!Ext.ClassManager.isCreated(className)) {
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);
            cmp = new ViewClass();
            cmp.title = "编辑BUG";
            tab = contentPanel.add(cmp);
            cmp.on('afterrender', function (panel) {
                //组件注册表单信息;
                Ext.Ajax.request({
                    url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                    params: {
                        params: '{"type":"getBug","manager":"lzh","bugID":"' + BugID + '"}'
                    },
                    success: function (response) {
                        var formData = Ext.JSON.decode(response.responseText);
                        var form = panel.child('form').getForm();
                        var store = panel.down('grid').getStore();
                        store.proxy.extraParams.params = '{"manager":"lzh","type":"queryFile","bugID":"'+BugID+'"}';
                        store.reload();
                        delete formData.files;
                        form.setValues(formData);
                    }
                });

                //form.setValues({"title":"123"});
            });


            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        }

    },

    searchBug:function(btn){
        var parBtn =btn;
        var status=Ext.create('Ext.data.Store',{

            fields:[{name:'condition',name:'conValue'}],
            data:[
                {condition:'新建',conValue:'0'},
                {condition:'已分配',conValue:'1'},
                {condition:'已修复',conValue:'2'},
                {condition:'成功关闭',conValue:'3'},
                {condition:'重新打开',conValue:'4'},
                {condition:'取消',conValue:'5'}
            ],
            autoLoad:true
        });
        var win= Ext.create('Ext.window.Window', {
            modal:true,
            title: '查询Bug' ,
            items: [
                new Ext.form.Panel({
                    xtype:'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    width:450,
                    bodyPadding: 10,
                    height: 'auto',
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                    items:[
                        {
                            xtype: 'textfield',
                            fieldLabel: '编号',
                            name: 'bugID',
                            allowBlank:false
                        },{
                            xtype: 'textfield',
                            fieldLabel: '名称',
                            name: 'name',
                            allowBlank:false
                        },{
                            xtype: 'combo',
                            fieldLabel: '处理状态',
                            name: 'status',
                            emptyText: '请选择',
                            queryMode: 'local',
                            editable: false,
                            valueField: 'conValue',
                            displayField: 'condition',
                            store:status,
                            maxeHeight:150,
                            width:300
                        }],
                    buttons:[
                        {text:'搜索',handler: function (btn) {
                            var window = btn.findParentByType("window");
                            var form = window.child("form").getForm();
                            var bugID = form.findField("bugID").getValue();
                            var name = form.findField("name").getValue();
                            var status = form.findField("status").getValue();
                            if(status==null){
                                status="";
                            }
                            var store = parBtn.findParentByType("grid").store;
                            store.proxy.extraParams={
                                params:'{"type":"queryBug","manager":"lzh","bugID":"'+bugID+'","name":"'+name+'","status":"'+status+'"}'
                            };
                            store.load({
                                callback:function(response){
                                    window.close();
                                }
                            })
                        }},
                        {text:'清空',handler:function(btn){
                            var window = btn.findParentByType("window");
                            var form = window.child("form").getForm();
                            form.reset();
                        }},
                        {text:'取消',handler:function(btn){
                            win.close();
                        }}]
                })]
        });
        win.show();
    },
    saveBug : function(btn){
        var panel = btn.findParentByType('panel');
        var form = panel.child('form');
        var formParams = form.getForm().getValues(),
            fileStore = form.down('grid').getStore();
        var files = [];
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].title == 'BUG管理') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }
        console.log(fileStore.getRemovedRecords());
        //附件信息管理
        var filePath = fileStore.getRange(),fileData = {};
        for(var x = 0;x<filePath.length;x++) {
            files.push({
                fileID: filePath[x].data.attachmentID.replace(/\./g, "_"),
                fileName: filePath[x].data.attachmentName.match(/\<a.*?\>.*?\<\/a\>/) ? filePath[x].data.attachmentName.match(/\>(.*?)\<\/a/)[1] : filePath[x].data.attachmentName,
                fileUrl: filePath[x].data.attachmentUrl
            });
        }
        //构建删除附件信息跟新请求对象
        if(fileStore.getRemovedRecords().length !== 0){
            var mid =  fileStore.getRemovedRecords(),
                arr = [];
            for(var x = mid.length-1;x>=0;x--) {
                delete mid[x].data.id;
                arr.push(mid[x].data);
            }
            fileData.remove = arr;
        }
        fileData.type = 'updateFile';
        fileData.manager = 'lzh';
        Ext.Ajax.request({
            url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
            params: {params: Ext.JSON.encode(fileData)},
            success: function () {
                form.items.items[7].items.items[1].child('grid').getStore().reload();
            }
        });
        //构建BUG信息跟新请求
        var jsonData = {};
        formParams.files = files;
        jsonData.update = formParams;
        jsonData.type = 'saveBug';
        jsonData.manager = 'lzh';
        switch(jsonData.update.status){
            case "新建":
                jsonData.update.status = 0;
                break;
            case "已分配":
                jsonData.update.status = 1;
                break;
            case "已修复":
                jsonData.update.status = 2;
                break;
            case "关闭":
                jsonData.update.status = 3;
                break;
            case "重新打开":
                jsonData.update.status = 4;
                break;
            case "取消":
                jsonData.update.status = 5;
                break;
            default:
                break;
        }
        Ext.Ajax.request({
            url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
            params: {params: Ext.JSON.encode(jsonData)},
            success: function () {
                grid.getStore().reload();
            }
        });
    },
    ensureBug:function(btn){
        var panel = btn.findParentByType('panel');
        var form = panel.child('form');
        var formParams = form.getForm().getValues(),
            fileStore = form.items.items[7].items.items[1].child('grid').getStore();

        var files = [];
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].title == 'BUG管理') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }
        console.log(fileStore.getRemovedRecords());
        //附件信息管理
        var filePath = fileStore.getRange(),fileData = {};
        for(var x = 0;x<filePath.length;x++) {
            files.push({
                fileID: filePath[x].data.attachmentID.replace(/\./g, "_"),
                fileName: filePath[x].data.attachmentName.match(/\<a.*?\>.*?\<\/a\>/) ? filePath[x].data.attachmentName.match(/\>(.*?)\<\/a/)[1] : filePath[x].data.attachmentName,
                fileUrl: filePath[x].data.attachmentUrl
            });
        }
        //构建删除附件信息跟新请求对象
        if(fileStore.getRemovedRecords().length !== 0){
            var mid =  fileStore.getRemovedRecords(),
                arr = [];
            for(var x = mid.length-1;x>=0;x--) {
                delete mid[x].data.id;
                arr.push(mid[x].data);
            }
            fileData.remove = arr;
        }
        /*
        if(fileStore.getUpdatedRecords().data.length !== 0){
            var mid = fileStore.getUpdatedRecords(),
                arr = [];
            for(var x = mid.length-1;x>=0;x--){
                delete mid[x].data.id;
                arr.push(mid[x].data);
            }
            fileData.update = arr;
        }*/
        fileData.type = 'updateFile';
        fileData.manager = 'lzh';
        Ext.Ajax.request({
            url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
            params: {params: Ext.JSON.encode(fileData)},
            success: function () {
                form.items.items[7].items.items[1].child('grid').getStore().reload();
            }
        });
        //构建BUG信息跟新请求
        var jsonData = {};
        formParams.files = files;
        jsonData.update = formParams;
        jsonData.type = 'saveBug';
        jsonData.manager = 'lzh';
        switch(jsonData.update.status){
            case "新建":
                jsonData.update.status = 0;
                break;
            case "已分配":
                jsonData.update.status = 1;
                break;
            case "已修复":
                jsonData.update.status = 2;
                break;
            case "关闭":
                jsonData.update.status = 3;
                break;
            case "重新打开":
                jsonData.update.status = 4;
                break;
            case "取消":
                jsonData.update.status = 5;
                break;
            default:
                break;
        }
        Ext.Ajax.request({
            url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
            params: {params: Ext.JSON.encode(jsonData)},
            success: function () {
                grid.getStore().reload();
                contentPanel.remove(panel);
            }
        });


    },
    cancel:function(btn){
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.remove(btn.findParentByType('panel'));
    },
    deleteArtAttenments: function(btn,rowIndex){
        //选中行
        var store = btn.findParentByType('grid').getStore();
        var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            if(checkLen == 0){
                Ext.Msg.alert("提示","请选择要删除的记录");
                return;
            }else{
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        store.remove(selList);
                        console.log(store.getRemovedRecords());
                    }
                },this);
            }

        }
    }
}) ;
