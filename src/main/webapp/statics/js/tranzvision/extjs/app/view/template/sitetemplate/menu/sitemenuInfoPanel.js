Ext.define('KitchenSink.view.template.sitetemplate.menu.sitemenuInfoPanel', {
    extend: 'Ext.window.Window',
    xtype: 'sitemenuInfoPanel', 
	controller: 'siteMenuInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.template.sitetemplate.menu.sitestateStroe',
		'KitchenSink.view.template.sitetemplate.menu.menutypeStroe',
		'KitchenSink.view.template.sitetemplate.menu.menulmStore',
		'KitchenSink.view.template.sitetemplate.menu.menuColumnStore',
		'KitchenSink.view.template.sitetemplate.menu.menudelStore',
		'KitchenSink.view.template.sitetemplate.menu.siteMenuController'
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
			value: 'SITEM_10'
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
