Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.HfyeditP', {
    extend: 'Ext.window.Window',
    xtype: 'HfyeditP', 
	controller: 'studentsList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.HfyeditD","录入公布结果"),
    reference:'HfyeditP',
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
				xtype: 'hiddenfield',
				fieldLabel: '班级ID',
				name:'bj_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名流程编号',
				name:'bmlc_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名表ID',
				name:'bmb_id'
			},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.name","姓名") ,
				name:'ry_name',
				fieldStyle:'background:#F4F4F4',
				readOnly:true
			},{
				xtype: 'combobox',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果") ,
				name: 'jg_id',
				queryMode: 'remote',
				editable:false,
				valueField: 'TZ_APPPRO_HF_BH',
				displayField: 'TZ_CLS_RESULT'
			},{
				//xtype: 'textarea',
				xtype: 'ueditor',
				model:'simple',
                height:300,
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.publishResult","公布结果") ,
				name:'plgb_area'
			}
		]
    }],
    buttons: [{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
		iconCls:"save",
		handler: 'SaveHfyeditP'
	},{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'EnsureHfyeditP'
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
		iconCls:"close",
		handler: 'CloseHfyeditP'
	}]
});
