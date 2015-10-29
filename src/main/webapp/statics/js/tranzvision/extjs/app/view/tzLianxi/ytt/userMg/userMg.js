Ext.define("KitchenSink.view.tzLianxi.ytt.userMg.userMg",{
    requires : [
        'KitchenSink.view.tzLianxi.ytt.userMg.userStore',
        'KitchenSink.view.tzLianxi.ytt.userMg.userController',
        'Ext.grid.*',
        'Ext.toolbar.Paging'
    ],
    extend : "Ext.grid.Panel",
    bodyStyle:"overflow-y:auto",
    controller:'userController',
    xtype : 'UserManager',
    style:"margin:8px",
    title : "BUG系统人员管理",
    stateful: true,
    collapsible: true,
    plugins:{
        ptype:'cellediting',
        clicksToEdit:1
    },
    frame:true,
    labelAlign: 'left',
    autoLoad: {start: 0, limit: 10},
    store: {
        type: 'userStore'
    },
    multiSelect: true,
    selModel: {
        selType: 'checkboxmodel'
    },
    dockedItems: [{
        xtype: 'toolbar',
        items: [
        {
            text: "查询",
            iconCls: "query",
            handler: 'queryUser'
        },{
            text: "新增",
            iconCls: "add",
            handler: 'addUser'
        }, {
            text: "删除",
            iconCls: 'remove',
            handler: 'deleteUser'
        }]
    },{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"保存",iconCls:"save",handler :"userMgSave"
        }]
    }],
    columns: [
        {header: '机构ID', dataIndex: 'orgID', width: 300, align: 'center',value:Ext.tzOrgID,hidden:true},
        {header: '用户ID', dataIndex: 'userID', width: 300, align: 'center'},
        {header: '用户名', dataIndex: 'realName', flex:1, align: 'center'},
        {
            header: '角色', dataIndex: 'roleName', width: 300, align: 'center',
            editor:{
                xtype:'combobox',
                editable:false,
                align:'center',
                queryMode:'local',
                valueField:'roleID',
                displayField:'roleName',
                store:{
                    fields:['roleID','roleName'],
                    data:[
                        {roleID:0,roleName:'程序员'},
                        {roleID:1,roleName:'管理员'}
                    ]
                }
            },
            renderer:function(v) {
                return v == 0 ? "程序员" : "管理员";
            }
        },
        {
            header: '操作', width:60, xtype: 'actioncolumn', align: 'center',
            items: [{
                iconCls: 'remove',tooltip: '删除',
                handler: function(view, rowIndex){
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        var store = view.findParentByType("grid").store;
                        store.removeAt(rowIndex);
                    }
                },this);
}
            }]
        }
    ],
    bbar: [{
        xtype: 'pagingtoolbar',
        pageSize: 10,
        listeners:{
            afterrender: function(pbar){
                var grid = pbar.findParentByType("grid");
                pbar.setStore(grid.store);
            }
        },
        displayInfo: true,
        displayMsg: '显示{0}-{1}条，共{2}条',
        beforePageText: '第',
        afterPageText: '页/共{0}页',
        emptyMsg: '没有数据显示'
    }]

});