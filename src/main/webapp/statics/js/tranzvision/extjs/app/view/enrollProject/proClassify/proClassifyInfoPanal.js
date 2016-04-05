Ext.define('KitchenSink.view.enrollProject.proClassify.proClassifyInfoPanal', {
    extend: 'Ext.panel.Panel',
    xtype: 'proClassifyInfoPanal',
    controller: 'proClassifyController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.proClassify.proClassifyController'
    ],
    title: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.xmfldyinfo","项目分类定义"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',
    items: [{
        xtype: 'form',
        reference: 'projectForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,
        bodyStyle: 'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.proTypeId", "项目分类编号"),
            name: 'proTypeId',
            //editable: false,
            readOnly: true,
            cls: 'lanage_1'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.proTypeName", "项目分类名称"),
            name: 'proTypeName',
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.proTypeDesc", "分类描述"),
            name: 'proTypeDesc',
            allowBlank: false
        },{
            xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.proTypeStatus", "有效状态"),
            name: 'proTypeStatus',
            emptyText: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.pleaseSelect", "请选择..."),
            queryMode: 'remote',
            editable: false,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_PRJ_TYPE_STATUS"),
            allowBlank: false,
			value:'Y'
        }]
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.save", "保存"),
        iconCls: "save",
        handler: 'onProjectSave'
    }, {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ensure", "确定"),
        iconCls: "ensure",
        handler: 'onProjectEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.close", "关闭"),
        iconCls: "close",
        handler: 'onProjectClose'
    }]
});

