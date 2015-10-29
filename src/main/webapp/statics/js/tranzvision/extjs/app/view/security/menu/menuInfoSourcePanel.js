Ext.define('KitchenSink.view.security.menu.menuInfoSourcePanel', {
    extend: 'Ext.window.Window',
    xtype: 'menuInfoSource',
	controller: 'menuInfoSource',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.security.menu.menuInfoSourceController',
		'KitchenSink.view.common.store.comboxStore'
	],
		reference: 'securityMenuInfoSourcePanel',
    title: '复制菜单到当前机构',
     width: 600,
  height: 250,
  minWidth: 600,
  minHeight: 250,
  modal: true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'menuInfoForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            //fieldLabel: '当前机构名称'
						name: 'menuOrg',
        		hidden: true
        },{
            xtype: 'combobox',
            //fieldLabel: '复制源机构名称',
            fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUADD_STD.copyOrganStore","复制源机构名称"),
            forceSelection: true,
            store: new KitchenSink.view.common.store.comboxStore({
							recname: 'TZ_GNCD_SRC_VW',
							condition:{},
							result:'TZ_JG_ID,TZ_JG_NAME'
						}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            typeAhead: true,
            queryMode: 'remote',
            name: 'sourceOrg',
            beforeLabelTextTpl: [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            allowBlank: false
        },{
        	xtype: 'textfield',
        	name: 'confirmBz',
        	hidden: true
        }]
    }],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onSourceFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onSourceFormClose'
	}]
});
