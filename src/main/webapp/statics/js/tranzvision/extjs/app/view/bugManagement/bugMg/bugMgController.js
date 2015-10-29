Ext.define("KitchenSink.view.bugManagement.bugMg.bugMgController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugMgController',
    searchBug:function(btn){
       Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BUGMG_COM.TZ_BUG_LIST_STD.TZ_BUG_QUERY_VW',
            condition:{
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                btn.findParentByType('grid').clearFilters();
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    deleteBugs: function(btn){
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
    deleteBug: function(obj , rowIndex , colIndex , item , e , record , row){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = obj.findParentByType("grid").store;
                store.remove(record);
            }
        },this);
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
        var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        var comView = this.getView();
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.commitChanges();
            },"",true,this);
        }
    },
    addBug:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BUG_INFO_STD，请检查配置。');
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
//        cmp.child('form').getForm().findField('recOprID').setValue('');
        Ext.resumeLayouts(true);
        if (cmp.floating) {
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
            maxRow:300,
            pageSize:20,
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

        var form = btn.findParentByType('panel').child("form").getForm(),grid,
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');

        //查找Bug管理界面并在之后刷新其grid的store
        for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
            if (contentPanel.items.items[x].xtype == 'bugMg') {
                grid = Ext.getCmp(contentPanel.items.keys[x]);
            }
        }
        if (form.isValid()) {

            //编号
            var bugID = form.findField("bugID").getValue();
            //标志是否是新增：N-更新；Y-新增；
            var flg;
            if(bugID=='NEXT'){flg='Y'}else{flg='N'};
//            console.log(flg);

            //状态
            var bugStatus=form.findField("status").getValue();
            //责任人
            var resOprID=form.findField("resOprID").getValue();

            /*检查一下处理状态与责任人，如果处理状态为“新建”、责任人不为空，则update处理状态为“已分配”*/
            if (bugStatus=='0'&&resOprID!=""){
                form.findField("status").setValue('1');
            };

            /*检查一下处理状态与责任人，如果处理状态为“已分配”、责任人为空，则update处理状态为“新建”*/
            if (bugID=='NEXT'&&bugStatus=='1'&&resOprID==""){
                form.findField("status").setValue('0');
            };

            var tzParams = this.getBugInfoParams();
            var comView = this.getView();

            Ext.tzSubmit(tzParams,function(responseData){
                form.findField("bugID").setValue(responseData);
            },"",true,this);

            var bugID_new=form.findField('bugID').getValue();
            var obj=this;
            var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID_new+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues({bugID:formData.bugID});
                var file_grid = btn.findParentByType('panel').down('grid[name=attachmentGrid]');
                var file_store = file_grid.getStore();
                file_store.tzStoreParams ='{"bugID":"'+formData.bugID+'","queryType":"FILE"}';
                file_store.load();

                obj.updateRecord(grid,flg,formData);
//                var store=grid.getStore();
//                store.reload();
            });
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

            //编号
            var bugID = form.findField("bugID").getValue();
            //标志是否是新增：N-更新；Y-新增；
            var flg;
            if(bugID=='NEXT'){flg='Y'}else{flg='N'};

            var bugStatus=form.findField("status").getValue();
            var resOprID=form.findField("resOprID").getValue();

            /*检查一下处理状态与责任人，如果处理状态为“新建”、责任人不为空，则update处理状态为“已分配”*/
            var actType = this.getView().actType;
            if (actType='add'&&bugStatus=='0'&&resOprID!="" ){
                form.findField("status").setValue('1');
            };

            var tzParams = this.getBugInfoParams();
            var comView = this.getView();

            Ext.tzSubmit(tzParams,function(responseData){
                form.findField("bugID").setValue(responseData);
            },"",true,this);

            var bugID_new=form.findField('bugID').getValue();
            var obj=this;
            var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID_new+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues({bugID:formData.bugID});
                var file_grid = btn.findParentByType('panel').down('grid[name=attachmentGrid]');
                var file_store = file_grid.getStore();
                file_store.tzStoreParams ='{"bugID":"'+formData.bugID+'","queryType":"FILE"}';
                file_store.load();

                obj.updateRecord(grid,flg,formData);
                comView.close();
//                var store=grid.getStore();
//                store.reload();
            });
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
        var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
                    }
                },this);
            }

        }
    },
    editBugByLink : function(grid,html,rowIndex,colIndex,table,record){
        var contentPanel, className, ViewClass, cmp,bugID;

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_INFO_STD"];
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
        ViewClass =Ext.ClassManager.get('KitchenSink.view.bugManagement.bugMg.bugInfoPanel');
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","displayOnly": true,"OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                var grid = panel.down('grid[name=attachmentGrid]');
                var store = grid.getStore();
                store.tzStoreParams ='{"bugID":"'+formData.bugID+'","queryType":"FILE"}';
                store.load();
            });
        });
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    editBug : function(obj , rowIndex , colIndex , item , e , record , row){

        var contentPanel, className, ViewClass, cmp,bugID;

        if(Ext.ComponentQuery.is(obj,"button")){
            var selList = obj.findParentByType("grid").getSelectionModel().getSelection();
            if (selList.length === 0 ) {
                Ext.Msg.alert("提示", "请选择要查看的记录");
                return;
            }
            if (selList.length > 1) {
                Ext.Msg.alert("提示", "请您仅选择一条记录进行查看");
                return;
            }
            bugID = selList[0].data.bugID;
        }else{
            bugID = record.get('bugID');
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        className = pageResSet["jsClassName"];
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","OperateType":"QF","comParams":{"bugID":"'+bugID+'"}}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                delete formData.files;
                form.setValues(formData);
                var grid = panel.down('grid[name=attachmentGrid]');
                var store = grid.getStore();
                store.tzStoreParams ='{"bugID":"'+formData.bugID+'","queryType":"FILE"}';
                store.load();
            });
        });

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    addAttach : function(file, value, attachmentType){

        attachmentType = 'ATTACHMENT';
        var form = file.findParentByType("form").getForm();


        if(value != ""){
            //如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
            var dateStr = Ext.Date.format(new Date(), 'Ymd');
            var upUrl = "";

            if(attachmentType=="ATTACHMENT"){
                //upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
                upUrl = "/linkfile/FileUpLoad/attachment/bug/";
                if(upUrl==""){
                    Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
                    return;
                }else{
                    if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                        upUrl = '/UpdServlet?filePath='+upUrl+dateStr;
                    }else{
                        upUrl = '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                    }
                }
            }
            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                //url: '/UpdServlet?filePath=/linkfile/FileUpLoad/imagesWall',
                url: upUrl,
                //waitMsg: '图片正在上传，请耐心等待....',
                success: function (form, action) {
                    var tzParams;
                    var picViewCom;

                    tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
                    tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_INFO_STD","OperateType":"HTML","comParams":' + tzParams +'}';

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

                                if(attachmentType=="ATTACHMENT"){
                                    //var applyItemGrid = this.lookupReference('attachmentGrid');
                                    var applyItemGrid = file.findParentByType("grid")
                                    var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
//                                        "bugID":bugID,
                                        "attachmentID": 'NEXT',
                                        "attachmentSysName": action.result.msg.sysFileName,
                                        "attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
                                        "attachmentUrl": accessPath
                                    });
                                    applyItemGrid.store.insert(0,r);
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
    },
    sendEmail:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_YJDX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }

        var form = btn.findParentByType('panel').child("form").getForm();
        //form中必填校验：NEXT-需要先保存；责任人-确保收件人有值
        if (form.isValid()) {
            var bugID = form.findField("bugID").getValue();
            var resOprID=form.findField("resOprID").getValue();

            if (bugID=='NEXT'){
                Ext.MessageBox.alert("提示","请先保存数据之后再发送邮件！");
                return;
            }
            if (resOprID==""){
                Ext.MessageBox.alert("提示","请先输入责任人、并保存数据之后再发送邮件！");
                return;
            }
            if(form.isDirty()){
                Ext.MessageBox.alert("提示","请先保存数据之后再发送邮件！");
                return;
            }

            //创建听众
            var params = {
                ComID:"TZ_BUGMG_COM",
                PageID:"TZ_BUG_YJDX_STD",
                OperateType:"U",
                comParams:{add:[{type:'EML',resOprID:resOprID,bugID:bugID}]}
            };

            Ext.tzLoad(Ext.JSON.encode(params),function(audID){
                Ext.tzSendEmail({
                    //发送的邮件模板;
                    "EmailTmpName": ["TZ_BUG_PER_EML"],
                    //创建的需要发送的听众ID;
                    "audienceId": audID,
                    //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                    "file": "Y"
                });
            });

        }
    },
    sendEmails:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        var grid=btn.findParentByType("grid");
        //选中行长度
        var checkLen = selList.length;

        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要发送邮件的记录");
            return;
        }else{
            //是否有访问权限
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_YJDX_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
                return;
            }

            //提交参数
            var comParams="";

            //编辑JSON
            var editJson="";

            for(var i=0;i<selList.length;i++){
                if(editJson == ""){
                    editJson = '{"bugID":'+'"'+selList[i].data.bugID+'"}';
                }else{
                    editJson = editJson + ',{"bugID":'+'"'+selList[i].data.bugID+'"}';
                }
            }
            if(editJson != "") {
                comParams = '"selected":[' + editJson + "]";
            }
            var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_LIST_STD","OperateType":"getEmailInfo","comParams":{'+comParams+'}}';

            Ext.tzLoad(tzParams,function(responseData){
                //alert(responseData['EmailTmpName']);
                var emailTmpName = responseData['EmailTmpName'];
                var arrEMLTmpls = new Array();
                arrEMLTmpls=emailTmpName.split(",");

                var audienceId = responseData['audienceId'];

                Ext.tzSendEmail({
                    //发送的邮件模板;
                    "EmailTmpName":arrEMLTmpls,
                    //创建的需要发送的听众ID;
                    "audienceId": audienceId,
                    //是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
                    "file": "Y"
                });
            });
        }
    },
    sendBugAssignEmails:function(btn){
    //选中行
    var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
    var grid=btn.findParentByType("grid");
    //选中行长度
    var checkLen = selList.length;

    if(checkLen == 0){
        Ext.Msg.alert("提示","请选择要发送邮件的记录");
        return;
    }else{
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_YJDX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }

        //提交参数
        var comParams="";

        //编辑JSON
        var editJson="";

        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = '{"bugID":'+'"'+selList[i].data.bugID+'"}';
            }else{
                editJson = editJson + ',{"bugID":'+'"'+selList[i].data.bugID+'"}';
            }
        }
        if(editJson != "") {
            comParams = '"selected":[' + editJson + "]";
        }
        var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_LIST_STD","OperateType":"getBugAssignEmailInfo","comParams":{'+comParams+'}}';

        Ext.tzLoad(tzParams,function(responseData){
            //alert(responseData['EmailTmpName']);
            var emailTmpName = responseData['EmailTmpName'];
            var arrEMLTmpls = new Array();
            arrEMLTmpls=emailTmpName.split(",");

            var audienceId = responseData['audienceId'];

            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName":arrEMLTmpls,
                //创建的需要发送的听众ID;
                "audienceId": audienceId,
                //是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "Y"
            });
        });
    }
},
    sendBugRetestEmails:function(btn){
    //选中行
    var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
    var grid=btn.findParentByType("grid");
    //选中行长度
    var checkLen = selList.length;

    if(checkLen == 0){
        Ext.Msg.alert("提示","请选择要发送邮件的记录");
        return;
    }else{
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_YJDX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }

        //提交参数
        var comParams="";

        //编辑JSON
        var editJson="";

        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = '{"bugID":'+'"'+selList[i].data.bugID+'"}';
            }else{
                editJson = editJson + ',{"bugID":'+'"'+selList[i].data.bugID+'"}';
            }
        }
        if(editJson != "") {
            comParams = '"selected":[' + editJson + "]";
        }
        var tzParams = '{"ComID":"TZ_BUGMG_COM","PageID":"TZ_BUG_LIST_STD","OperateType":"getBugRetestEmailInfo","comParams":{'+comParams+'}}';

        Ext.tzLoad(tzParams,function(responseData){
            //alert(responseData['EmailTmpName']);
            var emailTmpName = responseData['EmailTmpName'];
            var arrEMLTmpls = new Array();
            arrEMLTmpls=emailTmpName.split(",");

            var audienceId = responseData['audienceId'];

            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName":arrEMLTmpls,
                //创建的需要发送的听众ID;
                "audienceId": audienceId,
                //是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "Y"
            });
        });
    }
},
    clearFilter:function(btn){
        btn.findParentByType('grid').filters.clearFilters();
    },
    viewBugProg:function(btn){
        var form=btn.findParentByType("form").getForm();
        var formParams = form.getValues();
        var bugID=form.findField("bugID").getValue();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BUGMG_COM"]["TZ_BUG_PROG_STD"];
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
        store.comID="TZ_BUGMG_COM";
        grid.store.tzStoreParams = tzStoreParams;
        grid.store.load();
        if(form.isDirty()){
            var formdata = form.getValues();
            form.setValues(formdata);
        }
        win.show();

    },
    queryUnassigned:function(btn){
        var store=btn.findParentByType("grid").getStore();
        store.clearFilter();
        var condition=new Ext.util.Filter({
            filterFn: function(item) {
                return item.get('resOpr') =='空'&&(item.get('bugStatus') =='已分配'||item.get('bugStatus') =='重新打开'||item.get('bugStatus') =='新建');
            }
        });
        store.addFilter(condition);
    },
    queryNotUpdated:function(btn){
        var store=btn.findParentByType("grid").getStore();
        //清除filter
        store.clearFilter();
        btn.findParentByType("grid").clearFilters();
        //截止更新时间点
        var time1=Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1).toLocaleDateString()+" "+Ext.tzGetHardcodeValue("TZ_GD_BUG_UNUP_DLTM")+":00","Y-m-d H:i:s");
        //实际更新时间
        var bugUpdateDate,time;
        var condition=new Ext.util.Filter({
            filterFn: function(item) {
                //拼接实际更新时间
                bugUpdateDate=item.get('bugUpdateDate');
                if(bugUpdateDate){
                    time=Ext.util.Format.date(new Date(item.get('bugUpdateDate').toLocaleDateString()+" "+item.get('bugUpdateTime')+":00"),"Y-m-d H:i:s");
                    return time <time1&&(item.get('bugStatus') =='已分配'||item.get('bugStatus') =='重新打开'||item.get('bugStatus') =='新建');
                }else{
                return item.get('bugStatus') =='已分配'||item.get('bugStatus') =='重新打开'||item.get('bugStatus') =='新建';}
            }
        });
        store.addFilter(condition);
    },
    updateRecord:function(grid,flg,formData){
        var store=grid.getStore();
        var bugID=formData.bugID;
        if(flg=='N'){
            var index=store.find('bugID',bugID);
            var rec=grid.store.getAt(index);
            if(rec){
//                store.getAt(index).set('bugName',formData.bugName);
                rec.set('bugName',formData.bugName);
                rec.set('module',formData.bugGNMK);
                rec.set('bugPercent',formData.bugPercent);
                rec.set('recDate',formData.recDate);
                rec.set('estDate',formData.estDate);
                rec.set('expDate',formData.expDate);
                rec.set('recOpr',formData.recOprName);
                rec.set('resOpr',formData.resOprName);
                switch (formData.priority)
                {
                    case '0':
                        rec.set('bugPrior','P0');
                        break;
                    case '1':
                        rec.set('bugPrior','P1');
                        break;
                    case '2':
                        rec.set('bugPrior','P2');
                        break;
                };
                switch (formData.bugType)
                {
                    case '0':
                        rec.set('bugType','Bug');
                        break;
                    case '1':
                        rec.set('bugType','工作任务');
                        break;
                };
                switch (formData.bugMigration)
                {
                    case '0':
                        rec.set('bugMigration','已迁移UAT但未测试');
                        break;
                    case '1':
                        rec.set('bugMigration','UAT环境测试通过');
                        break;
                    case '2':
                        rec.set('bugMigration','已迁移生产');
                        break;
                };
                switch (formData.status)
                {
                    case '0':
                        rec.set('bugStatus','新建');
                        break;
                    case '1':
                        rec.set('bugStatus','已分配');
                        break;
                    case '2':
                        rec.set('bugStatus','已完成(待审核)');
                        break;
                    case '3':
                        rec.set('bugStatus','关闭');
                        break;
                    case '4':
                        rec.set('bugStatus','重新打开');
                        break;
                    case '5':
                        rec.set('bugStatus','取消');
                        break;
                    case '6':
                        rec.set('bugStatus','暂时搁置');
                        break;
                };
                rec.commit();
                store.commitChanges();
            }
        }
        else{
//            store.reload();
             var bugPrior;
             switch (formData.priority)
             {
                 case '0':
                     bugPrior='P0';
                     break;
                 case '1':
                     bugPrior='P1';
                     break;
                 case '2':
                     bugPrior='P2';
                     break;
             };
             var bugType;
             switch (formData.bugType)
             {
                 case '0':
                     bugType='Bug';
                     break;
                 case '1':
                     bugType='工作任务';
                     break;
             };
             var bugMigration;
             switch (formData.bugMigration)
             {
                 case '0':
                     bugMigration='已迁移UAT但未测试';
                     break;
                 case '1':
                     bugMigration='UAT环境测试通过';
                     break;
                 case '2':
                     bugMigration='已迁移生产';
                     break;
             };
             var bugStatus;
             switch (formData.status)
             {
                 case '0':
                     bugStatus='新建';
                     break;
                 case '1':
                     bugStatus='已分配';
                     break;
                 case '2':
                     bugStatus='已完成(待审核)';
                     break;
                 case '3':
                     bugStatus='关闭';
                     break;
                 case '4':
                     bugStatus='重新打开';
                     break;
                 case '5':
                     bugStatus='取消';
                     break;
                 case '6':
                     bugStatus='暂时搁置';
                     break;
             };
             var rec = new KitchenSink.view.bugManagement.bugMg.bugModel({
                 orgID: Ext.tzOrgID,
                 bugID:formData.bugID,
                 bugName:formData.bugName,
                 bugStatus:bugStatus,
                 bugType:bugType,
                 bugPrior:bugPrior,
                 bugPercent:formData.bugPercent,
                 bugUpdateDate:'',
                 bugUpdateTime:'',
                 bugUpdateDTTM:'',
                 recOpr:formData.recOprName,
                 recDate:formData.recDate,
                 estDate:formData.estDate,
                 resOpr:formData.resOprName,
                 expDate:formData.expDate,
                 bugMigration:bugMigration,
                 module:formData.bugGNMK
             });
             store.addSorted(rec);
             setTimeout(function(){
                 store.commitChanges();
             },400);
        };
    }
}) ;
