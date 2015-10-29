Ext.define('KitchenSink.view.common.commonEmailHisPanel', {
  extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.store.commonEmailHisStore'
  ],
  xtype: 'commonEmailHis',
  columnLines: true,
	style:"margin:8px",
  title: '查看发送历史',
	header:false,
	frame: true,
  dockedItems:[
  	{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
			{
				minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
								//获取窗口
								var win = btn.findParentByType("commonEmailHis");
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
    		var me = this;  										
    		var store = new KitchenSink.view.common.store.commonEmailHisStore(me.tmpId);	
    		 
        Ext.apply(this, {
            columns: [{ 
                text: '状态',
                dataIndex: 'status',
                minWidth: 120
            },{ 
                text: '发送人数',
                dataIndex: 'sendNum',
                minWidth: 90,                
								flex: 1
            },{ 
                text: '成功人数',
                dataIndex: 'sendSucNum',
                minWidth: 90,                
								flex: 1
            },{ 
                text: '失败人数',
                dataIndex: 'sendFailNum',
                minWidth: 90,                
								flex: 1
            },{ 
                text: '发送时间',
                dataIndex: 'sendDt',
                minWidth: 90,                
								flex: 1
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
								store: store,
                displayInfo: true,
								displayMsg: '显示{0}-{1}条，共{2}条',
								beforePageText: '第',
                afterPageText: '页/共{0}页',
								emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });	
        this.callParent();
  },
	constructor: function (config) {
		//发送的模板ID;
    this.tmpId = config.tmpId; 
		this.callParent();
	},
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	}
	
});
