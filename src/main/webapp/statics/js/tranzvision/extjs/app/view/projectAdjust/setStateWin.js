Ext.define('KitchenSink.view.projectAdjust.setStateWin', {
    extend: 'Ext.window.Window',
    xtype: 'setStateWin',
	requires: [],
	
	reference: 'setStateWin',
	controller: 'projectAdjustController',
	title: '设置审核状态',
	width: 400,
    height: 150,
    minWidth: 300,
    minHeight: 100,
    layout: 'fit',
    resizable: false,
    modal: true,
    listWin:{},
    closeAction: 'hide',
	closable: false,
	constructor:function(win) {
        this.listWin = win;
        this.callParent();
    },
	 items: [{
        xtype: 'form',
        reference: 'setStateForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,

        items: [
        	{
            xtype: 'textfield',
            fieldLabel: "调整编号",
            hidden:true,
            name: 'tz_proadjust_id'
        },{
			xtype:'combobox',
			fieldLabel:'审核状态',
			name: 'adjustStatus',
			valueField: 'TValue',
			displayField: 'TSDesc',
			typeAhead: true,
			mode:"remote",
			allowBlank: false,
			blankText: '请选择审核状态',
			emptyText: '请选择审核状态',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ADJUST_STATE")
		}],
		buttons:[{
			text: '确定',
			handler:'setState'
		},{
			text: '取消',
			handler:function(btn){
				btn.findParentByType("setStateWin").close();
			}
		}]
	}]
});
