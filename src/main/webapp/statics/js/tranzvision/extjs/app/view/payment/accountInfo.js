Ext.define('KitchenSink.view.payment.accountInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.payment.accountModel',
        'KitchenSink.view.payment.accountController',
       'KitchenSink.view.payment.accountStore',
       'KitchenSink.view.payment.accountEditForm',
       // 'tranzvision.extension.grid.Exporter'

    ],
    xtype: 'accountInfo',
    controller: 'accountController',
    reference:'accountInfo',
	selModel: {
       	type: 'checkboxmodel'
    },
    columnLines: true,

    style:"margin:8px",
    multiSelect: true,
    title: '支付账户管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"accountInfoWindowClose"},
               {minWidth:80,text:"保存",iconCls:"save",handler:"accountInfoWindowSave"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'selectAccount'},'-',
            {text:"新增",tooltip:"新增",iconCls:"add",handler:'addAccount'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deleteAccount'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.payment.accountStore();
        Ext.apply(this, {
            columns: [
                //new Ext.grid.RowNumberer() , 列表序号
                {
                    text:"账户号",
                    sortable: true,
                    dataIndex: 'accountId',
                    width:200
                },
                {
                    text:"账户名称",
                    sortable: true,
                    dataIndex: 'accountName',
                    width:200
                },
                 {
                     text:"账户描述",
                     sortable: true,
                     dataIndex: 'accountDescribe',
                     width:200
                 },
                 {
                	 text:"账户Key",
                	 sortable: true,
                	 dataIndex: 'accountKey',
                	 width:200
                 },
                 {  text: '账户状态',
                	 dataIndex: 'accountState',
                	 width:100,
                	 renderer:function(v) {
                		 if (v == 'Y') {
                			 return "正常";
                		 } else if (v == 'N') {
                			 return "暂停";
                		 }
                	 }
                 },
                 {
                     text:"支付平台",
                     sortable: true,
                     dataIndex: 'accountPlatform',
                     width:200
                 },
                {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    align: 'center',
                    xtype: 'actioncolumn',
                    fit:1,
                    items:[
                        {iconCls:'remove',tooltip:'删除',handler:'deleteOneAccount'},'-',
                        {iconCls:'edit',tooltip:'编辑',handler:'editAccount'}
                        ]
              
               }]
        , store:store,
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 10,
            store: store,
            displayInfo: true,
            displayMsg: '显示{0}-{1}条，共{2}条',
            beforePageText: '第',
            afterPageText: '页/共{0}页',
            emptyMsg: '没有数据显示',
            plugins: new Ext.ux.ProgressBarPager()}
        });
        this.callParent();
    }
});

