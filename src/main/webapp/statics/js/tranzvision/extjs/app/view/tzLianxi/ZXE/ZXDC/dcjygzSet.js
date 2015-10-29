Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzSet', {
            extend: 'Ext.grid.Panel',
            requires: [
                'Ext.data.*',
                'Ext.grid.*',
                'Ext.util.*',
                'Ext.toolbar.Paging',
                'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzModel',
        'KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'dcjygzSet',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'dcjygz',
    style:"margin:8px",
    multiSelect: true,
    title: '规则列表',
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
            var tzParams = '{"ComID":"TZ_ZXDC_JYGZ_COM","PageID":"TZ_DCJYGZ_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }}]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'querydcJygz'},"-",
            {text:"新增",tooltip:"新增规则",iconCls:"add",handler:'adddcJygz'},"-",
            {text:"编辑",tooltip:"编辑选中的规则",name:"toolbarEdit",iconCls:"edit",handler:'editdcJygz'},"-",
            {text:"删除",tooltip:"删除选中的规则",iconCls:"remove",handler:'deletedcJygz'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.tzLianxi.ZXE.ZXDC.dcjygzStore();
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
                    {iconCls: 'edit',tooltip: '编辑',handler:'editdcJygz'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deletedcJygz'}
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

