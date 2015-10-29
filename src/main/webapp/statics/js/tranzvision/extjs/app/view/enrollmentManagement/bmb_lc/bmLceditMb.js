Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmLceditMb', {
    extend: 'Ext.window.Window',
    xtype: 'bmLceditMb', 
	controller: 'bmLceditMb',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollmentManagement.bmb_lc.bmLceditController'
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.tplEdit","模板编辑"),
    //reference:'siteTemplate',
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
            fieldLabel: '班级ID',
			name: 'bj_id'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '报名流程ID',
			name: 'bmlc_id'
        },{
            xtype: 'ueditor',
			height:300,
            //fieldLabel: '栏目名称',
			name: 'bmlc_mbnr',
			model:'simple'
        }]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
        iconCls:"save",
		handler: 'onFormSave'
	}, {
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
        iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
        iconCls:"close",
		handler: 'onFormClose'
	}]
});
