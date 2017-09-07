Ext.define('KitchenSink.view.weChat.weChatMaterial.picInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'picInfoPanel',
	controller: 'weChatMaterialController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.weChat.weChatMaterial.weChatMaterialController',
        'KitchenSink.view.weChat.weChatMaterial.picStore'
	],
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
        //bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '上传PDF文件名',
            name: 'fileName',
            hidden:true
        },{
            xtype: 'textfield',
            fieldLabel: '文件路径',
            name: 'filePath',
            hidden:true
        },{
            xtype: 'textfield',
            fieldLabel: "素材名称", 
			name: 'name',
            allowBlank: false
        }, {
        	xtype: 'textareafield',
            grow:true,
			fieldLabel: "备注信息",
			name: 'bzInfo',
			allowBlank: false
        }, {
            layout: {
                type: 'column'
            },
            items:[{
            	columnWidth:.4
            },{
                columnWidth:.2,
                bodyStyle:'padding:10px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: "image",
                    src: ""	,
                    name: "titileImage"
                },{
                    layout: {
                        type: 'column'
                    },
                    bodyStyle:'padding:10px 0 0 0',
                    xtype: 'form',
                    items: [{
                        columnWidth:.65,
                        xtype: "fileuploadfield",
                        buttonText: '上传图片',
                        name: 'orguploadfile',
                        buttonOnly:true,
                        listeners:{
                            change:function(file, value, eOpts){
                                addAttach(file, value, "IMG");
                            }
                        }
                    },{
                        columnWidth:.35,
                        xtype: 'button',
                        text: '删除',
                        listeners:{
                            click:function(bt, value, eOpts){
                                deleteImage(bt, value, eOpts);
                            }
                        }
                    }]
                }]
            }]
        }, {
            xtype: 'displayfield',
			fieldLabel: "发布状态",
			name: 'status'
        }, {
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
		handler: 'pIssue'
	},{
		text: '撤销发布',
		iconCls:"revoke",
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

function addAttach(file, value, attachmentType){
   var form = file.findParentByType("form").getForm();
   var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
   if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
		Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
		form.reset();
		return;
	};	
		
 
}
