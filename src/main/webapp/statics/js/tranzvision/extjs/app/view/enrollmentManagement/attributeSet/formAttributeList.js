Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.formAttributeList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.enrollmentManagement.attributeSet.formattrStore',
        'KitchenSink.view.enrollmentManagement.attributeSet.formattrController',
        'KitchenSink.view.enrollmentManagement.attributeSet.fsetDropdownStore'

    ],
    xtype: 'appFormAttributeSet',
    controller: 'formattrController',

    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.attributeSet","个性化属性设置"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        ,{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.add","新增"),iconCls:"add",handler:"addAttrPage"},"-",
			{text:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.edit","编辑"),iconCls:"edit",handler:"attrEdit"}
        ]
    }],
    initComponent: function () {
        //组件注册信息列表
        var store = new KitchenSink.view.enrollmentManagement.attributeSet.formattrStore();

        var attrTypeStore=new KitchenSink.view.common.store.appTransStore("TZ_CONTROL_TYPE");
		var attrEnabledStore=new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED");
        Ext.apply(this, {
            plugins: {
                ptype: 'cellediting',
                pluginId: 'fAttrCellEditing'
                //	clicksToEdit: 1
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
                            items[i].set('attrSeq',i+1);
                        }
                        data.view.store.endUpdate();
                    }
                }
            },
            columns: [
                {
                    text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.attrDesc","属性名称"),
                    sortable: true,
                    dataIndex: 'attrDesc',
                    minWidth: 200,
                    flex:1,
                    allowBlank:false,
                    blankText: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.attrIsRequired","属性名称必填"),
                    editor: {
                        xtype:'textfield',
                        allowBlank :false
                    }
                },{
                    width:10,
                    dataIndex:'attrTypeDesc',
                    hidden:true
                },{
                    text:  Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.attrType","控件类型"),
                    sortable: false,
                    dataIndex: 'attrType',
                    minWidth: 120,
                    editor: {
                        xtype: 'combobox',
                        emptyText:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.pleaseSelect","请选择..."),
                        queryMode: 'remote',
                        editable:false,
                        name: 'attrType',
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: attrTypeStore
                    },
                    renderer: function(value,metadata,record){
                        var index = attrTypeStore.find('TValue',value);
                        if(index!=-1){
                            return attrTypeStore.getAt(index).data.TSDesc;
                        }
                        return record.get('attrTypeDesc');
                    }

                },{
					text:  Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.attrEnabled","启用"),
					sortable: false,
					dataIndex: 'attrEnabled',
					width: 120,
					editor: {
							xtype: 'combobox',
							emptyText:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.pleaseSelect","请选择..."),
							queryMode: 'remote',
							editable:false,
							hiddenName:'attrEnabled',
							allowBlank:false,
							name: 'attrEnabled',
							valueField: 'TValue',
							displayField: 'TSDesc',
							store: attrEnabledStore
					},
					renderer: function(value,metadata,record){   
							var index = attrEnabledStore.find('TValue',value);   
							if(index!=-1){   
								   return attrEnabledStore.getAt(index).data.TSDesc;   
							}   
							return record.get('attrEnabledDesc');   
					}
					
				},
                {
                    xtype: 'actioncolumn',
                    menuDisabled: true,
                    sortable: false,
                    width:60,
                    align: 'center',
                    items:[
						{
							iconCls: 'set',
							tooltip: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.setDropDownList","设置下拉值"),
						    handler: "setDDlist",
                            isDisabled:function(view,rowIndex,colIndex,item,record){
                                if (record.get('attrType')=="L"){
                                    return false;
                                }else{
                                    return true;
                                }
                            }
							
						},
						{
							iconCls: 'edit',
							tooltip: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.edit","编辑"),
                    		handler: "currAttrEdit"
						}
                    ]
                }],
            store: store
        });
        this.callParent();
    },
    buttons:[
        {text:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMGL_SXSZ_STD.close","关闭"),iconCls:"close",handler:"onComRegClose"}
    ]
});
