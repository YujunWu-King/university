Ext.define('KitchenSink.view.template.jygz.jygzController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jygz',

    ensureJygzSet:function(btn){
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
            grid.close();
            return;
        }
        var tzParams = '{"ComID":"TZ_JYGZ_COM","PageID":"TZ_JYGZ_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';

        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
        //关闭
        grid.close();
    },
    queryJygz:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_JYGZ_COM.TZ_JYGZ_LIST_STD.TZ_JYGZ_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addJygz:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JYGZ_COM"]["TZ_JYGZ_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_JYGZ_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
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
    editJygz:function(obj,rowIndex){
        var jygzID;
        if(obj.name=="toolbarEdit"){
            var selList = this.getView().getSelectionModel().getSelection();
            var checkLen = selList.length;
            if(checkLen == 0){
                Ext.MessageBox.alert("提示","请选择一条要修改的记录");
                return;
            }else if(checkLen >1){
                Ext.MessageBox.alert("提示","只能选择一条要修改的记录");
                return;
            };
            jygzID = selList[0].get("jygzID");
           // modalID=selList[0].get("modalID");
        }else{
            var store = obj.findParentByType("grid").store;
            var selRec = store.getAt(rowIndex);
            jygzID = selRec.get("jygzID");
           // modalID = selRec.get("modalID");
        }

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JYGZ_COM"]["TZ_JYGZ_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_JYGZ_INFO_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
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

            var form = panel.child('form').getForm();
            form.findField("jygzID").setReadOnly(true);
            form.findField("jygzID").addCls("lanage_1");
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_JYGZ_COM","PageID":"TZ_JYGZ_INFO_STD","OperateType":"QF","comParams":{"jygzID":"'+jygzID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
            });

        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteJygz:function(obj,rowIndex){
        //选中行
        var store = obj.findParentByType('grid').getStore();
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            var selList = obj.findParentByType('grid').getSelectionModel().getSelection();
            //选中行长度
            var checkLen = selList.length;
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
    onJygzInfoClose:function(){
    this.getView().close();
    },
    onJygzInfoSave:function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getJygzInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                form.findField("jygzID").setReadOnly(true);
                form.findField("jygzID").addCls("lanage_1");
            },"",true,this);
        }
    },
    onJygzInfoEnsure:function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getJygzInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getJygzInfoParams: function(){
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"JYGZ","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"JYGZ","data":'+Ext.JSON.encode(formParams)+'}';
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_JYGZ_COM","PageID":"TZ_JYGZ_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    }
});

