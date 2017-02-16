Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqzInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'bqzInfo', 
	controller: 'bqzManage',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.clmsHtpz.bqzManagement.bqStore'
	],
    title: '标签组定义',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'bqzManageForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.bqzID","标签组ID"),
			name: 'bqzID',
			maxLength: 20,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.bqzName","标签组名称"),
			name: 'bqzName'
        }]
    },{
		xtype: 'grid', 
		title: '标签列表',
		frame: true,
		columnLines: true,
		height: 350,
		reference: 'BqRegGrid',
		style: "margin:10px",
	 	multiSelect: true,
		selModel: {
			type: 'checkboxmodel'
		},
		plugins: [
			Ext.create('Ext.grid.plugin.CellEditing',{
				clicksToEdit: 1
			})
		],
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
			text: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.bqzID","标签组ID"),
			dataIndex: 'bqzID',
			hidden: true
		},{
			//xtype: 'rownumberer',
			text: '标签ID',
			dataIndex: 'bqID',
			width:60
		},{
			text: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.bqName","标签名称"),
			dataIndex: 'bqName',
			width:100
		},{
			text: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.bqDesc","标签说明"),
			dataIndex: 'bqDesc',
			minWidth: 140,
			flex: 1
		},{
			text: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQZ_DEFN_STD.java","JAVA类"),
			dataIndex: 'java',
			width:100
		},{
			menuDisabled: true,
			sortable: false,
			width:60,
			align:'center',
			xtype: 'actioncolumn',
			items:[
				{iconCls: 'edit',tooltip: '编辑',handler: 'editbqDefnInfoOne'},
				{iconCls: 'remove',tooltip: '删除',handler: 'deletebqDefnInfoOne'}
			]	
			}
		],
		store: {
			type: 'bqStore'
		},
		dockedItems:[{
			xtype:"toolbar",
			items:[
				{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addbqDefnInfo"},"-",
				{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editbqDefnInfo"},"-",
				{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deletebqDefnInfos"}
			]
		}],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			/*store: {
				type: 'bqStore'
			},*/
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
		iconCls:"save",
		handler: 'onbqzManageSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onbqzManageEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onbqzManageClose'
	}]
});
