Ext.define('tranzvision.extension.ContentPanelMaximize', {
    extend: 'Ext.util.Observable',
    alias: 'plugin.contentpanelmaximize',
    init : function(ct) {
        var breadcrumb = Ext.getCmp('tranzvision-framework-navigation-breadcrumb');
        var header = Ext.getCmp('tranzvision-framework-app-header');

        var maximizeTool = {
            itemId : 'maximize',
            xtype:'tool',
            handler : this.handleMaximize,
            scope : ct,
            type : 'maximize',
            columnWidth:1,
            qtip : TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00013']
        };
        ct.tools = ct.tools || [];
        var newTools = ct.tools.slice();
        ct.tools = newTools;
        for (var i = 0, len = ct.tools.length; i < len; i++) {
            if (ct.tools[i].id == 'maximize')
                return;
        }
        ct.tools[ct.tools.length] = maximizeTool;
        breadcrumb.add(['->',maximizeTool]);

    },

    handleMaximize :function(event, toolEl, owner){
        var animCollapse = this.animCollapse;
        var panel = owner.up('panel');
        var breadcrumb = Ext.getCmp('tranzvision-framework-navigation-breadcrumb');
        if(panel.getCollapsed( )){
            panel=panel.expand();
        };
        var task = new Ext.util.DelayedTask(function(){
            panel.originalOwnerCt = panel.ownerCt;
            panel.originalPosition = panel.ownerCt.items.indexOf(panel);
            panel.originalOwnerCt.insert(panel.originalPosition, panel);
            panel.originalSize = panel.getSize();
            if (!toolEl.window) {
                var defaultConfig = {
                    itemid : (panel.getId() + '-MAX'),
                    width : Ext.getBody().getSize().width,
                    height : Ext.getBody().getSize().height,
                    ignoreChangesFlag:true,
                    animCollapse : animCollapse,
                    animateTarget : panel,
                    listeners:{
                        show:function(win){
                            win.setSize(Ext.getBody().getSize());
                        }
                    },
                    viewModel: {
                        data: {
                            title: panel.title
                        }
                    },
                    bind: {
                        title: '{title}'
                    },
                    resizable : false,
                    draggable : false,
                    closable : true,
                    closeAction : 'hide',
                    hideBorders : true,
                    plain : true,
                    layout: {
                        type: 'fit',
                        align: 'stretch'
                    },
                    autoScroll : false,
                    border : false,
                    bodyBorder : false,
                    frame : true,
                    shadow : false,
                    modal : true,
                    title : panel.title,
                    bodyStyle : 'background-color: #ffffff;'
                };
                toolEl.window = Ext.create('Ext.window.Window', defaultConfig);
                toolEl.window.on('beforehide', handleMinimize, panel);
            }

            if (!panel.dummyComponent) {
                var dummyCompConfig = {
                    header:false,
                    width : panel.getSize().width,
                    height : panel.getSize().height,
                    bodyPadding:0,
                    html : '&nbsp;'
                };
                panel.setLocalXY(0,0);
                panel.dummyComponent = new Ext.create('Ext.panel.Panel',
                    dummyCompConfig);
            }

            var _breadcrumb;
            if(panel.header.isHidden( )){
                _breadcrumb=true;
                breadcrumb.hide();
            }else{
                _breadcrumb=false;
                panel.tools['maximize'].setVisible(false);
                panel.header.setVisible(false);
            }


            var isFramed = false;
            if(panel.hasCls('x-panel-default-framed')){
                isFramed=true;
                panel.removeCls('x-panel-default-framed');
            };
            toolEl.window.insert(0,panel);
            panel.originalOwnerCt.insert(panel.originalPosition,
                panel.dummyComponent);
            panel.dummyComponent.setSize(panel.originalSize);
            panel.dummyComponent.setVisible(true);

            if(Ext.ComponentQuery.is(panel,"tabpanel")){
                panel.on('beforetabchange',function(tabPanel, newCard, oldCard, eOpts){
                    toolEl.window.getViewModel().set('title', newCard.title);
                })
            };

            toolEl.window.show(Ext.getBody());

            function handleMinimize(window) {
                var me = this;
                this.dummyComponent.getEl().unmask();
                this.dummyComponent.setVisible(false);
                this.originalOwnerCt.insert(this.originalPosition, this);
                if(isFramed){this.addCls('x-panel-default-framed');}
                this.originalOwnerCt.doLayout();
                this.setSize(this.originalSize);
                if(this.findParentByType('panel')){
                    this.findParentByType('panel').addListener('resize',function( topPanel, width, height, oldWidth, oldHeight, eOpts){
                    me.setWidth(width-37);/*左右外边距10*2+滚动条17*/
                });
                }

                if(_breadcrumb){
                    breadcrumb.show();
                }else{
                    this.tools['maximize'].setVisible(true);
                    this.header.setVisible(true);
                }
            }
        });
        task.delay(10);
    }
});
