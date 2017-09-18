Ext.define('KitchenSink.view.main.Main', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border',
        'KitchenSink.view.main.MainController',
        'KitchenSink.view.main.MainModel',
        'KitchenSink.view.Header',
        'KitchenSink.view.ThemeSwitcher',
        'KitchenSink.view.ContentPanel',
        'KitchenSink.view.navigation.Breadcrumb',
    ],

    controller: 'main',
    viewModel: 'main',
    listeners:{
        afterrender:function(){
            var me = this,
				navStore = Ext.StoreMgr.get('navigation');

            document.onkeyup = function (event) {
                var e = event || window.event;
                var keyCode = e.keyCode || e.which;

                var activeTab =Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();

                switch (keyCode) {
                    case 74://J
                        if(event.ctrlKey){
                            var treeNode = navStore.getNodeById(activeTab.currentNodeId);

                            var win = Ext.create('Ext.window.Window',{
                                    title:activeTab.title,
                                    modal:true,
                                    layout:'fit',
                                    closeAction:'destroy',
                                    ignoreChangesFlag:true,
                                    width:500,
                                    resizable:false,
                                    items:[
                                        {
                                            xtype:'form',
                                            layout: {
                                                type: 'vbox',
                                                align: 'stretch'
                                            },
                                            border: false,
                                            bodyPadding: 10,
                                            defaults:{
                                                xtype:'textfield',
                                                labelWidth:90,
                                                labelStyle:'font-weight:bold',
                                                readOnly:true
                                            },
                                            items:[
                                                {
                                                    fieldLabel:'菜单编号',
                                                    value:activeTab.currentNodeId||"无"
                                                },{
                                                    fieldLabel:'菜单类型',
                                                    value:treeNode&&treeNode.get('menuType')=="B"&&"内容参考"||"功能组件",
                                                    hidden:!(treeNode)
                                                },{
                                                    fieldLabel:'组件编号',
                                                    value:treeNode&&treeNode.get('comID')||"",
                                                    hidden:!(treeNode&&treeNode.get('comID'))
                                                },{
                                                    fieldLabel:'内容参考URL',
                                                    value:treeNode&&treeNode.get('contentRefUrl')||"",
                                                    hidden:!(treeNode&&treeNode.get('contentRefUrl'))
                                                },{
                                                    fieldLabel:'控件类型',
                                                    value:activeTab.getXTypes().split('/').reverse()
                                                },{
                                                    fieldLabel:'类',
                                                    value:treeNode&&treeNode.get('className')||Ext.getClassName(activeTab)
                                                },{
                                                    fieldLabel:'外部链接',
                                                    value:activeTab.getXType()=="externallink"&&activeTab.getEl().down("iframe").dom.src,
                                                    hidden:!(activeTab.getXType()=="externallink")
                                                }
                                            ]
                                        }
                                    ]
                                });

                            win.show();
                        }
                        break;
                    default:
                        break;
                }
            }
            
            //只显示内容模式则隐藏主区域头
            if(me.isContentIndex==true){
            	me.down("contentPanel").getHeader().hide();
            }
        }
    },
    layout: 'border',
    stateful: true,
    stateId: 'tranzvision-framework-kitchensink-viewport',

    initComponent: function() {
    	var me = this;
    	
    	var items = [{
            region: 'center',
            xtype: 'contentPanel',
            reference: 'contentPanel',
            dockedItems: [{
                xtype: 'navigation-breadcrumb',
                reference: 'breadcrumb>'
            }]
        }];
    		
    	if(TranzvisionMeikecityAdvanced.Boot.getQueryString("model")=="content"){
    		this.isContentIndex = true;
    		items.push({
                region: 'north',
                xtype: 'appHeader',
                hidden:true
            });
    	}else{
    		items.push({
                region: 'north',
                xtype: 'appHeader'
            });
    	}
    	
    	this.items = items;
    	this.callParent();
    },

    applyState: function(state) {
        this.getController().applyState(state);

    },

    getState: function() {
        return this.getController().getState();
    }
});
