Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReview', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewStore',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewController',
        'KitchenSink.view.common.store.comboxStore'
    ],
    xtype: 'GSMmaterialsReview',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
   controller: 'GSMmaterialsReview',
	style:"margin:8px",
    multiSelect: true,
    title: '光华学院材料评审',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
           {minWidth:80,text:Ext.tzGetResourse("TZ_REVIEW_GSMCL_COM.TZ_GSMCL_LIST_STD.close","关闭"),iconCls:"close",handler:function(btn){
               var grid = btn.findParentByType("grid");
               grid.close();
           }}
        ]
    },{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:"queryClassBatch"},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addClassBatch"},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editClassBatch"}
        ]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewStore(),
            KSPWPSEHNZT = new KitchenSink.view.common.store.appTransStore("TZ_CLPS_STATUS");
			  KSPWPSEHNZT.load();
        Ext.apply(this, {
            columns: [{
                text: '招聘项目编号',
                dataIndex: 'classID',
                width:150,
                align:'center'
            },{
                text: '招聘项目名称',
				dataIndex: 'className',
                minWidth:180,
                sortable:false,
                align:'center',
                flex:2
            },{
                text: '批次编号',
                dataIndex: 'batchID',
                hidden:true
            },{
                text: '批次',
                dataIndex: 'batchName',
                minWidth:150,
                align:'center',
                sortable:false,
                flex:1
            },{
                text: '评审状态',
                dataIndex: 'status',
                width:100,
                align:'center',
                renderer: function (v,grid,record) {
                    var x;
                    v = v?v:'N';
                    if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                        return KSPWPSEHNZT.getAt(x).data.TSDesc;
                    }else{
                        return v;
                    }
                }

            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "操作",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    {iconCls: 'set',tooltip: '设置评审信息',handler:'setReviewRule'}
//                    ,"-",
//                    {iconCls: 'people',tooltip: '查看考生名单',handler:'viewApplicants'}
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

