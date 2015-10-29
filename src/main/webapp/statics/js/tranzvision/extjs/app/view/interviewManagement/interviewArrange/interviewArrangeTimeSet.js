Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeTimeSet', {
    extend: 'Ext.window.Window',
	reference: 'interviewArrTimeSet',
    xtype: 'interviewArrTimeSet',
	controller:'MspcController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        //'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel',
		//'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeStore',
		'KitchenSink.view.interviewManagement.interviewArrange.MspcController'
	],
	width: 600,
	height: 400,
	minWidth: 300,
	minHeight: 300,
    columnLines: true,
    title: '面试日程安排设置',
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',
    items: [{
        xtype: 'form',
        reference: 'interviewArrangeTimeSetForm',
		layout: {
            type: 'vbox',       // Arrange child items vertically
            align: 'stretch'    // 控件横向拉伸至容器大小
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
			labelSeparator:'',
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '班级ID',
			name: 'classID',
			hidden:true
        },{
            xtype: 'textfield',
            fieldLabel: '面试批次ID',
			name: 'batchID',
			hidden:true
        },{
            xtype: 'datefield',
            fieldLabel: '开始日期',
			labelSeparator:':',
			format: 'Y-m-d',
			allowBlank: false,
			name: 'startDate'
        },{
            xtype: 'datefield',
            fieldLabel: '结束日期',
			labelSeparator:':',
			format: 'Y-m-d',
			allowBlank: false,
			name: 'endDate'
        },{
            xtype: 'timefield',
            fieldLabel: '开始时间',
			increment:5,
			editable:false,
			allowBlank: false,
			format:'H:i',
			labelSeparator:':',
			name: 'startTime'
        },{
            xtype: 'timefield',
            fieldLabel: '结束时间',
			increment:5,
			editable:false,
			allowBlank: false,
			format:'H:i',
			labelSeparator:':',			
			name: 'endTime'
        },{
            xtype: 'numberfield',
            fieldLabel: '并发考官组数',
			hideTrigger:true,
			allowDecimals:false,
			allowBlank: false,
			minValue:1,
			labelSeparator:':',
			name: 'groupNum'
        },{
            xtype: 'numberfield',
            fieldLabel: '间隔时间(分钟)',
			hideTrigger:true,
			allowBlank: false,
			allowDecimals:false,
			minValue:1,
			labelSeparator:':',
			name: 'jg_time'
        },{
			layout: {
				type: 'column'
			},
			bodyStyle:'padding:10px 0 10px 0',
			items:[{
				width:120,
				xtype: 'numberfield',
				labelWidth: 50,
				fieldLabel: '每组第',
				hideTrigger:true,
				//allowBlank: false,
				allowDecimals:false,
				minValue:0,
				name: 'groupPersonNum'
			},{
				width:140,
				xtype: 'numberfield',
				fieldLabel: '人后休息',
				labelWidth: 70,
				hideTrigger:true,
				//allowBlank: false,
				allowDecimals:false,
				minValue:0,
				labelStyle: 'padding-left:8px;font-weight:bold',
				name: 'groupPersonDis'
			},{
				xtype: 'displayfield',
				fieldLabel: '分钟',
				labelStyle: 'padding-left:8px;font-weight:bold'
			}]
		}]
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onWindowEnsure1'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});