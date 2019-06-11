Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistory', {
	extend: 'Ext.panel.Panel',
	requires: [
		'Ext.data.*',
		'Ext.util.*',
		 'KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryStore',
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueController',
        'tranzvision.extension.grid.column.Link'
	],
	xtype: 'enrollmentClueSmsHistoryStore',
	title: "查看短信发送历史",
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	controller: 'enrollmentClueController',
	dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("enrollmentClueSmsHistory");
                    //关闭窗口
                    win.close();
                }
                }]
        }
    ],
	initComponent: function(){
		var enrollmentClueSmsHistoryStore = new KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryStore(this.mobile);

		var me = this;
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'smsHistoryForm',
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
						emptyText: Ext.tzGetResourse("TZ_XSXS_ZSXS_COM.TZ_XSXS_SMSHIS_STD.mobile","请输入号码"),
						name: 'mobile',
						allowBlank:false,
						hideLabel: true,
						ignoreChangesFlag: true,
						blankText:Ext.tzGetResourse("TZ_XSXS_ZSXS_COM.TZ_XSXS_SMSHIS_STD.emptyMobile","电话不能为空"),
						value: this.mobile
					},{
						xtype: 'button',
						text: Ext.tzGetResourse("TZ_XSXS_ZSXS_COM.TZ_XSXS_SMSHIS_STD.searchBtn","查询"),
						maxWidth: 120,
						iconCls:"search",
						margin: '0 0 0 20',
						handler: function(btn){
							var panel = btn.findParentByType("enrollmentClueSmsHistoryStore");
							var form = panel.child('form').getForm();
							var searchSms = form.findField('mobile').getValue(); 
							
							if (searchSms != ""){
								var smsHisGrid = panel.down('grid[name=smsHistoryGrid]');
								var tzStoreParams = '{"mobile":"'+searchSms+'"}';
								smsHisGrid.store.tzStoreParams = tzStoreParams;
								smsHisGrid.store.load();
							} else {
								Ext.Msg.alert("提示","请输入要查询的电话号码！");
							}
						}
					}]
				}]
		     },{
	        	xtype: 'grid',
				title: '短信发送历史列表',
				frame: true,
				columnLines: true,
				minHeight:360,
				maxHeight:390,
				name:'smsHistoryGrid',
				reference:'smsHistoryGrid',
				style: "margin:10px",
		        store: enrollmentClueSmsHistoryStore,
	            columns: [{
	                text: '发送时间',
	                dataIndex: 'sendDt',
	                minWidth: 200,
	                flex: 1	
				},{
	                text: '发送对象',
	                dataIndex: 'phone',
	                minWidth: 200,
	                flex: 1
	            },{
	                text: '发送状态',
	                dataIndex: 'status',
	                minWidth: 200,
	                flex: 1
	            },{
	                text: '操作人',
	                dataIndex: 'operator',
	                minWidth: 200,
	                flex: 1
	            },{
	               menuDisabled: true,
	               sortable: false,
				   width:60,
	               align:'center',
	               xtype: 'actioncolumn',
				   items:[
					  {iconCls: 'edit',tooltip: '查看发送历史正文',handler: 'editZw'}
				   ]
	            }],
	            bbar: {
	                xtype: 'pagingtoolbar',
	                pageSize: 10,
	                store: enrollmentClueSmsHistoryStore,
	                displayInfo: true,
	                displayMsg: '显示{0}-{1}条，共{2}条',
	                beforePageText: '第',
	                afterPageText: '页/共{0}页',
	                emptyMsg: '没有数据显示',
	                plugins: new Ext.ux.ProgressBarPager()
	            }
	        }],
		});
		this.callParent();
	},
	constructor: function (mobile) {
		this.mobile = mobile; 
		this.callParent();
	},
	
});