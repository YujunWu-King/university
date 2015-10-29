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
        var ZWJID=record.data.questionID;
        var form=grid.findParentByType('panel').child('form');
        var WJID=form.getForm().findField('onlinedcId').getValue();

        var tzParams = '{"ComID":"TZ_ZXDC_PSBB_COM","PageID":"TZ_ZXDC_PSBB_STD","OperateType":"WTTB ","comParams":{"onlinedcId":"'+WJID+'","wtId":"'+ZWJID+'"}}';

        console.log(tzParams);
        Ext.tzLoad(tzParams,function(responseData){
            console.log(responseData);
        });

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
        console.log(cmp);
        cmp.on('afterrender',function(win){

        });
        cmp.show();

    },
    //关闭窗口
    onPanelClose: function(btn){
        var panel = btn.findParentByType("panel");
        panel.close();
    }
});
