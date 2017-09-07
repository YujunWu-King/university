Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMessageInfo', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatMessage.weChatMsgController',
        'KitchenSink.view.weChat.weChatMessage.weChatMsgTagModel',
        'KitchenSink.view.weChat.weChatMessage.weChatMsgTagStore'
    ],
    extend : 'Ext.panel.Panel',
    controller:'weChatMsgController',
    autoScroll:false,
    xtype:'weChatMessageInfo',
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'发送微信消息',
    frame:true,
    sendMode:'',
    openIds:'',
    weChatTags:'',
    weChatAppId:'',
    listeners: {
        afterrender: function(panel){
        	//发送模式
        	var form=panel.down("form").getForm();
        	var sendMode=form.findField("sendMode").getValue();
        	var weChatAppId=form.findField("appId").getValue();
        	//从URL中获取参数信息
        	if(sendMode==''||weChatAppId==''){
        	   var url=window.top.location.href;
               var weChatAppId=GetQueryString(url,"appId");
               this.weChatAppId=weChatAppId;
		       form.findField("appId").setValue(weChatAppId);
		       
		       var sendMode=GetQueryString(url,"sendMode");
               this.sendMode=sendMode;
		       form.findField("sendMode").setValue(sendMode);
		       
		       var openIds=GetQueryString(url,"openIds");
               this.openIds=openIds;
		       form.findField("openIds").setValue(openIds);
		       
		       var tags=GetQueryString(url,"tags");
		       this.wechatTag=tags;
		       form.findField("wechatTag").setValue(tags);
		       
		     //如果为指定用户，按照标签字段隐藏；如果为按照标签，用户列表字段隐藏。
	        	if(sendMode=='A'){
	        		form.findField("wechatTag").setVisible(false);
	        	}
	        	if(sendMode=='B'){
	        		form.findField("openIds").setVisible(false);
	        	}
        	}
        }
    },
    initComponent:function(){
    	var tagStore = new KitchenSink.view.weChat.weChatMessage.weChatMsgTagStore();
    	/*var tagStore = new KitchenSink.view.weChat.weChatMessage.weChatMsgTagStore({
        	listeners:{
        		load:function(){
        			//var url=window.top.location.href;
        			var url="http://localhost:8080/university/index#SEM_A0000001982?appId=1&sendMode=B&tags=2";
                    var tags=GetQueryString(url,"tags");
                    if(tags!=""){
                    	this.wechatTag=tags;
                    	Ext.getCmp("wechatTag_20170830").setValue(tags);
                    }
        		}
        	}
        });
        var weChatTags=this.weChatTags;
        //加载URL模式的store
        if(this.weChatAppId!=""){
          tagStore.tzStoreParams='{"wxAppId":"' + this.weChatAppId + '"}';
        }*/
        tagStore.load();

        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                name:'msgForm',
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
                	name:'appId',
                	ignoreChangesFlag:true,
                	allowBlank:false
                },{
                    xtype: 'combo',
                    labelWidth: 100,
                    editable: false,
                    fieldLabel: '发送模式',
                    name: 'sendMode',
                    emptyText: '请选择',
                    mode: "remote",
                    hidden:true,
                    valueField: 'sendMode',
                    displayField: 'sendModeDesc',
                    allowBlank:false,
                    ignoreChangesFlag:true,
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
                    name:'openIds',
                    ignoreChangesFlag:true
                },{
                	xtype: 'combo',
                    labelWidth: 100,
                    fieldLabel: '按照标签',
                    name: 'wechatTag',
                    id:'wechatTag_20170830',
                    mode: "remote",
                    editable: false,
                    valueField: 'tagId',
                    displayField: 'tagName',
                    store:tagStore,
                    ignoreChangesFlag:true
                },/*{
                    xtype:'tagfield',
                    fieldLabel:'按照标签',
                    name:'wechatTag',
                    store:tagStore,
                    id:'wechatTag_20170830',
                    valueField: 'tagId',
                    displayField: 'tagName',
                    filterPickList: true,
                    createNewOnEnter: true,
                    createNewOnBlur: true,
                    enableKeyEvents: true,
                    queryMode: 'local',
                    listeners:
                    {
                        'select': function(combo,record,index,eOpts)//加入select监听事件，在输入框中输入字符执行选择后设为空值
                        {
                            var me = this;
                            me.inputEl.dom.value = "";
                        }
                    }
                    
                }*/,{
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
                            columnWidth:.4,
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
                                /*listeners:{
                                    click:function(bt, value, eOpts){
                                        ChoosePic(bt, value, eOpts);
                                    }
                                }*/
                                handler:'ChoosePic'
                            },{
                                layout: {
                                    type: 'column'
                                },
                                bodyStyle:'padding:10px 0 0 0',
                                xtype: 'form',
                                items: [{
                                    columnWidth:.46,
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
                                    text: '<span style="color:#459ae9;">删除</span>',
                                    //style:'width:60px;top:170px;border-width:0;box-shadow:none',
                                    border:false,
                                    style:{
            							background: 'white',
            							boxShadow:'none',
            							top:'170px'
            						},
                                    listeners:{ 
                                        click:function(bt, value, eOpts){ 
                                            deleteImage(bt, value, eOpts);
                                        }
                                    }
                                }]
                            }]
                        },{
                            columnWidth:.6,
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
                            columnWidth:.4,
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
                                handler:'ChooseTw'
                                /*listeners:{
                                    click:function(bt, value, eOpts){
                                        ChooseTw(bt, value, eOpts);
                                    }
                                }*/
                            },{
                                layout: {
                                    type: 'column'
                                },
                                bodyStyle:'padding:10px 0 0 0',
                                xtype: 'form',
                                items: [{
                                    columnWidth:.46,
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
                                	{   columnWidth:.46,
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
                                    text: '<span style="color:#459ae9;">删除</span>',
                                    //style:'width:60px;top:40px;background-color:white;border:0;outline:none',
                                    border:false,
                                    style:{
            							background: 'white',
            							boxShadow:'none',
            							bottom:0/*,
            							top:'40px'*/
            						},
                                    listeners:{
                                        click:function(bt, value, eOpts){
                                            deleteTw(bt, value, eOpts);
                                        }
                                    }
                                }]
                            }]
                        },{
                            columnWidth:.6,
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
                        margin:'20px 0 0 0px'
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




