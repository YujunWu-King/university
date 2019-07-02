Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangePlanSet', {
    extend: 'Ext.window.Window',
    requires: [
	    'Ext.data.*',
        'Ext.util.*',
		'KitchenSink.view.interviewManagement.interviewArrange.MspcController'
	],
	reference: 'interviewArrTimeSet',
    xtype: 'interviewArrPlanSet',
	controller:'MspcController',
	
	width: 600,
	height: 300,
	minWidth: 600,
	minHeight: 300,
    columnLines: true,
    title: '面试安排设置',
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'destroy',
    
    items: [{
        xtype: 'form',
        reference: 'interviewArrangePlanSetForm',
		layout: {
            type: 'vbox',       // Arrange child items vertically
            align: 'stretch'    // 控件横向拉伸至容器大小
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
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
            xtype: 'textfield',
            fieldLabel: '面试计划编号',
			name: 'msJxNo',
			hidden:true
        },{
            xtype: 'datefield',
            fieldLabel: '面试日期',
			labelSeparator:':',
			format: 'Y-m-d',
			allowBlank: false,
			name: 'interviewDate'
        },{
            xtype: 'timefield',
            fieldLabel: '开始时间',
			increment:5,
			//editable:false,
			allowBlank: false,
			format:'H:i',
			name: 'startTime',
			minValue: '7:00 AM',
	        maxValue: '19:00'
        },{
            xtype: 'timefield',
            fieldLabel: '结束时间',
			increment:5,
			//editable:false,
			allowBlank: false,
			format:'H:i',	
			name: 'endTime',
			minValue: '7:00 AM',
	        maxValue: '24:00',
	        validator: function(val){
				var form = this.findParentByType('form').getForm();
				if(val != ""){
					var startTime = form.findField('startTime').getValue();
					var endTime = form.findField('endTime').getValue();
					if(endTime <= startTime) return '结束时间必须大于开始时间';
				}
				return true;	
			}
        },{
        	xtype: 'textfield',
        	fieldLabel: '面试地点',	
			name: 'msLocation'
        },{
            xtype: 'numberfield',
            fieldLabel: '最多预约人数',
			allowDecimals:false,
			allowBlank: false,
			minValue:1,
			name: 'maxPerson'
        },{
        	xtype: 'textfield',
        	fieldLabel: '备注',
			name: 'notes'
        }]
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onMsPlanSaveEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});