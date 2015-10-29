Ext.define('KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jcbbQuestionListController',
    //生成交叉报表
    generateReport:function(btn){
        //进入查询问卷基本信息代码
        /*
        var surveyResultGrid = grid;
        var onlinedcId = surveyResultGrid. store.getAt(rowIndex).data.onlinedcId;
        win.on('afterrender',function(panel){
            var jcbbQuestionListForm = panel.child('form').getForm();
            var jcbbQuestionListGrid = panel.child('grid');
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"QF","comParams":{"onlinedcId":"'+onlinedcId+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                jcbbQuestionListForm.setValues(formData);
                tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"TJWT","comParams":{"onlinedcId":"'+onlinedcId+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    //jcbbQuestionListGrid.store.loadData(responseData['root']);

                    //var arrQL={"root":[{"questionID":"aaaa","questionDesc":"bbbbb"}]};
                    //jcbbQuestionListGrid.store.add(arrQL['root']);
                });
            });
        });
        */

        var jcbbQuestionListGrid = btn.up('grid');
        var onlinedcId="111";
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"TJWT","comParams":{"onlinedcId":"'+onlinedcId+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            //jcbbQuestionListGrid.store.loadData(responseData['root']);

            //var arrQL={"root":[{"questionID":"aaaa","questionDesc":"bbbbb"}]};
            jcbbQuestionListGrid.store.add(responseData['root']);
        });
/*
        var selectedRecs = jcbbQuestionListGrid.getSelectionModel().getSelection();
        if (selectedRecs.length<=0){
            Ext.Msg.alert("提示","请选择问题.");
            return;
        }

        var selectedJson="";
        for(var i=0;i<selectedRecs.length;i++){
            if(selectedJson == ""){
                selectedJson = selectedRecs[i].data;
            }else{
                selectedJson = selectedJson + ','+Ext.JSON.encode(selectedRecs[i].data);
            }
        }
*/

    },
    //关闭窗口
    onPanelClose: function(btn){
        var panel = btn.findParentByType("panel");
        panel.close();
    }
});
