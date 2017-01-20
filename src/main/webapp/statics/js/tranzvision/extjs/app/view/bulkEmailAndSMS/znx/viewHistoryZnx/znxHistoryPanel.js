Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanel', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'znxHistoryPanel',
    columnLines: true,
    style:"margin:8px",
    title: '查看发送历史',
    header:false,
    frame: true,
    controller: 'znxHistoryPanelController',
    dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("znxHistoryPanel");
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

        var znxHistoryPanelStore = new KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelStore();

        Ext.apply(this, {
            columns: [{
                text: '状态',
                dataIndex: 'status',
                minWidth: 120
            },/*{
                text: '发送人数',
                dataIndex: 'sendNum',
                minWidth: 90,
                flex: 1
            },*/{
				xtype: 'linkcolumn',
				text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.sendNum","发送人数"),
                dataIndex: 'sendNum',
                minWidth: 90,     
				flex: 1,           
				items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
			},/*{
                text: '成功人数',
                dataIndex: 'sendSucNum',
                minWidth: 90,
                flex: 1
            },*/{
				xtype: 'linkcolumn',
				text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.sendSucNum","成功人数"),
                dataIndex: 'sendSucNum',
                minWidth: 90, 
				flex: 1,               
				items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
			},/*{
                text: '失败人数',
                dataIndex: 'sendFailNum',
                minWidth: 90,
                flex: 1
            },*/{
				xtype: 'linkcolumn',
				text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.sendFailNum","失败人数"),
                dataIndex: 'sendFailNum',
                minWidth: 90,   
				flex: 1,             
				items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
			},/*{
                text: '重复人数',
                dataIndex: 'sendRptNum',
                minWidth: 90,
                flex: 1
            },*/{
				xtype: 'linkcolumn',
				text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.sendRptNum","重复人数"),
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
                text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.operator","操作人"),
                dataIndex: 'operator',
                minWidth: 90
            },{
                text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.sendDt","发送时间"),
                dataIndex: 'sendDt',
                minWidth: 120,
                flex: 1
            },{
                header: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.mxLink","明细"),
                xtype:'linkcolumn',
                sortable: false,
                value:'明细',
                width: 80,
				minWidth: 60,
                handler:'viewHistoryDetail'

            },{
				   text: Ext.tzGetResourse("TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VIEWTY_STD.reSendFailRw","重新发送失败任务"), 
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
           store: znxHistoryPanelStore,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: znxHistoryPanelStore,
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
