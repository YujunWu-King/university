Ext.define('KitchenSink.view.template.survey.testQuestion.testWjdcDetailPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'testwjdcDetailPanel',
    reference:'testwjdcDetailPanel',
    controller:'testWjdcController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.template.survey.testQuestion.testWjdcController',
        'KitchenSink.view.template.survey.testQuestion.testWjXxxStore'
    ], 
    title: '测试问卷详情',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    actType: 'add',
    items: [
        {
            xtype: 'form',
            reference: 'CscxqForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
          //  width:750,
            border: false,
          //  bodyPadding: 10,
            style:"margin:8px",
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 110,
                labelStyle: 'font-weight:bold'
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '测试问卷编号',
                    name: 'TZ_CS_WJ_ID',
                    cls:'lanage_1',
                    readOnly:true
                }, {
                    xtype: 'textfield',
                    name: 'TZ_CS_WJ_NAME',
                    allowBlank:false,
                    fieldLabel:'测试问卷名称',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype:'textfield',
                    name:'TZ_CLASS_ID',
                    fieldLabel:'MBA项目类型'
                },{
                    xtype: 'combobox',
                    fieldLabel: Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.TZ_DC_WJ_ZT", "问卷状态"),
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    editable:false,
                    name:'TZ_DC_WJ_ZT',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_DC_WJ_ZT"),
                    queryMode: 'local',
                    value: '0'
                }, {
                    xtype: 'datefield',
                    fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_KSRQ", "开始日期"),
                    format: 'Y-m-d',
                    allowBlank:false,
                    editable:false,
                    name: 'TZ_DC_WJ_KSRQ',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },
                {
                    xtype: 'timefield',
                    fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_KSSJ", "开始时间"),
                    format: 'H:i:s',
                    value:'8:30',
                    editable:false,
                    allowBlank:false,
                    name: 'TZ_DC_WJ_KSSJ',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },
                {
                    xtype: 'datefield',
                    fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_JSRQ", "结束日期"),
                    format: 'Y-m-d',
                    allowBlank:false,
                    editable:false,
                    name: 'TZ_DC_WJ_JSRQ',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },
                {
                    xtype: 'timefield',
                    fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_JSSJ", "结束时间"),
                    format: 'H:i:s',
                    value:'17:30',
                    allowBlank:false,
                    editable:false,
                    name: 'TZ_DC_WJ_JSSJ',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'combobox',
                    fieldLabel: Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.TZ_STATE", "有效状态"),
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    name:'TZ_STATE',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_STATE"),
                    queryMode: 'local',
                    editable:false,
                    name: 'TZ_STATE',
                    value: '0'
                },{
                    xtype:'textfield',
                    name:'TZ_PRESET_NUM',
                    fieldLabel:'预设测试人数'
                },  {
                    layout: {
                        type: 'column'
                    },
                    items: [
                        {
                            columnWidth: .4,
                            xtype: 'textfield',
                            fieldLabel: "问卷模板",
                            name: 'TZ_APP_TPL_ID',
                            editable: false,
                            allowBlank: false,
                            afterLabelTextTpl: [
                                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                            ],
                            triggers: {
                                search: {
                                    cls: 'x-form-search-trigger',
                                    handler: "cswj_mbChoice"
                                }
                            }
                        },
                        {
                            columnWidth: .35,
                            xtype: 'displayfield',
                            hideLabel: true,
                            name: 'TZ_APP_TPL_MC',
                            style: 'margin-left:8px'
                        },{
                            columnWidth:0.15,
                            xtype:'button',
                            text:'开通在线调查',
                            reference:'ktWjdcBtn',
                            listeners:{ 
                                click:function(button,e,eOpts ){

	                            	var form = button.findParentByType("form").getForm();
	                            	var grid=button.findParentByType("form").findParentByType("panel").child("grid");
	                                var tplId = form.findField("TZ_APP_TPL_ID").value;
	                                var csWjId = form.findField("TZ_CS_WJ_ID").value;
	                                var wjId=form.findField("TZ_DC_WJ_ID").value;

                                  if (wjId==''){
                                    if (tplId == '') {
                                        Ext.Msg.alert('提示', "请先选择问卷模板！");
                                    } else {
                                        if (csWjId == '' || csWjId == 'NEXT') {
                                            Ext.Msg.alert('提示', "请先保存测试问卷后，在开通在线调查！");
                                        }else{
                                            Ext.Msg.confirm('提示', '是否确定开通在线调查', function(btn) {
                                                if (btn == 'yes') {
                                                    button.setDisabled(true);
                                                    var tzParams = '{"ComID":"TZ_CSWJ_LIST_COM","PageID":"TZ_CSWJ_XXX_STD","OperateType":"U","comParams":{"add":[{"csWjId":"' + csWjId + '","tplId":"' + tplId + '"}]}}';
                                                    Ext.tzLoad(tzParams, function (responseData) {
                                                        var wjId = responseData.id;
                                                        var wjName = responseData.name;
                                                        form.findField("TZ_DC_WJ_ID").setValue(wjId);
                                                        form.findField("TZ_DC_WJMC").setValue(wjName);
                                                        button.setDisabled(true);
                                                      var tzStoreParams = '{"FLAG":"A","TZ_CS_WJ_ID":"' + csWjId + '"}';
                                                         grid.store.tzStoreParams = tzStoreParams;
                                                         grid.store.load(); 

                                                    });
                                                }
                                           });
                                        }
                                     }
                                    }else{
                                        Ext.Msg.alert('提示', "该测试问卷已经开通了调查，不能重复开通！");
                                    }
                                }
                            }
                        }

                    ]
                },  {
                    layout: {
                        type: 'column'
                    },
                    items: [ 
                        {
                            columnWidth: .4,
                            xtype: 'textfield',
                            fieldLabel: "测试问卷",
                            name: 'TZ_DC_WJ_ID',
                            editable: false,
                            cls:'lanage_1',
                            displayOnly:true
                        },
                        {
                            columnWidth: .35,
                            xtype: 'displayfield',
                            hideLabel: true,
                            name: 'TZ_DC_WJMC',
                            style: 'margin-left:8px'
                        },{
                            columnWidth:0.15,
                            xtype:'button',
                            reference:'setWjdcBtn',
                            text:'设置在线调查',
                            handler:'setWjdc'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'grid',
          // title: '问卷实例详情',
           //  width:750,  
            id:'wjdcXxxGrid',
            height:330,
            frame: true,
            columnLines: true,
            style: "margin:10px",
            plugins: {
                ptype: 'cellediting',
                pluginId: 'attrItemCellEditing',
                clicksToEdit:1
            },
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop',
                    dragText: '拖拽进行选项的排序'
                },
                listeners: {
                    drop: function(node, data, dropRec, dropPosition) {
                        data.view.store.beginUpdate();
                        var items = data.view.store.data.items;
                        for (var i = 0; i < items.length; i++) {
                            items[i].set('TZ_ORDER', i + 1);
                        }
                        data.view.store.endUpdate();
                    }
                }
            },
            store: {
                type: 'testWjXxxStore'
            },
            columns: [
              /*  {   text:'测试问卷编号',
                    dataIndex:'TZ_CS_WJ_ID',
                    width:50,
                    hidden:true
                },
                {   text:'调查问卷编号',
                    dataIndex:'TZ_DC_WJ_ID',
                    width:50,
                    hidden:true
                },{
                    text:'信息项名称',
                    dataIndex:'TZ_COM_LMC',
                    width:50,
                    hidden:true
                },{text:'序号',
                  dataIndex:'TZ_ORDER',
                  width:50,
                  hidden:true
                 },*/{
                    text:'信息项类型',
                    dataIndex:'TZ_COM_LMC',
                    width:50,
                    hidden:true
                },{
                    text: '信息项编号',
                    dataIndex: 'TZ_XXX_BH',
                    width: 170,
                    editor: {
						xtype:'textfield',
						maxLength:50,
						allowBlank: false,
						 triggers: {
		                        search: {
		                            cls: 'x-form-search-trigger',
		                            handler: "Cswj_XxxChoice"
		                        }
		                    }
					}
                },{ 
                    text: '信息项名称',
                    dataIndex: 'TZ_XXX_MC',
                    minWidth: 250
                   /* editor: {
						xtype:'textfield',
						maxLength:50,
						allowBlank: false
					}*/
                },{
                    xtype:'linkcolumn',
                    sortable: false,
                    width: 80,
                    text: '设置',
                    items:[{
                        text: '设置',
                        handler:'setCswjXXXInfo'
                    }]
                },{
                    text: '信息项描述',
                    dataIndex: 'TZ_XXX_DESC',
                    minWidth: 250
                },{
                    menuDisabled: true,
                    sortable: false,
                    width:100,
                    align: 'center',
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'add',text: Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.edit","新增"),handler:'addCswjXXX'},
                        {iconCls: 'remove',text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_DETAIL_STD.delete","删除"),handler:'deleteCswjXXX'}
                    ]
                }
            ], 
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                listeners:{
                    afterrender: function(pbar){
                        var grid = pbar.findParentByType("grid");
                        pbar.setStore(grid.store);
                    }
                },
                plugins: new Ext.ux.ProgressBarPager()
            }
        }],
        buttons: [{
            text: '保存', 
            iconCls: "close",
            handler:'onCsWjdcSave'
        },{
            text: '确定',
            iconCls: "close",
            handler:'onCsWjdcEnsure'
        },{
            text: '关闭',
            iconCls: "close",
            handler:'onCsWjdcClose'
            }
        ]
});







































