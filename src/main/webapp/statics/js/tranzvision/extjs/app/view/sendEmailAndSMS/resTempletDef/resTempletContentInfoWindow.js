Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'resTempletContentInfoWindow', 
    title: '模板内容参数定义', 
	reference: 'resTempletContentInfoWindow',
    width: 600,
    height: 200,
    minWidth: 300,
    minHeight: 200,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	actType: 'add',
	
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_CONT_STD.restempid","元模版编号"),
			name: 'restempid',
			hidden:true
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_CONT_STD.restemporg","机构"),
			name: 'restemporg',
			hidden:true
		},{
			layout: {
				type: 'column',
			},
			bodyStyle:'padding:0 0 10px 0',
			items:[{
				columnWidth:.6,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_CONT_STD.keyname","关键字"),
				name: 'keyname',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchRecKey"
					}
                },
				afterLabelTextTpl: [
							'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						],
				allowBlank: false
			}]
		},{
			layout: {
				type: 'column',
			},
			bodyStyle:'padding:0 0 10px 0',
			items:[{
				columnWidth:.6,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_CONT_STD.paraid","对应取值的模版参数"),
				name: 'paraid',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchCpara"
					}
                },
				afterLabelTextTpl: [
							'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						],
				allowBlank: false				
			},{
				columnWidth:.4,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'paraname',
				style:'margin-left:8px'
			}]
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onResTmplContentInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onResTmplContentInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onResTmplContentClose'
	}]
});
