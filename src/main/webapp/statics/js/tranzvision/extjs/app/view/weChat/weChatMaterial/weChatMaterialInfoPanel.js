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
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
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
    
    listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 36;/*button height plus panel body padding*/
			var toolbar = 36;
		}
	},
	
    initComponent: function () {
        var materialStore = new KitchenSink.view.weChat.weChatMaterial.materialStore();
        me = this;
        this.items = [
            {
                xtype : 'panel',
                //height:400,
                items:[{
                    name: 'picView',
                    xtype:'dataview',
                    store: materialStore,
                    tpl:[
                        '<tpl for=".">',
                        '<div class="thumb-wrap pic "  id="{index}">',
                            '<div style="width:100%;height:100%;background:url('+ TzUniversityContextPath +'{src});background-size: 150%;background-position: center center;background-repeat: no-repeat;position: relative;">',
                              '<div class="thumb-wrap-title">',
                  		        '<img src="'+TzUniversityContextPath+'{publishFlag}" style="width:12px;height:12px;margin-right: 5px;position: relative;top: 2px;">',
                  		        '<tpl ><span>{caption}</span></tpl>',
                  	          '</div>',
                            '</div>',
                        '</div>',
                        '</tpl>',
                        '<div class="x-clear"></div>'
                    ],
                    itemSelector: 'div.thumb-wrap',
                    selectedItemCls:'current-item',
                    emptyText: 'No images available',
                    style:{
                    	background:'white',
			    		border:'1px solid white',
			    		padding:'0 15px 25px 0'
                    },
                    listeners:{
                    	itemclick:function(v, record, item, index, e, eOpts) {
                    		/*var store=v.getStore();
                    		store.each(function(rec) {   
                    		       rec.set("isSelected","N");
                    			});  
                    		record.set("isSelected","Y");*/
				    	}
                    }
                }]
            }

        ];
        this.bbar = {
            xtype: 'pagingtoolbar',
            pageSize: 10,
            store: materialStore,
        },
        this.callParent();
    }
});


