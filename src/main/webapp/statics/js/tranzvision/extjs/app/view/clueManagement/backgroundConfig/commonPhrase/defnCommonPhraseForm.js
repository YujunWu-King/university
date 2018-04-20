Ext.define('KitchenSink.view.clueManagement.backgroundConfig.commonPhrase.defnCommonPhraseForm',{
    extend:'Ext.form.Panel',
    xtype:'defnCommonPhraseForm',
    controller: 'defnCommonPhraseController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding:8px',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.commonPhrase.defnCommonPhraseController'
    ],
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 110,
        labelStyle: 'font-weight:bold'
    },
    title:"常用短语详情",
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    commonPhraseID:"",
    initComponent: function () {
        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
            items:[{
                xtype:'textfield',
                fieldLabel: '常用短语编号:',
                name: 'commonPhraseID',
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
                fieldLabel: '常用短语名称',
                name: 'commonPhraseName',
	        afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
		allowBlank: false
            },{
                xtype:'textfield',
                fieldLabel: '描述',
                name: 'commonPhraseDesc'
            },{
                xtype:'combo',
                fieldLabel: '状态',
                name: 'commonPhraseStatus',
                store:statusStore,
                displayField:'TSDesc',
                valueField:'TValue',
                queryMode:'local',
                editable:false
            }],
            buttons:[{
                text: '保存',
                iconCls: "save",
                handler: 'commonPhraseSave'
            },{
                text: '确定',
                iconCls: "ensure",
                handler: 'commonPhraseEnsure'
            },{
                text: '关闭',
                iconCls: "close",
                handler: function(btn){
                    btn.findParentByType('panel').close();
                }
            }]
        })
        this.callParent();
    }


});