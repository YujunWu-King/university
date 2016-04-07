Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.fsetDropdownWindow', {
    extend: 'Ext.window.Window',
	reference: 'fsetDropdownWindow',
    xtype: 'fsetDropdownWindow',
	width: 600,
	minWidth: 300,
    columnLines: true,
    title:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.dropDownListSet","下拉框设置"),
    y:10,
    layout: 'fit',
    resizable: true,
	modal: true,
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
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
					xtype: 'grid',
                    bodyStyle:'overflow-y:auto;overflow-x:hidden;',
                    minHeight: 200,
                    maxHeight: 250,
					//id: 'applyItemOptionsGrid',
					name: 'fsetDropdownWindowGrid',
					attrEnabledStore:'',
					dockedItems: [{
						xtype: 'toolbar',
						items: [
							{ iconCls: 'add',text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.add","新增"),handler: 'faddDDList'},"-",
							{ iconCls: 'remove',text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.delete","删除"),handler: 'deleteItems'}
						]
					}],
					columnLines: true,
					selModel: {
						type: 'checkboxmodel'
					},
					reference: 'fsetDropdownWindowGrid',
					store: {
						type: 'fsetDropdownStore'
					},
					plugins: {
					ptype: 'cellediting',
					pluginId: 'fattrItemCellEditing'
						//	clicksToEdit: 1
					},
					/*viewConfig: {
						plugins: {
						ptype: 'gridviewdragdrop',
						dragText: '拖拽进行选项的排序'
						},
						listeners: {
								drop: function(node, data, dropRec, dropPosition) {
									data.view.store.beginUpdate();
									var items = data.view.store.data.items;
									for(var i = 0;i< items.length;i++){
											items[i].set('transPxXh',i+1);
									}
									data.view.store.endUpdate();
								}
						}
					},*/
					columns: [{
						text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrValue","属性"),
						sortable: true,
						dataIndex: 'attrValue',
						hidden:true,
						xtype: 'hiddenfield'
					},{
						text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrDropDownId","下拉值"),
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
						text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrDropDownDesc","下拉值描述"),
						sortable: true,
						dataIndex: 'attrDropDownDesc',
						width: 150,
						flex: 2,
						editor: {
							xtype:'textfield',
							maxLength:50,
							allowBlank: false
						}
					},/*{
						xtype: 'checkcolumn',
						disabled: false,
						//text: '启用标志',
						text:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrDDEnabled","启用"),
						sortable: true,
						dataIndex: 'attrDDEnabled', 
						width: 100,
						flex: 1
					},*/
					{
						  text:'',
						  width:10,   
						  dataIndex:'attrDDEnabledDesc',   
						  hidden:true
					},
					{
						text:  Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrDDEnabled","启用"),
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
									tooltip: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.delete","删除"),
									handler: "deleteItem"
								}
					   ]
					 }]	
				}]
  }],
    buttons: [{
		text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.save","保存"),
		iconCls:"save",
		handler: 'onAttrItemSave'
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onAttrItemEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.close","关闭"),
		iconCls:"close",
		handler: 'onPageClose'
	}]
});
