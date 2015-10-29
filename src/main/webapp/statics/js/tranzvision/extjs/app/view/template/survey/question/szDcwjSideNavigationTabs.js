Ext.define('KitchenSink.view.template.survey.question.szDcwjSideNavigationTabs',
    {
        extend : 'Ext.tab.Panel',
        xtype : 'sz-dcwj-side-navigation-tabs',
        config : {
            fields : undefined
        },
        isLoaded:false,
        predefinetpl:'',
        height : 400,
        width : 800,
        ui : 'navigation',
        tabPosition : 'left',
        tabRotation : 0,
        bodyPadding:15,
        tabBar : {
            border : false
        },
        defaults : {
            textAlign : 'left'
        },
        items:'',
        initComponent: function(){
            Ext.apply(this,{
                items : [
                    {
                        itemId: 'add',
                        title: '新建空白调查',
                        xtype:'form',
                        fieldDefaults: {
                            msgTarget: 'side',
                            labelWidth: 70,
                            labelStyle: 'font-weight:bold'
                        },
                        items:[
                            {
                                layout:'column',
                                items:[{
                                    xtype: 'textfield',
                                    fieldLabel: "调查标题",
                                    name: 'modelName',
                                    cls:'bmb_blank_text',
                                    allowBlank: false,
                                    columnWidth:0.95
                                }]
                            },{
                                html:'<br><div>空白调查<br>从零开始，您可以在每份问卷内，自定义创建多个问卷和选项，我们提供多种预设题型，帮助您快速完成在线问卷设计，提供各行业类型的丰富题库</div>'
                            }
                        ]
                    },
                    {
                        itemId: 'predefine',
                        title: '指定问卷模板创建调查',
                        html: (function(){
                            var divTmp = document.createElement("div");
                            var txt = Ext.create('Ext.form.field.Text', {
                                fieldLabel: '调查标题',
                                value:'',
                                labelStyle: 'font-weight:bold',
                                cls:'bmb_predefine_text',
                                width:550,
                                allowBlank: false
                            }).render(divTmp)
                            return divTmp.outerHTML;
                        })() + '<div class="predefinetpllist" style="overflow-y:auto;height:350px;">'+ this.getPredefinetpl() +"</div>"
                    }
                ]
            });
            this.callParent();
        },
        getPredefinetpl:function(){}
  });

