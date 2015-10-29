Ext.define('KitchenSink.view.interviewManagement.interviewArrange.MsjyTime', {
    extend: 'Ext.window.Window',
    xtype: 'MsjyTime', 
	controller: 'MsjyTimeController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.interviewManagement.interviewArrange.MsjyTimeController'
	],
    title: '面试建议时间定义',
    reference:'MsjyTime',
    width:500,
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
				xtype: 'timefield',
				fieldLabel: '开始时间',
				increment:10,
				editable:false,
				allowBlank: false,
				format:'H:i',
				labelSeparator:':',
				name: 'startJyTime'
			},{
				xtype: 'timefield',
				fieldLabel: '结束时间',
				increment:10,
				editable:false,
				allowBlank: false,
				format:'H:i',
				labelSeparator:':',
				name: 'endJyTime'
			}
		]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'SaveMsjyTime'
	},{
		text: '确定',
		iconCls:"ensure",
		handler: 'SureMsjyTime'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'CloseMsjyTime'
	}]
});
