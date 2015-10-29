Ext.define("KitchenSink.view.tzLianxi.ytt.userMg.userController",{
    requires:[
        'KitchenSink.view.tzLianxi.ytt.userMg.userStore'
    ],
    extend: 'Ext.app.ViewController',
    alias:'controller.userController',
    queryUser:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'LX_YTT_BUGUSER_COM.LX_YTT_BUGUSER_STD.YTT_BUGMG_PER_V',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams= seachCfg;
                store.load();
            }
        });
    },
    addUser:function(btn){
        Ext.tzShowPromptSearch({
            recname: 'TZ_AQ_YHXX_TBL',
            searchDesc: '新增用户',
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
                        desc:'用户ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'用户名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_DLZH_ID: '用户ID',
                TZ_REALNAME: '用户姓名'
            },
            multiselect: true,
            callback: function(selection){

                var store= btn.findParentByType("grid").getStore();
                for(var i=0;i<selection.length;i++){
                    var userID = selection[i].data.TZ_DLZH_ID;
                    var realName = selection[i].data.TZ_REALNAME;
                    var model = new KitchenSink.view.tzLianxi.ytt.userMg.userModel({
                        userID:userID,
                        realName:realName,
                        roleName:'0'
                    });
                    /*if(store.find("userID",userID)==-1){
                        store.add(model);
                    }*/
                    store.insert(0, model);
                }

            }
        })
    },
    deleteUser : function(btn){
        // var grid = Ext.getCmp(e.id).findParentByType('grid');
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
    userMgSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var comParams="";

        var modifyJson="";
        var modifyRecs=store.getModifiedRecords();
        for(var i=0;i<modifyRecs.length;i++){
            if(modifyJson == ""){
                modifyJson = Ext.JSON.encode(modifyRecs[i].data);
            }else{
                modifyJson = modifyJson + ','+Ext.JSON.encode(modifyRecs[i].data);
            }
        }
        if(modifyJson != ""){
            modifyParams = '"update":[' + modifyJson + "]";
            if(comParams !=""){
            comParams=comParams+","+modifyParams;
           } else{
                comParams= modifyParams;
            }
        }

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
            removeParams = '"delete":[' + removeJson + "]";
            if(comParams !=""){
                comParams=comParams+","+removeParams;
            } else{
                comParams=removeParams;
            }
        }
        //提交参数
        var tzParams = '{"ComID":"LX_YTT_BUGUSER_COM","PageID":"LX_YTT_BUGUSER_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
       if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }
    }

});