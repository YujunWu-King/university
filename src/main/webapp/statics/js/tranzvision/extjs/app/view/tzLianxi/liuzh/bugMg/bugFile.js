Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.bugFile', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.liuzh.bugMg.bugStore',
        'KitchenSink.view.tzLianxi.liuzh.bugMg.bugController'
    ],
    xtype: 'wy-projects',
    controller:'bugController',
    stateful: true,
    collapsible: true,
    multiSelect: true,
    style:"margin:8px",
    height: 350,
    title: 'Bug管理',
    viewConfig: {
        enableTextSelection: true
    },


    initComponent: function () {
        var items,columns;
        var response = Ext.Ajax.request({
            url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
            params: {params:'{"type":"getRole"}'},
            async:false
        });
        var store = new KitchenSink.view.tzLianxi.liuzh.bugMg.bugStore();
        this.store=store;
        console.log(response.responseText);
        var oprRole=response.responseText.match(/role=([1,0])/)[1];
        //var oprRole = 1;
        items=oprRole=='0'?[
            {text:'查询',tooltip:'检索bug数据',iconCls:"query",handler:'searchBug'},"-",
            {text: "新增",tooltip: "新增bug数据", iconCls:"add", handler: 'addNewBug'},"-",
            {text: "查看",tooltip: "查看bug数据", iconCls:"view", handler: 'viewBug'},"-",
            {text: "删除",tooltip: "删除bug数据", iconCls:"remove", handler: 'deleteBug'}
        ]:[
            {text:'查询',oprRole:oprRole,  tooltip:'检索bug数据',iconCls:"query",handler:'searchBug'},"-",
            {text: "查看",oprRole:oprRole, tooltip: "查看bug数据", iconCls:"view", handler: 'viewBug'}
        ];
        Item=oprRole=='0'?
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {text:"保存",handler:'saveRemoveBug'
                    /*handler:function(btn){
                        var values = btn.findParentByType('grid').getStore().getRemovedRecords(),
                            jsonData = {},
                            content = [];
                        if(values && values.length != 0) {
                            for (var i = 0; i < values.length; i++) {
                                content.push(values[i].data);
                            }
                            jsonData.content = content;
                            jsonData.type = 'removeBug';
                            console.log(Ext.encode(jsonData));
                            var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_LIST_STD","OperateType":"removeBug","comParams":{"bugID":"'+BugID+'"}}';
                            Ext.Ajax.request({
                                url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                                params: {
                                    params:Ext.encode(jsonData)
                                },
                                success: function(){
                                    btn.findParentByType('grid').getStore().reload();
                                }
                            })
                        }

                    }*/
                }
            ]
        }:null;
        columns = oprRole=='0'?[{
            width:1
        },{
            text:'编号',
            width:'10%',
            align:'center',
            dataIndex:'BugID'
        },{
            text:'说明',
            width:'50%',
            align:'center',
            dataIndex:'name'
        },{
            text:'责任人',
            width:'220',
            align:'center',
            dataIndex:'responsableOprID'
        },{
            text:'期望解决日期',
            width:'410',
            align:'center',
            dataIndex:'espectDate'
        },{
            text:'状态描述',
            width:'220',
            align:'center',
            dataIndex:'status',
            renderer:function(value){
                if(value==0)
                        return '新建';
                if(value==1)
                        return '已分配';
                if(value==2)
                        return '已修复';
                if(value==3)
                        return '成功关闭';
                if(value==4)
                        return '重新打开';
                if(value==5)
                        return '取消';
            }
        },{
            text:'操作',
            width:'200',
            xtype: 'actioncolumn',
            align:'center',
            items: [
                {iconCls: 'edit', tooltip: '修改',handler:'editBug',action:'0'},"-",
                {iconCls: 'remove', tooltip: '删除',
                    handler: function(view, rowIndex){
                        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                            if(btnId == 'yes'){
                                store.removeAt(rowIndex);

                            }
                        },this);
                    }}
            ]
        }]:[{
            width:1
        },{
            text:'编号',
            width:'300',
            align:'center',
            dataIndex:'BugID'
        },{
            text:'说明',
            flex:1,
            align:'center',
            dataIndex:'name'
        },{
            text:'责任人',
            width:'200',
            align:'center',
            dataIndex:'responsableOprID'
        },{
            text:'期望解决日期',
            width:'400',
            align:'center',
            dataIndex:'espectDate'
        },{
            text:'状态描述',
            width:'200',
            align:'center',
            dataIndex:'status',
            renderer:function(value){
                if(value==0)
                    return '新建';
                if(value==1)
                    return '已分配';
                if(value==2)
                    return '已修复';
                if(value==3)
                    return '成功关闭';
                if(value==4)
                    return '重新打开';
                if(value==5)
                    return '取消';
            }
        }];
        Ext.apply(this,{
            dockedItems:[
                {
                    xtype:"toolbar",
                    items: items
                },
                Item
           ],
            selModel: {
                type: 'checkboxmodel'
            },
            columns:columns,

            bbar: {
                flex:1,
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 10,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }});
       /*function formatStatus(value){
            switch(value){
                case 0 :
                    return '新建';
                case 1 :
                    return '已分配';
                case 2:
                    return '已修复';
                case 3:
                    return '成功关闭';
                case 4:
                    return '重新打开';
                case 5:
                    return '取消';
            }
        }*/
        this.callParent();
    }
});


