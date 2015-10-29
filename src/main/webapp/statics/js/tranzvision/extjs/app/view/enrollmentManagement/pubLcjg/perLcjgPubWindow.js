Ext.define('KitchenSink.view.enrollmentManagement.pubLcjg.perLcjgPubWindow', {
    extend: 'Ext.window.Window',
    xtype: 'perLcjgPubWindow',
	controller:'lcjgPub',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.pubLcjg.lcjgPubController'
	],
    title:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfysz","回复语设置") ,
    ignoreChangesFlag: true,
    reference:'perLcjgPubWindow',
    width:500,
    modal:true,
    layout:'fit',
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    perLcjgCallBack:"",
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
				fieldLabel: '报名流程编号',
				name:'bmlc_id'
			},{
				xtype: 'hiddenfield',
				fieldLabel: '报名表ID',
				name:'bmb_id'
			},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.name","姓名"),
				name:'ry_name',
				fieldStyle:'background:#F4F4F4',
				readOnly:true
			},{
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.procedure","流程"),
                name:'ry_lc',
                fieldStyle:'background:#F4F4F4',
                readOnly:true
            },{
				xtype: 'combobox',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果"),
				name: 'jg_id',
				queryMode: 'remote',
				editable:false,
				valueField: 'TZ_APPPRO_HF_BH',
				displayField: 'TZ_CLS_RESULT',
				listeners:{
					change:function(file, newValue, oldValue, eOpts){
						var form = file.findParentByType('perLcjgPubWindow').down('form[name=Hfy_form]').getForm();
						var bj_id=form.getValues().bj_id;
						var bmlc_id=form.getValues().bmlc_id;
						var comParams='"bj_id":"'+bj_id+'","bmlc_id":"'+bmlc_id+'","old_value":"'+oldValue+'","new_value":"'+newValue+'"';
						var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"EJSON","comParams":{'+comParams+'}}';
                        //加载数据
                        Ext.Ajax.request(
                            {
                                url: Ext.tzGetGeneralURL,
                                params:{tzParams: tzParams},
                                success: function(response, opts){
                                    //返回值内容
                                    var jsonText = response.responseText;
                                    try{
                                        var jsonObject = Ext.util.JSON.decode(jsonText);
                                        /*判断服务器是否返回了正确的信息*/
                                        if(jsonObject.state.errcode == 0){
                                            if (jsonObject.comContent.gbjg_pd=="1"){
                                                form.findField("plgb_area").setValue(jsonObject.comContent.gbjg_desc);
                                            }
                                        }else if(jsonObject.state.timeout == true){
                                            try{
                                                if(Ext.getCmp("tranzvision_relogin_20150626") == undefined){
                                                    var win = new tranzvision.view.window.ReloginWindow();
                                                    win.show();
                                                }
                                            }
                                            catch(e2)
                                            {}
                                        }else{
                                            TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
                                        }
                                    }
                                    catch(e){
                                        TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
                                    }
                                },
                                failure: function(response, opts){
                                    //错误信息响应报文
                                    try
                                    {
                                        var respText = Ext.util.JSON.decode(response.responseText);
                                        TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                                    }
                                    catch(e2)
                                    {
                                        if(response.responseText == "")
                                        {
                                            TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
                                        }
                                        else
                                        {
                                            TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                                        }
                                    }
                                }
                            });
					}
				}
			},{
				xtype: 'ueditor',
				model:'simple',
                height:300,
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.content","内容"),
				name:'plgb_area'
			}
		]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),
		iconCls:"save",
		handler: 'SaveperLcjgPubWindow'
	},{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定") ,
		iconCls:"ensure",
		handler: 'EnsureperLcjgPubWindow'
	},{
		text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭") ,
		iconCls:"close",
		handler: 'CloseperLcjgPubWindow'
	}]
});
