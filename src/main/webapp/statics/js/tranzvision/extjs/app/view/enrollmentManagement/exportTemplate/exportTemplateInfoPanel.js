Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'exportTemplateInfo',
	controller: 'exportTemplate',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.exportTemplate.fieldStore',
        'KitchenSink.view.enrollmentManagement.exportTemplate.exportFieldSetWindow'
	],
    actType: 'add',//默认新增
    initComponent:function(){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'exportTemplateInfoForm',
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
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    fieldLabel: '导出模板编号',
                    name: 'tplID',
                    maxLength:15,
                    allowBlank:false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '导出模板名称',
                    allowBlank:false,
                    name: 'tplName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'combo',
                    fieldLabel: '模板类型',
                    allowBlank:false,
                    queryMode:"local",
                    name: 'tplType',
                    editable:false,
                    store:new KitchenSink.view.common.store.appTransStore("TZ_EXP_TPL_TYPE"),
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'combo',
                    fieldLabel: '模板状态',
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
                    value:'A',/*默认有效*/
                    name: 'tplStatus',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },
                    {
                        layout: {
                        type: 'column'
                        },
                        bodyStyle:'padding:0 0 10 0',
                        items:[{
                            xtype: 'textfield',
                            fieldLabel: '报名表模板ID',
                            name: 'modalID',
                            allowBlank:false,
                            fieldWidth:200,
                            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            editable:false,
                            triggers: {
                                search: {
                                    cls: 'x-form-search-trigger',
                                    handler: "selectAppModalID"
                                }
                            }
                        }
                            ,{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:8px',
                            name: 'modalName'
                        }
                        ]
                    }
                ]
            }
           ,
                {
                    xtype: 'grid',
                    height:520,
                    title: '字段定义',
                    frame: true,
                    columnLines: true,
                    dockedItems:{
                        xtype:"toolbar",
                        items:[
                            {text:"加载报名表模板字段",tooltip:"加载报名表模板字段",iconCls:"refresh",handler:'loadAppFormModalFields'},"-",
                            {text:"添加字段",tooltip:"添加字段",iconCls:"add",handler:'addField'},"-",
                            {text:"添加应用程序类",tooltip:"添加应用程序类",iconCls:"add",handler:'addAppClass'}
                        ]
                    },
                    plugins: [
                        {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        }
                    ],
                    reference: 'fieldGrid',
                    style:"margin:0 10px 10px 10px",
                    store: {
                        type: 'exportTemplateFieldStore'
                    },
                    columns: [{
                        text: "导出字段序号",
                        dataIndex: 'fieldSeq',
                        width: 120,
                        editor: {
                            xtype:'numberfield'
                        }
                    },{
                            text: "导出字段名称",
                            dataIndex: 'fieldName',
                            minWidth: 200,
                            flex:1,
                            editor: {
                                xtype:'textfield'
                            }
                        },{
                            text: "应用程序类",
                            dataIndex: 'appClass',
                            width: 220
                        },{
                            xtype:'linkcolumn',
                            text:"导出字段设置",
                            sortable:false,
                            items:[{
                                text:"导出字段设置",
                                tooltip:"导出字段设置",
                                handler: "exportFieldSet",
                                isDisabled:function(view ,rowIndex ,colIndex ,item ,record ){
                                	if(record.get("appClass")!=undefined&&record.get("appClass")!=""){
                                		return true;
                                	}
                                	return false;
                                },
                                getClass:function(v ,metadata,r){
                                    if(r.get("appClass")!=undefined&&r.get("appClass")!=""){
                                        return Ext.baseCSSPrefix+"hidden-display";
                                    }
                                    return "";
                                }
                            }],
                            width:130
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
    title: '导出模板信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
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