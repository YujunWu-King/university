Ext.define('KitchenSink.view.classManage.Pcgl.PcglDetail', {
    extend: 'Ext.window.Window',
    xtype: 'PcglDetail',
    controller: 'PcglDetail',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.classManage.Pcgl.PcglController'//保存
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pcgl","批次管理") ,
    reference:'PcglDetail',
    width:600,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items:[
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            bodyPadding: 10,
            bodyStyle:'overflow-y:auto;overflow-x:hidden',
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 120,
                labelStyle: 'font-weight:bold'
            },
            items:[{
                xtype: 'hiddenfield',
                fieldLabel: '班级id',
                name: 'bj_id'
            },{
                xtype: 'hiddenfield',
                fieldLabel: '批次编号',
                name: 'pc_id',
                value: 'NEXT'
            },{
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.pcmc","批次名称") ,
                name: 'pc_name',
                allowBlank:false
            },{
                xtype: 'datefield',
                fieldLabel:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ksrq","开始日期") ,
                format: 'Y-m-d',
                name: 'pc_st_time'
            },{
                xtype: 'datefield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.jsrq","结束日期"),
                format: 'Y-m-d',
                name: 'pc_sp_time'
            },{
                xtype: 'datefield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmjzrq","报名截止日期"),
                format: 'Y-m-d',
                name: 'pc_stbm_time',
                allowBlank:false
            },{
                xtype: 'datefield',
                fieldLabel:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.msqrrq","面试确认日期") ,
                format: 'Y-m-d',
                name: 'interviewConfirmDate'
            },{
                xtype: 'combobox',
                fieldLabel:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sfwwfb","是否外网发布") ,
                queryMode: 'remote',
                editable:false,
                name: 'pc_fb',
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_APP_PUB_STATUS")
            }
            ]
        }],
    buttons: [{
        text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.save","保存"),
        iconCls:"save",
        handler: 'onFormSave'
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭"),
        iconCls:"close",
        handler: 'onFormClose'
    }]
});