Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzGZXQController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hcgzGZXQController',
    deleteHCGZCls:function(grid, rowIndex, colIndex){
        //alert('deleteHCGZCls');
        var record = grid.store.getAt(rowIndex);
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                grid.store.remove(record);
            }
        },this);
    },
    addHCGZCls:function(btn){
        //alert('addHCGZCls');
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CLS_HCGZ_COM"]["TZ_CLS_HCGZCLS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLS_HCGZCLS_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('hcgzClassListWin');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        var hcgzGZXQForm = btn.up('grid').up('panel').child('form').getForm();
        var hcgzGZXQFormRec = hcgzGZXQForm.getFieldValues();
        var hcgzId = hcgzGZXQFormRec["hcgzId"];

        var hcgzClassListForm = win.child("form").getForm();
        hcgzClassListForm.setValues({hcgzId:hcgzId});
        win.show();
    },
    onPanelSave:function(btn){
        //alert("onPanelSave");
        var hcgzGZXQClsForm = btn.up('panel').child('form').getForm();
        var hcgzGZXQClsFormRec = hcgzGZXQClsForm.getFieldValues();
        var hcgzId = hcgzGZXQClsFormRec["hcgzId"];

        var hcgzGZXQClsGrid =btn.up('panel').child('grid');
        var hcgzGZXQClsGridRemovedRecs= hcgzGZXQClsGrid.store.getRemovedRecords();

        var editJson="";
        var comParams="";
        for(var i=0;i<hcgzGZXQClsGridRemovedRecs.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(hcgzGZXQClsGridRemovedRecs[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(hcgzGZXQClsGridRemovedRecs[i].data);
            }
        };

        if(editJson != ""){
            comParams = '"delete":[' + editJson + "]";
        }else{
            comParams = '"delete":[]';
        }

        var tzParams = '{"ComID":"TZ_CLS_HCGZ_COM","PageID":"TZ_CLS_HCGZXQ_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            if(responseData.success='success'){
                var Params= '{"hcgzId":"'+hcgzId+'"}';
                hcgzGZXQClsGrid.store.tzStoreParams = Params;
                hcgzGZXQClsGrid.store.load();
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
    }
});
