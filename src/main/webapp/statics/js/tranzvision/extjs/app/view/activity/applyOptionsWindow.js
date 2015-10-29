Ext.define('KitchenSink.view.activity.applyOptionsWindow', {
  extend: 'Ext.window.Window',
  xtype: 'applyOptionsWindow', 
  title: '报名项下拉值定义', 
	reference: 'applyOptionsWindow',
  width: 600,
  height: 500,
  minWidth: 300,
  minHeight: 380,
  layout: 'fit',
  resizable: true,
  modal: true,
  closeAction: 'hide',
	actType: 'add',
	
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 150,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'hiddenfield',
			fieldLabel: "活动ID",
			name: 'activityId',
			allowBlank: false
		}, {
			xtype: 'hiddenfield',
			fieldLabel: "信息项ID",
			name: 'applyItemId',
			allowBlank: false
		},{
			xtype: 'grid',
			height: 360, 
			frame: true,
			//id: 'applyItemOptionsGrid',
			name: 'applyItemOptionsGrid',
			dockedItems: [{
        	xtype: 'toolbar',
        	items: [
        		{ iconCls: 'add',text: '新增', tooltip:"新增选项",handler: 'addApplyItemOption'},"-", 
						{ iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteApplyItemOptions'}
          ]
      }],
			columnLines: true,
			selModel: {
       		type: 'checkboxmodel'
   		},
			reference: 'applyItemOptionsGrid',
			style:"margin:10px",
			store: {
				type: 'applyItemOptionsStore'
			},
			plugins: {
        	ptype: 'cellediting',
        	pluginId: 'applyItemOptionsCellediting',
        		//	clicksToEdit: 1
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
							for(var i = 0;i< items.length;i++){
									items[i].set('transPxXh',i+1);
							}
							data.view.store.endUpdate();
						}
					}
    	},
    	columns: [{ 
						text: '选项ID',
						sortable: false,
						dataIndex: 'transId',	
						hidden: true
			},{ 
						xtype: 'hiddenfield',
						text: '排序',
						sortable: false,
						dataIndex: 'transPxXh',
						hidden: true
			},{
						text: '选项',
						sortable: false,
						dataIndex: 'transName',
						sortable: false,
						flex: 2,
						editor: {
						   xtype: 'textfield',
						   allowBlank: false
						}
			},{
						text: '选项英文',
						sortable: false,
						dataIndex: 'transNameEng',
						sortable: false,
						flex: 2,
						editor: {
						   xtype: 'textfield',
						   allowBlank: false
						}
			},{
					menuDisabled: true,
	        sortable: false,
				  width:60,
	        xtype: 'actioncolumn',
				 items:[
						 {iconCls: 'remove',tooltip: '删除',handler: 'deleteApplyItemOption'}
				  ]
			}]	
		}]
  }],
  buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onItemOptionsSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onItemOptionsEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onItemOptionsClose'
	}]
});