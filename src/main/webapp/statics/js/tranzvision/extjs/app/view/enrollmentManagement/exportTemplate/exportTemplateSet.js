Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateSet', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateStore',
        'KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'exportTemplateSet',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'exportTemplate',
	style:"margin:8px",
    multiSelect: true,
    title: '导出模板列表',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",
            handler:function(btn){
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
            var tzParams = '{"ComID":"TZ_BMGL_DCMB_COM","PageID":"TZ_DCMB_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryExportTemplate'},"-",
            {text:"新增",tooltip:"新增报名表导出模板",iconCls:"add",handler:'addTpl'},"-",
            {text:"编辑",tooltip:"编辑选中的报名表导出模板",name:'toolbarEdit',iconCls:"edit",handler:'editTpl'},"-",
            {text:"删除",tooltip:"删除选中的报名表导出模板",iconCls:"remove",name: 'toolbarDelete',handler:'deleteTpl'}
        ]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateStore();
        Ext.apply(this, {
            columns: [{
                text: '导出模板编号',
                dataIndex: 'tplID',
                width:200
            },{
                text: '导出模板名称',
				dataIndex: 'tplName',
                minWidth:400,
                flex:1
            },{
                text: '模板类型',
                dataIndex: 'tplTypeDesc',
                minWidth:200,
                flex:1
            },{
                text: '模板状态',
                dataIndex: 'tplStatus',
                width:100,
                renderer:function(v){
                    if(v=='I'){
                        return "无效";
                    }else{
                        return "有效";
                    }
                }
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',name: 'actionEdit',handler:'editTpl'},
                    {iconCls: 'remove',tooltip: '删除',name: 'actionDelete',handler:'deleteTpl'}
                ]
            }],
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

