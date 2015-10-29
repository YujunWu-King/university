Ext.define('KitchenSink.view.security.menu.menuInfoPanel', {
    extend: 'Ext.window.Window',
    xtype: 'menuInfo',
	controller: 'menuInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.security.menu.menuInfoController',
		'KitchenSink.view.common.store.comboxStore'
	],
		reference: 'securityMenuInfoPanel',
    title: '新增机构菜单',
     width: 600,
  height: 250,
  minWidth: 600,
  minHeight: 250,
    modal: true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	ignoreChangesFlag: true,
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
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'combobox',
            //fieldLabel: '机构名称',
            fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUADD_STD.organStore","机构名称"),
						forceSelection: true,
            store: new KitchenSink.view.common.store.comboxStore({
							recname: 'TZ_JG_BASE_T',
							condition:{
								TZ_JG_EFF_STA:{
									value:"Y",
									operator:"01",
									type:"01"
								}
							},
							result:'TZ_JG_ID,TZ_JG_NAME'
						}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            typeAhead: true,
            queryMode: 'remote',
						name: 'menuOrg',
            beforeLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
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
            name: 'sourceOrg'
        },{
        	xtype: 'textfield',
        	name: 'confirmBz',
        	hidden: true
        }]
    }],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
