Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrangeSetStuListController',
    //清空过滤条件
    onClearFilters:function(btn){
        btn.findParentByType('grid').filters.clearFilters();

    },
    addIntervieStus:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_ASTU_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_ASTU_STD，请检查配置。');
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

        var setStuListGrid = btn.up('grid');
        var setStuListForm = setStuListGrid.up('panel').child('form');
        var setStuListFormRec = setStuListForm.getForm().getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        var className = setStuListFormRec["className"];

        cmp.on('afterrender',function(panel){
            var addStuListForm = panel.child('form').getForm();
            var addStuListGrid = panel.child('grid');

            var addStuListFormRec = {"setStuListFormData":{
                "classID":classID,
                "className":className,
                "batchID":batchID
            }};
            addStuListForm.setValues(addStuListFormRec.setStuListFormData);
            addStuListGrid.store.load();
            addStuListGrid.store.filter([{property: 'msZGFlag', value: 'Y'},{property: 'className', value: className}]);
            //addStuListGrid.store.filterBy(function(record) {
            //    return record.get('msZGFlag') == "Y";
            //});
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    delSelStus: function(btn){
        var stuListGrid = btn.up('grid');
        var stuListForm = stuListGrid.up('panel').child('form').getForm();
        var setStuListFormRec = stuListForm.getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        var className = setStuListFormRec["className"];
        var batchName = setStuListFormRec["batchName"];
        //选中行
        var selList = stuListGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var stuListGridStore =stuListGrid.store;
                    stuListGridStore.remove(selList);
                    var setStuListGridStoreCount = stuListGridStore.getCount();
                    var setStuListFormRec = {"itwArrInfFormData":{
                        "classID":classID,
                        "className":className,
                        "batchID":batchID,
                        "batchName":batchName,
                        "stuCount":setStuListGridStoreCount
                    }};
                    stuListForm.setValues(setStuListFormRec.itwArrInfFormData);
                }
            },this);
        }
    },
    //保存
    onPanelSave:function(btn){
        var setStuListPanel = btn.up('panel');
        var setStuListForm = setStuListPanel.child('form');
        var setStuListGrid = setStuListPanel.child('grid');

        var setStuListFormRec = setStuListForm.getForm().getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        var className = setStuListFormRec["className"];
        var batchName = setStuListFormRec["batchName"];

        var setStuListGridStore = setStuListGrid.getStore();
        //删除json字符串
        var removeJson = "";
        var comParams="";
        //删除记录
        var removeRecs = setStuListGridStore.getRemovedRecords();
        if (removeRecs.length>0){
            for(var i=0;i<removeRecs.length;i++){
                if(removeJson == ""){
                    removeJson = Ext.JSON.encode(removeRecs[i].data);
                }else{
                    removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
                }
            }

            if(removeJson != ""){
                comParams = '"delete":[' + removeJson + "]";
            }
            //提交参数
            var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_SSTU_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
                Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                setStuListGridStore.tzStoreParams = Params;
                setStuListGridStore.load({
                    callback : function(records, operation, success) {
                        if (success == success) {
                            var setStuListGridStoreCount = setStuListGrid.store.getRange().length;
                            var setStuListFormRec = {"itwArrInfFormData":{
                                "classID":classID,
                                "className":className,
                                "batchID":batchID,
                                "batchName":batchName,
                                "stuCount":setStuListGridStoreCount
                            }};
                            setStuListForm.getForm().setValues(setStuListFormRec.itwArrInfFormData);
                        }
                    }
                });
            },"",true,this);
        };
    },
    //确定
    onPanelEnsure:function(btn){
        this.onPanelSave(btn);
        this.onPanelClose();
    },
    //关闭
    onPanelClose: function(){
        this.getView().close();
    }
});
