Ext.define('KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jcbbQuestionListController',
    //生成交叉报表
    generateReport:function(btn){
        var jcbbQuestionListGrid = btn.up('panel').child('grid');
        var selectedRecs = jcbbQuestionListGrid.getSelectionModel().getSelection();
        if (selectedRecs.length<=0){
            Ext.Msg.alert("提示","请选择问题.");
            return;
        }

        var jcbbQuestionListForm = jcbbQuestionListGrid.up('panel').child('form');
        var jcbbQuestionListFormRec = jcbbQuestionListForm.getForm().getFieldValues();
        var onlinedcId=jcbbQuestionListFormRec["onlinedcId"];

        var selectedRecsQuestionIds="";
        for(var i=0;i<selectedRecs.length;i++){
            if(selectedRecsQuestionIds == ""){
                selectedRecsQuestionIds = selectedRecs[i].data.questionID;
            }else{
                selectedRecsQuestionIds = selectedRecsQuestionIds + ';'+selectedRecs[i].data.questionID;
            }
        }

        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"VIEWTB","comParams":{"onlinedcId":"'+onlinedcId+'","wtIds":"'+selectedRecsQuestionIds+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            window.open(responseData.VIEWURL,'_blank');
        });
    },
    //关闭窗口
    onPanelClose: function(btn){
        var panel = btn.findParentByType("panel");
        panel.close();
    }
});
