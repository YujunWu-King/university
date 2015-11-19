Ext.define('KitchenSink.view.template.sitetemplate.SiteManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.sitetemplate.siteController',//调用控制器
        'KitchenSink.view.template.sitetemplate.websiteStore'//json路径
    ],
    xtype: 'SiteManagement',//不能变
    controller: 'siteBasicA',
    /*store: {
     type: 'websiteStore'
     },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '站点模板管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveComRegInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureComRegInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeComRegInfos'}]
    },{
        xtype:"toolbar",
        items:[
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addSite'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSite'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSite'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.template.sitetemplate.websiteStore();
        Ext.apply(this, {
            columns: [{
                text: '站点模板编号',
                dataIndex: 'siteId',
                hidden: true,
                width: 100
            },{
                text: '站点模板名称',
                dataIndex: 'sitetemplateName',
                width: 400
            },{
                text: '站点模板说明',
                sortable: true,
                dataIndex: 'explanation',
                minWidth: 400,
                flex: 1
            },{
                text: '操作',
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[{iconCls: 'edit',tooltip: '编辑',handler:'editSiteInfo'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteList'}
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
