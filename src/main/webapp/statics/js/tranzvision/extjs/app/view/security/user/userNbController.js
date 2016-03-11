Ext.define('KitchenSink.view.security.user.userNbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.userNbMg',
    createUserAccClass: function(){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_NB_YHZHGL_COM"]["TZ_NB_YHZHXX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_NB_YHZHXX_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        //contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        //contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.security.user.userInfoPanel';
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
    addUserAccount: function() {
        var roleGrid = this.getView();
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建用户账号信息类
        var cmp = this.createUserAccClass();
        //操作标志
        cmp.actType = "add";

        cmp.on('afterrender',function(){
            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
            //用户账号信息表单
            var form = this.lookupReference('userAccountForm').getForm();
            //当前用户的机构编号
            //var jhState = "Y";
           // form.setValues({"orgId": Ext.tzOrgID, "jhState": jhState });
           // form.findField("jhState").setValue("Y");
           // form.findField("jhMethod").setValue("R");
           // form.findField("rylx").setValue("NBYH");
           // form.findField("orgId").setValue(Ext.tzOrgID);
           // var tzStoreParams = '{"usAccNum":"","orgId":"'+Ext.tzOrgID+'"}';
            //grid.store.tzStoreParams = tzStoreParams;
            //grid.store.load();
            
    	
            //参数
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"","orgId":"'+Ext.tzOrgID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                form.setValues(responseData);

                var tzStoreParams = '{"usAccNum":"","orgId":"'+Ext.tzOrgID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
                
            });
        });

        //var grid = this.lookupReference('userRoleGrid');

        cmp.on('close',function(panel){
            try{
                roleGrid.store.reload();
            }catch(e){

            }

        });

        tab = contentPanel.add(cmp);
        
				//设置tab页签的beforeactivate事件的监听方法
    tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
    
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }


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
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建用户账号信息类
        var cmp = this.createUserAccClass();
        //操作标志
        cmp.actType = "update";

        //登录账号
        var usAccNum = selList[0].get("usAccNum");
        //机构编号
        var orgID = selList[0].get("orgId");

        cmp.on('afterrender',function(){
            //用户账号信息表单
            var form = this.lookupReference('userAccountForm').getForm();
            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
            form.findField("usAccNum").setReadOnly(true);
            form.findField("usAccNum").setFieldStyle('background:#F4F4F4');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                /*
                if (responseData['acctLock']==true){
                    responseData['acctLock']="1";
                }else{
                    responseData['acctLock']="0";
                }  */
                form.setValues(responseData);

                var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });


        cmp.on('close',function(panel){
            try{
                roleGrid.store.reload();
            }catch (e){
            }
        });

        tab = contentPanel.add(cmp);

    tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
    
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editSelUserAccount: function(view, rowIndex) {

        var roleGrid = view.findParentByType("grid");
        var store = roleGrid.store;
        var selRec = store.getAt(rowIndex);

        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //新建用户账号信息类
        var cmp = this.createUserAccClass();
        //操作标志
        cmp.actType = "update";

        //登录账号
        var usAccNum = selRec.get("usAccNum");
        //机构编号
        var orgID = selRec.get("orgId");

        cmp.on('afterrender',function(){
            //用户账号信息表单
            var form = this.lookupReference('userAccountForm').getForm();
            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
            form.findField("usAccNum").setReadOnly(true);
            //参数
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                form.setValues(responseData);

                var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });

        cmp.on('close',function(panel){
            try{
                roleGrid.store.reload();
            }catch (e){
                /*do nothing*/
            }
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    viewUserAccount: function(){
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
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        //新建用户账号信息类
        var cmp = this.createUserAccClass();
        //登录账号
        var usAccNum = selList[0].get("usAccNum");
        //机构编号
        var orgID = selList[0].get("orgId");

        cmp.on('afterrender',function(){
            //用户账号信息表单
            var form = this.lookupReference('userAccountForm').getForm();
            //用户角色信息列表
            var grid = this.lookupReference('userRoleGrid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHXX_STD","OperateType":"QF","comParams":{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                form.setValues(responseData);

                var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

            //隐藏保存按钮
            cmp.getDockedItems()[0].items.get(0).hide();
            //隐藏确定按钮
            cmp.getDockedItems()[0].items.get(1).hide();
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
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
            var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
            	
              grid.commitChanges(grid);
              grid.close();
     
            },"",true,this);
        }else{
        	grid.close();
        }
        
        
    },
    resetPassword: function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要重置密码的记录");
            return;
        }
        //重置密码窗口
        var win = this.lookupReference('setNbPasswordWindow');

        if (!win) {
            className = 'KitchenSink.view.security.user.setNbPassword';
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        win.show();
    },
    lockUserAccount: function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要锁定的记录");
            return;
        }
        Ext.MessageBox.confirm('确认', '您确定要锁定所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                //选中数据参数
                var comParams = "";
                var editJson = "";
                for(var i=0;i<checkLen;i++){
                    if(editJson == ""){
                        editJson = Ext.JSON.encode(selList[i].data);
                    }else{
                        editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
                    }
                }
                comParams = '"data":[' + editJson + "]";
                //提交参数
                var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHGL_STD","OperateType":"LOCK","comParams":{'+comParams+'}}';
                Ext.tzSubmit(tzParams,function(){
                },"锁定账号成功",true,this);
            }
        },this);
    },
    getUserAccInfoParams:function(){
        //用户账号panel
        var userInfo = this.getView();
        //用户账号信息标志
        var actType = userInfo.actType;
        //用户账号信息表单
        var form = userInfo.child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //邮箱绑定标志
        if(formParams["eBindFlag"] == undefined){
            formParams["eBindFlag"] = "N";
        }
        //手机绑定标志
        if(formParams["mBindFlag"] == undefined){
            formParams["mBindFlag"] = "N";
        }
        //账户锁定

        if(formParams["acctLock"] == "1"){
            formParams["acctLock"] = "on";
        } 
        //用户角色列表
        var grid = userInfo.child("grid");
        //角色信息数据
        var store = grid.getStore();

        //修改记录
        //var mfRecs = store.getModifiedRecords();
        //修改json字符串
        var editJson = "";
        //修改json字符串
        var editJson = "";
        editJson = '{"typeFlag":"USER","data":'+Ext.JSON.encode(formParams)+'}';

        var dataItems = store.getData().items;
        var roleArr = [];
        Ext.Array.each(dataItems, function( rec) {
            var roleData = rec.getData();
            // var isRole = roleData.isRole;
            // if(isRole == true){
            roleArr.push(roleData);
            //editJson = editJson + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleData)+'}';
            // }
        });
        editJson = editJson + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(roleArr)+'}';
        //for(var i=0;i<mfRecs.length;i++){
        //	editJson = editJson + ',{"typeFlag":"ROLE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
        //}
        var comParams = '"'+actType+'":[' + editJson + "]";
        //提交参数
        var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //console.log(tzParams);
        return tzParams;
    },
    onFormSave: function(){
        //用户账号表单
        var form = this.getView().child("form").getForm();
        if (!form.isValid()) {//表单校验未通过
            return false;
        }
        //保存用户账号信息参数
        var tzParams = this.getUserAccInfoParams();
        //用户账号panel
        var userInfo = this.getView();
        //用户角色列表
        var grid = userInfo.child("grid");
        //表单数据
        var formParams = form.getValues();
        //登录账号
        var usAccNum = formParams["usAccNum"];
        //机构编号
        var orgID = formParams["orgId"];
        Ext.tzSubmit(tzParams,function(){
            userInfo.actType = "update";
            //登录账号不可修改
            form.findField("usAccNum").setReadOnly(true);
            form.findField("originOrgId").setValue(orgID);
            var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+orgID+'"}';
            grid.store.tzStoreParams = tzStoreParams;
            userInfo.commitChanges(userInfo);
        },"",false,this);
    },
    onFormEnsure: function(){
        //用户账号表单
        var form = this.getView().child("form").getForm();
        if (!form.isValid()) {//表单校验未通过
            return false;
        }
        //保存用户账号信息参数
        var tzParams = this.getUserAccInfoParams();
        //用户账号panel
        var userInfo = this.getView();
        Ext.tzSubmit(tzParams,function(){
            //关闭页面
            userInfo.commitChanges(userInfo);
            userInfo.close();
        },"",false,this);
    },
    onFormClose: function(){
        //关闭用户账号信息页面
        this.getView().close();
    },
    onSetPwdEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //重置密码信息表单
        var form = win.child("form").getForm();
        if (!form.isValid()) {//表单校验未通过
            return false;
        }
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        //表单数据
        var formParams = form.getValues();
        //密码
        var password = formParams["password"];
        //密码参数
        var pwdParams = '"password":"'+password+'"';
        //选中数据参数
        var comParams = "";
        var editJson = "";
        for(var i=0;i<checkLen;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";
        //提交参数
        var tzParams = '{"ComID":"TZ_AQ_NB_YHZHGL_COM","PageID":"TZ_NB_YHZHGL_STD","OperateType":"PWD","comParams":{'+pwdParams+","+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        },"重置密码成功",true,this);
    },
    onSetPwdClose: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //重置密码信息表单
        var form = win.child("form").getForm();
        //关闭窗口
        win.close();
    },
    searchUserList: function(btn){
    	Ext.tzShowCFGSearch({

            cfgSrhId: 'TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHGL_STD.TZ_YHZH_NB_VW',
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