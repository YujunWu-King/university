Ext.define("KitchenSink.view.bugManagement.bugWhitelist.bugWhileListControl", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugWhileListControl',
    removePeople:function(view,rowIndex){
        Ext.MessageBox.confirm('提示', '您确定要删除所选记录吗?', function (btnId) {
            if (btnId == 'yes') {
                var store = view.store;
                store.removeAt(rowIndex);
            }
        }, this);
    },
    searchPeople:function(btn){Ext.tzShowCFGSearch({
        cfgSrhId: 'TZ_BUG_WHITE_COM.TZ_BUG_WHITE_STD.TZ_BUGMG_WLS_V',
        condition :{TZ_JG_ID:Ext.tzOrgID},
        callback: function(seachCfg){
            var store = btn.findParentByType("grid").store;
            store.tzStoreParams = seachCfg;
            store.load();
        }
    });
    },
    removePeoples:function(btn,rowIndex){
       var selList = btn.findParentByType("grid").getSelectionModel().getSelection(),
           checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert("提示", "请选择要删除的记录");
            return;
        } else {
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
                if (btnId == 'yes') {
                    var resSetStore = btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                   // this.deleteApplicantsAtOnce(btn.findParentByType('grid'));
                }
            }, this);
        }
    },
    addPeople:function(btn){
        Ext.tzShowPromptSearch({
            recname: 'TZ_BUG_STAFF_V',
            searchDesc: '选择人员',
            maxRow:20,
            condition:{
                presetFields:
                {
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_DLZH_ID:{
                        desc:'登录账号',
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
                TZ_DLZH_ID: '登录账号',
                TZ_REALNAME: '人员姓名'
            },
            multiselect: false,
            callback: function(selection){
                var store = btn.findParentByType('grid').getStore(),
                    record = selection[0];
                if(store.find('TZ_DLZH_ID',record.data.TZ_DLZH_ID)>=0){
                    Ext.MessageBox.alert("提示","当前人员已经添加");
                }else{
                    var model = Ext.create('KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListModel',{
                        account:record.data.TZ_DLZH_ID,
                        realName:record.data.TZ_REALNAME,
                        isWhite:1
                    });
                    store.add(model);
                }
            }
        })
    },
    onListSave:function(btn){
        var tzParams = this.getRuleParams(btn.findParentByType('panel').child('grid'));
        if(tzParams) {
            tzStoreParams = '{"ComID":"TZ_BUG_WHITE_COM","PageID":"TZ_BUG_WHITE_STD","OperateType":"U","comParams":{' + tzParams + '}}';
            Ext.tzSubmit(tzStoreParams,function(){
                btn.findParentByType("panel").child("grid").getStore().reload();
            },"",true,this);
        }
    },
    onListEnsure:function(btn){
        this.onListSave(btn);
        btn.findParentByType("panel").close();
    },
    onListClose:function(btn){
        btn.findParentByType("panel").close();
    },
    getRuleParams:function(grid){
        var editJson,comParams="";
        var store = grid.getStore();
        var editRecs = store.getModifiedRecords();
        if(editRecs.length !== 0) {
            for (var i = 0; i < editRecs.length; i++) {
                editRecs[i].data.isWhite = editRecs[i].data.isWhite?"1":"0";
                editJson = (editJson?editJson+ ',':'')  + Ext.JSON.encode(editRecs[i].data);
            }

            comParams = '"update":[' + editJson + "]";
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
        return comParams;
    }
});