Ext.define('KitchenSink.view.enrollProject.projectMg.Zyfx.ZyfxWindow', {
    extend: 'Ext.window.Window',
    xtype: 'ZyfxWindow',
    controller: 'ZyfxWindowsController',
    modal:true,
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.projectMg.Zyfx.ZyfxWindowsController'
    ],
    title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.ZyfxWindow","专业方向"),
    reference:'ZyfxWindow',
    width:600,
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
                fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.sortNum","序号"),
                name:'sortNum'
            },{
                xtype: 'hiddenfield',
                fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.projectId","项目id"),
                name: 'projectId'
            },{
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.pro_zyfx_id","专业方向ID"),
                name: 'pro_zyfx_id',
                maxLength: 15,
				allowBlank: false,
				afterLabelTextTpl: [
					'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
				]
            },{
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.pro_zyfx_name","专业方向名称"),
                name: 'pro_zyfx_name',
                maxLength: 100,
				allowBlank: false,
				afterLabelTextTpl: [
					'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
				]
            }
            ]
        }],
    buttons: [{
        text:  Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.save","保存"),
        iconCls:"save",
        handler: 'onFormSave'
    }, {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.close","关闭") ,
        iconCls:"close",
        handler: 'onFormClose'
    }]
});