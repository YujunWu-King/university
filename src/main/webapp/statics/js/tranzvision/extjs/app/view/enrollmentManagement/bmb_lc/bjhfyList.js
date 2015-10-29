Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.colorpick.Field',
        'KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyStore',
        'KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyController'
    ],
    xtype: 'bjhfyList',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'bjhfyList',
    style:"margin:8px",
	class_id:'1',
	bmlc_id:'2',
    multiSelect: true,
    title:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.hfysz","回复语设置") ,
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.save","保存"),iconCls:"save",handler:'save_hfy'},
				{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.ensure","确定"),iconCls:"ensure",handler:"ensure_hfy"},
				{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.close","关闭"),iconCls:"close",handler:"close_hfy"}
			]
    },{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.add","新增"),iconCls:"add",handler:'addHfy'},
			{text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.edit","编辑"),iconCls:"edit",handler:'editHfy'},
            {text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.delete","删除"),iconCls:"remove",handler:'deleteHfy'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.bmb_lc.bjhfyStore();
		Ext.tip.QuickTipManager.init(); 
        Ext.apply(this, {
            plugins: {
                ptype: 'cellediting',
                pluginId: 'colorSortCellEditing'
            },
            columns: [{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.color","颜色") ,
                    width:160,
                    bind: '{color}',
                    editable:false,
                    dataIndex: 'colorCode',
                    renderer:function(value){
                        return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
                    }
                },{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.result","结果"),
					dataIndex: 'result_desc',
					minWidth: 180,
					flex:1
				},{
					menuDisabled: true,
					sortable: false,
					align: 'center',
					width: 100,
					xtype: 'actioncolumn',
					items:[
						{iconCls: 'edit',tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.edit","编辑"),handler: 'hfy_edit'},'-',
						{iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.delete","删除"),handler: 'delete_Hfy'}
					]
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
    }
});
