Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jugMsMgInfo',
    controller: 'jugMsMg',
    requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	],
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	},
	actType: '',	
    title: '面试评审组定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'jugMsGroupForm',
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
            fieldLabel: '面试评审组ID',
			maxLength: 150,
			name: 'jugGroupId',
			readOnly: true,
			fieldStyle:'background:#F4F4F4'
        }, {
            xtype: 'textfield',
            fieldLabel: '面试评审组名称',
			maxLength: 125,
			name: 'jugGroupName'
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
