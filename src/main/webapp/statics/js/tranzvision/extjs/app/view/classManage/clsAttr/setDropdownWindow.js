Ext.define('KitchenSink.view.classManage.clsAttr.setDropdownWindow', {
    extend: 'Ext.window.Window',
	reference: 'setDropdownWindow',
    xtype: 'setDropdownWindow',
	width: 600,
	height: 500,
	minWidth: 300,
	minHeight: 380,
    columnLines: true,
    title:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xlksz","下拉框设置"),
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',
	actType: 'add',
	
	//header:false,
	//frame: true,
   
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
					xtype: 'grid',
					height: 360, 
					frame: true,
					//id: 'applyItemOptionsGrid',
					name: 'setDropdownWindowGrid',
					attrEnabledStore:'',
					dockedItems: [{
						xtype: 'toolbar',
						items: [
							{ iconCls: 'add',text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),handler: 'addDDList'},"-",
							{ iconCls: 'remove',text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),handler: 'deleteItems'}
						]
					}],
					columnLines: true,
					selModel: {
						type: 'checkboxmodel'
					},
					reference: 'setDropdownWindowGrid',
					style:"margin:10px",
					store: {
						type: 'setDropdownStore'
					},
					plugins: {
					ptype: 'cellediting',
					pluginId: 'attrItemCellEditing'
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
		                            items[i].set('attrOrder', i + 1);
		                        }
		                        data.view.store.endUpdate();
		                    }
		                }
		            },
					columns: [{
						text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.attribute","属性"),
						sortable: true,
						dataIndex: 'attrValue',
						hidden:true,
						xtype: 'hiddenfield'
					},{ 
						text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.attrDropDownId","下拉值"),
						sortable: true,
						dataIndex: 'attrDropDownId',
						width: 100,
						flex: 1,
						editor: {
							xtype:'textfield',
							maxLength:15,
							allowBlank: false
						}
					},{ 
						text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.attrDropDownDesc","下拉值描述"),
						sortable: true,
						dataIndex: 'attrDropDownDesc',
						width: 150,
						flex: 2,
						editor: {
							xtype:'textfield',
							maxLength:50,
							allowBlank: false
						}
					},
					{
						  text:'',
						  width:10,   
						  dataIndex:'attrDDEnabledDesc',   
						  hidden:true
					},
					{
						text:  Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.attrDDEnabled","启用"),
						sortable: false,
						dataIndex: 'attrDDEnabled',
						width: 100,
						editor: {
								xtype: 'combobox',
								queryMode: 'remote',
								editable:false,
								hiddenName:'attrDDEnabled',
								allowBlank:false,
								name: 'attrDDEnabled',
								valueField: 'TValue',
								displayField: 'TSDesc',
								store: new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED")
						},
						renderer: function(value,metadata,record){ 
								var attrEnabledStore=this.attrEnabledStore;
								var index = attrEnabledStore.find('TValue',value);   
								if(index!=-1){   
									   return attrEnabledStore.getAt(index).data.TSDesc;   
								}   
								return record.get('attrDDEnabledDesc');   
						}
					
				},{
					   xtype: 'actioncolumn',
					   menuDisabled: true,
					   sortable: false,
					   width:60,
					   flex: 1,
					   align: 'center',
					   items:[
								{
									iconCls: 'remove',
									tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),
									handler: "deleteItem"
								}
					   ]
					 },{
			                xtype: 'hiddenfield',
			                text: '排序',
			                sortable: false,
			                dataIndex: 'attrOrder',
			                hidden: true
			            }]	
				}]
  }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.save","保存"),
		iconCls:"save",
		handler: 'onAttrItemSave'
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onAttrItemEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.close","关闭"),
		iconCls:"close",
		handler: 'onPageClose'
	}]
});
