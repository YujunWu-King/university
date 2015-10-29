Ext.define('KitchenSink.view.activity.applicants.setStatusBatWin', {
    extend: 'Ext.window.Window',
    xtype: 'setStatusBatch',
	requires: [],
	
	reference: 'setStatusWindow',
	//controller: 'applicantsMg',
	title: '批量修改活动签到状态',
	width: 400,
    height: 150,
    minWidth: 300,
    minHeight: 100,
    layout: 'fit',
    resizable: false,
    modal: true,
    closeAction: 'hide',
	closable: false,
	
	 items: [{
        xtype: 'form',
        reference: 'setStatusForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,

        items: [{
			xtype:'combo',
			fieldLabel:'签到状态',
			name: 'signStatus',
			valueField: 'TValue',
			displayField: 'TSDesc',
			typeAhead: true,
			mode:"remote",
			allowBlank: false,
			blankText: '请选择签到状态',
			emptyText: '请选择签到状态',
			listeners: {afterrender: function(tvType){
						Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_MBRGL_CYZT"}',function(response){
							tvType.store = new Ext.data.Store({		
								fields: ['TValue', 'TSDesc', 'TLDesc'],
								data:response.TZ_MBRGL_CYZT,
							});
						})
					}}	
		}],
		buttons:[{
			text: '确定',
			handler:'setStatusBatch'
		},{
			text: '取消',
			handler:"closeWin"
		}]
	}]
});
