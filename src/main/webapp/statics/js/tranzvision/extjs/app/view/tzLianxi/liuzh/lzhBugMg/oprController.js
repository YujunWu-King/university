Ext.define("KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprController",{
    requires:[
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprStore'
    ],
    extend: 'Ext.app.ViewController',
    alias:'controller.oprController',
    addOpr:function(btn){
            Ext.tzShowPromptSearch({
            recname: 'TZ_AQ_YHXX_TBL',
            searchDesc: '新增人员',
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
                    OPRID:{
                        desc:'人员ID',
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
                OPRID: '人员ID',
                TZ_REALNAME: '人员姓名'
            },
            multiselect: true,
            callback: function(selection){

                var store= btn.findParentByType("grid").getStore();
                for(var i=0;i<selection.length;i++){
                    var oprID = selection[i].data.OPRID;
                    var name = selection[i].data.TZ_REALNAME;
                    var model = new KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprStruct({
                        oprID:oprID,
                        name:name,
                        role:'0'
                    })
                    if(store.find("oprID",oprID)==-1){
                        store.add(model);
                    }

                }

            }
        })
    },
    confirmDelete : function(e){
        var grid = Ext.getCmp(e.id).findParentByType('grid');
        if(grid.getSelectionModel().getSelection().length<=0){
            Ext.MessageBox.alert("提示","您没有选中任何记录。");
            return;
        }
        Ext.MessageBox.confirm('提示','确认要删除所选记录吗吗?',function(id){
            switch(id){
                case 'yes':
                    var rows = grid.getSelectionModel().getSelection();
                    grid.getStore().remove(rows);
                  /*  var removedRecord = grid.getStore().getRemovedRecords(),
                        JSONData = [];
                    for(var x = removedRecord.length-1;x>=0;x--){
                        JSONData[x] = {};
                        JSONData[x].name = removedRecord[x].data.name;
                        JSONData[x].oprID = removedRecord[x].data.oprID;
                    }
                    JSONData.type = 'DP';
                    grid.getStore().reload();
                    console.log(JSON.stringify(JSONData));*/
                    break;
                case 'no':
                    break;
            }
        });
    },
    saveUser: function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.store;
        var modifiedRecs = store.getModifiedRecords();
        var removedRecord = store.getRemovedRecords();
        var modifiedData = [],removedData=[],jsonData = {};
        for(var i = 0;i<modifiedRecs.length;i++){
            modifiedData.push(modifiedRecs[i].data);
        }
        for(var i = 0;i<removedRecord.length;i++) {
            removedData.push(removedRecord[i].data);
        }
        if(modifiedData.length !== 0 || removedData.length !== 0){
            if(modifiedData.length !== 0){
                jsonData.update = modifiedData;
            }
            if(removedData.length !== 0){
                jsonData.remove = removedData;
            }
            jsonData.type = 'saveUser';
            jsonData.manager = 'lzh';
            var content = Ext.encode(jsonData);
            Ext.Ajax.request({
                url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                params: {params:content},
                success: function(){
                    store.reload();
                }
            });
        }
    }
});