Ext.define('KitchenSink.view.clueManagement.backgroundConfig.clueType.defnClueTypeForm',{
    extend:'Ext.form.Panel',
    xtype:'defnClueTypeForm',
    controller: 'defnClueTypeController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding:8px',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.colorpick.Field',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.clueType.defnClueTypeController'
    ],
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 110,
        labelStyle: 'font-weight:bold'
    },
    title:"线索类别详情",
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    clueSortID:"",
    initComponent: function () {
        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();

        Ext.apply(this,{
            items:[{
                xtype:'textfield',
                fieldLabel: '线索类别编号:',
                name: 'clueSortID',
                fieldStyle:'background:#F4F4F4',
                readOnly:true,
                ignoreChangesFlag:true
            },{
                xtype:'textfield',
                fieldLabel: '机构ID',
                name: 'orgID',
                fieldStyle:'background:#F4F4F4',
                readOnly:true,
                ignoreChangesFlag:true
            },{
                xtype:'textfield',
                fieldLabel: '线索类别名称',
                name: 'clueSortName',
				afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
				allowBlank: false
            }, {
                xtype: 'colorfield',
                fieldLabel:"颜色",
                name: 'clueSortCode',
                allowBlank:false,
                renderer:function(value){
                    return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
                }
            },{
                xtype:'combo',
                fieldLabel: '状态',
                name: 'clueSortStatus',
                store:statusStore,
                displayField:'TSDesc',
                valueField:'TValue',
                queryMode:'local',
                editable:false
            }],
            buttons:[{
                text: '保存',
                iconCls: "save",
                handler: 'clueSortSave'
            },{
                text: '确定',
                iconCls: "ensure",
                handler: 'clueSortEnsure'
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