Ext.define('KitchenSink.view.common.commonEmailAddresseePanel', {
  extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.store.commonEmailAddresseeStore'
  ],
  xtype: 'commonEmailAddressee',
  columnLines: true,
	style:"margin:8px",
	title: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.viewAddressee","查看收件人"),
	header:false,
	frame: true,
  dockedItems:[
  	{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
			{
				minWidth:80,
				text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
				iconCls:"close",
				handler: function(btn){
					//获取窗口
					var win = btn.findParentByType("commonEmailAddressee");
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
    		var store = new KitchenSink.view.common.store.commonEmailAddresseeStore(me.audienceId);	
    		 
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.name","姓名"),
                dataIndex: 'name',
                minWidth: 125,                
				flex: 1
            },{ 
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.email","邮箱"),
                dataIndex: 'email',
                minWidth: 125,                
				flex: 1
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
				/*
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
				*/
                plugins: new Ext.ux.ProgressBarPager()
            }
        });	
        this.callParent();
  },
	constructor: function (config) {
		//是否有附件;
		this.audienceId = config.audienceId; 
    
		this.callParent();
	},
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	}
	
});
