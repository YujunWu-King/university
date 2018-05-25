Ext.define('KitchenSink.view.clueManagement.backgroundConfig.closeReason.modifiedClose',{
    extend:'Ext.form.Panel',
    xtype:'modifiedChannel',
    controller: 'XSGLGBYYController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding:8px',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.closeReason.defnReasonOfCloseController'
    ],
    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 110,
        labelStyle: 'font-weight:bold'
    },
    title:"关闭原因详情",
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    reasonID:"",
    // constructor:function(transValue){
    initComponent: function () {
    	var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
            items:[{
                xtype:'textfield',
                fieldLabel: '关闭原因编号:',
                name: 'reasonID',
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
                fieldLabel: '关闭原因',
                name: 'reason',
		afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
		allowBlank: false
            },{
                xtype:'textfield',
                fieldLabel: '描述',
                name: 'description'
            }, {
                xtype: 'combo',
                fieldLabel: "状态",
                name: 'status',
                // value: 'Y', /*默认有效*/
                // store: transValue.get("TZ_XSGL_STATUS"),
                store:statusStore,
                displayField: 'TSDesc',
                valueField: 'TValue',
                queryMode: 'local',
                editable:false
            }],
		    buttons:[{
		        text: '保存',
		        iconCls: "save",
		        handler: 'onModifiedSave'
		    },{
		        text: '确定',
		        iconCls: "ensure",
		        handler: 'onModifiedEnsurn'
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