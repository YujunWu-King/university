Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bjHfy', {
    extend: 'Ext.window.Window',
    xtype: 'bjHfy', 
	controller: 'bjhfyList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfybj","回复语编辑"),
    reference:'bjHfy',
    width:500,
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
				xtype: 'hiddenfield',
				fieldLabel: '班级ID',
				name:'bj_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名流程编号',
				name:'bmlc_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '回复短语编号',
				name:'dybh_id'
			},{
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.color","颜色"),
				xtype: 'colorfield',
				//bind: '{color}',
				//editable:false,
				name: 'colorCode',
				/*editor: {
					xtype: 'colorfield',
					allowBlank: false
				},*/
				renderer:function(value){
					return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
					//return "<div class='x-colorpicker-field-swatch-inner'' style='width:60%;height:50%;background-color: #"+value+"'></div>";
				}
            },{
				xtype: 'textfield',
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果"),
				name:'hf_jg'
			},{
				xtype: 'ueditor',
				height:300,
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.content","内容"),
				name: 'ms_zg',
				model:'simple'
			},{
				xtype: 'checkbox',
				boxLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.wfbsdmrz","未发布时的默认值"),
				name: 'hf_mrz'
			}
		]
    }],
    buttons: [{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
		iconCls:"save",
		handler: 'SaveHfyPage'
	},{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'SureHfyPage'
	},{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
		iconCls:"close",
		handler: 'CloseHfyPage'
	}]
});
