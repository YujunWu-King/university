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
    height:460,
    minHeight:300,
    maxHeight: 500,
    minWidth: 900,
    maxWidth: 1200,
    width: 1100,
    y:80,
    modal:true,
    resizable: true,
    listeners:{
        resize: function(win,width,height){
        	var panel = win.child('panel[reference=viewMaterialPanel]');
        	if(panel) panel.setHeight(height - 70);
            win.doLayout();
        }
    },
    bodyStyle:'overflow-y:auto;overflow-x:hidden;',
    initComponent: function() {
    	var materialType = this.materialType;
        var wxAppId = this.wxAppId;
        
    	var mediaPicStore = new KitchenSink.view.weChat.weChatMessage.mediaPicStore({
    		materialType: materialType,
    		wxAppId: wxAppId
    	});
        me = this;
        this.items = [{
            xtype : 'panel',
            reference: 'viewMaterialPanel',
            layout:'fit',
            bodyStyle:'overflow-y:auto;overflow-x:hidden;',
            items:[
                {
                name: 'picView',
                xtype:'dataview',
                store: mediaPicStore,
                tpl:[
                    '<tpl for=".">',
                    '<div class="thumb-wrap pic" id="{index}">',
                        '<div style="width:100%;height:100%;background:url('+ TzUniversityContextPath +'{src});background-size: 150%;background-position: center center;background-repeat: no-repeat;position: relative;">',
                           '<div class="thumb-wrap-title">',
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
                     background:'white'
                    //border:'1px solid #000000',
                    //padding:'0 15px 25px 0'
                }
               
            }],
            bbar:{
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: mediaPicStore
            }
        }];

        this.callParent();
    },
    constructor:function(config){
        this.materialType = config.materialType;
        this.wxAppId = config.wxAppId;
        this.callbackFun = config.callback;
        
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

