    Ext.define('KitchenSink.view.basicData.resData.theme.themeInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'themeInfoPanel',
	controller: 'themeInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.theme.themeInfoStore',
        'KitchenSink.view.basicData.resData.theme.themeInfoController'
	],
    title: '主题信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'themeForm',
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
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: '主题编号',
            name: 'themeID',
            maxLength: 30,
            fieldStyle:'background:#F4F4F4',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: '主题名称',
            name: 'themeName'
        }, {
            xtype: 'textfield',
            fieldLabel: '描述',
            name: 'themeDesc'
        }, {
            xtype: 'combobox',
            fieldLabel: '有效状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'themeState',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_THEME_STATE")
        }]
    },{
		xtype: 'grid',
		height: 360,
		title: '资源集合信息列表',
		frame: true,
		columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addResSet'},"-",
                {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editResSet"},"-",
                {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteResSets'}
            ]
        },

        selModel: {
            type: 'checkboxmodel'
        },
		reference: 'themeGrid',
        multiSelect: true,
		style:"margin:10px",
		store: {
			type: 'themeInfoStore'
		},
		columns: [{
            text: '主题编号',
            dataIndex: 'themeID',
            hidden: true
        },{
            text: '资源集合编号',
            dataIndex: 'resSetID',
            width: 200
        },{
            text: '资源集合名称',
			dataIndex: 'resSetDesc',
			minWidth: 200,
			flex: 1
		},{
            menuDisabled: true,
            sortable: false,
            width:60,
            xtype: 'actioncolumn',
            items:[
                {iconCls: 'edit',tooltip: '编辑',handler: 'editThemeInfoBL'},
                {iconCls: 'remove',tooltip: '删除',handler: 'deleteThemeInfoBL'}
            ]
        }
        ],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
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
		handler: 'onThemeResInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onThemeResInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onThemeResInfoClose'
	}]
});
