Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsSendMemberWindow', {
    extend: 'Ext.window.Window',
    xtype: 'SmsSendMemberWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsSendMemberStore'
    ],
    title: '',
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
		var store = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsSendMemberStore({
				taskId: this.taskId,
				sendStatus: this.sendStatus
			});
		
		var columns = [{
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.name","姓名"),
					dataIndex: 'name',
					minWidth: 100,
					flex:1
				},{
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SMS_MEMBER_STD.email","号码"),
					dataIndex: 'addresseePhone',
					minWidth: 120,                
					flex: 1	
				},{
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.sendDt","发送时间"),
					dataIndex: 'sendDt',
					minWidth: 120,                
					flex: 1		
				}];	
		var sendStatus = this.sendStatus;
		
		if(sendStatus == ""){
			me.title = Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.viewSendMember","查看发送人");
			columns.push({
					text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.sendStatus","发送状态"),
					dataIndex: 'sendStatus',
					minWidth: 90,
					flex: 1	
			});
		}
		if(sendStatus == "FAIL"){
			me.title = Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.viewFailMember","查看失败人数");
			columns.push({
				xtype: 'linkcolumn',
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.viewRz","失败日志"),
                minWidth: 80,
				width: 100,              
				items:[{
					getText: function(v, meta, rec) {
						return  Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.view","查看");
					},
					handler: this.onViewRz
				}]
			});	
		}
		if(sendStatus == "SUC"){
			me.title = Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.viewSucMember","查看成功人数");
		}
		if(sendStatus == "RPT"){
			me.title = Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_SEND_MEMBER_STD.viewRptMember","查看重复人数");
		}
		
		Ext.apply(this,{
            items: [{
				xtype: 'grid',
                columnLines: true,
				viewConfig: {
					enableTextSelection: true
				},
				store:store,
				columns: columns,
				dockedItems:[{
					xtype:"toolbar",
					dock:"bottom",
					ui:"footer",
					items:['->',
						{minWidth:80,text:'关闭',iconCls:"close",handler: function (btn)
						{   var win = btn.findParentByType("SmsSendMemberWindow");
							win.close();
						}}]
					},
					{ xtype:"toolbar",items:[ {text:"查询",iconCls: "query",handler: this.searchEmlMember}]
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
	searchEmlMember: function(btn){
		var win = btn.findParentByType('SmsSendMemberWindow');
		Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_MEMBER_STD.TZ_SMSHIS_RY_V',
           condition:
            {
                "TZ_EML_SMS_TASK_ID": win.taskId,
				"TZ_FS_ZT": win.sendStatus   
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
	},
	onViewRz: function(grid,rowIndex,colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var taskId = rec.data.taskId;	
		var rwInsId = rec.data.rwInsId;	
		
		var tzParams = '{"ComID":"TZ_SMSQ_VIEWTY_COM","PageID":"TZ_SMSQ_MEMBER_STD","OperateType":"QF","comParams":{"taskId":"'+ taskId +'","rwInsId":"'+ rwInsId +'"}}';
		Ext.tzLoad(tzParams,function(formData) {
			var Panel=Ext.create('Ext.form.Panel', {  
				bodyPadding: 10,  
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				fieldDefaults: {
				  labelWidth: 100,
				  labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'label',
					text:'错误原因：',
					style:'font-weight:bold'	
				},{
					xtype:'component',
					hideLabel: true,	
					margin: '10 0 0 0',
					html:[formData.errorMsg],
				}]
			});
			
			imgWin = Ext.widget('window', {
				 title: '查看失败日志',
				 closeAction: 'hide',
				 width: 600,
				 height: 300,
				 resizable: true,
				 modal: true,
				 items:Panel,
				 dockedItems:[{
						xtype:"toolbar",
						dock:"bottom",
						ui:"footer",
						items:['->',
							{minWidth:80,text:'关闭',iconCls:"close",handler: function (btn)
							{   var win = btn.findParentByType("window");
								win.close();
							}}]
					}]
			});
			imgWin.show();
		})
	}
});