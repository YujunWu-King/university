Ext.define('KitchenSink.view.enrollmentManagement.pubLcjg.perLcjgPubWindowP', {
    extend: 'Ext.window.Window',
    xtype: 'perLcjgPubWindowP',
	controller:'lcjgPub',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.pubLcjg.lcjgPubController'
	],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfyplsz","回复语批量设置"),
    reference:'perLcjgPubWindowP',
    ignoreChangesFlag: true,
    width:500,
    modal:true,
    perLcjgPCallBack:function(){},
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
                fieldLabel: 'callback',
                name:'callback'
            },
			{
				xtype: 'hiddenfield',
				fieldLabel: '班级ID',
				name:'bj_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名表ID',
				name:'bmb_id'
			},{
				xtype: 'textarea',
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.name","姓名"),
                //height:30,
                preventScrollbars:true,
				name:'ry_name',
				fieldStyle:'background:#F4F4F4',
				readOnly:true
			},{
                xtype: 'combobox',
                fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.procedure","流程"),
                name:'ry_lc',
                queryMode: 'remote',
                editable:false,
                valueField: 'TZ_APPPRO_ID',
                displayField: 'TZ_APPPRO_NAME',
                listeners:{
                    change:function(file, newValue, oldValue){
                        var form = file.findParentByType('perLcjgPubWindowP').down('form[name=Hfy_form]').getForm();
                        var bj_id=form.getValues().bj_id;
                        var bmlc_id=form.getValues().ry_lc;
//                        var comParams='"bj_id":"'+bj_id+'","bmlc_id":"'+bmlc_id+'","old_value":"'+oldValue+'","new_value":"'+newValue+'"';
//                        var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"EJSON","comParams":{'+comParams+'}}';
//                        Ext.tzLoad(tzParams,function(responseData){
//
//                        });
                        var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                            recname: 'TZ_CLS_BMLCHF_T',
                            condition:{
                                TZ_CLASS_ID:{
                                    value:bj_id,
                                    operator:"01",
                                    type:"01"
                                },
                                TZ_APPPRO_ID:{
                                    value:bmlc_id,
                                    operator:"01",
                                    type:"01"
                                }
                            },
                            result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
                        });
                        form.findField("jg_id").setStore(lm_mbStore);
                        form.findField("jg_id").setValue("");
                        form.findField("plgb_area").setValue("");
                    }
                }
            },/*{
                fieldLabel: '颜色',
                xtype: 'colorfield',
                name: 'bmlc_color',
                readOnly:true,
                renderer:function(value){
                    return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
                 }
            },*/{
				xtype: 'combobox',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果"),
				name: 'jg_id',
				queryMode: 'remote',
				editable:false,
				valueField: 'TZ_APPPRO_HF_BH',
				displayField: 'TZ_CLS_RESULT',
				listeners:{
					change:function(file, newValue, oldValue){
						var form = file.findParentByType('perLcjgPubWindowP').down('form[name=Hfy_form]').getForm();
						var bj_id=form.getValues().bj_id;
						var bmlc_id=form.getValues().ry_lc;
						var comParams='"bj_id":"'+bj_id+'","bmlc_id":"'+bmlc_id+'","old_value":"'+oldValue+'","new_value":"'+newValue+'"';
						var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"EJSON","comParams":{'+comParams+'}}';
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
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.content","内容"),
                ignoreChangesFlag: true,
				name:'plgb_area'
			}
		]
    }],
    buttons: [{
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
		iconCls:"save",
		handler: 'SaveperLcjgPubWindowP'
	},{
        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定") ,
		iconCls:"ensure",
		handler: 'EnsureperLcjgPubWindowP'
	},{
        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭") ,
		iconCls:"close",
		handler: 'CloseperLcjgPubWindowP'
	}]
});
