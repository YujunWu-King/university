Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmlcPldr', {
    extend: 'Ext.window.Window',
    xtype: 'bmlcPldr', 
	controller: 'studentsList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bmlcPldr","批量导入录入结果") ,
    reference:'bmlcPldr',
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
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '班级ID',
			name: 'bj_id'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '报名流程ID',
			name: 'bmlc_id'
        },{
			xtype: 'filefield',
			name: 'file_name',
			fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.attachment","附件") ,
            labelWidth: 50,
			msgTarget: 'side',
			allowBlank: false,
			anchor: '100%',
			buttonText: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.selectAttachment","选择附件")
        }/*,{
			xtype: 'fileuploadfield',
	        name: 'menutypeimg',
	        buttonText: '上传',
	        buttonOnly:true,
			listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							var panel = file.findParentByType("skinInfo");
							if(panel.actType == "update"){
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "doc" || fix.toLowerCase() == "docx"){
									form.submit({
										url: TzUniversityContextPath + '/UpdServlet?filePath=enrollment',
										waitMsg: '附件正在上传，请耐心等待....',
										success: function (form, action) {
											var message = action.result.msg;
											var path = message.accessPath;
											var sysFileName = message.sysFileName;
											if(path.charAt(path.length - 1) == '/'){
												path = path + sysFileName;
											}else{
												path = path + "/" + sysFileName;
											}
											var comSiteParams = panel.child("form").getForm().getValues();
											//站点模板id
											var siteId = comSiteParams["siteId"];
											//皮肤id
											var skinId = comSiteParams["skinId"];
											
											var tzParams = '{"siteId":"' + siteId + '","skinId":"' + skinId + '","name":"' + action.result.msg.filename +'","path":"' + path + '"}';
											Ext.Ajax.request({
											    url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_SkinPicAttUp',
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.success == 0){
											        	var applyItemGrid =  file.findParentByType("form").previousSibling();
														var tzStoreParams = '{"siteId":"'+siteId+'","skinId":"'+skinId+'"}';
														applyItemGrid.store.tzStoreParams = tzStoreParams;
														applyItemGrid.store.reload();
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.message);        
											        }
											    }
											});
											//重置表单
											form.reset();
										},
										failure: function (form, action) {
											//重置表单
											form.reset();
											Ext.MessageBox.alert("错误", action.result.msg);
										}
									});
								}else{
									//重置表单
									form.reset();
									Ext.MessageBox.alert("提示", "请上传jpg|png|gif|bmp|ico格式的图片。");
								}
							}else{
								//重置表单
								form.reset();
								Ext.MessageBox.alert("提示", "请先保存菜单类型。");
							}
						}
					}
				}
	        }*/,{
			xtype: 'displayfield',
			value:'<a href="/tranzvision/kitchensink/app/view/enrollmentManagement/bmb_lc/批量导入模板.xls" target="_blank">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.xzdrexcelmb","下载导入模板（Excel）") +'</a>'
		}]
    }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.upload","上传"),
		iconCls:"ensure",
		handler: 'upload'
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
		iconCls:"close",
		handler: 'ClosePldr'
	}]
});
