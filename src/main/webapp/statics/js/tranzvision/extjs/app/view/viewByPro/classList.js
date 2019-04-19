Ext.define('KitchenSink.view.viewByPro.classList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.viewByPro.classListStore',
        'KitchenSink.view.viewByPro.classListController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormAuditing',
    columnLines: true,
    controller: 'classList',
    name:'classList',
    /*style:"margin:8px",*/
    multiSelect: true,
    title: "班级查看",
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
        xtype:"toolbar",
        items:[
            {
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.query","查询"),
                iconCls:"query",
                handler:'queryClass'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    initComponent: function () {
		var store = new KitchenSink.view.viewByPro.classListStore(),
			applyStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_BMGL_APPLY_STATUS");
		
		/*applyStatusStore.on('load',function(){
			store.load();
		});*/
        
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.applyDirection","班级名称"),
                minWidth:180,
                dataIndex:'className',
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.applyBatch","批次名称"),
                dataIndex: 'batchName',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.admissionDate","入学日期"),
                dataIndex: 'admissionDate',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.applicantingNumber","已报名人数"),
                dataIndex: 'applicantingNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.submittedNumber","已提交人数"),
                dataIndex: 'submittedNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.passNumber","初审通过人数"),
                dataIndex: 'passNumber',
                width:110
            },{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.viewApplicants","查看报考学生"),
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewApplicants"
                }],
                width:130
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "面试评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSet","设置评审规则"),handler:'setInterviewReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsPeople","查看考生名单"),handler:'viewInterviewStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSchedule","评审进度管理"),handler:'msReviewScheduleMg'}
                ]
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "分数统计",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                	{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.autoScreen","自动初筛"),handler:'automaticScreen'}
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

