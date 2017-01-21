Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.previewEmail.emailBulkPreviewPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],

    xtype: 'emailBulkPreviewPanel',
   title: Ext.tzGetResourse("TZ_EMLQ_PREVIEW_COM.TZ_EMLQ_VIEW_STD.preview","预览"),
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
                    xtype: 'textfield',
                    name: 'AudID',
                    hidden:true
                },{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
                    name: 'senderEmail'
                },{
                    xtype:'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.AddresseeEmail","收件人"),
                    name:'AddresseeEmail'
                },{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailTheme","主题"),
                    name:'emailTheme'
                }]
            },{
                xtype: 'component',
                padding: 10,
                fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.emailContent","邮件内容"),
                //html: me.emailContentHtml,
                name:'emailContentHtml'
            }],
            buttons: [{
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.previous","上一页"),
                iconCls:"prev",
                handler: function(btn){

                    //获取窗口
                    var win = btn.findParentByType("emailBulkPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=emailContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    if(currentPageNum > 1){
                        currentPageNum = currentPageNum - 1;
                        var tzParams = '{"ComID":"TZ_EMLQ_PREVIEW_COM","PageID":"TZ_EMLQ_VIEW_STD","OperateType":"previewEmail","comParams":{"type":"previewOtherEmail","audID":"'+audID+'", "viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.emailContent));
                            htmlCom.updateLayout();
                        });
                    }else{
                        Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.firstPage","已经是第一页"));
                    }
                }
            },{
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.next","下一页"),
                iconCls:"next",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("emailBulkPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=emailContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    var audCyrTotal = parseInt(form.findField("audCyrTotal").getValue());
                    if(currentPageNum < audCyrTotal){

                        currentPageNum = currentPageNum + 1;
                        var tzParams = '{"ComID":"TZ_EMLQ_PREVIEW_COM","PageID":"TZ_EMLQ_VIEW_STD","OperateType":"previewEmail","comParams":{"type":"previewOtherEmail","audID":"'+audID+'", "viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.emailContent));
                            htmlCom.updateLayout();
                        });
                    }else{
                        Ext.Msg.alert(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.reminder","提示"),Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.lastPage","已经是最后一页"));
                    }
                }
            },{
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("emailBulkPreviewPanel");
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

