Ext.define('KitchenSink.view.orgmgmt.orgAccount', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.orgmgmt.orgController',
		'KitchenSink.view.orgmgmt.orgModel',
        'KitchenSink.view.orgmgmt.orgListStore'
    ],
    xtype: 'orgMg',
	controller: 'orgMg',
    title:Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.titleOrg","机构列表"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height-buttonHeight-16);
        }
    },
    initComponent: function (){
        var store = new KitchenSink.view.orgmgmt.orgListStore();
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                store: store,
                columnLines: true,
                selModel: {
                    type: 'checkboxmodel'
                },
                style:"margin:8px",
                multiSelect: true,
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryOrgAccount'},
                        {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addOrgAccount'},
                        {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editOrgAccount'}/*,{text:"删除",tooltip:"删除数据",iconCls:"remove",handler:'deleteOrgAccount'}*/
                    ]
                }],
                columns: [{
                    //text: '机构编号',
                    text:Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.orgId","机构编号"),
                    dataIndex: 'orgId',
                    width: 230
                },{
                    //text: '机构名称',
                    text:Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.orgName","机构名称"),
                    sortable: true,
                    dataIndex: 'orgName',
                    flex: 1
                },{
                    //text: '有效状态',
                    text:Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.orgYxState","有效状态"),
                    sortable: true,
                    dataIndex: 'orgYxState',
                    width: 120
                },{
                    menuDisabled: true,
                    sortable: false,
                    width:40,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'edit',tooltip: '编辑',handler:'editOrgAccountOne'}
                    ]
                }],
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    /*store: {
                     type: 'orgListStore'
                     },*/
                    store: store,
                    displayInfo: true,
                    displayMsg: '显示{0}-{1}条，共{2}条',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    emptyMsg: '没有数据显示',
                    plugins: new Ext.ux.ProgressBarPager()
                }

            }]
        });
        this.callParent();
    },
    buttons: [{
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
