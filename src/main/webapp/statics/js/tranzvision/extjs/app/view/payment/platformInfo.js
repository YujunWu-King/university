Ext.define('KitchenSink.view.payment.platformInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.payment.platformModel',
        'KitchenSink.view.payment.platformController',
       'KitchenSink.view.payment.platformStore',
       'KitchenSink.view.payment.platformEditForm',
       // 'tranzvision.extension.grid.Exporter'

    ],
    xtype: 'platformInfo',
    controller: 'platformController',
    reference:'platformInfo',

    columnLines: true,

    style:"margin:8px",
    multiSelect: true,
    title: '支付平台管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"platformInfoWindowClose"},
               {minWidth:80,text:"保存",iconCls:"save",handler:"platformInfoWindowSave"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'selectPlatform'},'-',
            {text:"新增",tooltip:"新增",iconCls:"add",handler:'addPlatform'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deletePlatform'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.payment.platformStore();
        Ext.apply(this, {
            columns: [
                //new Ext.grid.RowNumberer() , 列表序号
                {
                    text:"支付平台ID",
                    sortable: true,
                    dataIndex: 'platformId',
                    hidden:true,
                    width:400
                },
                {
                    text:"支付平台",
                    sortable: true,
                    dataIndex: 'platformName',
                    width:400
                },
                {
                    text:"启用",
                    sortable: true,
                    dataIndex: 'platformState',
                    width:400,
                    renderer:function(v){
                    	if(v=="Y"){
                    		return "正常";
                    	}
                    	else if(v=="N"){
                    		return "暂停";
                    	}
                    }
                },
                {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    align: 'center',
                    xtype: 'actioncolumn',
                    fit:1,
                    items:[
                        {iconCls:'remove',tooltip:'删除',handler:'deletePlatform'},'-',
                        {iconCls:'edit',tooltip:'编辑',handler:'editPlatform'}
                        ]
              
               }]
        , 
        store:store,
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

