Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmFlow', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.bmb_lc.bmLcController',
		'KitchenSink.view.enrollmentManagement.bmb_lc.bmLcModel',
        'KitchenSink.view.enrollmentManagement.bmb_lc.bmLcStore'
    ],
    xtype: 'bmFlow',
	controller: 'bmFlow',
    columnLines: true,
	bj_id: 'NEXT',
	style:"margin:8px",
    multiSelect: true,
    title:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bmlcjgfb","报名流程结果发布") ,
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    initComponent: function () {   
		var store = new KitchenSink.view.enrollmentManagement.bmb_lc.bmLcStore();
        Ext.apply(this, {
            columns: [{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bmlcx_xh","序号"),
					sortable: true,
					dataIndex: 'bmlc_xh',
					width: 150
				},{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bmlc_name","流程名称"),
					sortable: true,
					dataIndex: 'bmlc_name',
					minWidth: 250,
					flex: 1
				},{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfysz","回复语设置"),
					align: 'center',
					groupable: false,
					width: 150,
					renderer: function(v) {
						return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfysz","回复语设置")+'</a>';
					},
					listeners:{
						click:'bmlc_hfySet'
					}
				},{
					text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.fbjg","发布结果"),
					align: 'center',
					groupable: false,
					width: 150,
					renderer: function(v) {
						return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.fbjg","发布结果")+'</a>';
					},
					listeners:{
						click:'bmlc_fbjg'
					}
				},{
					xtype: 'checkcolumn',
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.sffb","是否发布"),
					hidden: true,
					dataIndex: 'bmlc_fb',
					//disabled: false,
					//sortable: true,
					width: 100
				}
			],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }	
        });
        this.callParent();
    },
	dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),iconCls:"save",handler:"onSaveData"},
			{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),iconCls:"ensure",handler:"onFormEnsure"},
			{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.publish","发布"),iconCls:"close",handler:"onFormClose"}
		]
	}]
});
