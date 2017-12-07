Ext.define('KitchenSink.view.uniPrint.uniPrintTplInfo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.uniPrintTplInfo', 
	controller: 'uniPrintTplInfoController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.uniPrint.uniPrintTplFieldStore',
        'KitchenSink.view.uniPrint.uniPrintTplInfoController'
	],
    title: '打印模板信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){

    	var fieldStore = new KitchenSink.view.uniPrint.uniPrintTplFieldStore();
		var ifEffectiveStore = new KitchenSink.view.common.store.appTransStore("TZ_IF_EFFECTIVE");

    	Ext.apply(this,{
    		items: [{
    	        xtype: 'form',
    	        reference: 'uniPrintTplForm',
    			layout: {
    	            type: 'vbox',
    	            align: 'stretch'
    	        },
    	        border: false,
    	        bodyPadding: 10,
    			bodyStyle:'overflow-y:auto;overflow-x:hidden',
    	        fieldDefaults: {
    	            msgTarget: 'side',
    	            labelWidth: 140,
    	            labelStyle: 'font-weight:bold'
    	        },
    	        items: [{
    	            xtype: 'textfield',
    	            fieldLabel: "机构编号",
    	            name: 'TZ_JG_ID',
					value:Ext.tzOrgID,
					fieldStyle:'background:#F4F4F4',
					readOnly:true
    	        }, {
    	            xtype: 'textfield',
    	            fieldLabel: "打印模板编号",
    	            name: 'TZ_DYMB_ID',
					value:'NEXT',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "打印模板名称",
    				name: 'TZ_DYMB_NAME',
    				afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    				allowBlank: false
    	        }, {
                    xtype: 'combo',
                    fieldLabel: '状态',
                    allowBlank:false,
                    queryMode:"local",
                    editable:false,
                    store:ifEffectiveStore,
                    valueField:'TValue',
                    displayField:'TSDesc',
                    allowBlank:false,
                    value:'Y',/*默认有效*/
                    name: 'TZ_DYMB_ZT',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
    	            xtype: 'textfield',
    				fieldLabel: "数据导入模板",
    				name: 'TZ_DYMB_DRMB_ID',
    				editable: false,
    	            triggers:{
    			        search: {
    			            cls: 'x-form-search-trigger',
    			            handler: 'searchImpTpl'
    			        }
    		    	}
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "备注信息",
    				name: 'TZ_DYMB_MENO'
    	        },{
					xtype:'hidden',
					fieldLabel:"上传PDF文件名",
					name:"TZ_DYMB_PDF_NAME"
				},{
					xtype:'hidden',
					fieldLabel:"PDF文件路径",
					name:"TZ_DYMB_PDF_URL"
				},{
					layout:{
						type:'column'
					},
					bodyStyle:'padding:0 0 10 0',
					items:[{
						xtype: 'displayfield',
						fieldLabel: "PDF打印模板",
						name:"downfileName"
					},{
						xtype:'fileuploadfield',
						name: 'pdfuploadfile',
						buttonText: '上传',
						hideLabel:true,
						buttonOnly:true,
						listeners:{
							change: 'uploadPDF'
						}
					}, {
						style: 'margin-left:8px',
						xtype: 'button',
						text: '删除PDF模板',
						name: 'pdfDeleteBtn',
						listeners: {
							click: 'deletePDF'
						}
					}]
				}, /*{
					layout: {
						type: 'column'
					},
					bodyStyle: 'padding:0 0 10 0',
					items:[{
						xtype: 'displayfield',
						hidden:true
					},{
						xtype:'button',
						text:'解析PDF模板字段',
						handler:'analysisPDF'
					}]
				},*/{
					xtype:"grid",
					title:"数据映射关系列表",
					minHeight:260,
					name:'fieldGrid',
					columnLines:true,
					autoHeight:true,
					frame:true,
					selModel:{
						selType:'checkboxmodel'
					},
					dockedItems:[{
						xtype: 'toolbar',
						items: [{text: "新增", tooltip: "新增", iconCls:"add", handler:"addField"}, "-",
							{text:"删除",tooltip:"删除",iconCls:"remove",handler:"removeField"},"-",
							{text:"加载PDF模板字段",tooltip:"加载PDF模板字段",iconCls:"edit",handler:"autoMatchField"}
						]
					}],
					plugins:[{
						ptype:'cellediting',
						clicksToEdit:1
					}],
					columns:[{
						text: '机构编号',
						dataIndex: 'TZ_JG_ID',
						hidden:true
					},{
						text: '模板编号',
						dataIndex: 'TZ_DYMB_ID',
						hidden:true
					},{
						text: '字段ID',
						dataIndex: 'TZ_DYMB_FIELD_ID',
						width: 200,
						sortable:false
					},{
						text: '字段名称',
						dataIndex: 'TZ_DYMB_FIELD_SM',
						width: 200,
						editor:{
							xtype:'textfield'
						},
						sortable:false
					},{
						text: '是否启用',
						xtype:'checkcolumn',
						dataIndex: 'TZ_DYMB_FIELD_QY',
						width: 100,
						sortable:false
					},{
						text: 'PDF模板对应字段',
						dataIndex: 'TZ_DYMB_FIELD_PDF',
						flex:1,
						sortable:false,
						editor:{
							xtype:'textfield',
							editable:false,
							triggers:{
								clear: {
									cls: 'x-form-clear-trigger',
									handler: 'clearField'
								},
								search:{
									cls:'x-form-search-trigger',
									handler:'searchPdfField'
								}
							}
						}
					}],
					store:fieldStore
				}]
    	    }]
    	});
    	this.callParent();
    },
    buttons: [{
		text: '保存',
		iconCls:"save",
		name:"save",
		handler: 'saveTplInfo'
	}, {
		text: '确定',
		iconCls:"ensure",
		name:"ensure",
		handler: 'saveTplInfo'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: "closeTplInfo"
	}]
});
