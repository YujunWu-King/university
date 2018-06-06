Ext.define('KitchenSink.view.activity.applicants.chooseClueOrgWindow', {
    extend: 'Ext.window.Window',
    xtype: 'chooseClueOrgWindow',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
	reference: 'chooseClueOrgWindow',
	controller: 'applicantsMg',
	
	title: '选择线索机构',
	width: 400,
    height: 150,
    minWidth: 300,
    minHeight: 100,
    layout: 'fit',
    resizable: false,
    modal: true,
	
    constructor: function(config){
		this.actId = config.actId;
		this.bmrIds = config.bmrIds;
		this.callParent();
	},
    
	initComponent: function () {
		
		var orgStore= new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_JG_BASE_T',
            condition:{
            	TZ_JG_ID:{
                    value: Ext.tzOrgID,
                    operator:'02',
                    type:'01'
                },
                TZ_JG_EFF_STA:{
                    value: 'Y',
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_JG_ID,TZ_JG_NAME'
        });
		
		
		Ext.apply(this, {
			items: [{
		        xtype: 'form',
				layout: {
		            type: 'vbox',
		            align: 'stretch'
		        },
		        border: false,
		        bodyPadding: 10,

		        items: [{
					xtype:'combo',
					fieldLabel:'机构',
					name: 'orgId',
					store: orgStore,
					valueField: 'TZ_JG_ID',
					displayField: 'TZ_JG_NAME',
					mode:"local",
					allowBlank: false,
					emptyText: '请选择机构'
				}]
			}]
		});
		
		this.callParent();
	},
	
	buttons:[{
		text: '确定',
		handler: 'createClueEnsure'
	},{
		text: '取消',
		handler: function(btn){
			btn.findParentByType('window').close();
		}
	}]
});
