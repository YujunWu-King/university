Ext.define('KitchenSink.view.security.plst.plstController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.plstMg',
    addPermission: function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

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

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editPermission: function() {
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
        //许可权编号
        var permID = selList[0].get("permID");
        //显示许可权信息编辑页面
        this.editPlstInfoByID(permID);
    },
    editCurrPermission: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //许可权ID
        var permID = selRec.get("permID");
        //显示许可权信息编辑页面
        this.editPlstInfoByID(permID);
    },
    editPlstInfoByID: function(permID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

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
        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('afterrender',function(panel){
            //许可权表单信息;
            var form = panel.child('form').getForm();
            form.findField("permID").setReadOnly(true);
            form.findField("permID").setFieldStyle('background:#F4F4F4');
            //授权组件列表
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_INFO_STD","OperateType":"QF","comParams":{"permID":"'+permID+'"}}';
            //加载数据
                Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //资源集合信息列表数据
                var roleList = responseData.listData;

                var tzStoreParams = '{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V","condition":{"CLASSID-operator": "01","CLASSID-value": "'+permID+'"}}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deletePermissions: function(){
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
                    var resSetStore = this.getView().store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    deleteCurrPermission: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    savePlst: function(btn){
        //许可权列表
        var grid = btn.findParentByType("grid");

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
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_AQ_PLST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    onPlstInfoSave: function(btn){
        //许可权信息表单
        var form = this.getView().child("form").getForm();
        var grid = this.getView().child("grid");
        var store = grid.getStore();

        if (form.isValid()) {
            //获取许可权信息参数
            var tzParams = this.getPlstInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                if(tzParams.indexOf('"typeFlag":"COM"')>-1||tzParams.indexOf("delete")>-1){
                    store.reload();
                }
                form.findField("permID").setReadOnly(true);
            },"",true,this);
        }
    },
    onPlstInfoEnsure: function(btn){
        //许可权信息表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取许可权信息参数
            var tzParams = this.getPlstInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getPlstInfoParams: function(){
        //许可权信息表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"PERM","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"PERM","data":'+Ext.JSON.encode(formParams)+'}';
        }

        //授权组件列表
        var grid = this.getView().child("grid");
        //授权组件数据
        var store = grid.getStore();

        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

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
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onPlstInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    addPlstCom:function(btn){
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存许可权信息后，再新增授权组件。");
            return;
        }
            var form = this.getView().child("form").getForm();
            var grid = this.getView().child("grid");
            var store = grid.store;
            Ext.tzShowPromptSearch({
                recname: 'TZ_AQ_COMZC_TBL',
                searchDesc: '新增授权组件',
                maxRow:20,
                condition:{
                    srhConFields:{
                        TZ_COM_ID:{
                            desc:'组件ID',
                            operator:'07',
                            type:'01'
                        },
                        TZ_COM_MC:{
                            desc:'组件名称',
                            operator:'07',
                            type:'01'
                        }
                    }
                },
                srhresult:{
                    TZ_COM_ID: '组件ID',
                    TZ_COM_MC: '组件名称'
                },
                multiselect: false,
                callback: function(selection){
                    var perID=form.findField("permID").getValue();
                    var comID=selection[0].data.TZ_COM_ID;
                    var comName=selection[0].data.TZ_COM_MC;

                        var plstComModal = Ext.create('KitchenSink.view.security.plst.plstComModel',{
                            permID:perID,
                            comID:comID,
                            comName:comName
                        });
//                        if(store.findRecord("comID",selection[0].data.TZ_COM_ID) == null){
//                            store.add(plstComModal);
//                        }
                    /*进入授权组件页面编辑窗口*/
                    me.editPlstComFromWindow(perID,comID,comName);
                }
            });
        },
    deleteCurrPlstCom: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    editCurrPlstCom: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var permID = selRec.get("permID");
        var comID = selRec.get("comID");
        var comName = selRec.get("comName");
        this.editPlstComByID(permID,comID,comName);
    },
    editPlstCom:function(btn) {
        //选中行
        var selList =this.getView().child("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        var permID = selList[0].get("permID");
        var comID = selList[0].get("comID");
        var comName = selList[0].get("comName");
        this.editPlstComByID(permID,comID,comName);
    },
    editPlstComByID: function(permID,comID,comName){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_COM_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_COM_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('comPageWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'comID', value:comID},
                {id:'comName', value:comName}
            ]
        );
        var tzStoreParams = '{"permID":"'+permID+'","comID":"'+comID+'","comName":"'+comName+'"}';
        grid.store.tzStoreParams = tzStoreParams;
        grid.store.load();

        win.show();
    },

    /*选择授权组件之后进入页面许可权编辑页面*/
    editPlstComFromWindow:function(permID,comID,comName){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_PLST_COM"]["TZ_PLST_COM_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PLST_COM_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('comPageWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //参数
        var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_COM_STD","OperateType":"QF","comParams":{"permID":"'+permID+'","comID":"'+comID+'","comName":"'+comName+'"}';

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'comID', value:comID},
                {id:'comName', value:comName}
            ]
        );

        var tzStoreParams = '{"permID":"'+permID+'","comID":"'+comID+'","comName":"'+comName+'"}';
        grid.store.tzStoreParams = tzStoreParams;
        grid.store.load();
        win.show();
    },

    deletePlstComs: function(btn){
        var grid = this.getView().child("grid");
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    onPlstComInfoSave: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        var grid= win.child("grid");
        if (form.isValid()) {
            /*保存资源信息*/
            var ret=this.savePlstComInfo(win);
            win.actType = "update"
            if(ret){
                grid.store.reload();
            }
        }
    },
    onPlstComInfoEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //资源信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存资源信息*/
            this.savePlstComInfo(win);
            //关闭窗口
            win.close();
        }
    },
    savePlstComInfo: function(win){
        //资源信息表单
        var form = win.child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";

        //授权组件页面列表
        var grid = win.child("grid");
        //授权组件页面数据
        var store = grid.getStore();
        //授权组件页面新增或者修改记录
        var gridEditJson = "";
        var mfRecs;
        if(actType=="update"){
            mfRecs = store.getModifiedRecords();
            for(var i=0;i<mfRecs.length;i++){
                if(gridEditJson == ""){
                    gridEditJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
                }else{
                    gridEditJson = gridEditJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
                }
            }
        };
        if(actType=="add"){
            mfRecs = store;
            for(var j=0;j<store.getCount();j++){
                if(gridEditJson == ""){
                    gridEditJson = '{"data":'+Ext.JSON.encode(store.getAt(j).data)+'}';
                }else{
                    gridEditJson = gridEditJson + ',{"data":'+Ext.JSON.encode(store.getAt(j).data)+'}';
                }
            }
        };

        if(gridEditJson != ""){
                comParams = '"update":[' + gridEditJson + "]";
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_AQ_PLST_COM","PageID":"TZ_PLST_COM_STD","OperateType":"U","comParams":{'+comParams+'}}';

        //提交参数
        var panel = win.findParentByType("panel");
        var permForm = panel.child("form").getForm();
        var pageGrid = panel.child("grid");
        if(gridEditJson!=""){
            Ext.tzSubmit(tzParams,function(){
                if(actType=="add"){
                    var permID = permForm.findField("permID").getValue();
                    var tzStoreParams = '{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V","condition":{"CLASSID-operator": "01","CLASSID-value": "'+permID+'"}}';
                    pageGrid.store.tzStoreParams = tzStoreParams;
                    pageGrid.store.load();

                };
            },"",true,this);
            return true;
       }else{
            return false;
        }
    },
    queryPermission:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_AQ_PLST_COM.TZ_AQ_PLST_STD.PSCLASSDEFN_SRC',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    queryPlstCom:function(btn){
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存许可权信息并添加授权组件后，再查询授权组件。");
            return;
        }

        var form = this.getView().child("form").getForm();
        var permID = form.findField("permID").getValue();
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V',
            condition :{CLASSID:permID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //确定
    ensurePlst:function(btn) {
        this.savePlst(btn);
        this.closePlst(btn);
    },
    //关闭
    closePlst: function(btn){
        //关闭
        var grid = btn.findParentByType("grid");
        grid.close();
    }
});

