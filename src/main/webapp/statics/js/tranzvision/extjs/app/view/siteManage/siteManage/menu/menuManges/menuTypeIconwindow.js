Ext.define('KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeIconwindow', {
    extend: 'Ext.window.Window',
    xtype: 'menuTypeIconwindow', 
	controller: 'menuTypeIconController',
	requires: [
       'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeIconController'
    ],
    title: '菜单类型图标设置', 
    reference:'menuTypeIconwindow',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
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
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '站点ID',
			name: 'siteid'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '菜单类型ID',
			name: 'menutypeid'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '皮肤ID',
			name: 'skinid'
        },{
            xtype: 'textfield',
            fieldLabel: '菜单类型名称',
			name: 'menutypename',
			readOnly:true,
			fieldStyle:'background:#F4F4F4'
        },{
            xtype: 'textfield',
            fieldLabel: '皮肤名称',
			name: 'skinname',
			readOnly:true,
			fieldStyle:'background:#F4F4F4'
        },{
			xtype: 'form',
			layout: 'hbox',
			width:'100%',
			height:'100%',
			defaults:{
				margin:'0 0 0 20px',
			},
			items:[{
				margin:'0 0 0 0',		
				xtype:'label',
				html:'<span style="font-weight:bold">菜单默认图标：</span>'
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
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'websitefile',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
				listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							//获取该类
							var panel = file.findParentByType("menuTypeIconwindow");
							
							if(panel.actType == "update"){
								var siteid = panel.child("form").getForm().findField("siteid").getValue();
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
									form.submit({
										url: TzUniversityContextPath + '/UpdWebServlet?siteid='+siteid+'&filePath=menuPic',
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
											var comSiteParams = panel.child("form").getForm().getValues();
											//站点模板id
											var siteId = comSiteParams["siteid"];
											//菜单类型id
											var menutypeid = comSiteParams["menutypeid"];
											//皮肤id
											var skinId=comSiteParams["skinid"];

											var comParams = '{"siteId":"' + siteId + '","menutypeid":"' + menutypeid + '","skinId":"'+skinId+'","path":"' + path + '","imgtype":"TZ_TYPE_IMG"}';
											var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ICON1_STD","OperateType":"EJSON","comParams":'+comParams+'}';
											Ext.Ajax.request({
											    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_menuAttUp',
											    url: Ext.tzGetGeneralURL,
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.comContent.success == 0){
											        	file.previousSibling().previousSibling().setSrc(TzUniversityContextPath +　path);
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.comContent.msg);        
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
	        }]
		},{
			xtype: 'form',
			name:'imgForm2',
			layout: 'hbox',
			width:'100%',
			height:'100%',
			defaults:{
				margin:'0 0 0 20px',
			},
			items:[{
				margin:'0 0 0 0',		
				xtype:'label',
				html:'<span style="font-weight:bold">菜单选中时图标：</span>'
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
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'websitefile',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
				listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							//获取该类
							var panel = file.findParentByType("menuTypeIconwindow");
							if(panel.actType == "update"){
								var siteid = panel.child("form").getForm().findField("siteid").getValue();
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
									form.submit({
										url: TzUniversityContextPath + '/UpdWebServlet?siteid='+siteid+'&filePath=menuPic',
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
											var comSiteParams = panel.child("form").getForm().getValues();
											//站点模板id
											var siteId = comSiteParams["siteid"];
											//菜单类型id
											var menutypeid = comSiteParams["menutypeid"];
											//皮肤id
											var skinId=comSiteParams["skinid"];

											var comParams = '{"siteId":"' + siteId + '","menutypeid":"' + menutypeid + '","skinId":"'+skinId+'","path":"' + path + '","imgtype":"TZ_NOW_IMG"}';
											var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ICON1_STD","OperateType":"EJSON","comParams":'+comParams+'}';
											Ext.Ajax.request({
											    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_menuAttUp',
											    url: Ext.tzGetGeneralURL,
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.comContent.success == 0){
											        	file.previousSibling().previousSibling().setSrc(TzUniversityContextPath+path);
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.comContent.msg);        
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
	        }]
		}]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onIconTypeWindSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onIconTypeWindEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(){
			this.up("window").close();
		}
	}]
});
