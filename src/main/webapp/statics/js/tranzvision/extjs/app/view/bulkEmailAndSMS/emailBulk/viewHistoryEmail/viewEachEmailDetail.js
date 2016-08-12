Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.viewEachEmailDetail', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
    xtype: 'viewEachEmailDetail',
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
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("viewEachEmailDetail");
                    //信息表单
                    var form = win.child("form").getForm();
                    //关闭窗口
                    win.close();
                }
            }]
        });
        this.callParent();
    }


});

