Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.uploadRefLetterWindow', {
    extend: 'Ext.window.Window',
	reference: 'uploadRefLetterWindow',
    xtype: 'uploadRefLetterWindow',
	width: 500,
	height: 170,
	minWidth: 300,
	minHeight: 150,
    columnLines: true,
    ignoreChangesFlag:true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.sctjx","上传推荐信"),
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',

    items: [{
			xtype: 'form',
			reference: 'uploadRefLetterForm',
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
					name: 'stuName',
					hidden:true
			},{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'currentRowIndex',
					hidden:true
			},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.filePurl","文件上传路径") ,
					name: 'filePurl',
                	hidden:true

			},{
				layout: {
					type: 'column'
				},
				bodyStyle:'padding:10px 0 10px 0',
				xtype: 'form',  
				items:[{
					width:450,
					xtype: 'filefield',
					fieldLabel: '',
					buttonText: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.browse","浏览"),
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
			text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),
			iconCls:"ensure",
			handler: 'onUploadRefLetterWindowEnsure'
		}, {
			text:  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),
			iconCls:"close",
			handler: 'onUploadRefLetterWindowClose'
		}]
});

function addAttach(bt, value, eOpts){
	
	var form = bt.findParentByType("form").getForm();

	if(value != "")
	{
		var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
		if(fix.toLowerCase() != "doc" && fix.toLowerCase() != "docx" && fix.toLowerCase() != "pdf" && fix.toLowerCase() != "jpg" && fix.toLowerCase() != "png"&& fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
			Ext.MessageBox.alert( Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qsczdgsdwj","请上传docx|docx|pdf|jpg|png|gif|bmp格式的文件。"));
			form.reset();
			return;
		};
	}
}
