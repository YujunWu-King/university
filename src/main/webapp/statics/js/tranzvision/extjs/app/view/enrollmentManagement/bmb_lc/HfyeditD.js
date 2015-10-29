Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.HfyeditD', {
    extend: 'Ext.window.Window',
    xtype: 'HfyeditD', 
	controller: 'studentsList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.HfyeditD","录入公布结果"),
    reference:'HfyeditD',
    width:500,
	actType:'add',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
		name:'Hfy_form',
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
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.name","姓名") ,
				name:'ry_name',
				fieldStyle:'background:#F4F4F4',
				readOnly:true
			},{
				xtype: 'combobox',
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果") ,
				name: 'jg_id',
				queryMode: 'remote',
				editable:false,
				valueField: 'TZ_APPPRO_HF_BH',
				displayField: 'TZ_CLS_RESULT',
				listeners:{
					change:function(file, value, eOpts){
						var form = file.findParentByType('HfyeditD').down('form[name=Hfy_form]').getForm();
						var bj_id=form.getValues().bj_id;
						var bmlc_id=form.getValues().bmlc_id;
						var comParams='"bj_id":"'+bj_id+'","bmlc_id":"'+bmlc_id+'","old_value":"'+eOpts+'","new_value":"'+value+'"';
						var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ2_STD","OperateType":"EJSON","comParams":{'+comParams+'}}';
						Ext.tzLoad(tzParams,function(responseData){
							if (responseData.gbjg_pd=="1"){
								form.findField("plgb_area").setValue(responseData.gbjg_desc);
							}
						});
					}
				}
			},{
				xtype: 'ueditor',
				model:'simple',
                height:300,
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.publishResult","公布结果") ,
				name:'plgb_area'
			}
		]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
		iconCls:"save",
		handler: 'SaveHfyeditD'
	},{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'EnsureHfyeditD'
	},{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
		iconCls:"close",
		handler: 'CloseHfyeditD'
	}]
});
