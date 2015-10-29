Ext.define("KitchenSink.view.tzLianxi.liuzh.lzhBugMg.OprManager",{
    requires : [
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprStore',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprController',
        'Ext.grid.*',
        'Ext.toolbar.Paging'
    ],
    extend : "Ext.grid.Panel",
    bodyStyle:"overflow-y:auto",
    controller:'oprController',
    xtype : 'OprManager',
    style:"margin:8px",
    title : "BUG管理系统人员管理",
    plugins:{
        ptype:'cellediting',
        clicksToEdit:1
    },
    frame:true,
    labelAlign: 'left',
    autoLoad: {start: 0, limit: 10},
    store: {
        type: 'oprStore'
    },
    multiSelect: true,
    selModel: {
        selType: 'checkboxmodel'
    },
    dockedItems: [{
        xtype: 'toolbar',
        items: [{
            iconCls: "query",
            text: "查询",
            handler: function (btn) {
                var parBtn =btn;
                Ext.create('Ext.window.Window', {
                    title: '账户查找',
                    modal: true,
                    layout: 'fit',
                    closable: false,
                    items: [{
                        xtype: 'form',
                        border: false,
                        items: [
                            {xtype: 'textfield',name:'oprID', style: 'margin:10px', fieldLabel: '账户ID'},
                            {xtype: 'textfield',name:'name', style: 'margin:10px', fieldLabel: '账户名称'}
                        ],
                        buttons: [
                            {
                                text: '搜索',
                                handler: function (btn) {
                                    var window = btn.findParentByType("window");
                                    var form = window.child("form").getForm();
                                    var oprID = form.findField("oprID").getValue();
                                    var name = form.findField("name").getValue();

                                    var store = parBtn.findParentByType("grid").store;
                                    store.proxy.extraParams={
                                        params:'{"type":"queryUser","oprID":"'+oprID+'","name":"'+name+'"}'
                                    };
                                    store.load({
                                        callback:function(response){
                                            window.close();
                                        }
                                    })
                                }
                            },
                            {
                                text: '取消',
                                handler: function (btn) {
                                    btn.findParentByType('window').close();
                                }
                            }
                        ]
                    }]
                }).show();


            }
        }, {
            text: "新增",
            iconCls: "add",
            handler: 'addOpr'
        }, {
            text: "删除",
            iconCls: 'remove',
            handler: 'confirmDelete'
        }]
    },{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"保存",iconCls:"save",handler :"saveUser"
        }]
    }],
    columns: [
        {header: '用户ID', dataIndex: 'oprID', width: '25%', align: 'center'},
        {header: '用户名', dataIndex: 'name', width: '25%', align: 'center'},
        {
            header: '角色', dataIndex: 'role', width: '25%', align: 'center',
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
                        {roleID:0,roleName:'管理员'},
                        {roleID:1,roleName:'程序员'}
                    ]
                }
            },
            renderer:function(v) {
                return v == 0 ? "管理员" : "程序员";
            }
        },
        {
            header: '操作', flex: 1, xtype: 'actioncolumn', align: 'center',
            items: [{
                iconCls: 'remove',
                handler: function(btn,index){
                    Ext.MessageBox.confirm('提示','确认要删除所选记录吗吗?',function(id) {
                        switch(id) {
                            case 'yes':
                                btn.findParentByType('grid').getStore().removeAt(index);
                                /*  var removed = btn.findParentByType('grid').getStore().getRemovedRecords(),
                                 JSONData = [],
                                 x = removed.length-1;
                                 for(;x>=0;x--){
                                 JSONData[x] = {};
                                 JSONData[x].oprID = removed[x].data.oprID;
                                 JSONData[x].name = removed[x].data.name;
                                 }
                                 JSONData.type = 'DP';
                                 btn.findParentByType('grid').getStore().reload();
                                 console.log(JSON.stringify(JSONData));*/
                                break;
                            case 'no':
                                break;
                        }
                    })
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