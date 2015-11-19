Ext.define('KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'resourceSetInfo',
	controller: 'resSet',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.resource.resourceStore'
	],
    title: '资源集合信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'resSetForm',
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
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.resSetID","资源集合编号"),
            name: 'resSetID',
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.resSetDesc","资源集合名称"),
            name: 'resSetDesc'
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">'+Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.publicRes","是否公共资源")+'</span>',
            margin: '0 0 0 115',
            name: 'publicRes',
            inputValue: 'Y'
        }]
    },{
		xtype: 'grid',
		height: 350,
        autoHeight:true,
        resizable:true,
		title: '资源信息列表',
		frame: true,
		columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addResource'},"-",
                {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editResource"},"-",
                {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteResources'}
            ]
        },

        selModel: {
            type: 'checkboxmodel'
        },
		reference: 'resourceGrid',
        multiSelect: true,
		style:"margin:10px",
		store: {
			type: 'resourceStore'
		},
		columns: [{
            text: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.resSetID","资源集合编号"),
            dataIndex: 'resSetID',
            hidden: true
        },{
            text: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.resourceID","资源编号"),
			dataIndex: 'resourceID',
            width: 300
		},{
            text: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_INFO_STD.resourceName","资源名称"),
			dataIndex: 'resourceName',
			minWidth: 200,
			flex: 1
		},{
            menuDisabled: true,
            sortable: false,
            width:60,
            align:'center',
            xtype: 'actioncolumn',
            items:[
                {iconCls: 'edit',tooltip: '编辑',handler:'editCurrResource'},
                {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrResource'}
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
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onResSetInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onResSetInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onResSetInfoClose'
	}]
});
