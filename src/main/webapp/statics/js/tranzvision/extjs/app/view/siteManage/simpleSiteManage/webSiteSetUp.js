Ext.define('KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUp', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpMode',
        'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpStore',
		'KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpController'
    ],
    xtype: 'webSiteSetUp',
	controller: 'webSiteSetUpController',
	/*store: {
        type: 'comStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '招生网站设置',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeWebSiteSetUp'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchWebSiteUp"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addWebSiteInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editWebSiteInfo"}
		]
	}],
    initComponent: function () { 
	    //招生网站设置
	    var store = new KitchenSink.view.siteManage.simpleSiteManage.webSiteSetUpStore();
        Ext.apply(this, {
            columns: [{ 
                text: '站点ID',
                dataIndex: 'siteId',
				width: 240
            },{
                text: '站点名称',
                sortable: true,
                dataIndex: 'siteName',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '站点基本信息',handler: 'editSelWebSiteInfo'},
				  {iconCls: 'edit',tooltip: '用户显示信息项配置',handler: 'editWebSiteReg'},
			   	  {iconCls: 'edit',tooltip: '站点风格选择',handler: 'editSelWebSiteStyle'},
			   	  {iconCls: 'edit',tooltip: '站点页面设置',handler: 'editWebSitePage'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
