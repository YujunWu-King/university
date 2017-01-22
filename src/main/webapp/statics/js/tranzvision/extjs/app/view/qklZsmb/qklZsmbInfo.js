Ext.define('KitchenSink.view.qklZsmb.qklZsmbInfo', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.qklZsmb.zsmbController'
    ],
    extend : 'Ext.panel.Panel',
    controller:'zsmbController',
    autoScroll:false,
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'证书模板定义',
    frame:true,
    initComponent:function(){
        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin:'8px',
                style:'border:0px',
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '证书模板编号',
                    name: 'certTmpl',
                    cls:'lanage_1',
                    allowBlank: false
                },{
                    xtype: 'textfield',
                    fieldLabel: '证书名称',
                    name: 'tmplName',
                    cls:'lanage_1',
                    allowBlank: false

                },{
                    xtype: 'textfield',
                    fieldLabel: '证书颁发机构',
                    name: 'certJGID',
                    cls:'lanage_1',
                    allowBlank: false
                },{
                        xtype: 'tabpanel',
                        frame: true,
                        activeTab: 0,
                        plain: false,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                        },
                        items:[{
                                title:'证书套打模板',
                                xtype:'form',
                                name:'certMergHtml1',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex:999,
                                    height: 415,
                                    allowBlank: true

                                }]
                            },{
                                title:'拥有人查看模版',
                                xtype:'form',
                                name:'certMergHtml2',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex:999,
                                    height: 415,
                                    allowBlank: true

                                }]
                            },{
                                title:'他人查看模版',
                                xtype:'form',
                                name:'certMergHtml3',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex:999,
                                    height: 415,
                                    allowBlank: true

                                }]
                            }]
                    }]
            },{
                xtype: 'hidden',
                //fieldLabel: '背景图片路径',
                fieldLabel: '标题图',
                name: 'attachSysFilena'
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
    				html:'<span style="font-weight:bold">'+ 标题图+':</span>'
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
    						var panel = file.findParentByType("orgInfo");
    						panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
    					}
    				}
    			},{
    	            xtype: 'fileuploadfield',
    	            name: 'orgLoginBjImg',
    	            buttonText: '上传',
    	            //msgTarget: 'side',
    	            buttonOnly:true,
    							listeners:{
    								change:function(file, value, eOpts ){
    									if(value != ""){
    										var form = file.findParentByType("form").getForm();
    										//获取该类
    										var panel = file.findParentByType("orgInfo");
    										
    											//获取后缀
    											var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
    											if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
    												form.submit({
    													url: '/UpdServlet?filePath=/linkfile/sysImages',
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
    														
    														file.previousSibling().previousSibling().setSrc(path);		
    														panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue(path);
    																		
    														tzParams = '{"ComID":"","PageID":"","OperateType":"HTML","comParams":' + Ext.JSON.encode(action.result.msg) +'}';
    				
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
    			}],
            buttons:[
                {
                    text: '预览',
                    handler:'sendEmail',
                    iconCls:'send'
                },
                {
                    text: '保存',
                    handler:'bugInfoSave',
                    iconCls:'save'
                },{
                    text:'确定',
                    handler:'bugInfoEnsure',
                    iconCls:'ensure'
                },{
                    text:'关闭',
                    iconCls:'close',
                    handler:'bugInfoClose'
                }
            ]
        });
        this.callParent();
    }
});
