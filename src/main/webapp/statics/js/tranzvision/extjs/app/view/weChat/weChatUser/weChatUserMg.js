Ext.define('KitchenSink.view.weChat.weChatUser.weChatUserMg',{
    extend:'Ext.grid.Panel',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatUser.weChatUserMgStore',
        'KitchenSink.view.weChat.weChatUser.weChatUserMgController'
    ],
    xtype:'weChatUserMg',
    controller:'weChatUserMgController',
    reference:'weChatUserMgPanel',
    name:'weChatUserMg',
    title:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.title","微信用户管理"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    jgId:'',
    wxAppId:'',
    columnLines:true,
    multiSelect:true,
    ignoreChangesFlag:true,
    plugins:[{
        ptype:'rowexpander',
        rowBodyTpl:new Ext.XTemplate(
            '<div class="x-grid-group-title" style="margin-left:80px;">',
            '<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" >',
            '<tr style="line-height:30px;">',
            '<td class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">标签</td>' +
            '<td style="font-weight: normal;">',
            '<tpl for="tagInfo">',
            '<tpl if="itemValue!=\'\'">',
            '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">{itemValue}</span>' +
            '</tpl>',
            '</tpl>',
            '</td>',
            '</tr>',
            '</table>',
            '</div>',{}
        ),
        lazyRender:true,
        enableCaching:false
    }],
    listeners:{
        afterrender:function() {
            var me = this;
            me.getView().on('expandbody',function(rowNode,record,expandRow,eOpts){
                if(!record.get("tagInfo")) {
                    var tagInfoArr = new Array();
                    var userTagDesc = record.get("userTagDesc");
                    var tagArr = userTagDesc.split(",");
                    for(var i=0;i<tagArr.length;i++) {
                        var tagObject = new Object();
                        tagObject.itemValue = tagArr[i];
                        tagInfoArr.push(tagObject);
                    }
                    record.set("tagInfo",tagInfoArr);

                    var store = me.store;
                    if(store.getModifiedRecords().length>0) {

                    } else {
                        store.commitChanges();
                    }
                }
            });
        }
    },
    viewConfig:{
        enableTextSelection:true
    },
    selModel:{
        type:'checkboxmodel'
    },
    header:false,
    frame:true,
    constructor:function(obj) {
        Ext.apply(this,obj);
        this.callParent();
    },
    initComponent: function () {
        var me = this;

        var userMgStore = new KitchenSink.view.weChat.weChatUser.weChatUserMgStore();

        //关注日期为当前日期
        /*
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = "00" + (nowDate.getMonth()+1);
        month = month.substr(month.length-2,2);
        var day = nowDate.getDate();

        var nowDateStr = year + "-" + month + "-" + day;

        var tzStoreParams = '{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW",' +
            '"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+Ext.tzOrgID+'","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"'+me.wxAppId+'","TZ_SUBSRIBE_DATE-operator":"01","TZ_SUBSRIBE_DATE-value":"'+nowDateStr+'"}}';
        userMgStore.tzStoreParams = tzStoreParams;
        userMgStore.reload();
        */


        Ext.apply(this,{
            dockedItems:[{
                xtype:'toolbar',
                items:[{
                    text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.query","查询"),
                    tooltip:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.query","查询"),
                    iconCls:'query',
                    handler:'queryUser'
                },"-",{
                    text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.preQuery","预查询"),
                    tooltip:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.preQuery","预查询"),
                    iconCls:'query',
                    menu:[{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.todayNew","今日新增用户"),
                        handler:'queryTodayNew'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.bind","已绑定用户"),
                        handler:'queryBind'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.typeTag","按类别标签"),
                        menu:this.initialData.tagData
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.unfollow","已取消关注"),
                        handler:'queryUnfollow'
                    }]
                },"-",{
                    text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.edit","设置标签"),
                    tooltip:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.edit","设置标签"),
                    iconCls:'edit',
                    handler:'setTag'
                },"->",{
                    xtype:'splitbutton',
                    text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.more","更多操作"),
                    iconCls:'list',
                    glyph:61,
                    menu:[{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.selectUser1","给选中用户发送普通消息"),
                        handler:'sendMessageForSelect'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.tagUser","按微信标签发送普通消息"),
                        handler:'sendMessageForTag'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.allUser1","给所有用户发送普通消息"),
                        handler:'sendMessageForAll'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.selectUser2","给选中用户发送模板消息"),
                        handler:'sendTplForSelect'
                    },{
                        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.allUser2","给所有用户发送模板消息"),
                        handler:'sendTplForAll'
                    }]
                }]
            },{
                xtype:'toolbar',
                dock:'bottom',
                ui:'footer',
                items:['->',{
                    minWidth:80,
                    text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.close","关闭"),
                    iconCls:'close',
                    handler:'closeUser'
                }]
            }],
            columns:[{
                xtype:'rownumberer',
                width:30
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.userPicture","用户头像"),
                dataIndex:'userPicture',
                align:'center',
                minWidth:120,
                renderer:function(value,cellmeta,record,rowIndex,columnIndex,store) {
                    var html = '<div style="margin:0 auto;background-image: url('+value+');height:50px;width:50px;'+
                            'background-size:100%;background-position-x:50%;background-position-y:50%;"></div>';
                    return html;
                }
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.nickName","用户昵称"),
                dataIndex:'nickName',
                minWidth:110
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.openId","OPEN_ID"),
                dataIndex:'openId',
                minWidth:150,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.followDttm","关注时间"),
                dataIndex:'followDttm',
                minWidth:160
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.associateUserId","关联用户ID"),
                dataIndex:'associateUserId',
                minWidth:130
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.associateUserName","关联用户名称"),
                dataIndex:'associateUserName',
                minWidth:140
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.associateClueId","关联线索ID"),
                dataIndex:'associateClueId',
                minWidth:130
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.associateClueName","关联线索名称"),
                dataIndex:'associateClueName',
                minWidth:140
            },{
                text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_USER_STD.operate","操作"),
                sortable:false,
                menuDisabled:true,
                draggable:false,
                minWidth:150,
                xtype:'actioncolumn',
                align:'center',
                items:[{
                    tooltip: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_HDBM_GL_STD.edit", "设置标签"),
                    iconCls: 'edit',
                    sortable:false,
                    handler: 'setTagForOne'
                },{
                    tooltip: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_HDBM_GL_STD.view", "关联/查看客户"),
                    iconCls: 'view',
                    sortable:false,
                    handler: 'viewUser'
                },{
                    tooltip: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_HDBM_GL_STD.view", "创建/查看销售线索"),
                    iconCls: 'view',
                    sortable:false,
                    handler: 'viewClue'
                },{
                    tooltip: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_HDBM_GL_STD.sms", "发送微信普通消息"),
                    iconCls: 'sms',
                    sortable:false,
                    handler: 'sendMessageForOne'
                }]
            }],
            store:userMgStore,
            bbar:{
                xtype:'pagingtoolbar',
                pageSize:20,
                store:userMgStore,
                plugins:new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});