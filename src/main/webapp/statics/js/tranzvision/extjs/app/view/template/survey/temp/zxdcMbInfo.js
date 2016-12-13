Ext.define('KitchenSink.view.template.survey.temp.zxdcMbInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.survey.temp.zxdcMbModel',
        'KitchenSink.view.template.survey.temp.zxdcMbController',
        'KitchenSink.view.template.survey.temp.zxdcMbListStore'
    ],
    xtype: 'zxdcMbListInfo',
    controller: 'zxdcMbController',
    reference:'zxdcMbListInfo',
    store: {
        type: 'zxdcMbListStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '问卷模板管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        items:[
            {text:"查询",iconCls:"query",handler:'findWjmb'},'-',
            {text:"新增",iconCls:"add",handler:'addWjmb'},'-',
            {text:'删除',iconCls:"remove",handler:'deleteWjmb'},'->'
        ]
    },{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"mbInfoSave"}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.template.survey.temp.zxdcMbListStore();
        Ext.apply(this, {
            columns: [
               // new Ext.grid.RowNumberer() ,
                {
                    text:Ext.tzGetResourse("TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBGL_STD.TZ_APP_TPL_ID","模板ID"),
                    sortable: true,
                    dataIndex: 'TZ_APP_TPL_ID',
                    hidden   : true
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBGL_STD.TZ_APP_TPL_MC","模板名称"),
                    sortable: true,
                    dataIndex: 'TZ_APP_TPL_MC',
                    width: 320,
                    allowBlank:false,
                    flex: 1
                },{text: '状态',
                    dataIndex: 'TZ_EFFEXP_ZT',
                    width:70,
                    renderer:function(v) {
                        if (v == 'N') {
                            return "失效";
                        } else if (v == 'Y') {
                            return "生效";
                        }
                      }
                    },
                {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    width:100,
                    align: 'center',
                    xtype: 'actioncolumn',
                    //flex: 1,
                    items:[
                        {iconCls: 'edit',tooltip: '编辑',handler:'editWjmb'},
                        {iconCls:'copy',tooltip:'复制',handler:'copyWjmb'},
                        {iconCls: 'preview',tooltip:'预览',handler:'previewWjmb'},
                        {iconCls: 'set',tooltip: '设置',handler:'setWjmb'},
                        {iconCls:'import',tooltip:'逻辑',handler:'onLogicalSet'}
                    ]
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

