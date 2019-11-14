Ext.define('KitchenSink.view.template.syncConfig.syncConfigInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'syncConfigInfoPanel',
    controller: 'syncConfigController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.syncConfig.syncConfigController'
    ],
    title: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_SYNCCONFIG_STD.tbpz","同步配置"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',
    items: [{
        xtype: 'form',
        reference: 'syncConfigForm',
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
            fieldLabel: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_ID", "同步配置ID"),
            name: 'TZ_APPSYNC_ID',
            //editable: false,
            readOnly: true,
            cls: 'lanage_1'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_DESC", "同步配置描述"),
            name: 'TZ_APPSYNC_DESC',
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_SYNC_TABLE", "同步配置表"),
            name: 'TZ_SYNC_TABLE',
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_SYNC_FILED", "同步字段"),
            name: 'TZ_SYNC_FILED',
            allowBlank: false
        },{
            xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_IS_ENABLE", "是否启用"),
            name: 'TZ_IS_ENABLE',
            emptyText: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.pleaseSelect", "请选择..."),
            queryMode: 'remote',
            editable: false,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_IS_ENABLE"),
            allowBlank: false,
			//value:'1'
        }]
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.save", "保存"),
        iconCls: "save",
        handler: 'onSyncConfigSave'
    }, {
        text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.ensure", "确定"),
        iconCls: "ensure",
        handler: 'onSyncConfigEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.close", "关闭"),
        iconCls: "close",
        handler: 'onSyncConfigClose'
    }]
});

