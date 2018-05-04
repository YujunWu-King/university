Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewStore',
        'KitchenSink.view.enrollmentManagement.interviewGroup.interviewController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormAuditing',
    columnLines: true,
    controller: 'appFormInterview',
    name:'appFormInterview',
    /*style:"margin:8px",*/
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicationFormAuditing_s","面试现场分组"),
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
                text:Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_CLASS_STD.query","查询"),
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
                text:Ext.tzGetResourse("TZ_MSXCFZ_COM.TZ_MSGL_CLASS_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    initComponent: function () {
		var store = new KitchenSink.view.enrollmentManagement.interviewGroup.interviewStore(),
			applyStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_BMGL_APPLY_STATUS");
		
		applyStatusStore.on('load',function(){
			store.load();
		});
        
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyDirection","报考名称方向"),
                minWidth:120,
                width:150,
                dataIndex:'className',
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applyBatch","申请批次"),
                dataIndex: 'batchName',
                width:180
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicantingNumber","已报名人数"),
                dataIndex: 'applicantingNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.submittedNumber","已提交人数"),
                dataIndex: 'submittedNumber',
                width:110
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.passNumber","初审通过人数"),
                dataIndex: 'passNumber',
                width:110
            },{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.ds","面试分组"),
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewApplicants"
                }],
                width:130
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

