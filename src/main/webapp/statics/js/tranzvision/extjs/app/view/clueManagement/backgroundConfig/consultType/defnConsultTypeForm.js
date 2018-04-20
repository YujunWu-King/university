Ext.define('KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeForm',{
    extend:'Ext.form.Panel',
    xtype:'defnClueTypeForm',
    controller: 'defnConsultTypeController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding:8px',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeController'
    ],
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 110,
        labelStyle: 'font-weight:bold'
    },
    title:"咨询类别详情",
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    consultSortID:"",
    initComponent: function () {
        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
    items:[{
        xtype:'textfield',
        fieldLabel: '咨询类别编号:',
        name: 'consultSortID',
        fieldStyle:'background:#F4F4F4',
        readOnly:true,
        ignoreChangesFlag:true
    },{
        xtype:'textfield',
        fieldLabel: '机构ID',
        name: 'orgID',
        readOnly:true,
        fieldStyle:'background:#F4F4F4',
        ignoreChangesFlag:true
    },{
        xtype:'textfield',
        fieldLabel: '咨询类别名称',
        name: 'consultSortName',
		afterLabelTextTpl: [
        	'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        ],
		allowBlank:false
    },{
        xtype:'textfield',
        fieldLabel: '描述',
        name: 'consultSortDesc'
    },{
        xtype:'combo',
        fieldLabel: '状态',
        name: 'consultSortStatus',
        store:statusStore,
        displayField:'TSDesc',
        valueField:'TValue',
        queryMode:'local',
        editable:false
    }],
    buttons:[{
        text: '保存',
        iconCls: "save",
        handler: 'consultSortSave'
    },{
        text: '确定',
        iconCls: "ensure",
        handler: 'consultSortEnsure'
    },{
        text: '关闭',
        iconCls: "close",
        handler: function(btn){
            btn.findParentByType('panel').close();
        }
    }]
        });
        this.callParent();
    }
});