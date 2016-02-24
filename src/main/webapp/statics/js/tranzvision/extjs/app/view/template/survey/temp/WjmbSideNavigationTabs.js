Ext.define('KitchenSink.view.template.survey.temp.WjmbSideNavigationTabs',
    {
        extend : 'Ext.tab.Panel',
        xtype : 'wjmb-side-navigation-tabs',
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
                        title: '新建问卷模板',
                        xtype:'form',
                        fieldDefaults: {
                            msgTarget: 'side',
                            labelWidth: 100,
                            labelStyle: 'font-weight:bold'
                        },
                        items:[
                            {
                                layout:'column',
                                items:[{
                                    xtype: 'textfield',
                                    fieldLabel: "模板名称",
                                    name: 'modelName',
                                    cls:'bmb_blank_text',
                                    allowBlank: false,
                                    columnWidth:0.95
                                }]
                            },{
                                html:'<br><div>空白模板<br>从零开始，您可以在每份问卷内，自定义创建多个问卷和选项，我们提供多种预设题型，帮助您快速完成在线问卷设计，提供各行业类型的丰富题库</div>'
                            }
                        ]
                    },
                    {
                        itemId: 'predefine',
                        title: '从现有问卷模板定义',
                        items:[{
                            xtype:'textfield',
                            fieldLabel: '模板名称',
                            value:'',
                            labelStyle: 'font-weight:bold',
                            cls:'bmb_predefine_text',
                            width:550,
                            allowBlank: false
                        },{
                            html:'<div class="predefinetpllist" style="overflow-y:auto;height:350px;">'+ this.getPredefinetpl() +"</div>"
                        }]
                      /*  html: (function(){
                            var divTmp = document.createElement("div");
                            var txt = Ext.create('Ext.form.field.Text', {
                                fieldLabel: '模板名称',
                                value:'',
                                labelStyle: 'font-weight:bold',
                                cls:'bmb_predefine_text',
                                width:550,
                                allowBlank: false
                            }).render(divTmp)
                            return divTmp.outerHTML;
                        })() + '<div class="predefinetpllist" style="overflow-y:auto;height:350px;">'+ this.getPredefinetpl() +"</div>"*/
                    }
                ]
            });
            this.callParent();
        }  ,
        getPredefinetpl:function(){
            var me = this, predefinetpl = '';
            if(!me.isLoaded){
                var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_MBLIST_STD","OperateType":"QF","comParams":""}';
                Ext.Ajax.request({
                    url:Ext.tzGetGeneralURL(),
                    async:false,
                    params: {
                        tzParams: tzParams
                    },
                    waitTitle : '请等待' ,
                    waitMsg: '正在加载中',
                    success: function(response){
                        var resText1 = response.responseText;
                        var responseData1 = Ext.JSON.decode(resText1)
                        var resText = responseData1.comContent;
                        var responseData = resText;
                        for(var i in responseData){
                            predefinetpl += '<div class="tplitem" style="padding: 10px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;text-align:center;width:176px;" onclick="myWjmb(this)" data-id="'+responseData[i].tplid+'"><img src="' + TzUniversityContextPath + '/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png"><br><span class="tplname" title="' + responseData[i].tplname + '">' + Ext.String.ellipsis(responseData[i].tplname,16,true) + '</span></div>';
                        }
                        me.isLoaded = true;
                    }
                });
            }
           // console.log(predefinetpl);
            return predefinetpl;
        }
    });
