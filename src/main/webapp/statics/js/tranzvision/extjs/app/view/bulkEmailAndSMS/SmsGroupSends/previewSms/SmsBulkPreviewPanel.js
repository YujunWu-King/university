Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.previewSms.SmsBulkPreviewPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],

    xtype: 'SmsBulkPreviewPanel',
   title: "预览",
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent: function(){


        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                ignoreLabelWidth: true,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                autoHeight:true,
                //minHeight: 800,
                items: [{
                    xtype: 'textfield',
                    name: 'audCyrTotal',
                    hidden:true
                },{
                    xtype: 'textfield',
                    name: 'currentPageNum',
                    hidden:true
                },{
                    xtype: 'textareafield',
                    name: 'configuration',
                    hidden:true
                },{
                        name: 'AudID',
                    xtype: 'textfield',
                    hidden:true
                    },{
                    xtype: 'displayfield',
                    fieldLabel: "发送人",
                    name: 'senderPhone',
                    hidden:true
                },{
                    xtype:'displayfield',
                    fieldLabel: "收件人",
                    name:'AddresseePhone'
                },{
                    xtype: 'displayfield',
                    fieldLabel: "主题",
                    name:'SmsTheme',
                    hidden:true
                },{
                    xtype: 'textareafield',
                    fieldLabel: "短信内容",
                    name:'SmsContentArea',
                    hidden:true
                }]
            },{
                        layout: {
                            type: 'column'
                        } ,  
                          padding:'10 0 10 10',
                          
                          items:[{
                        xtype: 'displayfield',
                        beforeLabelTpl:'',
                        fieldLabel: "短信内容",
                         labelStyle: 'font-weight:bold'
                        
                    },{
                xtype: 'component',
                padding: 10,
                fieldLabel: "短信内容",
                //html: me.emailContentHtml,
                name:'SmsContentHtml'
            }]
          }
          ],
            buttons: [{
                text: "上一条",
                iconCls:"prev",
                handler: function(btn){

                    //获取窗口
                    var win = btn.findParentByType("SmsBulkPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=SmsContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    if(currentPageNum > 1){
                        currentPageNum = currentPageNum - 1;
                        var tzParams = '{"ComID":"TZ_SMSQ_PREVIEW_COM","PageID":"TZ_SMSQ_VIEW_STD","OperateType":"previewSms","comParams":{"type":"previewOtherSMS","audID":"'+audID+'","viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.SmsContent));
                            htmlCom.updateLayout();
                        });
                    }else{
                        Ext.Msg.alert("提示","已经是第一页");
                    }
                }
            },{
                text: "下一页",
                iconCls:"next",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("SmsBulkPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=SmsContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    var audCyrTotal = parseInt(form.findField("audCyrTotal").getValue());
                    if(currentPageNum < audCyrTotal){
                        currentPageNum = currentPageNum + 1;
                        var tzParams = '{"ComID":"TZ_SMSQ_PREVIEW_COM","PageID":"TZ_SMSQ_VIEW_STD","OperateType":"previewSms","comParams":{"type":"previewOtherSMS","audID":"'+audID+'","viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.SmsContent));
                            htmlCom.updateLayout();
                        });
                    }else{
                        Ext.Msg.alert("提示","已经是最后一页");
                    }
                }
            },{
                text: "关闭",
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("SmsBulkPreviewPanel");
                    //关闭窗口
                    win.close();
                }
            }]
        });
        this.callParent();
    }


//    listeners:{
//        resize: function(win){
//            win.doLayout();
//        },
//        afterrender: function(win){
//            var form = win.child('form').getForm();
//            form.setValues(this.formData);
//        }
//    }

});

