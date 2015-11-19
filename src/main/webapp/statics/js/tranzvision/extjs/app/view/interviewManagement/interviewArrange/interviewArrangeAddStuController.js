Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrangeAddStuController',
    //清空过滤条件
    onClearFilters:function(btn){
        btn.findParentByType('grid').store.clearFilter();
        btn.findParentByType('grid').filters.clearFilters();
    },
    //保存
    onPanelSave:function(btn){
        var addStuListPanel = btn.up('panel');
        var addStuListForm = addStuListPanel.child('form');
        var addStuListGrid = addStuListPanel.child('grid');

        var addStuListFormRec = addStuListForm.getForm().getFieldValues();
        var classID = addStuListFormRec["classID"];
        var batchID = addStuListFormRec["batchID"];

        //json字符串
        var selectedJson = "";
        var comParams="";
        //选择记录
        var selectedRecs = addStuListGrid.getSelectionModel().getSelection();
        if (selectedRecs.length>0){
            for(var i=0;i<selectedRecs.length;i++){
                if(selectedJson == ""){
                    selectedJson = Ext.JSON.encode(selectedRecs[i].data);
                }else{
                    selectedJson = selectedJson + ','+Ext.JSON.encode(selectedRecs[i].data);
                }
            }

            if(selectedJson != ""){
                comParams = '"add":[' + selectedJson + "]";
            }
            //提交参数
            var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_ASTU_STD","OperateType":"addInterviewStuList","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'",'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(responseData){
                if(responseData.success='success'){
                    //刷新grid
                    var setStuListPanel=Ext.ComponentQuery.query("panel[reference=interviewArrangeSetStuListPanel]");

                    var setStuListForm,setStuListGrid,setStuListFormRec;
                    var setStuListlclassID,setStuListbatchId,setStuListGridStore;
                    var Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                    if(setStuListPanel.length>0){
                        for(var i=0;i<setStuListPanel.length;i++){
                            setStuListForm = setStuListPanel[i].child('form');
                            setStuListGrid = setStuListPanel[i].child('grid');
                            setStuListFormRec = setStuListForm.getForm().getFieldValues();
                            setStuListlclassID = setStuListFormRec["classID"];
                            setStuListbatchId = setStuListFormRec["batchID"];
                            if(setStuListlclassID==classID && setStuListbatchId==batchID){
                                setStuListGridStore=setStuListPanel[i].down('grid').store;
                                setStuListGridStore.tzStoreParams = Params;
                                setStuListGridStore.reload();
                            }
                        }
                    }
                };
            },"",true,this);
        }else{
            Ext.tzShowToast('保存成功','提示','t','#ffffff');
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
