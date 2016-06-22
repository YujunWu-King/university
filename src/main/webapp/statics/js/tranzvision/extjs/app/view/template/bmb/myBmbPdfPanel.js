Ext.define('KitchenSink.view.template.bmb.myBmbPdfPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'myBmbPdfPanel',
	controller: 'myBmb',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.bmb.myBmbPdfStore'
	],
    title: 'PDF导出模板设置',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',// 默认新增
    initComponent:function(){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'myBmbPdfForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                // heigth: 600,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'hidden',
                    fieldLabel: '上传PDF文件名',
                    name: 'fileName',
                    allowBlank:false,
                },{
                    xtype: 'hidden',
                    fieldLabel: '报名表模板ID',
                    name: 'tplID',
                    allowBlank:false,
                }, {
                    xtype: 'textfield',
                    fieldLabel: '归属机构',
                    name: 'jgName',
                    allowBlank:false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '报名表模板名称',
                    allowBlank:false,
                    name: 'tplName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
        	        xtype: 'fileuploadfield',
        	        fieldLabel: 'PDF打印模板',
        	        name: 'pdfuploadfile',
        	        buttonText: '上传PDF',
        	            // msgTarget: 'side',
        	        buttonOnly:true,
        	        listeners:{
        	        	change:function(file, value, eOpts ){
        	        		if(value != ""){
        	        			var form = file.findParentByType("form").getForm();
        	        			// Ext.MessageBox.alert('提示', form);
        						// 获取该类
        						var panel = file.findParentByType("myBmbPdfPanel");
        						
        						if(panel.actType == "update"){
        							var tplID =panel.child("form").getForm().findField("tplID").getValue();
        								// 获取后缀
        							var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
        							if(fix.toLowerCase() == "pdf" ){
        								form.submit({
        									url: TzUniversityContextPath + '/UpPdfServlet?tplid='+tplID,
        									waitMsg: 'PAD正在上传，请耐心等待....',
        									success: function (form, action) {
        										var message = action.result.msg;
        										var path = message.accessPath;
        										var sysFileName = message.sysFileName;
        										if(path.charAt(path.length - 1) == '/'){
        											path = path + sysFileName;
        										}else{
        											path = path + "/" + sysFileName;
        										}
        										panel.child("form").getForm().findField("fileName").setValue(path);
        										// Ext.MessageBox.alert("错误",
												// panel.child("form").getForm().findField("fileName").getValue());
        										// var comSiteParams =
												// panel.child("form").getForm().getValues();
        										// 重置表单
        										// form.reset();
        									},
        									failure: function (form, action) {
        											// 重置表单
        										form.reset();
        										Ext.MessageBox.alert("错误", action.result.msg);
        									}
        								});
        							}else{
        								// 重置表单
        								form.reset();
        								Ext.MessageBox.alert("提示", "请上传pdf格式的文件。");
        							}
        						}else{
        							// 重置表单
        							form.reset();
        							// Ext.MessageBox.alert('提示',
									// panel.actType);
        							Ext.MessageBox.alert("提示", "请先保存菜单类型。");
        						}
        					}
        				}
        			}
                }, {
                    xtype: 'combo',
                    fieldLabel: 'PDF打印模板状态',
                    allowBlank:false,
                    queryMode:"local",
                    editable:false,
                    store:{
                        fields:[
                            {name:'statusCode'},
                            {name:'statusDesc'}
                        ],
                        data:[
                            {statusCode:'A',statusDesc:'有效'},
                            {statusCode:'I',statusDesc:'无效'}
                        ]
                    },
                    valueField:'statusCode',
                    displayField:'statusDesc',
                    allowBlank:false,
                    value:'A',/* 默认有效 */
                    name: 'tpPdfStatus',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }
                ]
            },
                {
                    xtype: 'grid',
                    height:520,
                    title: 'PDF打印字段配置列表',
                    frame: true,
                    columnLines: true,
                    multiSelect: true,
                    selModel: {
                        type: 'checkboxmodel'
                    },
                    dockedItems:{
                        xtype:"toolbar",
                        items:[
                            {text:"加载报名表模板字段",tooltip:"加载报名表模板字段",iconCls:"refresh",handler:'loadAppFormModalFields'},"-",
                            {text:"添加字段",tooltip:"添加字段",iconCls:"add",handler:'addField'},"-",
                            {text:"加载PDF模板信息项",tooltip:"加载PDF模板信息项",iconCls:"add",handler:'addPdfField'},"-",
                            {text:"删除",tooltip:"删除",iconCls:"remove",handler:'removeSelectField'}
                        ]
                    },
                    reference: 'fieldGrid',
                    style:"margin:0 10px 10px 10px",
                    store: {
                        type: 'myBmbPdfStore'
                    },
                    plugins:[  
                             Ext.create('Ext.grid.plugin.CellEditing',{  
                            	 clicksToEdit:1 //设置单击单元格编辑  
                             })
                    ],  
                    viewConfig: {
                    	enableTextSelection: true,
            			/*plugins: {
            				ptype: 'gridviewdragdrop',
            				containerScroll: true,
            				dragGroup: this,
            				dropGroup: this
            			},
            			listeners: {
            				drop: function(node, data, dropRec, dropPosition) {
            					data.view.store.beginUpdate();
            					var items = data.view.store.data.items;
            					for(var i = 0;i< items.length;i++){
            						items[i].set('orderNum',i+1);
            					}
            					data.view.store.endUpdate();
            				}
            			}*/
            		},
                    columns: [{
                        text: "报名表模板ID",
                        dataIndex: 'tplID',
                        editable:false,
                        hidden: true
                    },{
                        text: "报名表信息项编号",
                        dataIndex: 'fieldID',
                        editable:false,
                        width: 160
                    },{
                        text: "报名表信息项名称",
                        dataIndex: 'fieldName',
                        minWidth: 160,
                        editable:false
                     },{
                    	 text: "打印PDF域名称1",
                         dataIndex: 'pdffield1',
                         minWidth: 160,
                         editor : {
                        	 allowBlank:false,
                        	 xtype:'textfield'
                        },
                        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){    
                            if(value==null || value=='null'){
                              return '';
                            } else {
                              return value;
                             }
                        }	
                     },{
                    	 text: "打印PDF域名称2",
                         dataIndex: 'pdffield2',
                         minWidth: 160,
                         editor : {
                        	 allowBlank:false,
                        	 xtype:'textfield'
                        },
                        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){    
                            if(value==null || value=='null'){
                              return '';
                            } else {
                              return value;
                             }
                        }
                     },{
                    	 text: "打印PDF域名称3",
                         dataIndex: 'pdffield3',
                         minWidth: 160,
                         editor : {
                        	 allowBlank:false,
                        	 xtype:'textfield'
                        },
                        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){    
                            if(value==null || value=='null'){
                              return '';
                            } else {
                              return value;
                             }
                        }
                      },{
                            menuDisabled: true,
                            sortable: false,
                            width:60,
                            text:'操作',
                            xtype: 'actioncolumn',
                            align:'center',
                            items:[
                                {iconCls: 'remove',tooltip: '删除',handler:'removeField'}
                            ]
                        }
                    ]
                }
            ]
        })
        this.callParent();
    },
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onTplInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onTplInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onTplInfoClose'
	}]
});