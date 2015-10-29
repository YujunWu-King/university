Ext.define('KitchenSink.view.classManage.Zyfx.ZyfxDetail', {
	extend: 'Ext.window.Window',
	xtype: 'ZyfxDetail', 
	controller: 'ZyfxDetail',
    modal:true,
 	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.classManage.Zyfx.ZyfxController'
	],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.zyfx_save","专业方向"),
    reference:'ZyfxDetail',
    width:600,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	items:[
	{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items:[{
				xtype: 'hiddenfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bj_id","班级id"),
				name: 'bj_id'
        	},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fx_id","专业方向ID"),
				name: 'fx_id',
				allowBlank:false
        	},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.fx_name","专业方向名称"),
				name: 'fx_name',
                allowBlank:false
        	}
		]
	}],
	buttons: [{
		text:  Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.save","保存"),
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭") ,
		iconCls:"close",
		handler: 'onFormClose'
	}]
});