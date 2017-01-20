Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanel', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelStore',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'SmsHistoryPanel',
    columnLines: true,
    style:"margin:8px",
    title: '查看发送历史',
    header:false,
    frame: true,
    controller: 'SmsHistoryPanelController',
    dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("SmsHistoryPanel");
                    //关闭窗口
                    win.close();
                }
                }]
        }/**,{
			xtype:"toolbar",
			items:[
				{
					text:"查询",tooltip:"查询数据",iconCls: "query",handler: function(btn){
								//获取窗口
								var win = btn.findParentByType("commonEmailAddressee");
								//关闭窗口
								win.close();
					}
				}
			]}***/
    ],
    initComponent: function () {

        var SmsHistoryPanelStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelStore();

        Ext.apply(this, {
            columns: [{
                text: '状态',
                dataIndex: 'status',
                minWidth: 120
            },{
            	xtype: 'linkcolumn',
                text: '发送人数',
                dataIndex: 'sendNum',
                minWidth: 90,
                flex: 1,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
            },{
            	xtype: 'linkcolumn',
                text: '已发送人数',
                dataIndex: 'smsSubmitNum',
                minWidth: 90,
                flex: 1,
                hidden:true,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
            },{
            	xtype: 'linkcolumn',
                text: '成功人数',
                dataIndex: 'sendSucNum',
                minWidth: 90,
                flex: 1,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
            },{
            	xtype: 'linkcolumn',
                text: '失败人数',
                dataIndex: 'sendFailNum',
                minWidth: 90,
                flex: 1,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
            },{
            	xtype: 'linkcolumn',
                text: '重复人数',
                dataIndex: 'sendRptNum',
                minWidth: 90,
                flex: 1,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
            },{
            	xtype: 'linkcolumn',
                text: '短信回复人数',
                dataIndex: 'smsReplyNum',
                hidden: true,
                minWidth: 90,
                flex: 1,
                items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'smsReplyView'
				}]
            },{
                text: '操作人',
                dataIndex: 'operator',
                minWidth: 90,
                flex: 1
            },{
                text: '发送时间',
                dataIndex: 'sendDt',
                minWidth: 90,
                flex: 1
            },{
                header: '明细',
                xtype:'linkcolumn',
                sortable: false,
                value:'明细',
                minWidth: 150,
                handler:'viewHistoryDetail'

            },{
			   text: '重新发送失败任务', 
			   menuDisabled: true,
			   sortable: false,
			   align:'center',
			   xtype: 'actioncolumn',
			   minWidth: 130,                
			   flex: 1,
			   items:[
				{iconCls: ' send',tooltip: '重新发送失败任务',handler:'reSendFailTask', 
					isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
						if(record.get('sendFailNum')=='0'){
							return true;
						}else{
							if(record.get('statusID')=="C" || record.get('statusID')=="D"){
								return false;
							}else{
								return true;	
							}
						}
					}
				},
			   ]	
			}],
           store: SmsHistoryPanelStore,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: SmsHistoryPanelStore,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
//    constructor: function () {
//        //发送的模板ID;
////        this.tmpId = config.tmpId;
////        this.callParent();
//    },
////    listeners:{
////        resize: function(win){
////            win.doLayout();
////        }
////    }

});
