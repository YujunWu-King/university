Ext.define('KitchenSink.view.basicData.systemVar.systemVarList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.systemVar.systemVarModel',
		'KitchenSink.view.basicData.systemVar.systemVarStore',
		'KitchenSink.view.basicData.systemVar.systemVarController'
    ],
    xtype: 'sysVarMg',
    viewConfig: {markDirty: false},
	controller: 'systemVarMg',
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: '系统变量管理',
	header:false,
	frame: true,
    dockedItems:[
		{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchSysVarList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addData"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editSelData"},"-",
			{text:"删除",tooltip:"批量删除数据",iconCls:"remove",handler:"deleteSelData"}
		]},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
                {minWidth:80,text:"保存",iconCls:"save",handler:"onSaveRemoveData"},
                {minWidth:80,text:"确定",iconCls:"ensure",handler:"onEnsureRemoveData"},
                {minWidth:80,text:"关闭",iconCls:"close",handler:"onCloseRemoveData"}
            ]
		}
	],
    initComponent: function () {
		var store = new KitchenSink.view.basicData.systemVar.systemVarStore({
            listeners:{
                load:function(store){
                    /*
                    for(var i=0;i<store.getCount();i++){
                        var rec = store.getAt(i);
                        if(rec.get('isValid')=='N'){
                            rec.set('isValid',false);
                        }else{
                            rec.set('isValid',true);
                        }
                    }
                    */
                }
            }
        });
        Ext.apply(this, {
            columns: [{ 
                //text: '系统变量ID',
				text: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.systemVarId","系统变量ID"),
				draggable:false,//姓名不可拖动
                dataIndex: 'systemVarId',
				width: 200
            },{
				text: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.systemVarName","系统变量名称"),
                sortable: true,
				//menuDisabled:true,
                dataIndex: 'systemVarName',
                minwidth: 250,
				flex:1
            },{
                text: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.isValid","是否有效"),
                sortable: true,
                dataIndex: 'isValid',
                width: 120,
                renderer:function(value){
                    if(value=='Y'){
                        return '是';
                    }
                    return "否";
                }
            },
            /*{
                    xtype: 'checkcolumn',
                    text: Ext.tzGetResourse("TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.isValid","是否有效"),
                    sortable: true,
                    disabled: true,
                    dataIndex: 'isValid',
                    width: 120
            },*/
            {
               //menuDisabled: true,
			   text:'操作',
               sortable: false,
			   menuDisabled:true,
			   draggable:false,
			   width:80,
               xtype: 'actioncolumn',
			   align:'center',
			   items:[
				 {text: '编辑',iconCls: 'edit',tooltip: '编辑',handler:"editData"},
				 {text: '删除',iconCls: 'remove',tooltip: '删除',handler:"deleteCurrData"}
			   ]
            }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store:store,
                displayInfo: true,
				/*
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
				*/
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
	
});
