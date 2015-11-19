Ext.define('KitchenSink.view.basicData.resData.translate.transDefinePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'transDefine', 
	controller: 'tranSetMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.translate.transDefineStore',
		'KitchenSink.view.basicData.resData.translate.transSetController'
	],
    title: '转换值定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	  lanageType: 'ZHS',
    items: [{
        xtype: 'form',
        reference: 'transDefineForm',
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
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.transSetID","转换值集合编号"),
			labelWidth: 120,
			name: 'transSetID',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.transSetDesc","转换值集合描述"),
			labelWidth: 120,
			name: 'transSetDesc'
        }]
    },{
		xtype: 'grid',
		height: 315, 
		title: '转换值信息列表',
		frame: true,
		columnLines: true,
		reference:'transDefineGrid',
		style:"margin:10px",
		
		//多选框
        selModel: {
        type: 'checkboxmodel'
        },	
	
	    dockedItems:[{
		   xtype:"toolbar",
		   items:[
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTransValInfo'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editTransValInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTransValInfos'},'->',

                    {
                        xtype:'splitbutton',
                        text:'更多操作',
                        iconCls:'list',
                        glyph: 61,
                        menu:[
                                {
                                    text:'切换语言',
                                    iconCls:"switch ",
                                    handler:'changeLanguage'
                                }
                            ]
                    }
		   ]
	    }], 
		columns: [{ 
		    text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.transSetID","转换值集合编号"),
			dataIndex: 'transSetID',
			hidden: true
		},{ 
		 text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.transID","转换值编号"),
			dataIndex: 'transID',
			minWidth: 100
		},{
		    text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.effeDate","生效日期"),
			format: 'Y-m-d',
			dataIndex: 'effeDate',
			width: 110
        },{
		    text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.isEffe","有效状态"),
			dataIndex: 'isEffe',
			width: 90
		},{ 
			text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.shortDesc","短描述"),
			dataIndex: 'shortDesc',
			width: 200
		},{ 
			text: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.longDesc","长描述"),
			dataIndex: 'longDesc',
			flex: 1
		},{
			menuDisabled: true,
			sortable: false,
			width:60,
			align:'center',
			xtype: 'actioncolumn',
			items:[
				{iconCls: 'edit',tooltip: '编辑',handler:'editCurrTransVal'},
				{iconCls: 'remove',tooltip: '删除',handler:'deleteCurrTransVal'}
			]
		}],
		store: {
				type: 'transDefineStore'
			},			
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
		    listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			reference: 'transDefineToolBar',
			displayInfo: true,
			displayMsg: '显示{0}-{1}条，共{2}条',
			beforePageText: '第',
			afterPageText: '页/共{0}页',
			emptyMsg: '没有数据显示',
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onTranslateSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onTranslateEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onTranslateClose'
	}]
});
