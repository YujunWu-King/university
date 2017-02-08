Ext.define('KitchenSink.view.scoreModelManagement.copyScoreModelWin', {
    extend: 'Ext.window.Window',
    xtype: 'copyScoreModelWin', 
	requires: [
	    'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.scoreModelManagement.scoreModelMgController'
	],
	controller: 'scoreModelMgController',
	reference: 'copyScoreModelWin',
	
	width: 450,
	height: 240,
  	resizable: false,
	modal: true,
	closeAction: 'destroy',
	
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.copyScoreModel","复制成绩模型"),
	//pageY:80, 
	
	constructor: function(config, callback){
		this.opeConfig = config;
		this.treeReload = callback;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var me = this;
		
        Ext.apply(this, {
		    items: [{        
		        xtype: 'form',
		        reference: 'scoreItemForm',
		        bodyPadding: 10,
				layout: {
					type: 'vbox',
					align: 'stretch'
		        },
		        border: false,
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'displayfield',
					value: '请输入复制的成绩模型ID:'
		        },{
		           	xtype: 'textfield',
					name: 'copyModeId',
					allowBlank: false
		        },{
		            xtype: 'displayfield',
					value: '请输入复制的成绩模型树名称:'
		        },{
		           	xtype: 'textfield',
					name: 'copyTreeName',
					maxLength: 18,
					allowBlank: false
		        }]
		    }]
        });
		
        this.callParent();
    },
    buttons: [{
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'copyScoreModelEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.cancel","取消"),
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType('copyScoreModelWin').close();
		}
	}]
})
