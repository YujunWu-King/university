Ext.define('KitchenSink.view.security.role.roleList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.role.roleModel',
        'KitchenSink.view.security.role.roleStore',
        'KitchenSink.view.security.role.roleController'
    ],
    xtype: 'roleMg',
    controller: 'roleMg',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '角色信息列表',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[
    {
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveRoles"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureRoles'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeRoles'}]
		},
        {
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryRole'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addRole'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editRole'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteRoles'}
		]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.security.role.roleStore();
        Ext.apply(this, {
            columns: [{ 
                text: '角色名称',
                dataIndex: 'roleName',
				width: 260
            },{
                text: '描述',
                sortable: true,
                dataIndex: 'roleDesc',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:40,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editCurrRole'},
			   	  {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrRole'}
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
