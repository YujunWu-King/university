Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'resTempletParaInfoWindow', 
    title: '模板参数定义', 
	reference: 'resTempletParaInfoWindow',
    width: 600,
    height: 240,
    minWidth: 300,
    minHeight: 200,
    layout: 'fit',
    resizable: true,
    modal: true,
    //closeAction: 'hide',
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
			labelWidth: 80,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.restempid","元模版编号"),
			name: 'restempid',
			hidden:true
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.restemporg","机构"),
			name: 'restemporg',
			hidden:true
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.5,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.paraid","参数名称"),
				name: 'paraid',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchResPara"
					}
                },
				afterLabelTextTpl: [
							'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						],
				allowBlank: false
			},{
				columnWidth:.5,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'paraname',
				style:'margin-left:8px'
			}]
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.paraalias","参数别名"),
			name: 'paraalias',
			afterLabelTextTpl: [
							'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						],
			allowBlank: false
		},{
			layout: {
				type: 'column'
			},
			bodyStyle:'padding:0 0 10px 0',
			items:[{
				columnWidth:.5,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.systvar","系统变量"),
				name: 'systvar',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchResSysVar"
					}
                },
				afterLabelTextTpl: [
							'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						],
				allowBlank: false				
			},{
				columnWidth:.5,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'systvarname',
				style:'margin-left:8px'
			}]
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onResTmplParaInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onResTmplParaInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onResTmplParaClose'
	}]
});
