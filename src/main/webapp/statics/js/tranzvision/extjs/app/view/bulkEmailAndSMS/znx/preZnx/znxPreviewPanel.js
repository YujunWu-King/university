Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.preZnx.znxPreviewPanel', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],

    xtype: 'znxPreviewPanel',
   title: Ext.tzGetResourse("TZ_ZNXQ_PREVIEW_COM.TZ_ZNXQ_VIEW_STD.preview","预览"),
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
                    name: 'znxContent',
                    hidden:true
                },{
                	 xtype: 'textareafield',
                     name: 'AudienceOprID',
                     hidden:true
                },{
                    xtype: 'textfield',
                    name: 'AudID',
                    hidden:true
                },{
                    xtype:'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxlTheme","站内信模板"),
                    name:'znxTmpId',
                    hidden:true
                }/*,{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.senderEmail","发送人"),
                    name: 'senderEmail',
                    hidden: true
                }*/,{
                    xtype:'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.oprName","收件人"),
                    name:'oprName'
                },{
                    xtype: 'displayfield',
                    fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxlbt","站内信标题"),
                    name:'znxlTheme'
                }]
            },{
                xtype: 'component',
                padding: 10,
                fieldLabel: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.znxContent","站内信内容"),
                //html: me.emailContentHtml,
                name:'znxContentHtml'
            }],
            buttons: [{
                text: Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.previous","上一页"),
                iconCls:"prev",
                handler: function(btn){

                    //获取窗口
                    var win = btn.findParentByType("znxPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var AudienceOprID=form.findField("AudienceOprID").getValue();
                    var znxTmpId=form.findField("znxTmpId").getValue();
                    var znxlTheme=form.findField("znxlTheme").getValue();
                    var znxContent=form.findField("znxContent").getValue();
                    //var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=znxContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    if(currentPageNum > 1){
                        currentPageNum = currentPageNum - 1;
                        var tzParamsJson = {
                        		"ComID":"TZ_ZNXQ_PREVIEW_COM",
                        		"PageID":"TZ_ZNXQ_VIEW_STD",
                        		"OperateType":"previewZnx",
                        		"comParams":{
                        			"type":"previewZnx",
                        			"viewNumber":currentPageNum,
                        			"audID": audID,
                        			"AudienceOprID":AudienceOprID, 
                        			"znxTmpId":znxTmpId,
                        			"znxlTheme":znxlTheme,
                        			"znxContent":znxContent
                        		}
                        };
                        var tzParams = Ext.JSON.encode(tzParamsJson); 
                        //var tzParams = '{"ComID":"TZ_ZNXQ_PREVIEW_COM","PageID":"TZ_ZNXQ_VIEW_STD","OperateType":"previewZnx","comParams":{"type":"previewOtherEmail","audID":"'+audID+'", "viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            //htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.znxContentHtml));
                            htmlCom.getEl().update(formData.znxContentHtml);
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
                    var win = btn.findParentByType("znxPreviewPanel");
                    //信息表单
                    var form = win.child("form").getForm();
                    var audID=form.findField("AudID").getValue();//听众ID
                    var AudienceOprID=form.findField("AudienceOprID").getValue();
                    var znxTmpId=form.findField("znxTmpId").getValue();
                    var znxlTheme=form.findField("znxlTheme").getValue();
                    var znxContent=form.findField("znxContent").getValue();
                    //var configuration=form.findField("configuration").getValue();
                    var htmlCom = win.down("component[name=znxContentHtml]");
                    var currentPageNum = parseInt(form.findField("currentPageNum").getValue());
                    var audCyrTotal = parseInt(form.findField("audCyrTotal").getValue());
                    if(currentPageNum < audCyrTotal){

                        currentPageNum = currentPageNum + 1;
                        var tzParamsJson = {
                        		"ComID":"TZ_ZNXQ_PREVIEW_COM",
                        		"PageID":"TZ_ZNXQ_VIEW_STD",
                        		"OperateType":"previewZnx",
                        		"comParams":{
                        			"type":"previewZnx",
                        			"viewNumber":currentPageNum,
                        			"audID": audID,
                        			"AudienceOprID":AudienceOprID, 
                        			"znxTmpId":znxTmpId,
                        			"znxlTheme":znxlTheme,
                        			"znxContent":znxContent
                        		}
                        };
                        var tzParams = Ext.JSON.encode(tzParamsJson); 
                        //var tzParams = '{"ComID":"TZ_ZNXQ_PREVIEW_COM","PageID":"TZ_ZNXQ_VIEW_STD","OperateType":"previewZnx","comParams":{"type":"previewOtherEmail","audID":"'+audID+'", "viewNumber":"'+currentPageNum+'",'+configuration.split("{")[1]+'}';
                        //加载数据
                        Ext.tzLoadAsync(tzParams,function(responseData){
                            var formData = responseData.formData;
                            form.setValues(formData);
                            //htmlCom.getEl().dom.innerHTML = formData.emailContent;
                            htmlCom.getEl().update(formData.znxContentHtml);
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
                    var win = btn.findParentByType("znxPreviewPanel");
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

