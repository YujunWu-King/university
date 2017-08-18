Ext.define('KitchenSink.view.basicData.filter.filterFldDataSetInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'filterFldInfoPanel',
	controller: 'filterDataSetController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.filter.FldDataSetCondStore',
        'KitchenSink.view.basicData.filter.FldDataSetRoleStore',
        'KitchenSink.view.basicData.filter.filterDataSetController'
	],
    title: '关联数据集查询条件',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',//默认新增
    changeNum:'',
    newDate:'',
    items: [{
        xtype: 'form',
        reference: 'filterDataSetFldForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '组件编号',
            name: 'ComID'
        }, {
            xtype: 'hiddenfield',
            fieldLabel: '页面编号',
            name: 'PageID'
        }, {
            xtype: 'hiddenfield',
            fieldLabel: '视图名称',
            name: 'ViewMc'
        },{
			xtype: 'textfield',
			fieldLabel: '序号',
			hidden:true,
			name: 'fieldOrder'
		},{								
			xtype: 'textfield',
			fieldLabel: "字段名称",
			name: 'dataSetFld',
			triggers: {
			  search: {
				 cls: 'x-form-search-trigger',
				 handler: "searchDataSetFld"
			  }
			}				
		},{									
			xtype: 'textfield',
			fieldLabel: "搜索记录名称",
			name: 'searchRec',
			triggers: {
				search: {
					cls: 'x-form-search-trigger',
					handler: "searchDataSetRec"
				}
			}						
		},{
            xtype: 'textfield',
            fieldLabel: '数据集描述',
            name: 'fldDstDesc'
        },{
        	 xtype: 'combobox',
        	 fieldLabel: '有效状态',
        	 valueField: 'TValue',
             displayField: 'TLDesc',
             store: new KitchenSink.view.common.store.appTransStore("TZ_ISVALID"),
             queryMode: 'local',
             value: "Y",
    		 name: 'fldDstStatus'
    	},{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">默认</span>',
            margin: '0 0 5 175',
            hideLabel: true,
            inputvalue:'Y',
			name: 'fldDstDefault'
        }]
	},{

	    xtype: 'tabpanel',
		frame: true,
		style:"margin:10px",
		items:[{
			xtype: 'grid',
			name:'filterCondGrid',
			minHeight: 360,
			maxHeight: 360,
			autoScroll: true,
			title: '搜索条件字段操作运算符设置',
			frame: true,
			columnLines: true,
			dockedItems:{
				xtype:"toolbar",
				items:[
					{text:"新增",tooltip:"新增",iconCls:"add",handler:'addCond'},"-",
					{text:"编辑",tooltip:"编辑",iconCls:"edit",handler:'editSelCond'},"-",
					{text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteConds'},"-",
					{text:"预览",tooltip:"预览",iconCls:"view",handler:'previewCondSql'}
				]
			},
			selModel: {
				type: 'checkboxmodel'
			},
			reference: 'filterCondGrid',
			multiSelect: true,
			style:"margin:10px",
			store: {
				type: 'FldDataSetCondStore'
			},
			columns: [{
				text: '组件编号',
				dataIndex: 'ComID',
				hidden: true
			},{
				text: '页面编号',
				dataIndex: 'PageID',
				hidden: true
			},{
				text: '视图',
				dataIndex: 'ViewMc',
				hidden: true
			},{
				text: '序号',
				dataIndex: 'fieldOrder',
				hidden: true,
				width: 70
			},{
				text: '序号',
				dataIndex: 'orderCond',
				width: 70
			},{
				text: '条件与/或',
				dataIndex: 'dstAndOr',
				width: 100
			},{
				text: '左括号',
				dataIndex: 'leftParen',
				width: 100
			},{
				text: '字段名称',
				dataIndex: 'dstCondFld',
				flex:1
			},{
				text: '操作符',
				dataIndex: 'dstOperator',
				hidden:true,
				width: 150
			},{
				text: '操作符',
				dataIndex: 'dstOperatorDesc',
				width: 150
			},{
				text: '取值类型',
				dataIndex: 'dstCondValueType',
				hidden: true,
				width: 150
			},{
				text: '字段取值',
				dataIndex: 'dstCondFldValue',
				width: 150
			},{
				text: '右括号',
				dataIndex: 'rightParen',
				width: 100
			},{
			   menuDisabled: true,
			   sortable: false,
			   width:60,
			   xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editCond'},
				  /** '-',  **/
				  {iconCls: 'remove',tooltip: '删除',handler: 'deleteCond'}
			   ]
			}]
		},{

			xtype: 'grid',
			name:'filterRoleGrid',
			minHeight: 360,
			maxHeight: 360,
			autoScroll: true,
			title: '数据集角色设置',
			frame: true,
			columnLines: true,
			dockedItems:{
				xtype:"toolbar",
				items:[
					{text:"新增",tooltip:"新增",iconCls:"add",handler:'addRole'},"-",
					{text:"删除",tooltip:"删除",iconCls:"remove",handler:'deleteRole'}
				]
			},
			selModel: {
				type: 'checkboxmodel'
			},
			reference: 'filterRoleGrid',
			multiSelect: true,
			style:"margin:10px",
			store: {
				type: 'FldDataSetRoleStore'
//				type: 'FldDataSetCondStore'
			},
			columns: [{
				text: '序号',
				dataIndex: 'orderNum',
				width: 300,
				hidden:true
			},{
				text: '角色名称',
				dataIndex: 'roleID',
				width: 300
			},{
				text: '描述',
				dataIndex: 'roleDesc',
				width: 600
			},{
			   menuDisabled: true,
			   sortable: false,
			   width:60,
			   xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'remove',tooltip: '删除',handler: 'deleteSelectedRole'}
			   ]
			}]
		}]	
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFilterDataSetFldSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFilterDataSetFldEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterDataSetFldClose'
	}]
});
