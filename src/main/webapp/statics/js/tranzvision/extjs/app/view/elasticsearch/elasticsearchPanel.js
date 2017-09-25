Ext.define('KitchenSink.view.elasticsearch.elasticsearchPanel',{
    extend:'Ext.panel.Panel',
    xtype:'elasticsearchPanel',
    controller:'elasticsearchController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.elasticsearch.elasticsearchController'
    ],
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'全文检索',
    frame:true,
    dockedItems:[{
        xtype:'toolbar',
        items:[{
            text:'创建索引',iconCls:'add',handler:'addIndex'
        }]
    }],
    initComponent: function () {

        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout:{
                    type:'vbox',
                    align:'stretch'
                },
                border:false,
                bodyPadding:10,
                items:[{
                    xtype:'fieldcontainer',
                    layout:'hbox',
                    items:[{
                        flex:1,
                        xtype:'textfield',
                        name:'searchText'
                    },{
                        width:120,
                        xtype:'button',
                        text:'查询',
                        margin:'0 0 0 5',
                        handler:'searchArticle'
                    }]
                },{
                    xtype:'panel',
                    flex:1,
                    html:''
                }]
            }]
        });

        this.callParent();
    },
    buttons:[{
        text: "关闭",
        iconCls: "close",
        handler: 'closeElastic'
    }]
});