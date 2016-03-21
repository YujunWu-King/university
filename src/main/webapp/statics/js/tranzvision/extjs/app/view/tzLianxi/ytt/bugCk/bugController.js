﻿Ext.define("KitchenSink.view.tzLianxi.ytt.bugCk.bugController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugController',
    searchBug:function(btn){
        Ext.tzShowCFGSearch({
//            cfgSrhId: 'TZ_BUG_MG_COM.TZ_BUG_LIST_STD.YTT_BUGMG_DFN_V',
            cfgSrhId: 'LX_YTT_BUG_CK_COM.LX_BUG_LIST_STD1.YTT_BUGMG_DFN_V',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    selectOprID: function(btn){
        var fieldName = btn.name;
        var searchDesc,oprID,oprName;
        if(fieldName=='recOprID'){
            searchDesc="选择录入人";
            oprID="recOprID";
            oprName="recOprName";
        }else if(fieldName=="resOprID"){
            searchDesc="选择责任人";
            oprID="resOprID";
            oprName="resOprName";
        }
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_AQ_YHXX_TBL',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
//                    TZ_JG_ID:{
//                        value: Ext.tzOrgID,
//                        type: '01'
//                    }
                },
                srhConFields:{

                    OPRID:{
                        desc:'人员ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'人员姓名',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                OPRID: '人员ID',
                TZ_REALNAME: '人员姓名'
            },
            multiselect: false,
            callback: function(selection){
                form.findField(oprID).setValue(selection[0].data.OPRID);
                form.findField(oprName).setValue(selection[0].data.TZ_REALNAME);
            }
        });
    },
    getBugInfoParams:function(){
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        console.log(formParams);
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
        var tzParams = '{"ComID":"LX_YTT_BUG_CK_COM","PageID":"LX_BUG_INFO_STD1","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    bugInfoClose:function(btn){
        this.getView().close();
    },
    viewBug : function(grid,rowIndex,colIndex){
        var contentPanel, className, ViewClass, cmp,bugID;

            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LX_YTT_BUG_CK_COM"]["LX_BUG_INFO_STD1"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            className = pageResSet["jsClassName"];

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
            bugID = grid.getStore().getAt(colIndex).data.bugID ;
            if (!Ext.ClassManager.isCreated(className)) {
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);
            cmp = new ViewClass();
            tab = contentPanel.add(cmp);
            cmp.on('afterrender', function (panel) {
                var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"LX_YTT_BUG_CK_COM","PageID":"LX_BUG_INFO_STD1","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
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


    }
}) ;
