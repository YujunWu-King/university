Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyWindow', {
    extend: 'Ext.window.Window',
    xtype: 'SmsReplyWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyStore'
    ],
    title: '短信回复内容查看',
    layout: 'fit',
	ignoreChangesFlag: true,
    modal: true,
    header:true,
    height: 450,
    width: 800,
	defaultListenerScope: true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
	constructor: function (config) {
		this.taskId = config.taskId;
		this.sendStatus = config.sendStatus;
		
		this.callParent();	
	},
    initComponent: function(){
		var me = this;
		var store = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyStore({
				taskId: this.taskId
			});

		Ext.apply(this,{
            items: [{
				xtype: 'grid',
                columnLines: true,
				viewConfig: {
					enableTextSelection: true
				},
				store:store,
				columns: [{
					text: '姓名',
					dataIndex: 'name',
					width: 90
				},{
					text: '手机号码',
					dataIndex: 'mobile',
					width: 120
				},{
					text: '回复内容',
					dataIndex: 'replyContent',
					width: 200,
					flex:1
				}],
				dockedItems:[{
					xtype:"toolbar",
					dock:"bottom",
					ui:"footer",
					items:['->',
						{minWidth:80,text:'关闭',iconCls:"close",handler: function (btn)
						{   var win = btn.findParentByType("SmsReplyWindow");
							win.close();
						}}]
					},
					{ xtype:"toolbar",items:[ {text:"查询",iconCls: "query",handler: this.searchSmsReply}]
				 }],

				 bbar: {
					 xtype: 'pagingtoolbar',
					 pageSize: 20,
					 listeners:{
						 afterrender: function(pbar){
							 var grid = pbar.findParentByType("grid");
							 pbar.setStore(grid.store);
						 }
					 },
				 	 plugins: new Ext.ux.ProgressBarPager()
				}
			}]
		});
		
		this.callParent();
	},
	searchSmsReply: function(btn){
		var win = btn.findParentByType('SmsReplyWindow');
		Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_REPLY_STD.TZ_SMS_REPLY_V',
           condition:
            {
                "TZ_EML_SMS_TASK_ID": win.taskId
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
	}
});