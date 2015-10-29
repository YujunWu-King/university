Ext.define('KitchenSink.view.classManage.clsAttr.classAttributeList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
		'KitchenSink.view.classManage.clsAttr.clsattrModel',
        'KitchenSink.view.classManage.clsAttr.clsattrStore',
		'KitchenSink.view.classManage.clsAttr.clsattrController',
		'KitchenSink.view.classManage.clsAttr.setDropdownModel',
        'KitchenSink.view.classManage.clsAttr.setDropdownStore'
    ],
    xtype: 'individuationSet',
	controller: 'clsattrController',

    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.gxhsxsz","个性化属性设置") ,
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.save","保存"),iconCls:"save",handler:"saveClsAttrInfo"},
			   '-',{minWidth:80,text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.ensure","确定"),iconCls:"ensure",handler:"ensureClsAttrInfo"},
			   '-',{minWidth:80,text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.close","关闭"),iconCls:"close",handler:"onComRegClose"}
			]
		},{
		xtype:"toolbar",
		items:[
			{text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.add","新增"),iconCls:"add",handler:"addAttrPage"},"-",
			{text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.edit","编辑"),iconCls:"edit",handler:"attrEdit"}
		]
	}],
	plugins: [
		Ext.create('Ext.grid.plugin.CellEditing',{
			clicksToEdit: 1
		})
	],
    initComponent: function () { 
	    //组件注册信息列表
	    var store = new KitchenSink.view.classManage.clsAttr.clsattrStore();
		
		var attrTypeStore=new KitchenSink.view.common.store.appTransStore("TZ_CONTROL_TYPE");
		var attrEnabledStore=new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED");
		/*
		this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });*/
		
        Ext.apply(this, {
			plugins: {
                ptype: 'cellediting',
                pluginId: 'clsAttrCellEditing'
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
                text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.attrDesc","属性名称"),
				sortable: true,
                dataIndex: 'attrDesc',
				minWidth: 250,
				flex:1,
				allowBlank:false,
				blankText:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.sxmcbt","属性名称必填"),
				editor: {
                    xtype:'textfield'
                }
            },{
				  text:'',
				  width:10,   
				  dataIndex:'attrTypeDesc',   
                  hidden:true
			},{
                text:  Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.attrType","控件类型"),
                sortable: false,
                dataIndex: 'attrType',
                minWidth: 130,
				editor: {
                		xtype: 'combobox',
						emptyText:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.pleaseSelect","请选择..."),
						queryMode: 'remote',
						editable:false,
						hiddenName:'attrType',
						allowBlank:false,
						blankText: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.kjlxbt","控件类型必填"),
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
				  text:'',
				  width:10,   
				  dataIndex:'attrEnabledDesc',   
                  hidden:true
			},
			{
			   xtype: 'actioncolumn',
			   menuDisabled: true,
               sortable: false,
			   width:100,
			   align: 'center',
			   items:[
						{
							iconCls: 'edit',
							tooltip:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.setDropDownList","设置下拉值"),
						    handler: "setDDlist",
							getClass:function(v,metadata,r){
                                if (r.get('attrType')=="L"){
                                    return 'set';
                                }else{
                                    return '';
                                }
                            }
							
						},
						{
							iconCls: 'edit',
							tooltip:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.edit","编辑"),
                    		handler: "currAttrEdit"
						}
			   ]
             },{
                text:  Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.attrEnabled","启用"),
                sortable: false,
                dataIndex: 'attrEnabled',
                width: 100,
				editor: {
                		xtype: 'combobox',
						emptyText:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.pleaseSelect","请选择..."),
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
				
            }
			],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
