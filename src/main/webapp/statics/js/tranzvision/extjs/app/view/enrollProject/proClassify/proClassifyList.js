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
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmfldy","项目分类定义"),
	header:false,
	frame: true,
    dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.query","查询"),iconCls: "query",handler:"searchProTypeList"},'-',
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.add","新增"),iconCls: "add",handler:"onAddInLastRow"},'-',
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.delete","删除"),iconCls: "remove",handler:"onDeleteBat"}
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',{minWidth:80,text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.save","保存"),iconCls:"save",handler:"onSaveData"}]
		}
	],
	plugins: [
		Ext.create('Ext.grid.plugin.CellEditing',{
			clicksToEdit: 1
		})
	],
    initComponent: function () {    
		var store = new KitchenSink.view.enrollProject.proClassify.proClassifyStore();
		
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
                width: 200,
				editor: {
                    xtype:'textfield'
                }
            },{
                text: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.proTypeDesc","分类描述"),
                sortable: true,
                dataIndex: 'proTypeDesc',
                minWidth: 200,
				//enableColumnHide: false,
				flex: 1,
				editor: {
                    xtype:'textfield'
                }
            },{
			   xtype: 'actioncolumn',
			   menuDisabled: true,
               sortable: false,
			   width:60,
			   align: 'center',
			   items:[
					  	{
							iconCls: 'remove',
							tooltip: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.delete","删除"),
						    scope: this,
                    		handler: this.onRemoveCurrRow
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
    },
	// 行删除
	onRemoveCurrRow: function(grid, rowIndex){
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.confirm","确认"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if (btnId == "yes"){
        		grid.getStore().removeAt(rowIndex);
			}
		});
    }
});