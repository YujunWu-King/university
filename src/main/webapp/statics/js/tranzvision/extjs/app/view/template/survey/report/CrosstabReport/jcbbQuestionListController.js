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
    /**
     * 批处理生成交叉报表
     * 刘阳阳   2015-11-04
     */
    batchGenerateReport:function(btn){
        var jcbbQuestionListGrid = btn.up('panel').child('grid');
        var selectedRecs = jcbbQuestionListGrid.getSelectionModel().getSelection();
        if (selectedRecs.length<=0){
            Ext.Msg.alert("提示","请选择问题.");
            return;
        };

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_JCBBBT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_JCBBBT_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('jcbbBatWin');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
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
        };

        var genJcbbFormData={onlinedcId:onlinedcId,jcbbName:'',wtIds:selectedRecsQuestionIds};
        var genJcbbForm = win.child("form").child('tabpanel').child('form').getForm();
        genJcbbForm.setValues(genJcbbFormData);
        win.show();
    },
    //关闭窗口
    onPanelClose: function(btn){
        var panel = btn.findParentByType("panel");
        panel.close();
    }
});
