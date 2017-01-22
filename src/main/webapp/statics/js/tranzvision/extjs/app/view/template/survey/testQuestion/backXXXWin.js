Ext.define('KitchenSink.view.template.survey.testQuestion.backXXXWin', {
    extend: 'Ext.window.Window',
	reference: 'backXXXWin',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.template.survey.testQuestion.backXXXStore'
    ],
    title: "调查项设置",
	actType : "add",//默认add
	width: 600,
    height: 400,
    layout: 'fit',
	cswjId:'',
	wjId:'',
    xxxBh:'',
    comMc:'',
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
            text: "序号",
            dataIndex: 'TZ_ORDER',
            hidden: true
        },{
		    text: "信息项编号",
			dataIndex: 'TZ_XXXKXZ_MC',
            hidden: true
		},{ 
		    text: "信息项描述",
			dataIndex: 'TZ_XXXKXZ_MS',
			minWidth: 150
		},{
		    text: "取值下限",
			dataIndex: 'TZ_L_LIMIT',
			width: 80,
            editor: {
                xtype:'textfield',
                maxLength:50
            }
        },{
		    text: "取值上限",
			dataIndex: 'TZ_U_LIMIT',
			minWidth:80,
            editor: {
                xtype:'textfield',
                maxLength:50
            }
		},{
            text: "往年取值(%)",
            dataIndex: 'TZ_HISTORY_VAL',
            minWidth: 80,
            editor: {
                xtype:'textfield',
                maxLength:50
            }
        },{
            text: "当初年份初始取值(%)",
            dataIndex: 'TZ_CURYEAR_VAL',
            minWidth: 80,
            editor: {
                xtype:'textfield',
                maxLength:50
            }
        }],
		store: {
			type: 'backXXXStore'
		},
		bbar: {
				xtype: 'pagingtoolbar',
				pageSize:20, 
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
		text:"保存",
		iconCls:"save",
		handler: 'onGridSave'
	},{
        text: "确定",
        iconCls:"ensure",
        handler: 'onGridSure' 
    }, {
		text: "关闭",
		iconCls:"close",
		handler: 'onGridClose'
	}]
});
