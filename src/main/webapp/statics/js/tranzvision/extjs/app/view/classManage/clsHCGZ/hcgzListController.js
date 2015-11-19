Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hcgzListController',
    queryHCGZ:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.TZ_CLS_HCGZ_V',
            condition:{
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addHCGZ:function(btn){
        //alert("add");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CLS_HCGZ_COM"]["TZ_CLS_HCGZADD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLS_HCGZADD_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('hcgzAddHCGZWin');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var hcgzAddHCGZForm = win.child("form").getForm();
        hcgzAddHCGZForm.setValues({hcgzId:"NEXT",hcgzName:""});
        win.show();
    },
    deleteHCGZs:function(btn){
        //alert("delete");
        var hcgzListGrid = btn.up('grid');
        var hcgzListGridSelectedRecs=hcgzListGrid.getSelectionModel().getSelection();
        var checkLen = hcgzListGridSelectedRecs.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录.");
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    hcgzListGrid.store.remove(hcgzListGridSelectedRecs);
                }
            },this);
        }
    },
    hcgzDetail:function(grid, rowIndex, colIndex){
        //alert("detail");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CLS_HCGZ_COM"]["TZ_CLS_HCGZXQ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLS_HCGZXQ_STD，请检查配置。');
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

        var record = grid.store.getAt(rowIndex);
        var hcgzId = record.data.hcgzId;
        var hcgzName = record.data.hcgzName;

        cmp.on('afterrender',function(panel){
            var hcgzGZXQForm = panel.child('form').getForm();
            var hcgzGZXQGrid = panel.child('grid');

            var hcgzGZXQFormRec = {"hcgzGZXQFormData":{
                "hcgzId":hcgzId,
                "hcgzName":hcgzName
            }};
            hcgzGZXQForm.setValues(hcgzGZXQFormRec.hcgzGZXQFormData);

            var Params= '{"hcgzId":"'+hcgzId+'"}';
            hcgzGZXQGrid.store.tzStoreParams = Params;
            hcgzGZXQGrid.store.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    onPanelSave:function(btn){
        //alert("onPanelSave");
        var hcgzListGrid =btn.up('panel').child('grid');
        var hcgzListGridRemovedRecs= hcgzListGrid.store.getRemovedRecords();

        var editJson="";
        var comParams="";
        for(var i=0;i<hcgzListGridRemovedRecs.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(hcgzListGridRemovedRecs[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(hcgzListGridRemovedRecs[i].data);
            }
        };

        if(editJson != ""){
            comParams = '"delete":[' + editJson + "]";
        }else{
            comParams = '"delete":[]';
        }

        var tzParams = '{"ComID":"TZ_CLS_HCGZ_COM","PageID":"TZ_CLS_HCGZ_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            if(responseData.success='success'){
                Params= '{"cfgSrhId": "TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.TZ_CLS_HCGZ_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}';
                hcgzListGrid.store.tzStoreParams = Params;
                hcgzListGrid.store.reload();
            };
        },"",true,this);
    },
    onPanelEnsure:function(btn){
        //alert("onPanelEnsure");
        this.onPanelSave(btn);
        this.onPanelClose();
    },
    onPanelClose:function(){
        //alert("onPanelClose");
        this.getView().close();
    },

    onWinConfirm:function(btn){
        //alert('onWinConfirm');
        var hcgzAddHCGZWin = btn.up('window');
        var hcgzListPanelGrid = hcgzAddHCGZWin.up('panel').child('grid');
        var hcgzAddHCGZForm = hcgzAddHCGZWin.child("form").getForm();
        var hcgzAddHCGZFormRec = hcgzAddHCGZForm.getFieldValues();
        var hcgzId = hcgzAddHCGZFormRec["hcgzId"];
        var hcgzName = hcgzAddHCGZFormRec["hcgzName"];
        var comParams = '"add":[{hcgzId:"NEXT",hcgzName:"'+hcgzName+'"}]';

        var tzParams = '{"ComID":"TZ_CLS_HCGZ_COM","PageID":"TZ_CLS_HCGZADD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            if(responseData.success='success'){
                Params= '{"cfgSrhId": "TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.TZ_CLS_HCGZ_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}';
                hcgzListPanelGrid.store.tzStoreParams = Params;
                hcgzListPanelGrid.store.reload();
            };
        },"",true,this);

        hcgzAddHCGZWin.close();
    },
    onWinClose:function(btn){
        //alert('onWinConfirm');
        var addHCGZWin = btn.up('window');
        addHCGZWin.close();
    }
});
