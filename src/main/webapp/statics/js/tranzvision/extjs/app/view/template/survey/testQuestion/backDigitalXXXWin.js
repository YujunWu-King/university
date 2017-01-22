Ext.define('KitchenSink.view.template.survey.testQuestion.backDigitalXXXWin', {
    extend: 'Ext.window.Window',
	reference: 'backDigitalXXXWin',
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
	width: 620,
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
    id: 'backDigitalXXXGrid',
    name: 'backDigitalXXXGrid',
    dockedItems:[{
        xtype:"toolbar",
        items:[
            {text:"新增",tooltip:'新增',iconCls:"add",handler:'addDigitalXxxKxz'},'->'
        ]
    }],
	items: [{
		xtype: 'grid',
		height: 315, 
		viewConfig: {
			enableTextSelection: true
		},
		columnLines: true, 
		plugins: {
                    ptype: 'cellediting',
                    pluginId: 'dropBoxSetCellediting',
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
		    text: "名称",
			dataIndex: 'TZ_XXXKXZ_MC',
            hidden:false
		},{ 
		    text: "描述",
			dataIndex: 'TZ_XXXKXZ_MS',
			minWidth: 100,
            editor: {
                xtype:'textfield',
                allowBlank:false
            }
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
        },
            {
                text: "当初年份初始取值(%)",
                dataIndex: 'TZ_CURYEAR_VAL',
                minWidth: 80,
                editor: {
                    xtype: 'textfield',
                    maxLength: 50
                }
            },{
                menuDisabled: true,
                sortable: false,
                width: 60,
                align:'center',
                xtype: 'actioncolumn',
                items: [{
                    iconCls: 'remove',
                    tooltip: '删除',
                    handler: 'deleteOption'
                }]
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
