Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjSet', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjModel',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'kjSet',
    columnLines: true,   
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'dckj',
    style:"margin:10px",
    multiSelect: true,
    title: '控件列表',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
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
            var tzParams = '{"ComID":"TZ_ZXDC_KJGL_COM","PageID":"TZ_DCKJGL_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }}]
    },
        {
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryKj'},"-",
            {text:"新增",tooltip:"新增控件",iconCls:"add",handler:'addKj'},"-",
            {text:"编辑",tooltip:"编辑选中的控件",name:"toolbarEdit",iconCls:"edit",handler:'editKj'},"-",
            {text:"删除",tooltip:"删除选中的控件",iconCls:"remove",handler:'deleteKj'}
        ]
    }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.tzLianxi.ZXE.ZXDCKJSZ.dckjStore();
        Ext.apply(this, {
            columns: [{
                text: '控件编号',
                dataIndex: 'kjID',
                width:100
            },{
                text: '控件名称',
                dataIndex: 'kjName',
                minWidth:140
            },{
                text: '控件英文名称',
                dataIndex: 'kjEnName',
                minWidth:140
            },{
                text: 'JS文件路径',
                dataIndex: 'kjJsUrl',
                minWidth:100,
                flex:1
            },{
                text: '图标路径',
                dataIndex: 'kjPtUrl',
                minWidth:100,
                flex:1
            },{
                text: '状态',
                dataIndex: 'kjState',
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
                    {iconCls: 'edit',tooltip: '编辑',handler:'editKj'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteKj'}
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

