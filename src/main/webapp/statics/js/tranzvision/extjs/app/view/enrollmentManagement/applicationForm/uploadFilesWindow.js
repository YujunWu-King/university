Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.uploadFilesWindow', {
    extend: 'Ext.window.Window',
	reference: 'uploadFilesWindow',
    xtype: 'uploadFilesWindow',
	width: 500,
	height: 250,
	minWidth: 300,
	minHeight: 250,
    columnLines: true,
    ignoreChangesFlag:true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.uploadAttachment","上传附件"),
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',

    items: [{
			xtype: 'form',
			reference: 'uploadFilesForm',
			layout: {
				type: 'vbox',       // Arrange child items vertically
				align: 'stretch'    // 控件横向拉伸至容器大小
			},
			border: false,
			bodyPadding: 10,
			bodyStyle:'overflow-y:auto;overflow-x:hidden',
			
			fieldDefaults: {
				msgTarget: 'side',
				labelWidth: 100,
				labelSeparator:'',
				labelStyle: 'font-weight:bold'
			},
			items: [{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'appInsID',
					hidden:true
			},{
                xtype: 'textfield',
                fieldLabel: '',
                name: 'stuName',
                hidden:true
            },{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'currentRowIndex',
					hidden:true
			},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.filePurl","文件上传路径") ,
					name: 'filePurl',
					hidden:true
			},{
				layout: {
					type: 'vbox',
                    align: 'stretch'
				},
				bodyStyle:'padding:10px 0 10px 0',
				xtype: 'form',  
				items:[{
                    xtype: 'label',
                    style:'margin-top:20px',
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.attachmentName","附件名称")
                },{
                    xtype: 'textfield',
                    //fieldLabel: '附件名称',
                    width:381,
                    name: 'FileName',
                    allowBlank: false
                },{
					width:450,
					xtype: 'filefield',
					fieldLabel: '',
					buttonText:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.browse","浏览"),
					//name: 'refLetterFile',
					name: 'orguploadfile',
					listeners:{
						change:function(bt, value, eOpts){
							addAttach(bt, value, eOpts);
						}
					}
				}]
			}
			]
		}],
		buttons: [{
			text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensure","确定"),
			iconCls:"ensure",
			handler: 'onUploadFilesWindowEnsure'
		}, {
			text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
			iconCls:"close",
			handler: 'onUploadFilesWindowClose'
		}]
});

function addAttach(bt, value, eOpts){
	
	var form = bt.findParentByType("form").getForm();

	if(value != "")
	{
		var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
		if(fix.toLowerCase() != "doc" && fix.toLowerCase() != "docx" && fix.toLowerCase() != "pdf" && fix.toLowerCase() != "jpg" && fix.toLowerCase() != "png"&& fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","浏览"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.qsczdgsdwj","请上传docx|docx|pdf|jpg|png|gif|bmp格式的文件。"));
			form.reset();
			return;
		};
		//form.findField("filename").setValue(value);
	}
}
