Ext.define('KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jugMgInfo',
    controller: 'jugMg',
    requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgController'
	],
//	listeners:{
//		resize: function(win){
//			win.doLayout();
//		}
//	},
	actType: '',	
    title: '评委类型定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'jugTypeForm',
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
            fieldLabel: '评审类型编号',
			maxLength: 150,
			name: 'jugTypeId',
			readOnly: true,
			fieldStyle:'background:#F4F4F4'
        }, {
            xtype: 'textfield',
            fieldLabel: '评审类型名称',
			maxLength: 125,
			name: 'jugTypeName'
        }, {
            xtype: 'textfield',
            fieldLabel: '角色名称',
			maxLength: 70,
			name: 'roleName',
			editable: false,
            triggers: {
                search: {
                    cls: 'x-form-search-trigger',
                    handler: "searchRoleName"
                }
            }
        },{
    		xtype: 'combobox',
	        fieldLabel: "有效状态",
	        editable:false,
	        emptyText:'请选择',
	        queryMode: 'remote',
	    	name: 'jugTypeStatus',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_JUGTYP_STAT"),
    		allowBlank: false,
			value:'0'
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
