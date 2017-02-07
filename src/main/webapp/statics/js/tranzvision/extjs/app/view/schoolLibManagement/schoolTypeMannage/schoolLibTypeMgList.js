/**
 * Created by tzhjl on 2017/1/19.
 */

Ext.define('KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgController',
        'KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgListModel',
        'KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgListStore'
    ],
    xtype: 'schoolMgTypeList',
    title:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.schoolLibList","院校类型管理") ,
    controller: 'schoolMgTypeConter',
    viewConfig: {markDirty: false},
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
	header:false,
	frame: true,
    
    
      dockedItems:[{
                   xtype:"toolbar",
                    items:[
                         {text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.query","查询"),tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.querydata","查询数据"),iconCls:"query",handler:'searchschMgList'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.add","新增"),tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.adddata","新增数据"),iconCls:"add",handler:'addResSet'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.edit","编辑"),tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.editdata","编辑数据"),iconCls: "edit",handler:'editResSet'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.remove","删除"),tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.removedata","删除选中的数据"),iconCls:"remove",handler:'deleteResSets'}
 
                        
                    ]},
                    {xtype:"toolbar",
                    dock:"bottom",
			        ui:"footer",
                    items:['->', 
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.save","保存"),iconCls:"save",handler:'onSaveRemoveData'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.ensure","确定"),iconCls:"ensure",handler:'ensureonSaveRemoveData'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.close","关闭"),iconCls:"close",handler:'closeResSets'}]
                }],
 

    initComponent: function () {
        var store = new KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgListStore();
        Ext.apply(this, {
            
              
                 columns: [{
                text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.typeID","编号") ,
                dataIndex: 'typeID',
                width: 100
            },{
                text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.typeName","院校类型名称"),
                dataIndex: 'typeName',
                minWidth: 300
            },{
                text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.typedec","备注"),
                dataIndex: 'typedec',
                minWidth: 300,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.typeCreatime","创建时间"),
                dataIndex: 'typeCreatime',
                minWidth: 200
                
            },{
                 
                text:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.typeFlag","状态"),
                dataIndex: 'typeFlag',
                minWidth: 100,
                renderer:function(v) {
                		 if (v == 'Y') {
                			 return Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.isY","生效");
                		 } else if (v == 'N') {
                			 return Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.isN","失效");
                		 }
                }  
                
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.edit","编辑"),handler:'editCurrResSet'},
                    {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.edit","删除") ,handler:'deleteCurrResSet'}
                ]
            }
            ],
               
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
                }
            

        });
        this.callParent();
    }
   
});



