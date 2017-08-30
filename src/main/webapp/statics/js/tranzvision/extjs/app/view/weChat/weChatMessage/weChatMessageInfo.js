Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMessageInfo', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatMessage.weChatMsgController'
    ],
    extend : 'Ext.panel.Panel',
    controller:'weChatMsgController',
    autoScroll:false,
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'发送微信消息',
    frame:true,
    sendMode:'',
    openIds:'',
    weChatTags:'',
    weChatAppId:'',
    listeners: {
        beforerender: function(panel){
        	//发送模式
        	var sendMode=this.sendMode;
        	var form=panel.down("form").getForm();
        	//console.log(form);
        	if(sendMode==''){
        		//从URL中获取参数信息
        		//var url=window.top.location.href;
        	   var url="http://localhost:8080/university/index#SEM_A0000001982?appId=111&sendMode=B&openIds=11,22,33&tags=Farscape,Firefly";
        	   var num=url.indexOf("?") 
        	   var str=url.substr(num+1);
        	   var arr=str.split("&"); //各个参数放到数组里
    		   for(var i=0;i < arr.length;i++){ 
    		    num=arr[i].indexOf("="); 
    		    if(num>0){ 
    		     name=arr[i].substring(0,num);
    		     value=arr[i].substring(num+1,arr[i].length);
    		      if(name=="appId"){
    		    	 this.weChatAppId=value;
    		    	 form.findField("appId").setValue(value);
    		      }
    		      if(name=="sendMode"){
     		    	 this.sendMode=value;
     		    	 form.findField("sendMode").setValue(value);
     		      }
    		      if(name=="openIds"){
     		    	 this.openIds=value;
     		    	 form.findField("openIds").setValue(value);
     		      }
    		      if(name=="tags"){
      		    	this.weChatTags=value;
      		    	var tagArrays=value.split(",");
      		    	
                    form.findField("wechatTag").setValue(tagArrays);
      		      }
    		     }
    		    } 
        	}
        	//如果为指定用户，按照标签字段隐藏；如果为按照标签，用户列表字段隐藏。
        	if(this.sendMode=='A'){
        		form.findField("wechatTag").setVisible(false);
        	}
        	if(this.sendMode=='B'){
        		form.findField("openIds").setVisible(false);
        	}
        	//删除按钮隐藏
            //panel.down("form").items.items[4].down('button[name=chooseScBtn]').setVisible(false);
        }
    },
    initComponent:function(){
        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin:'8px',
                style:'border:0px',
                items: [{
                	xtype:'hiddenfield',
                	fieldLabel:'应用ID',
                	name:'appId'
                },{
                    xtype: 'combo',
                    labelWidth: 100,
                    editable: false,
                    fieldLabel: '发送模式',
                    name: 'sendMode',
                    emptyText: '请选择',
                    mode: "remote",
                    valueField: 'sendMode',
                    displayField: 'sendModeDesc',
                    store: {
                        fields: ["sendMode", "sendModeDesc"],
                        data: [
                            {sendMode: "A", sendModeDesc: "指定用户"},
                            {sendMode: "B", sendModeDesc: "按照标签"}
                        ]
                    },
                    readOnly:true,
                    style: 'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 0px solid;'
                },{
                    xtype:'textareafield',
                    fieldLabel: "用户列表",
                    grow:true,
                    name:'openIds'
                },{
                    xtype:'tagfield',
                    fieldLabel:'按照标签',
                    name:'wechatTag',
                    anyMatch:true,
                    filterPickList: true,
                    createNewOnEnter: true,
                    createNewOnBlur: false,
                    enableKeyEvents: true,
                    ignoreChangesFlag:true,
                    store: {
                        fields: ['tagId','tagName'],
                        data: [  {tagId: 0, tagName: 'Battlestar Galactica'},
                                 {tagId: 1, tagName: 'Doctor Who'},
                                 {tagId: 2, tagName: 'Farscape'},
                                 {tagId: 3, tagName: 'Firefly'},
                                 {tagId: 4, tagName: 'Star Trek'},
                                 {tagId: 5, tagName: 'Star Wars: Christmas Special'} ]
                    },
                    valueField: 'tagId',
                    displayField: 'tagName'

                },{
                    xtype: 'tabpanel',
                    frame: true,
                    activeTab: 0,
                    plain: false,
                    id:'weChatTabPanel',
                    name:'weChatTabPanel',
                    resizeTabs: true,
                    defaults: {
                        autoScroll: false
                    },
                    items:[{
                        title:'文字消息',
                        xtype:'form',
                        name:'form1',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        style:'border:0',
                        items:[{
                            xtype: 'textareafield',
                            grow:true,
                            name: 'wordMessage',
                            minHeight:200,
                            hideLable:true

                        }]
                    },{
                        title: "图片消息",
                        layout: {
                            type: 'column'
                        },
                        xtype:'form',
                       
                        name:'form2',
                        items:[{
                            columnWidth:.3,
                            bodyStyle:'padding:10px',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [{
                                xtype: 'button',
                                text: '<br><font color="#c0c0c0" size="8">+<br><span style="font-size:18px;">从素材库中选择</span></font>',
                                name:'chooseScBtn',
                                style:'background-color:white;border:2px dotted #d9dadc;height:200px;',
                                listeners:{
                                    click:function(bt, value, eOpts){
                                        ChoosePic(bt, value, eOpts);
                                    }
                                }
                            },{
                                layout: {
                                    type: 'column'
                                },
                                bodyStyle:'padding:10px 0 0 0',
                                xtype: 'form',
                                items: [{
                                    columnWidth:.62,
                                    xtype: "image",
                                    src: TzUniversityContextPath + "/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png",
                                    name: "titileImage",
                                    height:186, 
                                    hidden:true
                                },{
                                    columnWidth:.2,
                                    xtype: 'button',
                                    name:'deletePicBtn',
                                    hidden:true,
                                    text: '<span color="#459ae9">删除</span>',
                                    style:'width:60px;top:170px;border-width:0;box-shadow:none',
                                    listeners:{ 
                                        click:function(bt, value, eOpts){ 
                                            deleteImage(bt, value, eOpts);
                                        }
                                    }
                                }]
                            }]
                        },{
                            columnWidth:.7,
                            bodyStyle:'padding:10px 10px 10px 30px',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [{
                                xtype: 'hiddenfield',
                                fieldLabel: '图片素材ID',
                                name: 'tpMediaId'
                            }]
                        }]
                    },{
                        title: "图文消息",
                        layout: {
                            type: 'column'
                        },
                        xtype:'form',
                        name:'form3',
                        items:[{
                            columnWidth:.3,
                            bodyStyle:'padding:10px',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [{
                                xtype: 'button',
                                text: '<br><font color="#c0c0c0" size="8">+<br><span style="font-size:18px;">从素材库中选择</span></font>',
                                name:'chooseTwBtn',
                                style:'background-color:white;border:2px dotted #d9dadc;height:200px;',
                                listeners:{
                                    click:function(bt, value, eOpts){
                                        ChooseTw(bt, value, eOpts);
                                    }
                                }
                            },{
                                layout: {
                                    type: 'column'
                                },
                                bodyStyle:'padding:10px 0 0 0',
                                xtype: 'form',
                                items: [{
                                    columnWidth:.61,
                                    xtype: "image",
                                    src: TzUniversityContextPath + "/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png",
                                    name: "twImage",
                                    height:186, 
                                    hidden:true
                                }]
                            },{
                                layout: {
                                    type: 'column'
                                },
                                bodyStyle:'padding:10px 0 0 0',
                                xtype: 'form',
                                items: [
                                	{   columnWidth:.62,
                                        xtype:'textarea',
                                        fieldLabel: '图文标题',
                                        name:'twTitle',
                                        readOnly:true,
                                        hideLabel:true,
                                        hidden:true,
                                        fieldStyle:'color:#c0c0c0',
                                        preSubTpl: [
                         							'<div id="{cmpId}-triggerWrap" data-ref="triggerWrap" style="border:0" class="{triggerWrapCls} {triggerWrapCls}-{ui}">',
                         							'<div id={cmpId}-inputWrap data-ref="inputWrap" class="{inputWrapCls} {inputWrapCls}-{ui}">'
                         			    ]
                                    },{
                                    columnWidth:.2,
                                    xtype: 'button',
                                    name:'deleteTwBtn',
                                    hidden:true,
                                    text: '<font color="#459ae9">删除</font>',
                                    style:'width:60px;top:40px;background-color:white;border:0;outline:none',
                                    listeners:{
                                        click:function(bt, value, eOpts){
                                            deleteTw(bt, value, eOpts);
                                        }
                                    }
                                }]
                            }]
                        },{
                            columnWidth:.7,
                            bodyStyle:'padding:10px 10px 10px 30px',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [{
                                xtype: 'hiddenfield',
                                fieldLabel: '图文素材ID',
                                name: 'twMediaId'
                            }]
                        }]
                    }]
                },{
                    xtype: 'form',
                    layout: 'hbox',
                    width:'100%',
                    height:'100%',
                    name:'imagesForm', 
                    defaults:{
                        margin:'20px 0 0 20px'
                    },
                    items:[{
                    	 xtype: 'combo',
                         labelWidth: 100,
                         editable: false,
                         fieldLabel: '发送状态',
                         name: 'sendStatus',
                         mode: "remote",
                         preSubTpl: [
         							'<div id="{cmpId}-triggerWrap" data-ref="triggerWrap" style="border:0" class="{triggerWrapCls} {triggerWrapCls}-{ui}">',
         								'<div id={cmpId}-inputWrap data-ref="inputWrap" class="{inputWrapCls} {inputWrapCls}-{ui}">'
         						],
                         readOnly:true,
                         valueField: 'sendStatus',
                         displayField: 'sendStatusDesc',
                         store: {
                             fields: ["sendStatus", "sendStatusDesc"],
                             data: [
                                 {sendStatus: "Y", sendStatusDesc: "已发送"},
                                 {sendStatus: "N", sendStatusDesc: "未发送"}
                             ]
                         },
                         value:'N',
                         style: 'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 0px solid;'
                    }]
                }]
            }],
            buttons:[
               
                {
                    text: '发送',
                    handler:'sendWxMsg',
                    iconCls:'send'
                },{
                    text:'查看发送历史',
                    handler:'viewSendHis',
                    iconCls:'view'
                },{
                    text:'关闭',
                    iconCls:'close',
                    handler:'closeWxPanel'
                }
            ]
        });
        this.callParent();

    }

});

//从素材库中选择图片
function ChoosePic(btn){
    btn.setVisible(false);
    var tabpanel=btn.findParentByType("tabpanel");
    var from2=tabpanel.down('form[name=form2]').getForm();
    from2.findField("tpMediaId").setValue("222");
    tabpanel.down('image[name=titileImage]').setHidden(false);
    tabpanel.down('button[name=deletePicBtn]').setHidden(false);
    tabpanel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png");

}
//删除图片
function deleteImage(btn){
	btn.setHidden(true);
	var tabpanel=btn.findParentByType("tabpanel");
    var from2=tabpanel.down('form[name=form2]').getForm();
    from2.findField("tpMediaId").setValue("");
	tabpanel.down('button[name=chooseScBtn]').setVisible(true);
	tabpanel.down('image[name=titileImage]').setHidden(true);
	tabpanel.down('image[name=titileImage]').setSrc("");
}

//从素材库中选择图文
function ChooseTw(btn){
    btn.setVisible(false);
    var tabpanel=btn.findParentByType("tabpanel");
    var from3=tabpanel.down('form[name=form3]').getForm();
    from3.findField("twTitle").setHidden(false);
    from3.findField("twMediaId").setValue("333");
    from3.findField("twTitle").setValue("北京创景咨询有限公司是由清华校友和海外人士创建的一家科技公司");
    tabpanel.down('button[name=deleteTwBtn]').setHidden(false);
    tabpanel.down('image[name=twImage]').setHidden(false);
    tabpanel.down('image[name=twImage]').setSrc(TzUniversityContextPath + "/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png");

}
//删除图文
function deleteTw(btn){
    btn.setHidden(true);
    var tabpanel=btn.findParentByType("tabpanel");
    var from3=tabpanel.down('form[name=form3]').getForm();
    from3.findField("twTitle").setHidden(true);
    from3.findField("twMediaId").setValue("");
    from3.findField("twTitle").setValue("");
    tabpanel.down('button[name=chooseTwBtn]').setVisible(true);
    tabpanel.down('image[name=twImage]').setHidden(true);
    tabpanel.down('image[name=twImage]').setSrc("");
}





