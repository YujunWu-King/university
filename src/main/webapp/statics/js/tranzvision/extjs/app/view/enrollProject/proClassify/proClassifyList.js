Ext.define('KitchenSink.view.enrollProject.proClassify.proClassifyList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollProject.proClassify.proClassifyModel',
		'KitchenSink.view.enrollProject.proClassify.proClassifyStore',
		'KitchenSink.view.enrollProject.proClassify.proClassifyController'
    ],
    xtype: 'proClassifyMg',
	controller: 'proClassifyController',
	reference: 'proClassifyLIstGridPanal',
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmfldy","项目分类列表"),
	header:false,
	frame: true,
    dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.query","查询"),iconCls: "query",handler:"searchProTypeList"},'-',
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.add","新增"),iconCls: "add",handler:"addNewPrjType"},'-',
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.edit","编辑"),iconCls:"edit",handler:'editPrjTypeDfn'},'->',
				/*{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.delete","删除"),iconCls: "remove",handler:"onDeleteBat"}*/
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
				{minWidth:80,text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.close","关闭"),iconCls:"close",handler: 'closeProList'}]
		}
	],
	plugins: [
		Ext.create('Ext.grid.plugin.CellEditing',{
			clicksToEdit: 1
		})
	],
    initComponent: function () {    
		var store = new KitchenSink.view.enrollProject.proClassify.proClassifyStore();
		var store_trans=new KitchenSink.view.common.store.appTransStore("TZ_PRJ_TYPE_STATUS");
		store_trans.load();
		this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
		
        Ext.apply(this, {
		   plugins: [this.cellEditing],
           columns: [{ 
                text: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.proTypeId","项目分类编号"),
                sortable: true,
                dataIndex: 'proTypeId',
				width: 150
            },{
                text: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.proTypeName","分类名称"),
                sortable: true,
                dataIndex: 'proTypeName',
			    readOnly: true,
                width: 200
            },{
                text: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.proTypeDesc","分类描述"),
                sortable: true,
                dataIndex: 'proTypeDesc',
                minWidth: 200,
			    readOnly: true,
				//enableColumnHide: false,
				flex: 1
            },{
			   //text: '有效状态',
			   text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.proTypeStatus","有效状态"),
			   sortable: true,
			   dataIndex: 'proTypeStatus',
			   readOnly: true,
			   width: 120,
			   renderer : function(value, metadata, record) {

					var index = store_trans.find('TValue',value);
					if(index!=-1){
						return store_trans.getAt(index).data.TSDesc;
					}
					return record.get('');
			   }
		   },{
			   xtype: 'actioncolumn',
			   menuDisabled: true,
               sortable: false,
			   width:60,
			   align: 'center',
			   items:[
				   {
					   iconCls: 'edit',
					   tooltip: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.edit","编辑"),
					   handler: 'onEditCurrRow'
				   }
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