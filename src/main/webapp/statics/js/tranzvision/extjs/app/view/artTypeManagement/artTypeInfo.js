Ext.define('KitchenSink.view.artTypeManagement.artTypeInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'artTypeInfo', 
	controller: 'artTypeController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.artTypeManagement.artTypeFieldInfoStore'
	],

    title: '内容类型定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG")
        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'artTypeInfoForm',
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
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.artTypeId","类型编号"),
                    name: 'artTypeId',
                    value:'NEXT',
                    readOnly:true,
                    cls:'lanage_1'
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.artTypeName","类型名称"),
                    name: 'artTypeName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    allowBlank: false
                },{
					xtype: 'combobox',
					fieldLabel: Ext.tzGetResourse('TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.isused','是否启用'),
					forceSelection: true,
					editable:false,
					store: useFlagStore,
					valueField: 'TValue',
					displayField: 'TSDesc',
					queryMode: 'remote',
					name: 'isused',
					value: "Y",
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				}]
            },
			{
				xtype: 'grid',
				title: '字段定义',
				minHeight:370,
				frame: true,
				columnLines: true,
				name:'artTypeFieldGrid',
				style: "margin:0 8px",
				multiSelect: true,
				viewConfig: {
                enableTextSelection: true
				},

				plugins: [{
					ptype: 'cellediting',
					listeners: {
						beforeedit: function( editor, context, eOpts ){
							var dataIndex = context.field;
							var isSysField = context.record.data.isSysField;
							if(dataIndex == "fieldValue"){
								editor.cancelEdit();
								return false;
							}
						}
					}
				}],
				store: {
					type: 'artTypeFieldInfoStore'
				},
				columns: [
					{
						text: Ext.tzGetResourse('TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.seq','序号'),
						dataIndex: 'seq',
						width: 100
					},
					{
						text: Ext.tzGetResourse('TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.fieldValue','字段值'),
						dataIndex: 'fieldValue',
						width: 160
					},
					{
						text: Ext.tzGetResourse('TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.fieldDescr','字段描述'),
						dataIndex: 'fieldDescr',
						editor: {
							xtype: 'textfield',
							allowBlank: false,
							emptyText:"不允许为空！"
						},
						flex: 1,
						width: 200
					},
					{
						xtype: 'checkcolumn',
						text: Ext.tzGetResourse('TZ_ART_TYPE_MG_COM.TZ_ARTTYPE_INF_STD.isFieldUsed','启用'),
						dataIndex: 'isused',
						width:100
					}
				]
			}
			]
        });
        this.callParent();
    },
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



