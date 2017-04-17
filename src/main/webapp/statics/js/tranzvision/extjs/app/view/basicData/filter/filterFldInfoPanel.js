    Ext.define('KitchenSink.view.basicData.filter.filterFldInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'filterFldInfoPanel',
	controller: 'filterFldInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.filter.fldYsfStore',
        'KitchenSink.view.basicData.filter.fltprmFldStore',
        'KitchenSink.view.basicData.filter.filterFldInfoController'
	],
    title: '可配置搜索配置-搜索字段属性配置',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',//默认新增
    changeNum:'',
    newDate:'',
    items: [{
        xtype: 'form',
        reference: 'filterForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '组件编号',
            name: 'ComID'
        }, {
            xtype: 'hiddenfield',
            fieldLabel: '页面编号',
            name: 'PageID'
        }, {
            xtype: 'hiddenfield',
            fieldLabel: '视图名称',
            name: 'ViewMc'
        },{
			xtype: 'textfield',
			fieldLabel: '字段名称',
			readOnly:true,
			cls:"lanage_1",
			name: 'FieldMc'
		},{
			xtype: 'textfield',
			fieldLabel: '字段描述',
			//readOnly:true,
			name: 'fieldDesc'
		},{
        	 xtype: 'combobox',
        	 fieldLabel: '取值类型',
        	 valueField: 'TValue',
           displayField: 'TLDesc',
           store: new KitchenSink.view.common.store.appTransStore("TZ_FLT_FLD_QZ_TYPE"),
           queryMode: 'local',
    			 name: 'fltFldQzLx',
            listeners: {
							"change": function( cbox , newValue, oldValue){
								
								var translateValueFld = cbox.findParentByType("form").getForm().findField("translateValueFld");
								var promptTab = cbox.findParentByType("form").getForm().findField("promptTab");
								var promptFld = cbox.findParentByType("form").getForm().findField("promptFld");
								var promptDesc = cbox.findParentByType("form").getForm().findField("promptDesc");
								var fldIsDown = cbox.findParentByType("form").getForm().findField("fldIsDown");
								
								if(newValue=="A"){
									translateValueFld.allowBlank = true;
									promptTab.allowBlank = false;
									promptFld.allowBlank = false;
									promptDesc.allowBlank = false;
									
									translateValueFld.setValue("");
									translateValueFld.hide();
									fldIsDown.show();
									promptTab.show();
									promptFld.show();
									promptDesc.show();
									
								}else if(newValue=="B"){
									translateValueFld.allowBlank = false ;
									promptTab.allowBlank = true;
									promptFld.allowBlank = true;
									promptDesc.allowBlank = true;
									
									translateValueFld.show();
									promptTab.setValue("");
									promptFld.setValue("");
									promptDesc.setValue("");
									fldIsDown.hide();
									promptTab.hide();
									promptFld.hide();
									promptDesc.hide();
								}else{
									translateValueFld.allowBlank = true ;
									promptTab.allowBlank = true;
									promptFld.allowBlank = true;
									promptDesc.allowBlank = true;
									
									translateValueFld.setValue("");
									promptTab.setValue("");
									promptFld.setValue("");
									promptDesc.setValue("");
									
									translateValueFld.hide();
									fldIsDown.hide();
									promptTab.hide();
									promptFld.hide();
									promptDesc.hide();
								}
							},
							"afterrender": function(cbox){
								var newValue = cbox.getValue();
								var translateValueFld = cbox.findParentByType("form").getForm().findField("translateValueFld");
								var promptTab = cbox.findParentByType("form").getForm().findField("promptTab");
								var promptFld = cbox.findParentByType("form").getForm().findField("promptFld");
								var promptDesc = cbox.findParentByType("form").getForm().findField("promptDesc");
								var fldIsDown = cbox.findParentByType("form").getForm().findField("fldIsDown");
								if(newValue=="A"){
									translateValueFld.allowBlank = true ;
									promptTab.allowBlank = false;
									promptFld.allowBlank = false;
									promptDesc.allowBlank = false;
									
									translateValueFld.hide();
									fldIsDown.show();
									promptTab.show();
									promptFld.show();
									promptDesc.show();
								}else if(newValue=="B"){
									translateValueFld.allowBlank = false ;
									promptTab.allowBlank = true;
									promptFld.allowBlank = true;
									promptDesc.allowBlank = true;
									
									translateValueFld.show();
									fldIsDown.hide();
									promptTab.hide();
									promptFld.hide();
									promptDesc.hide();
								}else{
									translateValueFld.allowBlank = true;
									promptTab.allowBlank = true;
									promptFld.allowBlank = true;
									promptDesc.allowBlank = true;
									
									translateValueFld.hide();
									fldIsDown.hide();
									promptTab.hide();
									promptFld.hide();
									promptDesc.hide();
								}
							}
						}
    },{
			xtype: 'textfield',
			fieldLabel: 'Translate Value字段名',
			name: 'translateValueFld'
		},{
			xtype: 'textfield',
			fieldLabel: 'Prompt表名称',
			name: 'promptTab'
		},{
											
												
												xtype: 'textfield',
												fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_FILTER_FLD_STD.promptFld","Prompt字段名称"),
												name: 'promptFld',
												triggers: {
								          search: {
								             cls: 'x-form-search-trigger',
								             handler: "pmtSearchPromptFldTmp"
								          }
								        }
										
		},{
												
												xtype: 'textfield',
												fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_FILTER_FLD_STD.promptDesc","Prompt描述字段名称"),
												name: 'promptDesc',
												triggers: {
								          search: {
								             cls: 'x-form-search-trigger',
								             handler: "pmtSearchPromptDescTmp"
								          }
								        }
										
		},,{
            xtype: 'textfield',
            fieldLabel: '返回搜索结果最大行数',
            name: 'maxNum'
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">是否下拉框</span>',
            margin: '0 0 5 170',
            hideLabel: true,
            inputvalue:'true',
			name: 'fldIsDown'
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">只读</span>',
            margin: '0 0 5 170',
            hideLabel: true,
            inputvalue:'true',
			name: 'fldReadonly'
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">隐藏</span>',
            margin: '0 0 5 170',
            hideLabel: true,
            inputvalue:'true',
						name: 'fldHide'
        },{
        	 xtype: 'combobox',
        	 fieldLabel: '搜索字段不区分大小写',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_NO_UPORLOW"),
             queryMode: 'local',
             value: "B",
    		 name: 'fltFldNoUpperLower'
    	 },{
    			xtype: 'textfield',
    			fieldLabel: '是否DeepQuery字段',
    			name: 'deepQueryFlg'
    	 },{
	 			xtype: 'textfield',
				fieldLabel: 'DeepQuery视图',
				name: 'deepQueryView'
		 },{
				xtype: 'textfield',
				fieldLabel: 'DeepQuery关联字段',
				name: 'deepQueryFld'
		 },{
	    	xtype: 'tabpanel',
	      frame: true,
	      items:[{
				xtype: 'grid',
				name:'searchFld',
				minHeight: 360,
				maxHeight: 360,
				autoScroll: true,
				title: '搜索条件字段操作运算符设置',
				columnLines: true,
				store: {
					type: 'fldYsfStore'
				},
				columns: [{
		            text: '序号',
		            dataIndex: 'orderNum',
		            width: 70
		        },{
		            text: '操作运算符',
		            dataIndex: 'FieldYsf',
		            width: 150
		        },{
		        	xtype: 'checkcolumn',
					text: '启用',
	                dataIndex: 'isQy',
	                flex: 1
				},{
					text: '默认',
	                dataIndex: 'isOprt',
	                align: 'center',
	                flex: 1,
	                renderer: function(value){
	                	if(value == "1"){
							return '<div><input type="radio" name="isOprt" checked="true" style="width:16px;height:16px"/></div>';
	                	}else{
	                		return '<div><input type="radio" name="isOprt" style="width:16px;height:16px"/></div>';
	                	}
					},
					listeners:{
						click:function(view,t,rowIndex){
							var store = view.findParentByType("grid").store;
					    	var rec = store.getAt(rowIndex);
				    		if(!rec.get("isQy")){
				    			store.removeAt(rowIndex);
				    			rec.set("isOprt", "0");
				    			store.insert(rowIndex,rec);
				    			alert("请先启用");
				    		}else{
				    			for(var i =0;i<store.getCount();i++){
				    				var record = store.getAt(i);
				    				if(i == rowIndex){
				    					record.set("isOprt", "1");
				    				}else{
				    					record.set("isOprt", "0");
				    				}
				    			}
				    		}
						}
					}
				}]
			},{
	        	xtype: 'grid',
	        	name:'promptFld',
				minHeight: 360,
				maxHeight: 360,
				autoScroll: true,
				title: 'Prompt表搜索条件关联字段设置',
				columnLines: true,
		        dockedItems:{
		            xtype:"toolbar",
		            items:[
		                {text:"新增",tooltip:"新增",iconCls:"add",handler:'addFld'},"-",
		                {text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteFlds'}
		            ]
		        },
		        selModel: {
		            type: 'checkboxmodel'
		        },
		        multiSelect: true,
				store: {
					type: 'fltprmFldStore'
				},
				plugins: {
	                ptype: 'cellediting',
	                pluginId: 'fldCellEditing',
	                clicksToEdit: 1
	            },
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
								items[i].set('orderNum',i+1);
							}
							data.view.store.endUpdate();
						}
					}
				},
				columns: [{
		            text: '组件编号',
		            dataIndex: 'ComID',
		            hidden: true
		        },{
		            text: '页面编号',
		            dataIndex: 'PageID',
		            hidden: true
		        },{
		            text: '视图',
		            dataIndex: 'ViewMc',
		            hidden: true
		        },{
		            text: '序号',
		            dataIndex: 'orderNum',
		            width: 70
		        },{
		            text: '字段名称',
		            hidden: true,
		            dataIndex: 'FieldMc'
		        },{
		            text: '字段名称',
		            dataIndex: 'FieldGL',
		            width: 400,
		            flex:1,
		            editor:{
		            	xtype: 'combobox',
			            queryMode: 'remote',
			            editable:false,
						valueField: 'TZ_FILTER_FLD',
			    		displayField: 'TZ_FILTER_FLD',
			    		//store: new KitchenSink.view.common.store.appTransStore("TZ_SITEM_ENABLE"),
			    		listeners:{
			            	change:'fieldChange',
			            	render:'comRender'
			            }
		            }
		        },{
		            text: '字段描述',
		            dataIndex: 'fieldDesc',
		            width: 450
		        }]
		     /***** ,
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
		            listeners:{
		                afterrender: function(pbar){
		                    var grid = pbar.findParentByType("grid");
		                    pbar.setStore(grid.store);
		                }
		            },
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}  ****/
			}]
        }]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFilterInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFilterInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterInfoClose'
	}]
});
