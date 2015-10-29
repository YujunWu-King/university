Ext.define('KitchenSink.view.template.survey.question.wjdcController', {
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
       // console.log(win);
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
        //console.log(comParams);
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
         //   console.log(formData);
            form.setValues(formData);

            if(formData.TZ_DC_WJ_DLZT=='Y'){
                form.findField("TZ_DC_WJ_DLZT").setValue(true)  ;
            }else{
                form.findField("TZ_DC_WJ_DLZT").setValue(false)  ;
            }

            if(formData.TZ_DC_WJ_NEEDPWD=='Y'){
                form.findField("TZ_DC_WJ_NEEDPWD").setValue(true);
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
           dcfs.allowBlank=true;
       } else{

            dtgz.items.items[2].setHidden(false);
            sjcjgz.items.items[3].setHidden(false);
            dcfs.setHidden(false);
            dcfs.allowBlank=false;/*如果没有勾选，调查方式必填；如果没有选中，就不是必填的*/
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
    /*列表上方的发布*/
    publishWjdc:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要发布的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您将发布选中的内容?', function(btnId){
                if(btnId == 'yes'){
                    for(var i = 0;i < selList.length;i++){
                        selList[i].set("TZ_DC_WJ_FB","1");
                    }
                    var tzParams = this.submitWjdcParams("P","发布成功");
                }
            },this);
        }
    },
    /*grid每一行操作类（发布）
    releaseWjdc:function(view,t,rowIndex){
      // btn.setDisabled(true);
        var msg = "";
        var record=view.findParentByType("grid").store.getAt(rowIndex);
        console.log(t );
        record.set("TZ_DC_WJ_FB", "1");
        msg = "发布成功";
        var tzParams = this.submitWjdcParams("P",msg);
    },*/
    releaseWjdc:function(btn,rowIndex){
        // btn.setDisabled(true);
        var msg = "";
        var record=btn.findParentByType("grid").getStore().getAt(rowIndex);
        record.set("TZ_DC_WJ_FB", "1");
        msg = "发布成功";
        var tzParams = this.submitWjdcParams("P",msg);
    },
    //获取修改记录
    submitWjdcParams: function(clickTyp,msg){
        var comParams = "";
        var editJson = "";
        var store = this.getView().getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                //editJson = Ext.JSON.encode(mfRecs[i].data);
                editJson = '{"ClickTyp":"'+clickTyp+'","data":'+ Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                //editJson = editJson + ','+Ext.JSON.encode(mfRecs[i].data);
                editJson = editJson + ',{"ClickTyp":"'+clickTyp+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
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
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },msg,true,this);

    },
    editWjdc:function(){
        alert("问卷编辑");
    },
    getJDBBData:function(){
        Ext.tzSetCompResourses('TZ_ZXDC_JDBB_COM');/*组件之间的跳转，需要哪个组件就把它加载进来*/
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_JDBB_COM"]["TZ_ZXDC_JDBB_STD"];
},
    pinShuBB:function(btn,rowIndex){

        //是否有访问权限
        Ext.tzSetCompResourses("TZ_ZXDC_PSBB_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_PSBB_COM"]["TZ_ZXDC_PSBB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        console.log(pageResSet);
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_PSBB_STD，请检查配置。');
            return;
        }
        console.log(className);
        var contentPanel,cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        console.log(ViewClass);
        clsProto = ViewClass.prototype;
        console.log(clsProto);
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

         var WJID = btn.findParentByType("grid").store.getAt(rowIndex).data.TZ_DC_WJ_ID;
       console.log(WJID);
        cmp = new ViewClass();
        console.log(cmp);
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var PSBBQuestionListGrid = panel.child('grid');
            var tzParams = '{"ComID":"TZ_ZXDC_PSBB_COM","PageID":"TZ_ZXDC_PSBB_STD","OperateType":"QF","comParams":{"onlinedcId":"'+WJID+'"}}';
            //alert(tzParams);
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;

               form.setValues(formData);
                tzParams = '{"ComID":"TZ_ZXDC_PSBB_COM","PageID":"TZ_ZXDC_PSBB_STD","OperateType":"TJWT","comParams":{"onlinedcId":"'+WJID+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    console.log(responseData);
                  PSBBQuestionListGrid.store.add(responseData['root']);
                });
            });
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }




    },
jiaoChaBB:function(grid,rowIndex,colIndex){
        //alert("交叉报表");
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_MS_ARR_MG_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_JCBBLB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_JCBBLB_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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

        var surveyResultGrid = grid;
        var onlinedcId = surveyResultGrid. store.getAt(rowIndex).data.TZ_DC_WJ_ID;
        cmp.on('afterrender',function(panel){
            var jcbbQuestionListForm = panel.child('form').getForm();
            var jcbbQuestionListGrid = panel.child('grid');
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"QF","comParams":{"onlinedcId":"'+onlinedcId+'"}}';
            //alert(tzParams);
            Ext.tzLoad(tzParams,function(responseData){
                jcbbQuestionListForm.setValues(responseData['formData']);
                tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"TJWT","comParams":{"onlinedcId":"'+onlinedcId+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    //jcbbQuestionListGrid.store.loadData(responseData['root']);

                    //var arrQL={"root":[{"questionID":"aaaa","questionDesc":"bbbbb"}]};
                    jcbbQuestionListGrid.store.add(responseData['root']);
                });
            });
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    /*设置页面的发布按钮*/
    onWjdcRelease:function(btn){
        //内容表单

        var win = btn.findParentByType("window");
        var form = win.child("form").getForm();
        if(!form.isValid() ){
            return false;
        }
        //获取内容信息参数
        var comView = this.getView();
        var actType = comView.actType;
        var wjId = form.findField("TZ_DC_WJ_ID").getValue();
        if(actType=="update" && (wjId=="" || typeof(wjId) == "undefined" )){
            Ext.Msg.alert("提示","保存出错");
        }else{
            btn.setDisabled(true);
            form.findField("TZ_DC_WJ_FB").setValue("1");
            form.findField("TZ_DC_WJ_PC_URL").setValue("http://www.baidu.com");
            form.findField("TZ_DC_WJ_MB_URL").setValue("http://www.baidu.com");
            var tzParams = this.getWjdcInfoParams(btn);
            Ext.tzSubmit(tzParams,function(responseData){
                if(actType=="add"){
                    comView.actType = "update";
                    form.findField("TZ_DC_WJ_ID").setValue(responseData.TZ_DC_WJ_ID);
                }
                comView.commitChanges(comView);
            },"发布成功",true,this);

        }

    },
    getWjdcInfoParams: function(btn){
        var win = btn.findParentByType("window");
        var form = win.child("form").getForm();
        //问卷ID;
        var wjId = form.findField("TZ_DC_WJ_ID").getValue();
        var comParams = "";
        var editJson="";
        var clickTyp="P";
        if(editJson == ""){
            //editJson = Ext.JSON.encode(mfRecs[i].data);
            editJson = '{"ClickTyp":"'+clickTyp+'","data":'+ Ext.JSON.encode(form.getValues())+'}';
        }else{
            //editJson = editJson + ','+Ext.JSON.encode(mfRecs[i].data);
            editJson = editJson + ',{"ClickTyp":"'+clickTyp+'","data":'+Ext.JSON.encode(form.getValues())+'}';
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJGL_STD","OperateType":"U","comParams":{'+comParams+'}}';

        return tzParams;
    },
    onLogicalSet:function(view,rowindex){
        var selRec = view.getStore().getAt(rowindex);
      	var tplId = selRec.get("TZ_DC_WJ_ID");
		
		var logicUrl = Ext.tzGetGeneralURL()+'?classid=surveyLogic&TZ_DC_WJ_ID='+tplId;
		 $.layer({
			type: 2,
			title: false,
			fix: true,
			closeBtn: false,
			shadeClose: false,
			icon:2,
			shade : [0.3 , '#000' , true],
			border : [3 , 0.3 , '#000', true],
			offset: ['30%',''],
			area: ['1040px','600px'],
			move : true,
			iframe: {src: logicUrl}
		});
    },
    previewWjdc:function(){
        alert("预览");
    },
    outputData:function(btn,rowIndex){
        /*
        修改人:刘智宏
        功能:数据导出逻辑控制 
        */  
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_ZXDC_DCBB_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_DCBB_COM"]["TZ_ZXDC_DCBB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_DCBB_STD，请检查配置。');
            return;
        }
        var cmp, ViewClass, clsProto;


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

        var WJID = btn.findParentByType("grid").store.getAt(rowIndex).data.TZ_DC_WJ_ID;
        console.log(WJID);
        cmp = new ViewClass();
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_ZXDC_DCBB_COM","PageID":"TZ_ZXDC_DCBB_STD",' +
                '"OperateType":"QF","comParams":{"onlinedcId":"'+WJID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                console.log(respData);
                form.setValues(respData);
                
            });
        });
        cmp.show();
    },
     /*进度报表*/
    jinDuBB:function(grid, rowIndex, colIndex){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_JDBB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_JDBB_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
        //操作类型设置为查询
        //cmp.actType = "query";

        var surveyResultGrid = grid;
        var onlinedcId = surveyResultGrid. store.getAt(rowIndex).data.TZ_DC_WJ_ID;

        cmp.on('afterrender',function(panel){

            var form = panel.child('form').getForm();
            var chart1 = panel.down('chart[name=chart1]');
            var chart2 = panel.down('chart[name=chart2]');
            var chart1store  = chart1.getStore();
            var chart2store  = chart2.getStore();

//            form.findField("onlinedcId").setReadOnly(true);
//            form.findField("onlinedcId").addCls("lanage_1");

            //参数
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"QF","comParams":{"onlinedcId":"' + onlinedcId + '"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
            });
//            var wcztStore = new KitchenSink.view.template.survey.report.JDBB.wcztStore({
//                fields:['wczt','pepNum'],
//                data:[]
//            });
//            var weekStore = new KitchenSink.view.template.survey.report.JDBB.weekStore({
//                fields:['date','pepNum'],
//                data:[]
//            });
            tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WCBL","comParams":{"onlinedcId":"' +onlinedcId + '"}}';;
            Ext.tzLoad(tzParams,function(responseData){
//                alert(responseData['root']);
                //winGridStore.reload();
                chart1store.loadData(responseData);
            });
            tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WEEK","comParams":{"onlinedcId":"' +onlinedcId + '"}}';;
            Ext.tzLoad(tzParams,function(responseData){
                //alert(responseData['root']);
                //winGridStore.reload();
                chart2store.loadData(responseData);
            });
//           console.log(weekStore)

            /*  tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WCBL","comParams":{"onlinedcId":"' +onlinedcId + '"}}';
             Ext.tzLoad(tzParams,function(responseData){

             var chart = panel.down('chart[name=chart1]');
             Params= '{"onlinedcId":"' + onlinedcId + '"}';
             chart.store.tzStoreParams = Params;
             chart.store.reload();
             });

             tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WEEK","comParams":{"onlinedcId":"' +onlinedcId + '"}}';
             Ext.tzLoad(tzParams,function(responseData){

             var chart = panel.down('chart[name=chart2]');
             Params= '{"onlinedcId":"' + onlinedcId + '"}';
             chart.store.tzStoreParams = Params;
             chart.store.reload();
             });*/


        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    }
});

