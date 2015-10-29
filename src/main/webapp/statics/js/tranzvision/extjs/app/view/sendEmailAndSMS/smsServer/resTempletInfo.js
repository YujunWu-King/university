Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'resTempletInfo', 
	controller: 'resTempletInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfoMth',
        'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoModel',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoStore',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoModel',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoStore'
		
	],

    title: '元模板定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',

    items: [{
        xtype: 'form',
        reference: 'resTempletForm',
		layout: {
            type: 'vbox',       // Arrange child items vertically
            align: 'stretch'    // 控件横向拉伸至容器大小
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '元模板编号',
			name: 'restempid'
        },{
            xtype: 'textfield',
            fieldLabel: '元模板名称',
			name: 'restempname'
        },
		{
			xtype: 'tabpanel',
        	frame: true,
			
			items:[
			{	
				title:'基本信息',
				items:[{
					layout: {
						type: 'vbox',       // Arrange child items vertically
						align: 'stretch'    // 控件横向拉伸至容器大小
					},
					border: 1,
					bodyPadding: 10,
					bodyStyle:'overflow-y:auto;overflow-x:hidden',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 120,
						labelStyle: 'font-weight:bold'
					},
					items:[{
						xtype: 'textfield',
						fieldLabel: '机构',
						name: 'restemporg'
					},{
						xtype: 'checkbox',
						boxLabel: '是否启用',
						name:'isneed',
						inputValue:'Y',
						uncheckedValue:'N'
					},{
						xtype: 'checkbox',
						boxLabel: '是否包含动态个性化内容',
						name:'ispersonalize',
						inputValue: 'Y',
						uncheckedValue:'N'
					},{
						xtype: 'textarea',
						fieldLabel: '描述',
						name:'baseinfodesc'
					}]
				}]
			},
			{
				title:'模板参数列表',
				
				items:[
				{	
					layout: {
						type: 'vbox',       // Arrange child items vertically
						align: 'stretch'    // 控件横向拉伸至容器大小
					},
					border: 1,
					bodyPadding: 10,
					bodyStyle:'overflow-y:auto;overflow-x:hidden',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 120,
						labelStyle: 'font-weight:bold'
					},
					items:[{
						xtype: 'textfield',
						fieldLabel: '模板参数类别名',
						name: 'tempclassalias'
					} ,
					
					{
						xtype: 'grid',
						height: 360, 
						frame: true,
						name: 'resTmplRaraGrid',
						reference: 'resTmplRaraGrid',
						store: {
									type: 'resTempletParaInfoStore'
							   },
						dockedItems:[{
							xtype:"toolbar",
							items:[
								{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addParaRow'}
							]
						}],
						columnLines: true,    //显示纵向表格线
						plugins:[             //可编辑单元格
							Ext.create('Ext.grid.plugin.CellEditing', {
								clicksToEdit: 1   //单击进行编辑
							})
						],	
						columns: [{ 
							text: '参数名称',
							sortable: true,
							dataIndex: 'paraid',
							editor:{
								xtype:'textfield',
								allowBlank:false
							},
							width: 120
						},{
							text: '参数中文名',
							sortable: true,
							dataIndex: 'paraname',
							width: 150
						},{
							text: '参数别名',
							sortable: true,
							dataIndex: 'paraalias',
							editor:{
								xtype:'textfield',
								allowBlank:false
							},
							width: 150
						},{
							text: '系统变量',
							sortable: true,
							dataIndex: 'systvar',
							editor:{
								xtype:'textfield',
								allowBlank:false
							},
							width: 120
						},{
							text: '系统变量名称',
							sortable: true,
							dataIndex: 'systvarname',
							minWidth: 150,
							flex:1
						},{
							menuDisabled: true,
							sortable: false,
							width:40,
							xtype: 'actioncolumn',
							items:[
							  {iconCls: 'remove',tooltip: '删除',handler:'deleteParaRow'}
						   ]
						}]
					}
					
					]
				}]
			},
			{
				title:'模板内容采集规则',
				items:[{
					layout: {
						type: 'vbox',       // Arrange child items vertically
						align: 'stretch'    // 控件横向拉伸至容器大小
					},
					border: 1,
					bodyPadding: 10,
					bodyStyle:'overflow-y:auto;overflow-x:hidden',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 120,
						labelStyle: 'font-weight:bold'
					},
					items:[
						{
							xtype: 'radio',
							boxLabel: '手工编辑模板内容',
							name: 'restempopra',
							checked:true,
							inputValue:'A'
						},{
							xtype: 'textfield',
							fieldLabel: '表名',
							name:'recname'
						},{
							xtype: 'textfield',
							fieldLabel: '别名',
							name:'recalias'
						},
						{
							xtype: 'grid',
							height: 360, 
							frame: true,
							name: 'resTmplContentGrid',
							reference: 'resTmplContentGrid',
							/**/
							store: {
										type: 'resTempletContentInfoStore'
								   },
								   
							dockedItems:[{
								xtype:"toolbar",
								items:[
									{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addConentRow'}
								]
							}],
							columnLines: true,    //显示纵向表格线
							plugins:[             //可编辑单元格
								Ext.create('Ext.grid.plugin.CellEditing', {
									clicksToEdit: 1   //单击进行编辑
								})
							],	
							columns: [{ 
								text: '关键字',
								sortable: true,
								dataIndex: 'keyname',
								editor:{
									xtype:'textfield',
									allowBlank:false
								},
								width: 200
							},{
								text: '对应取值的模板参数',
								sortable: true,
								dataIndex: 'paraid',
								editor:{
									xtype:'textfield',
									allowBlank:false
								},
								width: 200
							},{
								text: '参数描述',
								sortable: true,
								dataIndex: 'paraname',
								minWidth: 200,
								flex:1
							},{
								menuDisabled: true,
								sortable: false,
								width:40,
								xtype: 'actioncolumn',
								items:[
								  {iconCls: 'remove',tooltip: '删除当前行数据',handler:'deleteContentRow'}
							   ]
							}]
						},
						{
							xtype: 'radio',
							boxLabel: '通过AppClass取值',
							name: 'restempopra',
							inputValue:'B'
						},{
							xtype: 'textfield',
							fieldLabel: '应用程序类ID',
							name: 'contclassid'
						},{
							xtype: 'radio',
							boxLabel: '其他',
							name: 'restempopra',
							inputValue:'C'
						}
				]}]
			}
		]
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
