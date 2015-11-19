Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.PinShuBBQuestionListController',
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
    //评书报表-查看报表
    viewBaoBiao:function(btn,rowIndex){

        var grid=btn.findParentByType('grid'),
            record = grid.store.getAt(rowIndex);
        //总的问题ID：可能是父问题ID+子问题ID，也可能仅仅是父问题ID;
        var TotalID=record.data.questionID,
             ZwjID=record.data.childQuestionId,
             questionDesc=record.data.questionDesc,
             childQuestionDesc=record.data.childQuestionDesc;
        var form=grid.findParentByType('panel').child('form'),
            WJID=form.getForm().findField('onlinedcId').getValue(),//问卷ID
            WJMS=form.getForm().findField('onlinedcTitle').getValue()//问卷描述
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_PSBB_COM"]["TZ_ZXDC_PSBB_W_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        console.log(pageResSet);
//该功能对应的JS类
        var className = pageResSet["jsClassName"];
        console.log(className);
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_PSBB_W_STD，请检查配置。');
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
        console.log(ViewClass);
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
        cmp = new ViewClass(

        );
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
             var tzParams = '{"ComID":"TZ_ZXDC_PSBB_COM","PageID":"TZ_ZXDC_PSBB_STD","OperateType":"QF","comParams":{"onlinedcId":"'+WJID+'"}}';

            console.log(tzParams);
            Ext.tzLoad(tzParams,function(responseData){
                var formData=responseData.formData;
                formData.questionDesc = questionDesc;
                formData.childQuestionDesc = childQuestionDesc;
                form.setValues(formData);

                var chartStore = panel.child('form').child('chart').store;
                var gridStore = panel.child('form').child('grid').store;
                console.log(chartStore)
                console.log(gridStore)

                var chart_tzParams='{"type":"chart","onlinedcId":"'+WJID+'","wtId":"'+TotalID+'"}';
                var grid_tzParams='{"type":"grid","onlinedcId":"'+WJID+'","wtId":"'+TotalID+'"}';
                chartStore.tzStoreParams=chart_tzParams;
                gridStore.tzStoreParams = grid_tzParams;
                chartStore.load({
                    scope: this,
                    callback: function(records, operation, success) {
                    }
                });
                gridStore.load({
                    scope: this,
                    callback: function(records, operation, success) {
                    }
                });
            });


        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }

    },
    //关闭窗口
    onPanelClose: function(btn){
        var panel = btn.findParentByType("panel");
        panel.close();
    }
});
