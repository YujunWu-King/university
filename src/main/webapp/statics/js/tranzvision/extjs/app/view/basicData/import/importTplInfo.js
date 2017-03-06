Ext.define('KitchenSink.view.basicData.import.importTplInfo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.importTplInfo', 
	controller: 'importTplController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.basicData.import.importTplFieldStore'
	],
    title: '导入模板信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 36,/*button height plus panel body padding*/
            	formHeight = panel.lookupReference('impTplForm').getHeight(),
            	formPadding = 10,
            	tab = panel.child('tabpanel');
            
            tab.setMinHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    initComponent:function(){
    	var fieldStore = new KitchenSink.view.basicData.import.importTplFieldStore();
    	this.fieldStore = fieldStore;
    	
    	Ext.apply(this,{
    		items: [{
    	        xtype: 'form',
    	        reference: 'impTplForm',
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
    	            fieldLabel: "导入模板编号",
    				name: 'tplId',
    	            afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    	            allowBlank: false
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "导入模板名称",
    				name: 'tplName',
    				afterLabelTextTpl: [
    	                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	            ],
    				allowBlank: false
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "目标表名称",
    				name: 'targetTbl',
    				editable: false,
    	            triggers:{
    			        search: {
    			            cls: 'x-form-search-trigger',
    			            handler: 'searchTbl'
    			        }
    		    	}
    	        }, {
    	            xtype: 'textfield',
    				fieldLabel: "导入Java类",
    				name: 'javaClass'
    	        },{
    	            xtype: 'displayfield',
    				value:"此处设置的Java类必须实现接口 com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase",
    				fieldStyle:"color:#666;font-weight:bold",
    				fieldLabel:" ",
    	            labelSeparator:""
    	        }, {
    	        	layout:{
    	        		type:'column'
    	        	},
    	        	width:'100%',
    	        	items:[
    					{
    					    xtype: 'textfield',
    						fieldLabel: "上传Excel模板",
    						name: 'excelTpl',
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
    	        },{
    	            xtype: 'checkbox',
    				fieldLabel: "允许用户调整映射关系",
    				name: 'enableMapping',
    				inputValue: 'Y',
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
    	    	listeners:{
    	            tabchange:function(tabPanel, newCard, oldCard){
    	            	tabPanel.updateLayout();
    	            }
    	        },
    	    	items:[{
    	    		xtype:'grid',
    	    		title:'加载字段',
    	    		store:fieldStore,
    	    		plugins:[{
    	    			ptype:'cellediting',
    	    			clicksToEdit:1
    	    		}],
    	    		viewConfig: {
    	    			plugins: {
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
    	    						items[i].set('seq',i+1);
    	    					}
    	    					data.view.store.endUpdate();
    	    				}
    	    			},
    	    			enableTextSelection: true
    	    		},
    	    		columns:[{
    	                text: '序号',
    	                dataIndex: 'seq',
    	                width: 80,
    	                sortable:false
    	            },{
    	                text: '字段',
    	                dataIndex: 'field',
    	                width: 200,
    	                sortable:false
    	            },{
    	                text: '字段描述',
    	                dataIndex: 'fieldName',
    	                editor:{
    	                	xtype:'textfield'
    	                },
    	                flex:1,
    	                sortable:false
    	            },{
    	                text: '是否必填',
    	                xtype:'checkcolumn',
    	                dataIndex: 'required',
    	                width: 100,
    	                sortable:false
    	            },{
    	                text: '是否前台展示',
    	                xtype:'checkcolumn',
    	                dataIndex: 'display',
    	                width: 120,
    	                sortable:false
    	            }]
    	    	},{
    	    		xtype:'grid',
    	    		title:'映射关系',
    	    		plugins:[{
    	    			ptype:'cellediting',
    	    			clicksToEdit:1
    	    		}],
    	    		viewConfig:{
    	    			enableTextSelection: true
    	    		},
    	    		store:fieldStore,
    	    		columns:[{
    	                text: '<span style="font-family:FontAwesome;margin-right:5px;font-weight:normal;">&#xf1c3;</span>导入模板列标题',
    	                dataIndex: 'columnTitle',
    	                editor:{
    	                	xtype:'textfield'
    	                },
    	                flex:1,
    	                sortable:false
    	            },{
    	                text: '<span style="font-family:FontAwesome;margin-right:5px;font-weight:normal;">&#xf1c0;</span>业务数据目标表字段',
    	                dataIndex: 'fieldName',
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
			btn.findParentByType("importTplInfo").close();
		}
	}]
});
