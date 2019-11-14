Ext.define('KitchenSink.view.template.syncConfig.syncConfigList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.template.syncConfig.syncConfigModel',
		'KitchenSink.view.template.syncConfig.syncConfigStore',
		'KitchenSink.view.template.syncConfig.syncConfigController'
    ],
    xtype: 'syncConfigMg',
	controller: 'syncConfigController',
	reference: 'syncConfigListGridPanal',
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.bmbtbpz","报名表同步配置"),
	header:false,
	frame: true,
    dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.query","查询"),iconCls: "query",handler:"searchList"},'-',
				{text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.add","新增"),iconCls: "add",handler:"addNewSyncConfig"},'-',
				{text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.edit","编辑"),iconCls:"edit",handler:'editSyncConfigDfn'},'-',
				{text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.delete","删除"),iconCls: "remove",handler:"deleteProTmp"}
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
				{minWidth:80,text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.close","关闭"),iconCls:"close",handler: 'closeProList'}]
		}
	],
	plugins: [
		Ext.create('Ext.grid.plugin.CellEditing',{
			clicksToEdit: 1
		})
	],
    initComponent: function () {    
		var store = new KitchenSink.view.template.syncConfig.syncConfigStore();
		var store_trans=new KitchenSink.view.common.store.appTransStore("TZ_IS_ENABLE");
		store_trans.load();

		this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
		
        Ext.apply(this, {
		   plugins: [this.cellEditing],
           columns: [{ 
                text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_ID","同步配置ID"),
                hidden: true,
                dataIndex: 'TZ_APPSYNC_ID',
				width: 150
            },{
                text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_DESC","同步配置描述"),
                sortable: true,
                dataIndex: 'TZ_APPSYNC_DESC',
			    readOnly: true,
                width: 200
            },{
                text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_SYNC_TABLE","同步配置表"),
                sortable: true,
                dataIndex: 'TZ_SYNC_TABLE',
                minWidth: 200,
			    readOnly: true,
				//enableColumnHide: false,
				flex: 1
            },{
                text: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_SYNC_FILED","同步字段"),
                sortable: true,
                dataIndex: 'TZ_SYNC_FILED',
                minWidth: 200,
			    readOnly: true,
				//enableColumnHide: false,
				flex: 1
            },{
			   text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_IS_ENABLE","是否启用"),
			   sortable: true,
			   dataIndex: 'TZ_IS_ENABLE',
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
					   tooltip: Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.edit","编辑"),
					   handler: 'onEditCurrRow'
				   },
                   {iconCls: 'remove',text:Ext.tzGetResourse("TZ_APPSYNC_COM.TZ_APPSYNC_STD.delete","删除"),handler:'deleteSelSmtDtTmp'}
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