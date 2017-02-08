/**
 * Created by tzhjl on 2017/1/12.
 */


Ext.define('KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.slider.Widget',
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageController',
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageModel',
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageStore'
    ],
    xtype: 'schoolMgList',
    title:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.schoolLibList","院校库管理") ,
    controller: 'schoolMgConter',
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
                         {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.query","查询"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.querydata","查询数据"),iconCls:"query",handler:'searchschMgList'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.add","新增"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.adddata","新增数据"),iconCls:"add",handler:'addResSet'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.edit","编辑"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.editdata","编辑数据"),iconCls: "edit",handler:'editResSet'},"-",
                         {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.remove","删除"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.removedata","删除选中的数据"),iconCls:"remove",handler:'deleteResSets'}
 
                        
                    ]},
                    {xtype:"toolbar",
                    dock:"bottom",
			        ui:"footer",
                    items:['->', 
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.save","保存"),iconCls:"save",handler:'onSaveRemoveData'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ensure","确定"),iconCls:"ensure",handler:'ensureonSaveRemoveData'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.close","关闭"),iconCls:"close",handler:'closeResSets'}]
                }],
 /*   listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;button height plus panel body padding
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -8);
        }
    },*/
    initComponent: function () {
        var store = new KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageStore();
        Ext.apply(this, {
       
               
                 columns: [{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.OrganizationID","机构代码") ,
                dataIndex: 'orgaID',
                width: 100
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ZHSName","中文名称"),
                dataIndex: 'chinaName',
                minWidth: 100
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ENGName","英文名称"),
                dataIndex: 'engName',
                minWidth: 100
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.country","国家"),
                dataIndex: 'country',
                minWidth: 150
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.maindepartment","主管部门"),
                dataIndex: 'mainDeart',
                minWidth: 100
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.city","所在城市"),
                dataIndex: 'city',
                minWidth: 100
           
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.Level","办学层次"),
                dataIndex: 'level',
                minWidth: 100
             
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.attribute","属性"),
                dataIndex: 'attriBute',
                minWidth: 250
               
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.dec","备注"),
                dataIndex: 'adddec',
                minWidth: 250
            
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.State","洲"),
                dataIndex: 'state',
                minWidth: 100

            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.type","类型"),
                dataIndex: 'type',
                minWidth: 100
                
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.hemisphere","所在半球"),
                dataIndex: 'hemiHere',
                minWidth: 100,
                flex:1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.edit","编辑"),handler:'editCurrResSet'},
                    {iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.edit","删除") ,handler:'deleteCurrResSet'}
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



