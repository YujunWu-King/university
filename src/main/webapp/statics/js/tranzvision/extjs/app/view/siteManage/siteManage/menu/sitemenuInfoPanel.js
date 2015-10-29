Ext.define('KitchenSink.view.siteManage.siteManage.menu.sitemenuInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'sitemenuInfoPanel', 
	controller: 'siteMenuInfo1',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.siteManage.siteManage.menu.sitestateStroe',
		'KitchenSink.view.siteManage.siteManage.menu.menutypeStroe',
		'KitchenSink.view.siteManage.siteManage.menu.menulmStore',
		'KitchenSink.view.siteManage.siteManage.menu.arealmStore',
		'KitchenSink.view.siteManage.siteManage.menu.menudelStore',
		'KitchenSink.view.siteManage.siteManage.menu.siteMenuController',
		'KitchenSink.view.siteManage.siteManage.menu.menuIconStore'
	],
    title: '菜单设置',
    reference:'sitemenuInfoPanel',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'userAccountForm',
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
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId',
			value: ''
        	},{
            xtype: 'textfield',
            readOnly:true,
            hidden: true,
            fieldLabel: '菜单编号',
			name: 'menuid',
			value: ''
        	},{
            xtype: 'textfield',
            fieldLabel: '菜单名称',
			name: 'menuname'
        }, {
            xtype: 'textfield',
            fieldLabel: '顺序',
			name: 'menuxh'
        }, {
            xtype: 'combobox',
            fieldLabel: '状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			name: 'menustate',
			store: new KitchenSink.view.common.store.appTransStore("TZ_MENU_STATE")
			/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_MENU_STATE"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_MENU_STATE
						}));
					});
				}
			}*/
        },{
            xtype: 'combobox',
            fieldLabel: '菜单类型',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_MENU_TYPE_ID',
    		displayField: 'TZ_MENU_TYPE_NAME',
			name: 'menutypeid'
        }, {
            xtype: 'combobox',
            fieldLabel: '菜单对应的栏目',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_COLU_ID',
    		displayField: 'TZ_COLU_NAME',
			name: 'menulm',
			tpl:'<tpl for=".">' +   
				'<div class="x-boundlist-item" style="height:30px;">' +   
				'{TZ_COLU_NAME}' +   
				'</div>'+   
				'</tpl>' 
        }, {
            xtype: 'combobox',
            fieldLabel: '是否允许删除',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			name: 'menuisdel',
			store: new KitchenSink.view.common.store.appTransStore("TZ_IS_DEL")
			/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_IS_DEL"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_IS_DEL
						}));
					});
				}
			}*/
        }, {
            xtype: 'combobox',
            fieldLabel: '是否允许编辑',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			name: 'menuisedit',
			store: new KitchenSink.view.common.store.appTransStore("TZ_IS_EDITOR")
			/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_IS_EDITOR"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_IS_EDITOR
						}));
					});
				}
			}*/
        },{
			xtype : 'tabpanel',
			activeTab: 0,
        	plain:false,
        	frame: true,
        	resizeTabs:true,
        	autoHeight:true,
        	defaults :{
            	autoScroll: false,
            	//margin:5
        	},
			listeners:{
				tabchange:function(tp,p){
					alert(p.title);
				}
			},
			items:[{
				//id:'skin',
				title:'菜单类型图标设置',
				xtype: 'grid',
				//frame: true,
				columnLines: true,
				minHeight: 250,
				//reference: 'skinGrid',
				store: {
					type: 'menuIconStore'
				},
				columns: [{
					text: '皮肤编号',
					dataIndex: 'skinId',
					width: 200
				},{
					text: '皮肤名称',
					dataIndex: 'skinName',
					minWidth: 5,
					flex: 1
				},{
					text: '状态',
					dataIndex: 'skinStatus',
					width: 200,
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteSkin2'}
				   	]
	            }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'skinToolBar',
					store: {
						type: 'skinStore'
					},
					*/
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
			}]
		}]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
