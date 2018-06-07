Ext.define('KitchenSink.view.clueManagement.clueManagement.personChooseWindow',{
    extend:'Ext.window.Window',
    xtype:'personChooseWindow',
    controller:'clueDealWithController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.personChooseWindowStore',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    title:'选择责任人',
    width:700,
    height:400,
    modal:true,
    layout:{
        type:'fit'
    },
    constructor:function(config) {
        this.selModel = config.selModel;
        this.callback = config.callback;
        this.callParent();
    },
    initComponent:function(btn) {
    	var me = this;
    	var selMode = 'SINGLE';
    	if(me.selModel == "M") selMode = 'MULTI';
    	
        var store = new KitchenSink.view.clueManagement.clueManagement.personChooseWindowStore();

        Ext.apply(this,{
            items:[{
                xtype:'grid',
                autoHeight:true,
                columnLines:true,
                frame:true,
                style:'border:0',
                selModel:{
                    type: 'checkboxmodel',
					mode: selMode
                },
                store:store,
                dockedItems:[{
                    xtype:'toolbar',
                    items:[{
                        text:'查询',iconCls:'query',tooltip:'查询',handler:'queryPerson'
                    }]
                }],
                columns:[{
                    text:'登录账号',
                    dataIndex:'dlzhId',
                    minWidth:120
                },{
                    text:'用户名称',
                    dataIndex:'name',
                    minWidth:160
                },{
                    text:'手机号码',
                    dataIndex:'mobile',
                    minWidth:120
                },{
                    text:'电子邮箱',
                    dataIndex:'email',
                    minWidth:180,
                    flex:1
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 20,
                    store: store,
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });

        this.callParent();
    },
    buttons:[{
        text:'确定',iconCls:'ensure',handler:'personChooseEnsure'
    },{
        text:'关闭',iconCls:'close',handler:'onClueDealWithClose'
    }]
});
