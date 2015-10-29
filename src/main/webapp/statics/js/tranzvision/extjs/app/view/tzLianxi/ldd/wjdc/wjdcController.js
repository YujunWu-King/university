//Ext.syncRequire("KitchenSink.view.tzLianxi.ldd.wjdc.myDcwjWindow");
Ext.define('KitchenSink.view.tzLianxi.ldd.wjdc.wjdcController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wjdcController',

    /*查询调查问卷*/
    findDcwj:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_ZXDC_WJ_VW', //这里面的组件页面视图需要换成自己的
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    /*新增调查问卷*/
    addDcwj:function(btn){
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_XJWJ_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_XJWJ_STD，请检查配置。');
            return;
        }
        var win =this.lookupReference('myDcwjWindow');
        if (!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }else{
            var activeTab = win.items.items[0].getActiveTab();
            document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
        }
        win.show();
        window.wjdc_pre=null;//目的是让他每次都执行下面的操作
        if (!window.wjdc_pre) {
            window.wjdc_pre = function(el) {
                Ext.each(Ext.query(".tplitem"),
                    function(i) {
                        this.style.backgroundColor = null
                    });
                el.style.backgroundColor = "rgb(173, 216, 230)";
                var activeTab = win.items.items[0].getActiveTab();
                var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
                document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
            }
        }
    },
    /*关闭新增窗口*/
    onNewClose:function(btn){
        var win=btn.findParentByType("window");
        win.close();
    },
    /*新增窗口保存*/
    onNewWjEnsure:function(btn){
        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        //组件注册信息列表
        var grid = btn.findParentByType("wjdcInfo");
        //组件注册信息数据
        var store = grid.getStore();
        var win = this.lookupReference('myDcwjWindow');
        console.log(win);
        var activeTab = win.items.items[0].getActiveTab(),
            id = '';
        var wjbt = Ext.get(activeTab.id).select('input').elements[0].value,
            wjId = "";
        if (activeTab.itemId == "add") { //新增
            var form = activeTab.getForm();
        };
        if (activeTab.itemId == "predefine") { //从现有模板复制
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    if (this.style.backgroundColor == "rgb(173, 216, 230)") {
                        wjId = this.getAttribute("data-id");  //这里获得的id实际上是模板id
                        return false;
                    }
                });
        } else {
            wjId = "";
        }
        if (wjbt) {
            var tzStoreParams = '{"add":[{"id":"' + wjId + '","name":"' + wjbt + '"}]}';
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_XJWJ_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
            Ext.tzSubmit(tzParams,
                function(jsonObject) {
                  store.reload();
                  win.close();
                },"",true,this);
        }
    },
    /*删除问卷调查 */
    deleteWjdc:function(btn){
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
    /*问卷调查列表保存*/
    wjdcInfoSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
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
        console.log(comParams);
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    /*复制问卷调查*/
    copyWjdc:function(view,rowindex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowindex);
        this.TZ_DC_WJ_ID = selRec.get("TZ_DC_WJ_ID");
        Ext.MessageBox.prompt('复制调查问卷', '请输入另存问卷的名称:', this.showResultText, this);
    },
    showResultText: function(id, text) {

        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        if (id == "ok") {
            if (text) {
                //组件注册信息数据
                var store = this.getView().getStore();
                var tzStoreParams = '{"add":[{"id":"' + this.TZ_DC_WJ_ID + '","name":"' + text + '"}]}';
                var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM" ,"PageID":"TZ_ZXDC_XJWJ_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
                Ext.tzSubmit(tzParams,
                    function(jsonObject) {
                        store.reload();
                    },"",true,this);
            } else {
                /*模板名称不能为空*/
                Ext.MessageBox.alert('提示', '新的问卷调查名称不能为空！');
                return;
            }
        }
        return;
    },
    /*设置问卷调查*/
    setWjdc:function(view,rowindex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowindex);
        var wjId = selRec.get("TZ_DC_WJ_ID");
        //显示资源集合信息编辑页面
        this.editWjdcByID(wjId);
    },
    /*根据问卷id设置问卷*/
    editWjdcByID:function(wjId){
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_WJSZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_WJSZ_STD，请检查配置。');
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        var win = this.lookupReference('myDcwjSzWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();
            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJSZ_STD","OperateType":"QF","comParams":{"wjId":"'+wjId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
            if(formData.TZ_DC_WJ_DLZT=='Y'){
                form.findField("TZ_DC_WJ_DLZT").setValue(true)  ;
            }else{
                form.findField("TZ_DC_WJ_DLZT").setValue(false)  ;
            }

            if(formData.TZ_DC_WJ_NEEDPWD=='Y'){
                form.findField("TZ_DC_WJ_NEEDPWD").setValue(true);
                ///form.findField("TZ_DC_WJ_DLZT").setHidden(true)  ;
            }else{
                form.findField("TZ_DC_WJ_NEEDPWD").setValue(false);
            }
        });

        win.show();
    },
    /*问卷调查设置下面的问卷模板*/
    wjmb_mbChoice:function(btn){
        var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        searchDesc="报名表模板";
        modal="TZ_APP_TPL_ID";
        modal_desc="TZ_APP_TPL_MC";
        var form = btn.findParentByType('window').child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_DC_DY_T',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_EFFEXP_ZT:{
                        value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_APP_TPL_ID:{
                        desc:'问卷模板ID',
                        operator:'01',
                        type:'01'
                    },
                    TZ_APP_TPL_MC:{
                        desc:'问卷模板名称',
                        operator:'01',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_APP_TPL_ID: '问卷模板ID',
                TZ_APP_TPL_MC: '问卷模板名称'
            },
            multiselect: false,
            callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_APP_TPL_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_APP_TPL_MC);
            }
        });
    },
    /*数据导出（导出）调查问卷----直接调用数据导出功能接口即可*/
    downloadDcwj:function(btn,rowindex) {
        var grid=btn.findParentByType('grid');
        grid.saveDocumentAs({
            type: 'excel',
            title: '问卷调查',
            fileName: '问卷调查.xls'
        })
    },
    checkBoxAction: function(checkbox,checked){
        var dtgz=Ext.getCmp("dtgz"); //答题规则
        var sjcjgz=Ext.getCmp("sjcjgz");//数据采集规则
        var dcfs=Ext.getCmp("dcfs");//调查方式
       if(checked){ //如果选中，就隐藏掉
           dtgz.items.items[2].setHidden(true);
           sjcjgz.items.items[3].setHidden(true);
           dcfs.setHidden(true);
       } else{
            dtgz.items.items[2].setHidden(false);
            sjcjgz.items.items[3].setHidden(false);
            dcfs.setHidden(false);
        }
    },
    /*问卷调查密码，默认隐藏，如果勾选，就表示需要密码，密码框显示*/
    needPwdFun:function(checkbox,checked){
        var pwd=Ext.getCmp('TZ_DC_WJ_PWD');
        if(checked){
          pwd.setHidden(false);
        }else{
        pwd.setHidden(true);
        }
    },
    publishWjdc:function(){
        alert("问卷发布");
    },
    editWjdc:function(){
        alert("问卷编辑");
    },
    jinDuBB:function(){
    alert("进度报表");
},

    pinShuBB:function(){
        alert("频数报表");
    },
    jiaoChaBB:function(){
        alert("交叉报表");
    }
});

