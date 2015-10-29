Ext.define('KitchenSink.view.classManage.addClass', {
    extend: 'Ext.window.Window',
    xtype: 'addClass', 
	controller: 'classManage',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.add_class","新增班级"),
    reference:'addClass',
    ignoreChangesFlag:true,
    width:500,
    modal:true,
	actType: 'add',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        items: [
			{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_id","班级ID"),
				readOnly:true,
				name:'bj_id',
				value: 'NEXT'
			},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_name","班级名称"),
				name:'bj_name',
				allowBlank: false
			},{
				layout: {
					type: 'column'
				},
				items:[{
					columnWidth:.5,
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.xm_name","所属项目"),
					name: 'xm_id',
					editable: false,
					triggers: {
						search: {
							cls: 'x-form-search-trigger',
							handler: "xm_idChoice1"
						}
					},
					allowBlank: false
				},{
					columnWidth:.5,
					xtype: 'displayfield',
					hideLabel: true,
					name: 'xm_id_desc',
					style:'margin-left:8px;'
				}]
			}
		]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'AddClassEasy'
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭"),
		iconCls:"close",
		handler: 'CloseClassEasy'
	}]
});
