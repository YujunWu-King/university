Ext.define('KitchenSink.view.classManage.Bmlc.BmlcXsnr', {
    extend: 'Ext.window.Window',
    xtype: 'BmlcXsnr', 
	controller: 'BmlcXsnr',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.classManage.Bmlc.XsnrController'
	],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.mrxsnrsz","默认显示内容设置"),
    reference:'BmlcXsnr1',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'userAccountForm',
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
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '行NO',
			name: 'h_no'
        },{
            xtype: 'htmleditor',
			name: 'bmlc_desc'
        }]
    }],
    buttons: [{
		text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭"),
		iconCls:"close",
		handler: 'onFormClose'
	}]
});