Ext.define('KitchenSink.view.siteManage.siteManage.menu.menuManges.siteMenuTypeInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'siteMenuTypeInfoPanel', 
	controller: 'siteTypeInfo1',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.form.FileUploadField',
        'Ext.tip.QuickTipManager.init',
		'KitchenSink.view.siteManage.siteManage.menu.menuManges.siteStateStroe',
		'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeController',
		'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeIconStore'
	],
    title: '菜单类型设置', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'menuTypeForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '站点模板编号',
			name: 'siteId',
        	},{
            xtype: 'hiddenfield',
            fieldLabel: '菜单类型编号',
			name: 'menutypeid',
        },{
            xtype: 'textfield',
            fieldLabel: '菜单类型名称',
			name: 'menutypename'
        }, {
            xtype: 'combobox',
            fieldLabel: '状态',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			name: 'menutypestate',
			store: new KitchenSink.view.common.store.appTransStore("TZ_TYPE_STATE")
			/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_TYPE_STATE"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_TYPE_STATE
						}));
					});
				}
			}*/
        },{
            xtype: 'combobox',
            fieldLabel: '栏目类型',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_ZHZ_ID',
    		displayField: 'TZ_ZHZ_DMS',
			name: 'menuColuType',
			//store: new KitchenSink.view.common.store.appTransStore("TZ_ZDLM_LX")
			tpl:'<tpl for=".">' +   
				'<div class="x-boundlist-item" style="height:30px;">' +   
				'{TZ_ZHZ_DMS}' +   
				'</div>'+   
				'</tpl>' 
        },{
            xtype: 'combobox',
            fieldLabel: '栏目模板',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME',
			name: 'menuColuTmpl',
			tpl:'<tpl for=".">' +   
				'<div class="x-boundlist-item" style="height:30px;">' +   
				'{TZ_TEMP_NAME}' +   
				'</div>'+   
				'</tpl>' 
			//store: new KitchenSink.view.common.store.appTransStore("TZ_TYPE_STATE")
        },{
            xtype: 'combobox',
            fieldLabel: '内容类型',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_ZHZ_ID',
    		displayField: 'TZ_ZHZ_DMS',
			name: 'menuContType',
			//store: new KitchenSink.view.common.store.appTransStore("TZ_ZD_NRLX")
			tpl:'<tpl for=".">' +   
				'<div class="x-boundlist-item" style="height:30px;">' +   
				'{TZ_ZHZ_DMS}' +   
				'</div>'+   
				'</tpl>' 
        },{
            xtype: 'combobox',
            fieldLabel: '内容模板',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME',
			name: 'menuContTmpl',
			tpl:'<tpl for=".">' +   
				'<div class="x-boundlist-item" style="height:30px;">' +   
				'{TZ_TEMP_NAME}' +   
				'</div>'+   
				'</tpl>' 
			//store: new KitchenSink.view.common.store.appTransStore("TZ_TYPE_STATE")
        },{
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">允许用户创建此类型菜单</span>',
            margin: '0 0 5 105',
            hideLabel: true,
            inputvalue:'true',
			name: 'menuisadd'
        },{
            xtype: 'combobox',
            fieldLabel: '是否允许选择关联栏目',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			name: 'menucoluadd',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ADD_COLU")
			/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_TYPE_STATE"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_TYPE_STATE
						}));
					});
				}
			}*/
        },{
            xtype: 'ueditor',
            fieldLabel:'功能类型说明',
			zIndex: 900,
        	name: 'menutypedescr'
        },{
            xtype: 'textfield',
            fieldLabel: '业务人员设置菜单处理代码',
            labelWidth: 250,
			name: 'menusetcode'
        },{
            xtype: 'textfield',
            fieldLabel: '考生点击菜单处理代码',
            labelWidth: 250,
			name: 'menushowcode'
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
					type: 'menuTypeIconStore'
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
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteSkin'}
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
