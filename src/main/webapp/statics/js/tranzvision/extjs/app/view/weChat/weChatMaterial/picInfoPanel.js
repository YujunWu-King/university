Ext.define('KitchenSink.view.weChat.weChatMaterial.picInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'picInfoPanel',
	controller: 'weChatMaterialController',
    actType:'add',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.weChat.weChatMaterial.weChatMaterialController',
        'KitchenSink.view.weChat.weChatMaterial.picStore'
	],
    name:'picInfoPanel',
    title: "图片素材",
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'picform',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '应用ID',
            name: 'wxAppId',
            allowBlank:false,
            ignoreChangesFlag:true,
            hidden:true
        },{
            xtype: 'textfield',
            fieldLabel: '机构ID',
            name: 'jgId',
            allowBlank:false,
            ignoreChangesFlag:true,
            hidden:true
        },{
        	xtype: 'textfield',
            fieldLabel: '素材序号',
            name: 'tzSeq',
            hidden:true
        },{
        	xtype: 'textfield',
            fieldLabel: '图片路径',
            name: 'filePath',
            hidden:true
        },{
            xtype: 'textfield',
            fieldLabel: "素材名称", 
			name: 'name',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
        	xtype: 'textareafield',
            grow:true,
			fieldLabel: "备注信息",
			name: 'bz'
        }, {
            layout: {
                type: 'column'
            },
            items:[{
                columnWidth:.3,
                bodyStyle:'padding:10px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: "image",
                    src: ""	,
                    name: "titileImage",
                    height:186, 
                    style:'margin-left:105px;'
                },{
                    layout: {
                        type: 'column'
                    },
                    bodyStyle:'padding:10px 0 0 0',
                    xtype: 'form',
                    items: [{
                        columnWidth:.75,
                        xtype: "fileuploadfield",
                        buttonText: '上传图片',
                        name: 'orguploadfile',
                        buttonOnly:true,
                        style:'margin-left:105px;',
                        listeners:{
                            change:function(file, value, eOpts){
                            	if(value != ""){
            	        			var form = file.findParentByType("form").getForm();
                                    var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
                                    if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
                                        Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
                                        return;
                                    };
                                    // 获取该类
            						var panel = file.findParentByType("picInfoPanel");
            						if(panel.actType == "update"||panel.actType == "add"){

            								form.submit({
                                                url : TzUniversityContextPath + '/UpdServlet?filePath=weChatPic',
            									//waitMsg: '附件正在上传，请耐心等待....',
            									success: function (form, action) {
            										var message = action.result.msg;
            										var path = message.accessPath;
            										var filename = message.filename;
            										var sysFileName = message.sysFileName;
            										if(path.charAt(path.length - 1) == '/'){
            											path = path + sysFileName;
            										}else{
            											path = path + "/" + sysFileName;
            										}
            										panel.child("form").getForm().findField("filePath").setValue(path);
                                                    panel.down("image[name=titileImage]").setSrc(TzUniversityContextPath+path);
            									},
            									failure: function (form, action) {
            											// 重置表单
            										//form.reset();
            										Ext.MessageBox.alert("错误", action.result.msg);
            									}
            								});
            								
            						}else{
            							// 重置表单
            							form.reset();
            							Ext.MessageBox.alert("提示", "请先保存菜单类型。");
            						}
            					}
            				}
                        }
                    },{
                        columnWidth:.25,
                        xtype: 'button',
                        text: '删除',
                        listeners:{
                            click:function(btn, value, eOpts){
                            	var panel = btn.findParentByType("picInfoPanel");
                            	var form = panel.child("form").getForm();
                            	form.findField("filePath").setValue("");
                            	panel.down("image[name=titileImage]").setSrc("");
                            }
                        }
                    }]
                }]
            }]
        }, {
             xtype: 'combo',
             labelWidth: 100,
             editable: false,
             fieldLabel: '发布状态',
             name: 'status',
             mode: "remote",
             preSubTpl: [
                            '<div id="{cmpId}-triggerWrap" data-ref="triggerWrap" style="border:0" class="{triggerWrapCls} {triggerWrapCls}-{ui}">',
                                '<div id={cmpId}-inputWrap data-ref="inputWrap" class="{inputWrapCls} {inputWrapCls}-{ui}">'
                        ],
             readOnly:true,
             valueField: 'status',
             displayField: 'statusDesc',
             store: {
                 fields: ["status", "statusDesc"],
                 data: [
                     {status: "Y", statusDesc: "已发布"},
                     {status: "N", statusDesc: "未发布"}
                 ]
             },
             value:'N',
             style: 'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 0px solid;'
    },{
            xtype: 'displayfield',
			fieldLabel: "同步时间",
			name: 'tbTime'
        },
        {
            xtype: 'displayfield',
            fieldLabel: "本地最后修改时间",
			name: 'editTime'
        }]
    }],
    buttons: [{
		text: '发布',
		iconCls:"publish",
        name:'publishBtn',
		handler: 'pIssue'
	},{
		text: '撤销发布',
		iconCls:"revoke",
        name:'revokeBtn',
		handler: 'pRevoke'
	},{
		text: '保存',
		iconCls:"save",
		handler: 'pSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'pEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'pClose'
	}]
});
