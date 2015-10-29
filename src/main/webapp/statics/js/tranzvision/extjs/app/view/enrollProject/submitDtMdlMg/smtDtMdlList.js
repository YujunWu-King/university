Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlStore',
		'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlController'
    ],
    xtype: 'submitDataModelMg',
	controller: 'submitDataModelMg',
	store: {
        type: 'smtDtMdlStore'
    },
    columnLines: true,
	style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.djzlmxlb","递交资料模型列表"),
    viewConfig: {
        enableTextSelection: true
    },
    reference:"smtDtlist",
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.save","保存"),iconCls:"save",handler:"saveSmtDtTmp"}]
		},{
		xtype:"toolbar",
		items:[
			//{text:"查询",tooltip:"查询数据",iconCls:"query"},"-",
			{text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.add","新增"),iconCls:"add",handler:'addSmtDataModel'}
			//{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTranslate'},"-",
			//{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTranslates'}
		]
	}],
    initComponent: function () { 
	   var store = new KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlStore();
        Ext.apply(this, {
            columns: [/*{ 
                text: '序号',
                dataIndex: 'sqeNum',
				width: 100
            },*/{
			 	xtype: 'rownumberer',
				text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.number","序号")
			},{
                text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.djzlmxmc","递交资料模型名称") ,
				dataIndex: 'smtDtName',
                minWidth: 160,
				flex:1
            },{
                text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.enable","启用"),
				dataIndex: 'smtDtStatus',
                minWidth: 50,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               xtype: 'actioncolumn',
               align:'center',
			   items:[
				  {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.edit","编辑"),handler:'editSelSmtDtTmp'},
			   	  {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.delete","删除"),handler:'deleteSelSmtDtTmp'}
			   ]
            }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
