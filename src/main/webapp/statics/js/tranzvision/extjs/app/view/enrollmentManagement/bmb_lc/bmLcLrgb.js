Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmLcLrgb', {
    extend: 'Ext.window.Window',
    xtype: 'bmLceditMb', 
	controller: 'studentsList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bmLcLrgb","录入结果"),
	reference:'bmLcLrgb',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        //reference: 'userAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 700,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.row_num","行数"),
			name: 'row_num'
        },{
            xtype: 'ueditor',
			height:300,
			name: 'ms_zg',
			model:'simple'
        }]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'MakeSure'
	},{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
		iconCls:"close",
		handler: 'MaleClose'
	}]
});
