Ext.define('KitchenSink.view.template.proTemplate.protmBackMsgWin', {
    extend: 'Ext.window.Window',
    xtype: 'protmBackMsgWin', 
	//controller: 'bjhfyList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.hfybj","回复语编辑"),
    reference:'protmBackMsgWin',
    modal: true,
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
				fieldLabel: '流程ID',
				name:'TZ_APPPRO_ID'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名流程编号',
				name:'TZ_APPPRO_TMP_ID'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '回复短语编号',
				name:'TZ_APPPRO_HF_BH'
			},{
				fieldLabel:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.color","颜色"),
				xtype: 'colorfield',
				//bind: '{color}',
				//editable:false,
				name: 'TZ_APPPRO_COLOR',
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
				fieldLabel:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.result","结果"),
				name:'TZ_CLS_RESULT',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank: false
			},{
				xtype: 'ueditor',
				height:300,
				fieldLabel: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.content","内容"),
				name: 'TZ_DEF_CONTENT',
				model:'simple'
			},{
				layout: {
					type: 'column'
				},
				bodyStyle:'padding:0 0 10px 0',
				items:[{
					columnWidth:.6,
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RESTPL_PARA_STD.systvar","动态结果显示"),
					name: 'TZ_SYSVAR',
					editable: false,
					triggers: {
						search: {
							cls: 'x-form-search-trigger',
							handler: "searchSysVar"
						}
	                }			
				},{
					columnWidth:.4,
					xtype: 'displayfield',
					hideLabel: true,
					name: 'TZ_SYSVAR_NAME',
					style:'margin-left:8px'
				}]
			},{
				xtype: 'checkbox',
				boxLabel:Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.wfbsdmrz","未发布时的默认值") ,
				name: 'TZ_WFB_DEFALT_BZ'
			}
		]
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.save","保存"),
        iconCls:"save",
        handler: 'onProBackMsgSave'
    }, {
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onProBackMsgEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.close","关闭"),
        iconCls:"close",
        handler: 'onProBackMsgClose'
    }]
});