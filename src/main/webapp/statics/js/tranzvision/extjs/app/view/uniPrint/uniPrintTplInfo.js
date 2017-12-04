Ext.define('KitchenSink.view.uniPrint.uniPrintTplInfo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.uniPrintTplInfo', 
	controller: 'uniPrintTplController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.uniPrint.uniPrintTplFieldStore'
	],
    title: '打印模板信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 36,/*button height plus panel body padding*/
            	formHeight = panel.lookupReference('uniPrintTplInfo').getHeight(),
            	formPadding = 10,
            	tab = panel.child('tabpanel');
            
            tab.setMinHeight( height- formHeight -buttonHeight-formPadding);
           
        }
    },
    initComponent:function(){
    	var fieldStore = new KitchenSink.view.uniPrint.uniPrintTplFieldStore();
    	this.fieldStore = fieldStore;

    	
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
    	            afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    	            allowBlank: false
    	        }, {
    	            xtype: 'textfield',
    	            fieldLabel: "打印模板编号",
    	            name: 'TZ_DYMB_ID',
    	            afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    	            allowBlank: false
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
                    store:{
                        fields:[
                            {name:'statusCode'},
                            {name:'statusDesc'}
                        ],
                        data:[
                            {statusCode:'Y',statusDesc:'生效'},
                            {statusCode:'N',statusDesc:'失效'}
                        ]
                    },
                    valueField:'statusCode',
                    displayField:'statusDesc',
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
    			            handler: 'searchTbl'
    			        }
    		    	}
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "备注信息",
    				name: 'TZ_DYMB_MENO'
    	        },{
    	        	layout:{
    	        		type:'column'
    	        	},
    	        	width:'100%',
    	        	items:[
    					{
    					    xtype: 'textfield',
    						fieldLabel: "上传PDF模板",
    						dataIndex: 'TZ_DYMB_PDF_URL',
    					    columnWidth:.95,
    					    editable:false,
    						triggers:{
    					        clear: {
    					            cls: 'x-form-clear-trigger',
    					            handler: function(field){
    					                field.setValue("");
    					            }
    					        }
    					    }
    					},
    					{
    						xtype: 'form',
    						layout: 'hbox',
    						maxWidth:60,
    						columnWidth:.05,
    						defaults:{
    							margin:'0 0 0 5px',
    						},
    						items:[{
    					        xtype: 'fileuploadfield',
    					        name: 'orguploadfile',
    					        buttonText: '上传',
    					        buttonOnly:true,
    							listeners:{
    								change:'uploadExcelTpl'
    							}
    						}]
    					}]
    	        }]
    	    },{
    	    	xtype:'tabpanel',
    	    	frame: true,
    	        plain:false,
    	        resizeTabs:true,
    	        defaults :{
    	            autoScroll: false
    	        },
    	    	margin:'0 8',
    	    	header:false,
    	    	items:[{
    	    		xtype:'grid',
    	    		title:'数据映射关系列表',
    	    		//height:280,
    	    		store:fieldStore,
    	    		plugins:[{
    	    			ptype:'cellediting',
    	    			clicksToEdit:1
    	    		}],
    	    		columns:[{
    	                text: '机构编号',
    	                dataIndex: 'TZ_JG_ID',
    	                width: 100,
    	                sortable:false
    	            },{
    	                text: '模板编号',
    	                dataIndex: 'TZ_DYMB_ID',
    	                width: 100,
    	                sortable:false
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
    	                //width: 200,
    	                flex:1,
    	                sortable:false
    	            }]
    	    	}]
    	    }]
    	});
    	this.callParent();
    },
    buttons: [{
		text: '保存',
		iconCls:"save",
		name:"save",
		handler: 'infoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		name:"ensure",
		handler: 'infoSave'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType("uniPrintTplInfo").close();
		}
	}]
});
