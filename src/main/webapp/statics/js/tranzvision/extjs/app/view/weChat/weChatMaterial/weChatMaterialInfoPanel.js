Ext.define('KitchenSink.view.weChat.weChatMaterial.weChatMaterialInfoPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatMaterial.weChatMaterialController',
        'KitchenSink.view.weChat.weChatMaterial.materialStore'
    ],
    xtype: 'weChatMaterialInfoPanel',
    controller: 'weChatMaterialController',
    reference:'weChatMaterialInfoPanel',
    style:"margin:8px",
    title: '素材管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    jgId:'',
    wxAppId:'',
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"materialWindowSave"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"materialWindowEnsure"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"materialWindowClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'queryMaterial'},'-',
            {text:"新增图片素材",tooltip:'新增图片素材',iconCls:"add",handler:'addPic'},'-',
            {text:"新增图文素材",tooltip:'新增图文素材',iconCls:"add",handler:'addPicAndWord'},'-',
            {text:"编辑",tooltip:'编辑',iconCls:"edit",handler:'editMaterial'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deleteMaterial'}
        ]
    }],
    initComponent: function () {
        var materialStore = new KitchenSink.view.weChat.weChatMaterial.materialStore();
        me = this;
        this.items = [
            {
                xtype : 'panel',
                //layout:'fit',
                autoHeight:true,
                items:[
                    {
                        name: 'picView',
                        xtype:'dataview',
                        store: materialStore,
                        tpl:[
                            '<tpl for=".">',
                            '<div class="thumb-wrap" id="{index}">',
                                '<div style="width:160px;height:113px;background:url('+ TzUniversityContextPath +'{src});background-size:100%">',
                            '</div>',
                            '<tpl if="caption.length &gt; 20"><marquee scrollamount=3 width: 100%">{caption}</marquee></tpl>',
                            '<tpl if="caption.length <= 20"><span>{caption}</span></tpl>',
                            '</div>',
                            '</tpl>',
                            '<div class="x-clear"></div>'
                        ],
                        itemSelector: 'div.thumb-wrap',
                        emptyText: 'No images available',
                        style:{
                        	//background:'#eee',
                        	background:'white',
				    		border:'1px solid #000000',
				    		padding:'0 15px 25px 0'
                        },
                        listeners:{
                        	itemclick:function(v, record, item, index, e, eOpts) {
				    			//console.log(v,item);
					    	}
                        }
                    }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: materialStore
                }
            }

        ];
        this.callParent();
    }
});


