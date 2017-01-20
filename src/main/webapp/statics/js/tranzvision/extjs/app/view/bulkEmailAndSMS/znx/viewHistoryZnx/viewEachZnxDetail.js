Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.viewEachZnxDetail', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
    xtype: 'viewEachZnxDetail',
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
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderName","发送人"),
                    name: 'senderName'
                },{
                    xtype:'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxSjr","收件人"),
                    name:'znxSjr'
                },{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxTheme","站内信标题"),
                    name:'znxTheme'
                }]
            },{
                xtype: 'component',
                padding: 10,
                fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxContent","站内信内容"),
                //html: me.znxContentHtml,
                name:'znxContentHtml'
            }],
            buttons: [{
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("viewEachZnxDetail");
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

