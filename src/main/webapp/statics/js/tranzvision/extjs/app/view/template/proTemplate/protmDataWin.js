Ext.define('KitchenSink.view.template.proTemplate.protmDataWin', {
    extend: 'Ext.window.Window',
    xtype: 'transDetail',
    title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.bmlcxxxx","报名流程详细信息"),
	reference: 'protmDataWin',
    width: 600,
    height: 150,
    minWidth: 600,
    minHeight: 150,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	actType: 'add',
	
	initComponent: function(){
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,

				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
		
				items: [{
					xtype: 'textfield', 
					fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_TMP_ID","报名流程模版ID"),
					name: 'TZ_APPPRO_TMP_ID',
                    hidden:true

				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_ID","流程ID"),
					name: 'TZ_APPPRO_ID',
                    hidden:true

				},{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_APPPRO_NAME","流程名称"),
                    name: 'TZ_APPPRO_NAME',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    allowBlank: false
                    //displayOnly:true
                },{
					xtype: 'numberfield',
					fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_SORT_NUM","显示顺序"),
					name: 'TZ_SORT_NUM',
                   hidden:true
					//minValue: 1
				},,{
                    xtype: 'text',
                    fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.TZ_DEF_CONTENT","内容"),
                    name: 'TZ_DEF_CONTENT',
                    hidden:true
                    //minValue: 1
                }]
			}]		  
		});
		this.callParent();
	},
    buttons: [{
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.save","保存"),
		iconCls:"save",
		handler: 'onDataInfoSave'
	}, {
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onDataInfoEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.close","关闭"),
		iconCls:"close",
		handler: 'onDataInfoClose'
	}]
});
