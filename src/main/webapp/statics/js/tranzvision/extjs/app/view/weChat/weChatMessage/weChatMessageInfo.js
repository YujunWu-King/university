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
                            {sendMode: "N", sendModeDesc: "按照标签"}
                        ]
                    }
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
                        data: [ {tagId: 0, tagName: 'Battlestar Galactica'},
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
                    resizeTabs: true,
                    defaults: {
                        autoScroll: false
                    },
                    items:[{
                        title:'文字消息',
                        xtype:'form',
                        name:'wordMessageForm1',
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
                        title:'图片消息',
                        xtype:'form',
                        name:'certMergHtml2',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        style:'border:0',
                        items:[{
                                html:'<br><div id="picWordDiv" style="display:table-cell;height:200px;width:400px;border:1px dotted #d9dadc;line-height:30px;text-align:center;vertical-align:middle;font-size:38px;color:#c0c0c0" onclick="ChoosePic()">+<br><span style="font-size:18px;">从素材库中选择</span></div>'
                        }]
                    },{
                        title:'图文消息',
                        xtype:'form',
                        name:'certMergHtml3',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        style:'border:0',
                        items:[{
                            html:'<br><div id="picWordDiv" style="display:table-cell;height:200px;width:400px;border:1px dotted #d9dadc;line-height:30px;text-align:center;vertical-align:middle;font-size:38px;color:#c0c0c0" onclick="ChoosePic()">+<br><span style="font-size:18px;">从素材库中选择</span></div>'
                            //html:'<br><div id="picWordDiv" style="height:200px;width:400px;border:1px dotted #d9dadc;display:block;line-height:200px;text-align:center;font-size:38px;color:#c0c0c0" onclick="test()">+<br>从素材库中选择</div>'
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
                         fieldLabel: '发送模式',
                         name: 'sendStatus',
                         mode: "remote",
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
                         value:'N'
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
    },
    //图片消息
    getPicList:function(){
        var me = this, predefinetpl = '';
        if(!me.isLoaded){
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJGL_STD","OperateType":"QF","comParams":""}';
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
                    var responseData1 = Ext.JSON.decode(resText1);
                    var resText = responseData1.comContent;
                    var responseData = resText;
                    for(var i in responseData){
                        predefinetpl += '<div class="tplitem" style="padding: 10px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;text-align:center;width:150px;" onclick="wjdc_pre(this)" data-id="'+responseData[i].tplid+'"><img src="' + TzUniversityContextPath + '/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png"><br><span class="tplname" title="' + responseData[i].tplname + '">' + Ext.String.ellipsis(responseData[i].tplname,16,true) + '</span></div>';
                    }
                    me.isLoaded = true;
                }
            });
        }
        return predefinetpl;
    }

});

function ChoosePic(){
    alert("从素材中添加图片");
}





