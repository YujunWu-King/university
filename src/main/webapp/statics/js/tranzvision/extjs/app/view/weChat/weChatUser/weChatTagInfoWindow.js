Ext.define('KitchenSink.view.weChat.weChatUser.weChatTagInfoWindow',{
    extend:'Ext.window.Window',
    xtype:'weChatTagInfoWindow',
    controller:'weChatTagInfoController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.weChat.weChatUser.weChatTagInfoController'
    ],
    title:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAGXX_STD.title","标签信息"),
    width: 500,
    modal: true,
    actType:'',
    listWin:{},
    constructor:function(win) {
        this.listWin = win;
        this.callParent();
    },
    initComponent: function () {
        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout:{
                    type:'vbox',
                    align:'stretch'
                },
                border:false,
                bodyPadding:10,
                items:[{
                    xtype:'textfield',
                    name:'jgId',
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'wxAppId',
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'tagId',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAGXX_STD.tagName","标签名称"),
                    name: 'tagName',
                    allowBlank:false
                }]
            }]
        });
        this.callParent();
    },
    buttons:[{
        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.delete","删除"),
        iconCls:'delete',
        handler:'deleteTagInfo'
    },{
        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.save","保存"),
        name:'saveTagInfoBtn',
        iconCls:'save',
        handler:'saveTagInfo'
    },{
        text: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_WX_TAG_STD.ensure","确定"),
        name:'ensureTagInfoBtn',
        iconCls: "ensure",
        handler: 'saveTagInfo'
    },{
        text: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_WX_TAG_STD.close","关闭"),
        iconCls: "close",
        handler: 'closeTagInfo'
    }]
});