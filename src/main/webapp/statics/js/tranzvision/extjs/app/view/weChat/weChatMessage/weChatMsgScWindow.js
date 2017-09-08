Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgScWindow', {
    extend: 'Ext.window.Window',
    xtype: 'weChatMsgScWindow',
    reference:'weChatMsgScWindow',
    controller: 'weChatMsgController',
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatMessage.mediaPicModel',
        'KitchenSink.view.weChat.weChatMessage.mediaPicStore'
    ],
    title: '素材管理',
    //height:400,
    minHeight:250,
    width: 1000,
    materialType:'',
    wxAppId:'',
    tabpanel:{},
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent: function() {
    	var mediaPicStore = new KitchenSink.view.weChat.weChatMessage.mediaPicStore();
        me = this;
        this.items = [
            {
                xtype : 'panel', 
                layout:'fit',
                items:[
                    {
                    name: 'picView',
                    xtype:'dataview',
                    store: mediaPicStore,
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
                        background:'white',
                        //border:'1px solid #000000',
                        padding:'0 15px 25px 0'
                    }
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: mediaPicStore/*,
                     plugins: new Ext.ux.ProgressBarPager()*/
                }
            }

        ];
        this.callParent();
    },
    constructor:function(tabpanel){
        this.tabpanel=tabpanel;
        this.callParent();
    },
    buttons: [{
        text: '确定',
        iconCls:"ensure",
        handler: 'chooseScEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            win.close();
        } 
    }] 
});

