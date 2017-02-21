Ext.define('KitchenSink.view.automaticScreen.setWeedOutWindow', {
    extend: 'Ext.window.Window',
    xtype: 'setWeedOutWindow', 
	requires: [
	    'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.automaticScreen.autoScreenController'
	],
	controller: 'autoScreenController',
	
	width: 350,
	height: 180,
  	resizable: false,
	modal: true,
	closeAction: 'destroy',
	
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.setWeedOut","调整淘汰人数"),
	
	constructor: function(config, callback){
		this.classId = config.classId;
		this.batchId = config.batchId;
		this.reLoadGrid = callback;
		
		var respJson;
		var tzParams ='{"ComID":"TZ_AUTO_SCREEN_COM","PageID":"TZ_AUTO_SCREEN_STD","OperateType":"queryWeedOutInfo","comParams":{"classId":"'+ config.classId +'","batchId":"'+ config.batchId +'"}}';
		Ext.tzLoadAsync(tzParams,function(respDate){
			respJson = respDate;
		});
		
		this.totalNum = respJson.totalNum;
		this.screenNum = respJson.screenNum;
		this.lastNum = respJson.lastNum;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var me = this;
		
        Ext.apply(this, {
		    items: [{        
		        xtype: 'form',
		        bodyPadding: 20,
		        border: false,
		        
		        items: [{
		            xtype: 'displayfield',
					value: '报考考生数量共'+ this.totalNum +'人,参与自动初筛'+ this.screenNum +'人'
		        },{
		        	layout:'column',
		        	items:[{
		        		xtype: 'displayfield',
						value: '淘汰后'
		        	},{
		        		xtype: 'numberfield',
			           	name: 'personNum',
			           	minValue: 1,
			           	hideLabel: true,
			           	width: 100,
			           	value: this.lastNum
		        	},{
		        		xtype: 'displayfield',
						value: '名考生'
		        	}]
		        }]
		    }]
        });
		
        this.callParent();
    },
    buttons: [{
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.ensure","确定"),
		iconCls:"ensure",
		handler: 'setWeedOutStuEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.close","关闭"),
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType('setWeedOutWindow').close();
		}
	}]
})
