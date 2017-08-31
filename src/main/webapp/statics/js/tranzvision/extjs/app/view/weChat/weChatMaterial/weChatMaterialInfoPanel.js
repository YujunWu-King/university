Ext.define('KitchenSink.view.weChat.weChatMaterial.weChatMaterialInfoPanel', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatMaterial.weChatMaterialController',
       // 'tranzvision.extension.grid.Exporter'

    ],
    xtype: 'weChatMaterialInfoPanel',
    controller: 'weChatMaterialController',
    reference:'weChatMaterialInfoPanel',
	selModel: {
       	type: 'checkboxmodel'
    },
    columnLines: true,

    style:"margin:8px",
    multiSelect: true,
    title: '素材管理',
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
               {minWidth:80,text:"关闭",iconCls:"close",handler:"materialWindowClose"},
               {minWidth:80,text:"确定",iconCls:"ensure",handler:"materialWindowEnsure"},
               {minWidth:80,text:"保存",iconCls:"save",handler:"materialWindowSave"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'query'},'-',
            {text:"新增图片素材",tooltip:'新增图片素材',iconCls:"add",handler:'addPic'},'-',
            {text:"新增图文素材",tooltip:'新增图文素材',iconCls:"add",handler:'addPicAndWord'},'-',
            {text:"编辑",tooltip:'编辑',iconCls:"edit",handler:'editMaterial'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deleteMaterial'}
        ]
    }],
	 items:[{
		 xtype:'dataview',
     	tpl : new Ext.XTemplate(
 				'<div align="center">',
 				'<tpl for=".">',
 				'<div class="dataana-nav">',
 				'<img src="{src}" />', '<br/><span>{text}</span>',
 				'</div>',
 				'</tpl>',
 				'</div>'),
		store : Ext.create('Ext.data.Store', {
			fields : ['src', 'text', 'card'],
			data : [{
					src : 'webapp/statics/images/tranzvision/bj.png',
					text : '1',
					card : 0
				}, {
					src : 'webapp/statics/images/tranzvision/bj.png',
					text : '2',
					card : 1
				}, {
					src : 'webapp/statics/images/tranzvision/bj.png',
					text : '3',
					card : 2
				}, {
					src : 'webapp/statics/images/tranzvision/bj.png',
					text : '4',
					card : 3
				}, {
					src : 'webapp/statics/images/tranzvision/bj.png',
					text : '5',
					card : 4
				}
			]
		})
	}],
    initComponent: function () {
//        var store = new KitchenSink.view.signIn.signStore();
        Ext.apply(this, {
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 30,
//          store: store,
            displayInfo: true,
            displayMsg: '显示{0}-{1}条，共{2}条',
            beforePageText: '第',
            afterPageText: '页/共{0}页',
            emptyMsg: '没有数据显示',
            plugins: new Ext.ux.ProgressBarPager()}
        });
        this.callParent();
    }
});

