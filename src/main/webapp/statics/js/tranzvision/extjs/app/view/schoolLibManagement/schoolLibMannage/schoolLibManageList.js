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
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageController',
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageModel',
        'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageStore'
    ],
    xtype: 'schoolMgList',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'schoolMgConter',
    style:"margin:8px",
    multiSelect: true,
    title:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.schoolLibMg","院校库管理") ,
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
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.save","保存"),iconCls:"save",handler:'saveResSets'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ensure","确定"),iconCls:"ensure",handler:'ensureResSets'},
            {minWidth:80,text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.close","关闭"),iconCls:"close",handler:'closeResSets'}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.query","查询"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.querydata","查询数据"),iconCls:"query",handler:'queryResSet'},"-",
            {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.add","新增"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.adddata","新增数据"),iconCls:"add",handler:'addResSet'},"-",
            {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.edit","编辑"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.editdata","编辑数据"),iconCls: "edit",handler:'editResSet'},"-",
            {text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.remove","删除"),tooltip:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.removedata","删除选中的数据"),iconCls:"remove",handler:'deleteResSets'}
        ]
    }],
    initComponent: function () {
       // var store = new KitchenSink.view.basicData.resData.resource.resourceSetStore();
        Ext.apply(this, {
            columns: [{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.OrganizationID","机构代码") ,
                dataIndex: 'resSetID',
                width: 300
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ZHSName","中文名称"),
                dataIndex: 'resSetDesc',
                minWidth: 160
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.ENGName","英文名称"),
                dataIndex: 'resSetDesc',
                minWidth: 160
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.country","国家"),
                dataIndex: 'resSetDesc'
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.maindepartment","主管部门"),
                dataIndex: 'resSetDesc',
                minWidth: 160
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.city","所在城市"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.Level","办学层次"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.attribute","属性"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.dec","备注"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.State","州"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },,{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.type","类型"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
                flex:1
            },,{
                text:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.hemisphere","所在半球"),
                dataIndex: 'resSetDesc',
                minWidth: 160,
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
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});

