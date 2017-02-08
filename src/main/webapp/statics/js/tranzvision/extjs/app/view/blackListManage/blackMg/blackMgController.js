Ext.define('KitchenSink.view.blackListManage.blackMg.blackMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.blackMg',
    createUserInfoClass: function(){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BLACK_LIST_COM"]["TZ_BLACK_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BLACK_INFO_STD，请检查配置。');
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
    addUserAccount: function(btn){
    	
    	var grid=btn.findParentByType("grid");
    	var store = grid.getStore();

        Ext.tzShowPromptSearch({
            recname: 'TZ_BLACK_USER_V',
            searchDesc: '新增黑名单人员',
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
//                        value:'SEM',
                        type: '01'
                    }
                },
                srhConFields:{
                    OPRID:{
                        desc:'用户编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'姓名',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                OPRID: '编号',
                TZ_REALNAME: '姓名'
            },
            multiselect: false,
            callback: function(selection){
                var oprid=selection[0].data.OPRID;
//                console.log(oprid);
                    var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_ADD_STD","OperateType":"U","comParams":{"update":[{"OPRID":"'+oprid+'"}]}}';
//                    console.log(tzParams);
                    //加载数据
                    Ext.tzSubmit(tzParams,function(){
                        store.reload();
                    },"",true,this);
            }
        });
    },
    editUserAccount: function() {

        var roleGrid = this.getView();

        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        var OPRID = selList[0].get("OPRID");
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        var cmp = this.createUserInfoClass();

        cmp.on('afterrender',function(){
            var msgForm = this.lookupReference('userMgForm');
            var form = this.lookupReference('userMgForm').getForm();
            var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]');

            var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_INFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                var formData = responseData.formData;

                form.setValues(formData);

                var userInfoItems = [];

                var fields = formData.column;
                var size = fields.length;
                typeField = {};
                for(var i = 0;i < size;i++){
                    var field = fields[i];
                    var fieldLabel,name,value;
                    for(var fieldName in field){
                        if(fieldName == "desc"){
                            fieldLabel = field["desc"];
                        }else{
                            name = fieldName;
                            value = field[fieldName];
                        }
                    }
                    typeField = {
                        xtype: 'textfield',
                        fieldLabel: fieldLabel,
                        readOnly:true,
                        name: name,
                        value: value,
                        fieldStyle:'background:#F4F4F4',
                    }
                    userInfoForm.add(typeField);
                }
                if(msgForm.down('hiddenfield[name=titleImageUrl]').getValue()){
                    msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + msgForm.down('hiddenfield[name=titleImageUrl]').getValue());
                }else{
                    msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
                }
            });

        });

        tab = contentPanel.add(cmp);

        tab.on(Ext.tzTabOn(tab,this.getView(),cmp));

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },

    editSelUserAccount: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);

        var OPRID = selRec.get("OPRID");

        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        var cmp = this.createUserInfoClass();

        cmp.on('afterrender',function(){
            var msgForm = this.lookupReference('userMgForm');
            var form = this.lookupReference('userMgForm').getForm();
            var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]');

            var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_INFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                var formData = responseData.formData;

                form.setValues(formData);

                var userInfoItems = [];

                var fields = formData.column;
                var size = fields.length;
                typeField = {};
                for(var i = 0;i < size;i++){
                    var field = fields[i];
                    var fieldLabel,name,value;
                    for(var fieldName in field){
                        if(fieldName == "desc"){
                            fieldLabel = field["desc"];
                        }else{
                            name = fieldName;
                            value = field[fieldName];
                        }
                    }
                    typeField = {
                        xtype: 'textfield',
                        fieldLabel: fieldLabel,
                        readOnly:true,
                        name: name,
                        value: value,
                        fieldStyle:'background:#F4F4F4',
                    }
                    userInfoForm.add(typeField);
                }
                if(msgForm.down('hiddenfield[name=titleImageUrl]').getValue()){
                    msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + msgForm.down('hiddenfield[name=titleImageUrl]').getValue());
                }else{
                    msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
                }
            });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }

    },
    deleteUserAccounts: function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var userStore = this.getView().store;
                    userStore.remove(selList);
                }
            },this);
        }
    },
    deleteUserAccount: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveUserInfos: function(btn){
        //用户账号信息列表
        var grid = btn.findParentByType("grid");
        //用户账号信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
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
            //提交参数
            var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
                store.reload();
                grid.commitChanges(grid);
            },"",true,this);
        }else{
            Ext.Msg.alert("提示","保存成功");
        }
    },
    closeUserInfos: function(btn){
        //用户账号信息列表
        var grid = btn.findParentByType("grid");
        grid.close();
    },
    ensureUserInfos: function(btn){
        //用户账号信息列表
        //用户账号信息列表
        var grid = btn.findParentByType("grid");
        //用户账号信息数据
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
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
            //提交参数
            var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){

                grid.commitChanges(grid);
                grid.close();

            },"",true,this);
        }else{
            grid.close();
        }
    },
    saveDataInfo: function(){
        var win =this.getView();

        //页面注册信息表单
        var form = this.getView().child('form').getForm();

        //表单数据
        var formParams = form.getValues();

        
        win.actType = "update";

        //提交参数
        var tzParams = '{"ComID":"TZ_BLACK_LIST_COM","PageID":"TZ_BLACK_INFO_STD","OperateType":"U","comParams":{"'+win.actType+'":[{"data":'+Ext.JSON.encode(formParams)+'}]}}';

        Ext.tzSubmit(tzParams,function(){
        	 var interviewMgrPanel = Ext.ComponentQuery.query("panel[reference=blackMgPanel]");
             interviewMgrPanel[0].getStore().reload();
        },"",true,this);
    },
    onFormClose: function(){
        this.getView().close();
    },
    onFormSave:function(){
        this.saveDataInfo();
    },
    onFormEnsure:function(){
        this.saveDataInfo();

        this.getView().close();


    },
    searchUserList: function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BLACK_LIST_COM.TZ_BLACK_LIST_STD.TZ_BLACK_LIST_V',
            condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});