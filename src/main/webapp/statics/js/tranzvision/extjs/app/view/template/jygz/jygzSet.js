Ext.define('KitchenSink.view.template.jygz.jygzSet', {
            extend: 'Ext.grid.Panel',
            requires: [
                'Ext.data.*',
                'Ext.grid.*',
                'Ext.util.*',
                'Ext.toolbar.Paging',
                'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.jygz.jygzModel',
        'KitchenSink.view.template.jygz.jygzStore',
        'KitchenSink.view.template.jygz.jygzController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'jygzSet',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'jygz',
    style:"margin:8px",
    multiSelect: true,
    title: '规则列表',
    reference:'jygzSet',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:function(btn){
            var grid = btn.findParentByType("grid");
            var store = grid.getStore();
            var removeJson = "";
            var removeRecs = store.getRemovedRecords();
            for(var i=0;i<removeRecs.length;i++){
                if(removeJson == ""){
                    removeJson = Ext.JSON.encode(removeRecs[i].data);
                }else{
                    removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
                }
            }
            if(removeJson != ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                return;
            }
            var tzParams = '{"ComID":"TZ_JYGZ_COM","PageID":"TZ_JYGZ_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }},
        {minWidth:80,text:Ext.tzGetResourse("TZ_JYGZ_COM.TZ_JYGZ_LIST_STD.ensure","确认"),iconCls:"ensure",handler:"ensureJygzSet"},
        {minWidth:80,text:Ext.tzGetResourse("TZ_JYGZ_COM.TZ_JYGZ_LIST_STD.close","关闭"),iconCls:"close",handler:function(btn){
            var grid = btn.findParentByType("grid");
            grid.close();
        }
        }]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryJygz'},"-",
            {text:"新增",tooltip:"新增规则",iconCls:"add",handler:'addJygz'},"-",
            {text:"编辑",tooltip:"编辑选中的规则",name:"toolbarEdit",iconCls:"edit",handler:'editJygz'},"-",
            {text:"删除",tooltip:"删除选中的规则",iconCls:"remove",handler:'deleteJygz'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.template.jygz.jygzStore();
        Ext.apply(this, {
            columns: [{
                text: '规则编号',
                dataIndex: 'jygzID',
                width:100
            },{
                text: '校验规则名称',
                dataIndex: 'jygzName',
                minWidth:140
            },{
                text: '类名称',
                dataIndex: 'jygzClssName',
                minWidth:100,
                flex:1
            },{
                text: '服务端校验',
                dataIndex: 'jygzFwdJy',
                minWidth:100,
                flex:1
            },{
                text: '提示信息',
                dataIndex: 'jygzTsxx',
                minWidth:100,
                flex:1
            },{
                text: '英文提示信息',
                dataIndex: 'jygzEnTsxx',
                minWidth:100,
                flex:1
            },{
                text: '状态',
                dataIndex: 'jygzState',
                width:70,
                renderer:function(v){
                    if(v=='N'){
                        return "失效";
                    }else if(v=='Y'){
                        return "生效";
                    }
                }
            },{
                text: '操作',
                menuDisabled: true,
                sortable: false,
                width:60,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editJygz'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteJygz'}
                ]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});

