Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.viewEachSmsDetail', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
    xtype: 'viewEachSmsDetail',
    title: Ext.tzGetResourse("TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_VW_E_D_STD.preview","短信发送明细"),
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
                items: [{
                    xtype:'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_VW_E_D_STD.AddresseePhone","收信号码"),
                    name:'AddresseePhone'
                },{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_VW_E_D_STD.sendDatetime","发送时间"),
                    name:'sendDatetime'
                }]
            },{
				layout: {
					type: 'column'
				} ,  
				padding:'0 0 10 10',
				style: 'margin-top: -10px;',  
				items:[{
					xtype: 'displayfield',
					beforeLabelTpl:'',
					fieldLabel: Ext.tzGetResourse("TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_VW_E_D_STD.SmsContent","短信内容"),
					labelStyle: 'font-weight:bold'
					
				},{
					xtype: 'component',
					name:'SmsContentHtml',
					style: 'padding-top:3px'
				}]
          }],
            buttons: [{
                text: Ext.tzGetResourse("TZ_SMSQ_VIEWTY_COM.TZ_SMSQ_VW_E_D_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("viewEachSmsDetail");
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

