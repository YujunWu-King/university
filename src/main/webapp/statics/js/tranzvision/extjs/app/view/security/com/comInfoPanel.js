﻿Ext.define('KitchenSink.view.security.com.comInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'comRegInfo', 
	controller: 'comReg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.com.pageStore',
		'KitchenSink.view.security.com.processStore'
	],
    title: '组件注册信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'comRegForm',
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
            fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.comID","组件ID"),
			name: 'comID',
			maxLength: 20,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.comName","组件名称"),
			name: 'comName'
        }]
    },{
    	xtype:'tabpanel',
		items:[{
			xtype: 'grid',
			title: '页面注册信息列表',
			id:'pageRegGrid',
			frame: true,
			columnLines: true,
			height: 350,
			reference: 'pageRegGrid',
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
				text: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.comID","组件ID"),
				dataIndex: 'comID',
				hidden: true
			},{
				text: '序号',
				dataIndex: 'orderNum',
				width:60
			},{
				text: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.pageID","页面ID"),
				dataIndex: 'pageID',
				width:240
			},{
				text: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.pageName","页面名称"),
				dataIndex: 'pageName',
				minWidth: 100,
				flex: 1
			},
				//将原来的checkbox 改为下拉框
				{
					text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.isDefault","是否默认首页"),
					sortable: true,
					dataIndex: 'isDefault',
					minWidth: 100,
					align:'center',
					renderer : function(value, metadata, record) {
						if (value=="Y"){
							return "是";
						}else if(value=="N"){
							return "否";
						}
					}
				},
				{
					menuDisabled: true,
					sortable: false,
					width:60,
					align:'center',
					xtype: 'actioncolumn',
					items:[
						{iconCls: 'edit',tooltip: '编辑',handler: 'editPageRegInfoOne'},
						{iconCls: 'remove',tooltip: '删除',handler: 'deletePageRegInfoOne'}
					]
				}
			],
			store: {
				type: 'pageStore'
			},
			dockedItems:[{
				xtype:"toolbar",
				items:[
					{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addPageRegInfo"},"-",
					{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editPageRegInfo"},"-",
					{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deletePageRegInfos"}
				]
			}],
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 5,
				/*store: {
				 type: 'pageStore'
				 },*/
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
				},
				plugins: new Ext.ux.ProgressBarPager()
			}
		},{
			xtype: 'grid',
			title: '进程列表',
			id:'processGrid',
			frame: true,
			columnLines: true,
			height: 350,
			reference: 'processGrid',
			style: "margin:10px",
			multiSelect: true,
			selModel: {
				type: 'checkboxmodel'
			},
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					containerScroll: true,
					dragGroup: this,
					dropGroup: this
				},
			},
			columns: [{
				text: Ext.tzGetResourse("TZ_AQ_COMREG_COM.TZ_AQ_COMREG_STD.comID","组件ID"),
				dataIndex: 'comID',
				hidden: true
			},{
				text: '进程名称',
				dataIndex: 'processName',
				flex:1
			},{
				text: '进程描述',
				dataIndex: 'processDesc',
				flex:1
				}
			],
			store: {
				type: 'processStore'
			},
			dockedItems:[{
				xtype:"toolbar",
				items:[
					{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
				]
			}],
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 5,
				/*store: {
				 type: 'pageStore'
				 },*/
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
				},
				plugins: new Ext.ux.ProgressBarPager()
			}
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onComRegSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onComRegEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onComRegClose'
	}]
});
