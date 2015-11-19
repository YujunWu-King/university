Ext.define('KitchenSink.view.security.menu.funcMenu', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.security.menu.menuController',
        'KitchenSink.view.security.menu.funcMenuStore',
        'tranzvision.extension.grid.column.Link'
    ],

    xtype: 'menuMg',
    controller: 'menuMg',
    /*
     store: {
     type: 'funcMenuStore'
     },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '功能菜单管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveOrgList",name:'save'},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'saveOrgList',name:'ensure'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeOrgList'}]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchMenu'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addFuncMenu'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editFuncMenu'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteFuncMenu'},"-",
            {text:"重新计算功能菜单树节点编号",tooltip:"重新计算功能菜单树节点编号",iconCls:"refresh",handler:'refreshFuncMenu'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.security.menu.funcMenuStore();

        Ext.apply(this, {
            columns: [{
                //text: '机构编号',
                text:Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENULIST_STD.orgId","机构编号"),
                dataIndex: 'orgId',
                width: 160
            },{
                //text: '机构名称',
                text:Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENULIST_STD.orgName","机构名称"),
                sortable: true,
                dataIndex: 'orgName',
                minWidth:350,
                flex:1
            },{
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler: 'editFuncMenuById'},
                    {iconCls: 'delete',tooltip: '删除',handler: 'deleteFuncMenuById'}
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
