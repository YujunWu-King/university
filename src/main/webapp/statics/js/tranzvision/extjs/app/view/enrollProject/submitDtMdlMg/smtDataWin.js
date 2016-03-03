Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDataWin', {
    extend: 'Ext.window.Window',
    xtype: 'transDetail',
    title: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.djzlxxxx","递交资料详细信息"),
	reference: 'smtDataWindow',
    width: 600,
    height: 200,
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
                selModel: {
                    type: 'checkboxmodel'
                },
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
		
				items: [{
					xtype: 'textfield', 
					fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.smtDtTmpID","递交资料模型ID"),
					name: 'smtDtTmpID',
					hidden: true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.smtDtID","递交资料ID"),
					name: 'smtDtID',
					hidden: true
				},{
					xtype: 'numberfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.order","显示顺序"),
					name: 'order',
                    hidden: true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.content","内容简介"),
					name: 'content',
                    maxLength:100,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    allowBlank: false
				},{ 
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.remark","备注"),
					name: 'remark'
				}]
			}]		  
		});
		this.callParent();
	},
    buttons: [{
		text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.save","保存"),
		iconCls:"save",
		handler: 'onSmtDataInfoSave'
	}, {
		text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onDataInfoEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.close","关闭"),
		iconCls:"close",
		handler: 'onDataInfoClose'
	}]
});
