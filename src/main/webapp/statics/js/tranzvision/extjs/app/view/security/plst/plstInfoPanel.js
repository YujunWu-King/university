Ext.define('KitchenSink.view.security.plst.plstInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'plstInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.security.plst.plstComStore'
	],
    title: '许可权信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',
    controller: 'plstMg',
    items: [{
        xtype: 'form',
        reference: 'plstInfoForm',
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
            fieldLabel: '许可权编号',
			name: 'permID',
            vtype:'toUppercase',
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: '描述',
			name: 'permDesc'
        }]
    },
        {
		xtype: 'grid',
        height:340,
        autoHeight:true,
		title: '组件授权列表',
		frame: true,
		columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryPlstCom'},"-",
                {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addPlstCom'} ,"-",
                {text:"编辑",tooltip:"编辑选中的数据",iconCls:"edit",handler:'editPlstCom'},"-",
                {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deletePlstComs'}
            ]
        },
        selModel: {
            type: 'checkboxmodel'
        },
		reference: 'plstComGrid',
		style:"margin:10px",
		store: {
			type: 'plstComStore'
		},
		columns: [{
            text: '许可权编号',
            dataIndex: 'permID',
            hidden:true
        },{
			text: '组件ID',
			dataIndex: 'comID',
            width: 250
		},{
			text: '组件名称',
			dataIndex: 'comName',
			minWidth: 200,
			flex: 1
		},{
            menuDisabled: true,
            sortable: false,
            align:'center',
            width:50,
            xtype: 'actioncolumn',
            items:[
                {iconCls: 'edit',tooltip: '编辑',handler:'editCurrPlstCom'},
                {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrPlstCom'}
            ]
        }],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			reference: 'plstComToolBar',
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
		handler: 'onPlstInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onPlstInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onPlstInfoClose'
	}]
});
