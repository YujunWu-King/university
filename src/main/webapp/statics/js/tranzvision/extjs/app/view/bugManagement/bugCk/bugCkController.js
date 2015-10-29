Ext.define("KitchenSink.view.bugManagement.bugCk.bugCkController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugCkController',
    getBugInfoParams:function(){
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        var desc=form.findField("descript").getValue();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增

        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"BUG","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"BUG","data":'+Ext.JSON.encode(formParams)+'}';
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

        //附件
        var grid = this.getView().lookupReference('attachmentGrid');
        var store = grid.getStore();

        //修改grid json字符串
        var gridEditJson = "";
        //修改grid
        var editRecs = store.getModifiedRecords();
        for(var i=0;i<editRecs.length;i++){
            if(editRecs[i].data.attachmentName.match(/>([\s\S]*?)<\/a>/)) {
                editRecs[i].data.attachmentName = editRecs[i].data.attachmentName.match(/>([\s\S]*?)<\/a>/)[1];
            }
            delete editRecs[i].data.id;
            delete editRecs[i].data.attachmentID;
            editRecs[i].data.bugID = formParams.bugID;
            if (gridEditJson == "") {
                gridEditJson = '{"typeFlag":"ATTACH","data":' + Ext.JSON.encode(editRecs[i].data) + '}';
            } else {
                gridEditJson = gridEditJson + ',{"typeFlag":"ATTACH","data":' + Ext.JSON.encode(editRecs[i].data) + '}';
            }

        };

        if(gridEditJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + gridEditJson + "]";
            }else{
                comParams = comParams + ',"update":[' + gridEditJson + "]";
            }
        }
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
        if(removeRecs.id){
            delete removeRecs.id;
        }
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        };
        if(removeJson != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    editBugByLink : function(grid,html,rowIndex,colIndex,table,record){
        var contentPanel, className, ViewClass, cmp,bugID;

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGCK_COM"]["TZ_BUG_INFO_STD1"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        className = pageResSet["jsClassName"];

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        bugID = record.data.bugID ;
        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass =Ext.ClassManager.get('KitchenSink.view.bugManagement.bugCk.mybugInfoPanel');
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                var grid = panel.down('grid[name=attachmentGrid]');
                var store = grid.getStore();
                store.tzStoreParams ='{"bugID":"'+formData.bugID+'"}';
                store.load();
            });
        });


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }


    },
    viewBug : function(btn,rowdex){
        var contentPanel, className, ViewClass, cmp,bugID;

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGCK_COM"]["TZ_BUG_INFO_STD1"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有查看数据的权限');
            return;
        }
        //该功能对应的JS类
        className = pageResSet["jsClassName"];

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        bugID = rowdex.toString().match(/^\d+$/) ? btn.findParentByType("grid").getStore().getAt(rowdex).data.bugID : selList[0].data.bugID;
        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get('KitchenSink.view.bugManagement.bugCk.mybugInfoPanel');
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                var grid = cmp.down('grid[name=attachmentGrid]');
                var store = grid.getStore();
                store.tzStoreParams ='{"bugID":"'+formData.bugID+'"}';
                store.load();
            });
        });

        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    bugInfoSave:function(btn){
        var form = btn.findParentByType('panel').child("form").getForm(),grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');

        //查找任务查看界面获取grid
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugCk') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }

        if (form.isValid()) {
            var percent=form.findField("bugPercent").getValue();
            if (typeof (percent) == "undefined" ||percent==null){
                form.findField("bugPercent").setValue('0');
            };
            var tzParams = this.getBugInfoParams();
            var comView = this.getView();

            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";

            },"",true,this);

            var obj=this;
            var bugID=form.findField('bugID').getValue();
            var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                obj.updateRecord(form,grid,formData);
//                grid.getStore().reload();
            });
        }
    },
    bugInfoEnsure:function(btn){
        var form = btn.findParentByType('panel').child("form").getForm(),grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');

        //查找Bug管理界面并在之后刷新其grid的store
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugCk') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }

        if (form.isValid()) {
            var tzParams = this.getBugInfoParams();
            var comView = this.getView();
            var obj=this;
            Ext.tzSubmit(tzParams,function(responseData){
//                comView.close();
            },"",true,this);

            var obj=this;
            var bugID=form.findField('bugID').getValue();
            var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                obj.updateRecord(form,grid,formData);
//                grid.getStore().reload();
                comView.close();
            });
        }
    },
    bugInfoClose:function(btn){
        this.getView().close();
    },
    bugFinSubmit:function(btn){
        var form = btn.findParentByType('panel').child("form").getForm(),grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');

        //查找Bug管理界面并在之后刷新其grid的store
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugCk') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        };

        if (form.isValid()) {

            Ext.MessageBox.confirm('确认', '是否确认任务完成?', function(btnId){
                if(btnId == 'yes'){

                    /*	完成并提交：仅当任务处理状态不等于“已完成(待审核)、关闭、取消”时显示，点击后提示用户信息并确认后，将任务状态设置为“100%”，处理状态设置为“已完成(待审核)”*/
                    form.findField("status").setValue('已完成(待审核)');
                    form.findField("bugPercent").setValue('100');

                    var tzParams = this.getBugInfoParams();
                    var comView = this.getView();
                    var obj=this;
                    Ext.tzSubmit(tzParams,function(responseData){
                        comView.actType = "update";
                    },"",true,this);

                    var obj=this;
                    var bugID=form.findField('bugID').getValue();
                    var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
                    Ext.tzLoad(tzParams,function(respData){
                        var formData = respData.formData;
                        delete formData.files;
                        form.setValues(formData);
                        obj.updateRecord(form,grid,formData);
//                        grid.getStore().reload();
                    });
                }
            },this);}
    },
    bugWithdrawSubmit:function(btn){
        Ext.MessageBox.confirm('确认', '您确定要撤回提交吗?', function(btnId){
            if(btnId == 'yes'){
                var form = btn.findParentByType('panel').child("form").getForm(),grid,
                    contentPanel = Ext.getCmp('tranzvision-framework-content-panel');

                //查找Bug管理界面并在之后刷新其grid的store
                for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                    if (contentPanel.items.items[x].xtype == 'bugCk') {
                        grid = Ext.getCmp(contentPanel.items.keys[x]);
                    }
                };

                if (form.isValid()) {
                    /*	撤回提交：仅当任务处理状态为“已完成(待审核)”时显示，点击后提示用户信息并确认后，将任务状态回滚为“已分配”，进度百分比？*/
                    form.findField("status").setValue('已分配');
                    form.findField("bugPercent").setValue('0');

                    var tzParams = this.getBugInfoParams();
                    var comView = this.getView();
                    var obj=this;
                    var bugID=form.findField('bugID').getValue();
                    Ext.tzSubmit(tzParams,function(responseData){
                        comView.actType = "update";
                    },"",true,this);

                    var obj=this;
                    var tzParams = '{"ComID":"TZ_BUGCK_COM","PageID":"TZ_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
                    Ext.tzLoad(tzParams,function(respData){
                        var formData = respData.formData;
                        delete formData.files;
                        form.setValues(formData);
                        obj.updateRecord(form,grid,formData);
//                        grid.getStore().reload();
                    });
                }
            }
        },this);
    },
    viewBugProg:function(btn){
        var form=btn.findParentByType("form").getForm();
        var formParams = form.getValues();
        var bugID=form.findField("bugID").getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGCK_COM"]["TZ_BUG_PROG_STD"];
        if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || typeof(className) == "undefined"  ){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BUG_PROG_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('bugProg');
        if (!win) {
            //className = 'KitchenSink.view.activity.applyOptionsWindow';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var grid = this.getView().down('grid[name=bugProgGrid]');
        var store = grid.getStore();

        var tzStoreParams = '{"bugID":"'+bugID+'","queryType":"PROG"}';

        store.comID="TZ_BUGCK_COM";
        grid.store.tzStoreParams = tzStoreParams;

        grid.store.load();
        win.show();

    },
    updateRecord:function(form,grid,formData){
        //点击保存更新rec
        var bugID=form.findField('bugID').getValue();
        var store=grid.getStore();
//        var filters=store.getFilters();
//        var filters=this.filters;

        var index=store.find('bugID',bugID);
        var rec=store.getAt(index);
        if(rec){

            var bugUpdateDate=form.findField('bugUpdateDTTM').getRawValue();
            var date=bugUpdateDate.substring(0,10);
            var time=bugUpdateDate.substring(11,16);
            store.getAt(index).set('bugUpdateDate',date);
            store.getAt(index).set('bugUpdateTime',time);
            store.getAt(index).set('bugPercent',form.findField('bugPercent').getValue());
            store.getAt(index).set('estDate',form.findField('estDate').getValue());
            switch (form.findField('bugMigration').getValue())
            {
                case '0':
                    store.getAt(index).set('bugMigration','已迁移UAT但未测试');
                    break;
                case '1':
                    store.getAt(index).set('bugMigration','UAT环境测试通过');
                    break;
                case '2':
                    store.getAt(index).set('bugMigration','已迁移生产');
                    break;
            };
            store.getAt(index).set('bugStatus',form.findField('status').getValue());
            store.commitChanges();
        }
    }
}) ;
