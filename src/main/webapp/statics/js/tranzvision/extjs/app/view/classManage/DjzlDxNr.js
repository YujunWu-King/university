Ext.define('KitchenSink.view.classManage.DjzlDxNr', {
    extend: 'Ext.window.Window',
	reference: 'DjzlDxNr',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.classManage.DjzlDxNrStore'
    ],
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.djzl_cyhfysz","常用回复短语设置"),
	actType : "add",
	width: 600,
    height: 400,
    layout: 'fit',
	djzl_id:'',
	bj_id:'',
    resizable: true,
    modal: true,
	multiSel: '',
	rowNum: 0,
	items: [{
		xtype: 'grid',
		height: 315, 
		viewConfig: {
			enableTextSelection: true
		},
		columnLines: true,
		reference:'DjzlDxNrGrid',
		selModel: {
			type: 'checkboxmodel'
		},
	    dockedItems:[{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.add","新增"),iconCls:"add",handler:'addLastBackMsg'},"-",
				{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除"),iconCls:"remove",handler:'deleteBackMsgT'}
		   ]
	    }], 
		plugins: {
			ptype: 'cellediting',
			pluginId: 'dataCellediting',
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
						items[i].set('order',i+1);
					}
					data.view.store.endUpdate();
				}
			}
		},
        columns: [{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.hfdynr","回复短语内容") ,
			sortable: false,
			dataIndex: 'hfdx_desc',
			minWidth: 200,
            flex:1,
			editor: {
                 xtype:'textfield'
            }
		},{
			menuDisabled: true,
			sortable: false,
			width:60,
			xtype: 'actioncolumn',
			align: 'center',
			items:[
				{iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.delete","删除") , handler: 'deleteBackMsg'}
			]
		}],
		store: {
			type: 'DjzlDxNrStore'
		},
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 10,
			listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			reference: 'backMsgToolBar',
			plugins: new Ext.ux.ProgressBarPager()
		}	
	}],
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    buttons: [{
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.save","保存") ,
		iconCls:"save",
		handler: 'onBackMsgSave'
	},{
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'onBackMsgSure'
	},{
		text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.close","关闭") ,
		iconCls:"close",
		handler: 'onBackMsgClose'
	}]
});
