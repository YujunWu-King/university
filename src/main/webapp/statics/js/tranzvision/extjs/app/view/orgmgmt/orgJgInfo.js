Ext.define('KitchenSink.view.orgmgmt.orgJgInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'orgJgInfo',
	controller: 'orgMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.orgmgmt.orgController',
        'KitchenSink.view.orgmgmt.orgInfoStore'
	],
    title: '机构信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'update',//默认修改
    listeners:{
        afterrender: function(panel){

            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"QF","comParams":{}}';

            Ext.tzLoad (tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                form.findField("orgId").setReadOnly(true);
                form.findField("orgName").setReadOnly(true);
                form.findField("orgYxState").setReadOnly(true);
                
				if(formData.orgLoginBjImgUrl!=""){
					panel.down("image").setSrc(TzUniversityContextPath + formData.orgLoginBjImgUrl);
				}
                panel.commitChanges(panel);
            });
        }
    },
    items: [{
        xtype: 'form',
        reference: 'orgInfoForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            //fieldLabel: '机构编号',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgId","机构编号"),
			name: 'orgId',
            cls:'lanage_1',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            //fieldLabel: '机构名称',
            cls:'lanage_1',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgName","机构名称"),
			name: 'orgName'
        }, {
            xtype: 'combobox',
            cls:'lanage_1',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.stateStore","有效状态"),
			forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_EFF_STATE"),
            typeAhead: true,
            queryMode: 'local',
			name: 'orgYxState',
            value:'Y'
        }, {
            xtype: 'textarea',
            //fieldLabel: '备注',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgBeizhu","备注"),
			name: 'orgBeizhu'
        },/* {
            html:'<br><a href="#initOrgInfo">机构初始化设置</a><br><br>'
           // handler:'initOrg'
        }, */{
            xtype: 'textfield',
            //fieldLabel: '联系人',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrName","联系人"),
			name: 'orgLxrName'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人电话',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrPhone","联系人电话"),
			name: 'orgLxrPhone'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人邮箱',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrEmail","联系人邮箱"),
			name: 'orgLxrEmail',
			vtype: 'email'
        }, {
            xtype: 'textfield',
            //fieldLabel: '登录系统文字',
            fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLoginInf","登录系统名称"),
            name: 'orgLoginInf'
        }, {
            xtype: 'hidden',
            //fieldLabel: '背景图片路径',
            fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLoginBjImgUrl","背景图片路径"),
            name: 'orgLoginBjImgUrl'
        },{
			xtype: 'form',
			layout: 'hbox',
			width:'100%',
			height:'100%',
			defaults:{
				margin:'0 0 0 20px',
			},
			items:[{
				margin:'10 35 0 0',		
				xtype:'label',
				html:'<span style="font-weight:bold">'+ Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLoginBjImg","登录页面背景图") +':</span>'
			},{
				xtype:'image',
				width:70,
				height:50,
				border:1,
				style: {
				    borderColor: '#eee'
				},
				margin:'0 20 10 0',
				src:''
			},{
				xtype:'button',
				text:'删除',
				listeners:{
					click:function(file, value, eOpts ){
						file.previousSibling().setSrc("");
						//获取该类
						var panel = file.findParentByType("orgJgInfo");
						panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'orguploadfile',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
							listeners:{
								change:function(file, value, eOpts ){
									if(value != ""){
										var form = file.findParentByType("form").getForm();
										//获取该类
										var panel = file.findParentByType("orgJgInfo");
										
											//获取后缀
											var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
											if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
												form.submit({
													url: TzUniversityContextPath + '/UpdServlet?filePath=org',
													waitMsg: '图片正在上传，请耐心等待....',
													success: function (form, action) {
														var message = action.result.msg;
														var path = message.accessPath;
														var sysFileName = message.sysFileName;
														if(path.charAt(path.length - 1) == '/'){
															path = path + sysFileName;
														}else{
															path = path + "/" + sysFileName;
														}
														
														file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);	
														panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue(path);
																		
														tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"HTML","comParams":' + Ext.JSON.encode(action.result.msg) +'}';
				
														Ext.Ajax.request({
														    url: Ext.tzGetGeneralURL,
														    params: {
														        tzParams: tzParams
														    },
														    success: function(response){
														    	var responseText = eval( "(" + response.responseText + ")" );
														      if(responseText.success == 0){
																		
																	}else{
																		file.previousSibling().previousSibling().setSrc("");
																		panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
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
										
									}
								}
							}
	      }]
			},{
            xtype: 'ueditor',
            //fieldLabel: '登录页面版权信息',
            fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLoginCopr","登录页面版权信息"),
            name: 'orgLoginCopr',
            model: 'normal',
            panelXtype: 'orgJgInfo',
            zIndex: 900 
        }]
    }],
    buttons: [{
        text: '生成机构登录页面',
        iconCls:"publish",
        handler: 'onFormPublish',
		hidden: true
    },{
		text: '保存',
		iconCls:"save",
		handler: 'onJgdFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onJgdFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
