Ext.define('KitchenSink.view.tzLianxi.ldd.wjdc.wjdcInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ldd.wjdc.wjdcModel',
        'KitchenSink.view.tzLianxi.ldd.wjdc.wjdcController',
        'KitchenSink.view.tzLianxi.ldd.wjdc.wjdcStore',
        'tranzvision.extension.grid.Exporter'

    ],
    xtype: 'wjdcInfo',
    controller: 'wjdcController',
    reference:'wjdcInfo',
    store: {
        type: 'wjdcStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    plugins:[
        {ptype: 'gridexporter'}
    ],
    style:"margin:8px",
    multiSelect: true,
    title: '调查问卷管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'findDcwj'},'-',
            {text:"新增",tooltip:"新增",iconCls:"add",handler:'addDcwj'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deleteWjdc'},'-',
            {text:'发布',tooltip:'发布',iconCls:"publish",handler:'publishWjdc'},'->',
            {
                xtype:'splitbutton',
                text:'更多操作',
                iconCls:  'list',
                glyph: 61,
                menu:[
                    {
                        text: '下载数据',
                        handler: 'downloadDcwj'
                    }]
            }
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.tzLianxi.ldd.wjdc.wjdcStore();
        Ext.apply(this, {
            columns: [
                new Ext.grid.RowNumberer() ,{
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_ID","问卷ID"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_ID',
                    hidden   : true
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJBT","问卷标题"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJBT',
                    width: 320,
                    flex: 1
                },{text: '状态',
                    dataIndex: 'TZ_DC_WJ_ZT',
                   // width:70,
                    renderer:function(v) {
                        if (v == '0') {
                            return "未开始";
                        } else if (v == '1') {
                            return "进行中";
                        }else if(v=='2'){
                            return "暂停";
                        }else{
                            return "已结束";
                        }
                      }
                    },
                {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                  //  width:100,
                    align: 'center',
                    xtype: 'actioncolumn',
                    //flex: 1,
                    items:[
                        {iconCls:'edit',toolotip:'编辑',handler:'editWjdc'},
                        {iconCls: 'set',tooltip: '设置',handler:'setWjdc'},
                       // {iconCls: 'pass',tooltip: '使用',handler:'useWjdc'}, //没有使用的图标，暂用通过来代替
                        {iconCls:'copy',tooltip:'复制',handler:'copyWjdc'}
                    ]
                },  {
                    menuDisabled: true,
                    sortable: false,
                    text: '报表',
                    align: 'center',
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls:'word',tooltip:'进度报表',handler:'jinDuBB'},
                        {iconCls: 'word',tooltip: '频数报表',handler:'pinShuBB'},
                        {iconCls:'word',tooltip:'交叉报表',handler:'jiaoChaBB'}
                    ]
                }/*,{
                    text: '报表',
                    width:100,
                    align: 'center',
                    xtype: 'splitbutton',
                   // flex: 1,
                    iconCls:  'list',
                    glyph: 61,
                    menu:[
                        {
                            text:'进度报表',
                            handler:'jinDuBB'
                        },{
                            text:'频数报表',
                            handler:'pinShuBB'
                        },{
                           text:'交叉报表',
                           handler:'jiaoChaBB'
                        }
                    ]
                }*/],
            buttons:[{
                text: '保存',
                handler:'wjdcInfoSave',
                iconCls:'save' //页面保存按钮
            }],
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
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});

