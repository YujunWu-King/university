Ext.define('KitchenSink.view.interviewManagement.interviewManage.interviewMgr', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewMgr',
    controller: 'interviewMgrController',
    reference:'interviewMgrPanel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'tranzvision.extension.grid.Exporter',
        'KitchenSink.view.interviewManagement.interviewManage.interviewMgrModel',
        'KitchenSink.view.interviewManagement.interviewManage.interviewMgrStore',
        'KitchenSink.view.interviewManagement.interviewManage.interviewMgrController'
    ],
    title: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.panelTitle","面试结果管理"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid)grid.setHeight( height -buttonHeight -100-8);
        }
    },
    initComponent: function (){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewArrangeForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.fclassID","报考班级ID") ,
                    name: 'classID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.fclassName","报考班级") ,
                    name: 'className',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.fbatchID","面试批次ID"),
                    name: 'batchID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.fbatchName","面试批次") ,
                    name: 'batchName',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                }]
            }]
        });

        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.butSave","保存") ,
        iconCls:"save",
        handler: 'onPanelSave'
    }, {
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler: 'onPanelConfirm'
    }, {
        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.butClose","关闭") ,
        iconCls:"close",
        handler: 'onPanelClose'
    }]

});
