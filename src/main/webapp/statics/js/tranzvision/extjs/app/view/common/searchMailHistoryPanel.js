/*=============================================================
说明：历史邮件查询页面，张浪，20151113
============================================================*/
Ext.define('KitchenSink.view.common.searchMailHistoryPanel', {
	extend: 'Ext.panel.Panel',
	requires: [
		'Ext.data.*',
		'Ext.util.*',
		'KitchenSink.view.common.store.searchMailHistoryStore'
	],
	xtype: 'searchMailHistoryPanel',
	title: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.mailHistoryTitle","发送邮件历史"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	initComponent: function(){
		var me = this;
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'mailHistoryForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				ignoreLabelWidth: true,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
					
				fieldDefaults: {
				  msgTarget: 'side',
				  labelWidth: 100,
				  labelStyle: 'font-weight:bold'
				},
				
				items: [{
					layout: {
						type: 'column',
					},
					items:[{
						columnWidth:.4,
						xtype: 'textfield',
						emptyText: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.Email","请输入邮箱"),
						name: 'Email',
						allowBlank:false,
						hideLabel: true,
						ignoreChangesFlag: true,
						blankText:Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.EmailBlankText","邮箱不能为空"),
						value: this.EmailAddress
					},{
						xtype: 'button',
						text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.searchBtn","查询"),
						maxWidth: 120,
						iconCls:"search",
						margin: '0 0 0 20',
						handler: function(btn){
							var panel = btn.findParentByType("searchMailHistoryPanel");
							var form = panel.child('form').getForm();
							var searchMail = form.findField('Email').getValue(); 
							
							if (searchMail != ""){
								var mailHisGrid = panel.down('grid[name=mailHistoryGrid]');
								var tzStoreParams = '{"emailAddress":"'+searchMail+'"}';
								mailHisGrid.store.tzStoreParams = tzStoreParams;
								mailHisGrid.store.load();
							} else {
								Ext.Msg.alert("提示","请输入要查询的邮箱地址！");
							}
						}
					}]
				}]
		     },{
				xtype: 'grid',
				title: '邮件发送历史列表',
				frame: true,
				columnLines: true,
				minHeight:360,
				maxHeight:390,
				name:'mailHistoryGrid',
				style: "margin:10px",
				store: new KitchenSink.view.common.store.searchMailHistoryStore(this.EmailAddress),
				columns: [{
					xtype: 'rownumberer',
					align:'center',
					text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.orderby","序号"),
					width:60
				},{ 
					text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.sendTime","发送时间"),
					dataIndex: 'sendTime',
					width:165
				},{ 
					text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.sender","发件人"),
					dataIndex: 'sender',
					minWidth:160,
					flex: 1
				},{
					text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.subject","主题"),
					dataIndex: 'subject',
					minWidth:200,
					flex: 1
				},{
					text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.operator","操作人"),
					dataIndex: 'operator',	
					width:100
				},{
					menuDisabled: true,
					sortable: false,
					align:'center',
					width:60,
					xtype: 'actioncolumn',
					items:[
						{iconCls: 'view',tooltip: '查看详情',handler:function(view, rowIndex){
							
							var store = view.findParentByType("grid").store;
							var selRec = store.getAt(rowIndex);
							//任务实例ID
							var rwInsID = selRec.get("rwInsid");

							var contentPanel,cmp, className, ViewClass;
		
							contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
							contentPanel.body.addCls('kitchensink-example');
					
							className = 'KitchenSink.view.common.mailHistoryDetailsPanel';
							if(!Ext.ClassManager.isCreated(className)){
								Ext.syncRequire(className);
							}	
							ViewClass = Ext.ClassManager.get(className);
							
							cmp = new ViewClass(rwInsID);
							
							tab = contentPanel.add(cmp);     
							
							contentPanel.setActiveTab(tab);
							Ext.resumeLayouts(true);
					
							if (cmp.floating) {
								cmp.show();
							}
						}},
					]
				}],
				/*
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
					reference: 'plstComToolBar',
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					plugins: new Ext.ux.ProgressBarPager()
				}
				*/
			}],
			buttons: [{
			 	text: Ext.tzGetResourse("TZ_MAIL_HISTORY_COM.TZ_MIAL_HIS_STD.close","关闭"),
				iconCls:"close",
				ignoreChangesFlag: true,
				handler: function(btn){
					//获取窗口
					var panel = btn.findParentByType("searchMailHistoryPanel");
					panel.commitChanges(panel);
					//关闭窗口
					panel.close();
				}
			}]	 
		});
		this.callParent();
	},
	constructor: function (email) {
		//搜索邮件地址;
		this.EmailAddress = email; 
		this.callParent();
	},
	listeners:{
		beforeclose: function(pnl){
			pnl.commitChanges(pnl);
		}
		
	}
});

