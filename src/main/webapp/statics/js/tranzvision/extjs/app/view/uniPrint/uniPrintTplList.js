Ext.define('KitchenSink.view.uniPrint.uniPrintTplList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.uniPrint.uniPrintTplController',
        'KitchenSink.view.uniPrint.uniPrintTplStore'
    ],
    alilas: 'widget.uniPrintTplList',
    controller: 'uniPrintTplController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '统一打印模板管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"listSave",name:'save'},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"listSave",name:'ensure'},
            {minWidth:80,text:"关闭",iconCls:"close",handler:
                function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'listSearch'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTpl'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTpl'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTpls'}
        ]
    }],
    initComponent: function(){
        var store = new KitchenSink.view.uniPrint.uniPrintTplStore();
        //是否有效取值
        var ifEffectiveStore = new KitchenSink.view.common.store.appTransStore("TZ_IF_EFFECTIVE");
        Ext.apply(this, {
            columns: [{
                text: '机构编号',
                dataIndex: 'TZ_JG_ID',
                width: 150
            },{
                text: '模板编号',
                dataIndex: 'TZ_DYMB_ID',
                width: 150
            },{
                text: '模板名称',
                dataIndex: 'TZ_DYMB_NAME',
                minWidth: 200,
                flex: 1
            },{
                text: '状态',
                dataIndex: 'TZ_DYMB_ZT',
                width: 200,
                renderer:function(v){
                	if(v){
                		var index = ifEffectiveStore.find('TValue',v,0,false,true,true);
                		if(index>-1){
                			return ifEffectiveStore.getAt(index).get("TSDesc");
                		}
                		return "";
                	}
                }
            },{
                text: '数据导入模板',
                dataIndex: 'TZ_TPL_NAME',
                minWidth: 200,
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editTpl'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteTpl'}
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
