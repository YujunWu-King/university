﻿Ext.define("KitchenSink.view.tzLianxi.ytt.bugMg.bugController", {
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
    searchBug:function(btn){
        Ext.tzShowCFGSearch({
//            cfgSrhId: 'TZ_BUG_MG_COM.TZ_BUG_LIST_STD.YTT_BUGMG_DFN_V',
            cfgSrhId: 'LX_YTT_BUG_MG_COM.LX_BUG_LIST_STD.YTT_BUGMG_DFN_V',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    bugMgSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var comParams="";
        var removeRecs = store.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"LX_YTT_BUG_MG_COM","PageID":"LX_BUG_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        var comView = this.getView();
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }
    },
    addBug:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LX_YTT_BUG_MG_COM"]["LX_BUG_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：LX_BUG_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel';
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

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);
        //处理页面显示被滚动条遮挡，触发EXT的重绘
        cmp.child('form').getForm().findField('status').setValue('');
        Ext.resumeLayouts(true);
        if (cmp.floating) {
//            cmp.actType = "add";
            cmp.show();

        }
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
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{

                    TZ_DLZH_ID:{
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
                TZ_DLZH_ID: '人员ID',
                TZ_REALNAME: '人员姓名'
            },
            multiselect: false,
            callback: function(selection){
                form.findField(oprID).setValue(selection[0].data.TZ_DLZH_ID);
                form.findField(oprName).setValue(selection[0].data.TZ_REALNAME);
            }
        });
    },
    bugInfoSave:function(btn){
        var form = btn.findParentByType('panel').child("form").getForm(),
            grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        //查找Bug管理界面并在之后刷新其grid的store
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugMg') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }
        if (form.isValid()) {
            var tzParams = this.getBugInfoParams();
            var comView = this.getView();
            console.log(tzParams);
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                console.log(comView.actType);
                 grid.getStore().reload();
            },"",true,this);

        }
    },
    bugInfoEnsure:function(btn){
        var form = this.getView().child("form").getForm(),grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugMg') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }
        if (form.isValid()) {
            var tzParams = this.getBugInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.close();
                grid.getStore().reload();
            },"",true,this);
        }
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
//            delete editRecs[i].data.attachmentID;
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
        var tzParams = '{"ComID":"LX_YTT_BUG_MG_COM","PageID":"LX_BUG_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    bugInfoClose:function(btn){
        this.getView().close();
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
    },
    viewBug : function(grid,rowIndex,colIndex){
        var contentPanel, className, ViewClass, cmp,bugID;

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LX_YTT_BUG_MG_COM"]["LX_BUG_INFO_STD"];
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
            var tzParams = '{"ComID":"LX_YTT_BUG_MG_COM","PageID":"LX_BUG_INFO_STD","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
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
    editBug : function(btn,rowdex){

        var contentPanel, className, ViewClass, cmp,bugID;
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if (selList.length === 0 && !rowdex.toString().match(/^\d+$/)) {
            Ext.Msg.alert("提示", "请选择要查看的记录");
            return;
        } else if (selList.length !== 1 && !rowdex.toString().match(/^\d+$/)) {
            Ext.Msg.alert("提示", "请您仅选择一条记录进行查看");
            return;
        } else {
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["LX_YTT_BUG_MG_COM"]["LX_BUG_INFO_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
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
            ViewClass = Ext.ClassManager.get(className);
            cmp = new ViewClass();
            tab = contentPanel.add(cmp);

            cmp.on('afterrender', function (panel) {
                var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"LX_YTT_BUG_MG_COM","PageID":"LX_BUG_INFO_STD","OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
                cmp.actType="update";

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

    },
    addAttach : function(file, value, attachmentType){
        if(file.findParentByType('form').findParentByType('form').getForm().findField('bugID').value == 'NEXT') {
            Ext.MessageBox.alert('确认', '请先保存BUG信息,然后在编辑BUG中上传附件', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
            return false;
        }
        console.log(this.getView().actType);
//        if(this.getView().actType == "add"){
////            Ext.MessageBox.alert('确认', '请先保存BUG信息,然后在编辑BUG中上传附件', function(btnId){
////                if(btnId == 'yes'){
////                    store.removeAt(rowIndex);
////                }
////            },this);
//            Ext.MessageBox.alert("提示","请先保存BUG信息，再上传附件。");
//            return false;
//        }

        attachmentType = 'ATTACHMENT';
        var form = file.findParentByType("form").getForm();

        if(value != ""){
            if(attachmentType=="IMG" || attachmentType=="TPJ"){
                var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
                if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
                    Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
                    form.reset();
                    return;
                };
            }

            //如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
            var dateStr = Ext.Date.format(new Date(), 'Ymd');


            var upUrl = "";
            if(attachmentType=="ATTACHMENT"){
                //upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
                upUrl = "/linkfile/FileUpLoad/attachment/";
                if(upUrl==""){
                    Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
                    return;
                }else{
					/*
                    if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
                    }else{
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                    }
					*/
					upUrl = TzUniversityContextPath + '/UpdServlet?filePath=bug';
                }
            }else{
                upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveImageAccessUrl").getValue();
                if(upUrl==""){
                    Ext.Msg.alert("错误","未定义上传图片的路径，请与管理员联系");
                    return;
                }else{
					/*
                    if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
                    }else{
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                    }
					*/
					upUrl = TzUniversityContextPath + '/UpdServlet?filePath=bug';
                }
            }
            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                url: upUrl,
                //waitMsg: '图片正在上传，请耐心等待....',
                success: function (form, action) {

                    var tzParams;
                    var picViewCom;

                    if(attachmentType=="TPJ"){
                        picViewCom = file.findParentByType("tabpanel").down('dataview[name=picView]');
                        tzParams = '{"order":' + picViewCom.getStore().getCount() + ',"attachmentType":"'+attachmentType+'","data":' + Ext.JSON.encode(action.result.msg) + '}';
                    }else{
                        tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
                    }

                    tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"HTML","comParams":' + tzParams +'}';

                    Ext.Ajax.request({
                        //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_AddArtAttach',
                        url: Ext.tzGetGeneralURL,
                        params: {
                            tzParams: tzParams
                        },
                        success: function(response){
                            var responseText = Ext.JSON.decode(response.responseText);
                            if(responseText.success == 0){
                                var accessPath = action.result.msg.accessPath;
                                var sltPath = action.result.msg.accessPath;
                                if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                                    accessPath = accessPath + action.result.msg.sysFileName;
                                    //sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
                                    sltPath = sltPath + responseText.minPicSysFileName;
                                }else{
                                    accessPath = accessPath + "/" + action.result.msg.sysFileName;
                                    //sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
                                    sltPath = sltPath+ "/" + responseText.minPicSysFileName;
                                }


                                //viewStore.reload();
                                if(attachmentType=="IMG"){
                                    //Ext.getCmp( "titileImage").setSrc(accessPath);
                                    //Ext.getCmp( "titleImageUrl").setValue(accessPath);
                                    file.findParentByType("tabpanel").down('image[name=titileImage]').setSrc(accessPath);
                                    file.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue(accessPath);
                                    //Ext.ComponentQuery.query('image[name=titileImage]')[0].setSrc(accessPath);
                                    //Ext.ComponentQuery.query('hiddenfield[name=titleImageUrl]')[0].setValue(accessPath);


                                }

                                if(attachmentType=="ATTACHMENT"){
                                    //var applyItemGrid = this.lookupReference('attachmentGrid');
                                    var applyItemGrid = file.findParentByType("grid")
                                    var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
                                        "attachmentID": 'NEXT',
                                        "attachmentSysName": action.result.msg.sysFileName,
                                        "attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
                                        "attachmentUrl": accessPath
                                    });
                                    applyItemGrid.store.insert(0,r);
                                }

                                if(attachmentType=="TPJ"){

                                    var viewStore = picViewCom.store;
                                    var picsCount = viewStore.getCount();

                                    var r = Ext.create('KitchenSink.view.activity.picModel', {
                                        "sysFileName": action.result.msg.sysFileName ,
                                        "index": picsCount+1,
                                        "src": accessPath,
                                        "caption": action.result.msg.filename,
                                        "picURL": "",
                                        "sltUrl": sltPath
                                    });

                                    viewStore.insert(picsCount ,r);
                                    viewStore.loadData(r,true);

                                    console.log(viewStore);
                                    // Ext.Msg.alert("",Ext.JSON.encode(action.result.msg));
                                }
                            }else{
                                Ext.Msg.alert("提示", responseText.message);
                            }
                        },
                        failure: function (response) {
                            Ext.MessageBox.alert("错误", "上传失败");
                        }
                    });

                    //重置表单
                    myMask.hide();
                    form.reset();
                },
                failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert("错误", action.result.msg);
                }
            });


        }
    }
}) ;
