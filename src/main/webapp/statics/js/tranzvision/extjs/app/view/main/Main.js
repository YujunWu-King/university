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
        //'KitchenSink.view.CodePreview'
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

                var activeTab =me.down('contentPanel').getActiveTab();

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
        }
    },
    layout: 'border',
    stateful: true,
    stateId: 'tranzvision-framework-kitchensink-viewport',

    items: [{
        region: 'north',
        xtype: 'appHeader'
    }, {
        region: 'center',
        xtype: 'contentPanel',
        reference: 'contentPanel',
        dockedItems: [{
            xtype: 'navigation-breadcrumb',
            reference: 'breadcrumb>'
        }]
    }, /*{
        xtype: 'codePreview',
        region: 'east',
        id: 'tranzvision-framework-east-region',
        itemId: 'tranzvision-framework-codePreview',
        stateful: true,
        stateId: 'tranzvision-framework-mainnav.east',
        split: true,
        collapsible: true,
        collapsed: true,
        width: 350,
        minWidth: 100,
		hidden: true
    }*/],

    applyState: function(state) {
        this.getController().applyState(state);

    },

    getState: function() {
        return this.getController().getState();
    }
});
