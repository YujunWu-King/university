Ext.define('KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugStore',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugController'
    ],
    xtype:'bugManager',
    controller:'bugController',
    stateful: true,
    collapsible: true,
    multiSelect: true,
    height: 350,
    title: 'BUG管理',
    viewConfig: {
        enableTextSelection: true
    },


    initComponent: function () {
        var store = new KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugStore();
        Ext.apply(this,{
            dockedItems:[
                {
                    xtype:"toolbar",
                    items: [
                        {text:'查询',tooltip:'检索bug数据',iconCls:"query",handler:'searchBug'},"-",
                        {text: "新增",tooltip: "新增bug数据", iconCls:"add", handler: 'addNewBug'},"-",
                        {text: "查看",tooltip: "查看bug数据", iconCls:"view", handler: 'editBug'},"-",
                        {text: "删除",tooltip: "删除bug数据", iconCls:"remove", handler: 'deleteBug'}
                    ]
                },
                {
                    xtype:"toolbar",
                    dock:"bottom",
                    ui:"footer",
                    items: ['->',{
                        text:'保存',
                        handler:function(btn){
                            var values = btn.findParentByType('grid').getStore().getRemovedRecords(),
                                jsonData = {},
                                content = [];
                            if(values && values.length != 0) {
                                for (var i = 0; i < values.length; i++) {
                                    content.push(values[i].data);
                                }
                                jsonData.content = content;
                                jsonData.type = 'removeBug';
                                jsonData.manager = 'lzh';
                                console.log(Ext.encode(jsonData));
                                Ext.Ajax.request({
                                    url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                                    params: {params:Ext.encode(jsonData)},
                                    success: function(){
                                        btn.findParentByType('grid').getStore().reload();
                                    }
                                })
                            }

                        }
                    }]
                }
            ],
            selModel: {
                type: 'checkboxmodel'
            },
            multiSelect: true,
            columns:[{
                width:1
            },{
                text:'编号',
                width:'15%',
                align:'center',
                dataIndex:'BugID'
            },{
                text:'说明',
                width:'25%',
                align:'center',
                dataIndex:'name'
            },{
                text:'责任人',
                width:'15%',
                align:'center',
                dataIndex:'responsableOprID'
            },{
                text:'期望解决日期',
                width:'20%',
                align:'center',
                dataIndex:'espectDate'
            },{
                text:'状态描述',
                width:'10%',
                align:'center',
                dataIndex:'status'
                // renderer:formatStatus
            },{
                text:'操作',
                flex:1,
                xtype: 'actioncolumn',
                align:'center',
                items: [
                    {iconCls: 'edit', tooltip: '修改',handler:'editBug',action:'0'},
                    {iconCls: 'remove', tooltip: '删除',
                        handler: function(view, rowIndex){
                            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                                if(btnId == 'yes'){
                                    store.removeAt(rowIndex);

                                }
                            },this);
                        }}
                ]
            }],
            store:store,
            bbar: {
                flex:1,
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 10,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});


