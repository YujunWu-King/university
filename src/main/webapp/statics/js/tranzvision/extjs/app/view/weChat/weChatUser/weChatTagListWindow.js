Ext.define('KitchenSink.view.weChat.weChatUser.weChatTagListWindow',{
    extend:'Ext.window.Window',
    xtype:'weChatTagListWindow',
    controller:'weChatTagListController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.weChat.weChatUser.weChatTagListStore',
        'KitchenSink.view.weChat.weChatUser.weChatTagListController'
    ],
    title:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.title","标签列表"),
    reference:'weChatTagListWindow',
    name:'weChatTagListWindow',
    width: 600,
    height: 380,
    modal: true,
    layout:{
        type:'fit'
    },
    initComponent: function () {

        var tagStore = new KitchenSink.view.weChat.weChatUser.weChatTagListStore();

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                items: [{
                    xtype: 'displayfield',
                    name: 'jgId',
                    hidden: true
                }, {
                    xtype: 'displayfield',
                    name: 'wxAppId',
                    hidden: true
                }, {
                    xtype: 'displayfield',
                    name: 'openIdList',
                    hidden: true
                }, {
                    xtype: 'grid',
                    autoHeight: true,
                    columnLines: true,
                    frame: true,
                    style: 'border:0',
                    height: 300,
                    store: tagStore,
                    dockedItems: [{
                        xtype: 'toolbar',
                        items: [{
                            text: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.add", "新增标签"),
                            tooltip: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.add", "新增标签"),
                            iconCls: 'add',
                            handler: 'addTag'
                        }]
                    }],
                    columns: [{
                        text: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.selectFlag", "选择"),
                        dataIndex: 'selectFlag',
                        minWidth: 100,
                        xtype: 'checkcolumn',
                        ignoreChangesFlag: true
                    },{
                        text: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.tagName", "名称"),
                        dataIndex: 'tagName',
                        minWidth: 110,
                        flex: 1
                    }, {
                        text: Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.operate", "操作"),
                        sortable: false,
                        menuDisabled: true,
                        draggable: false,
                        minWidth: 80,
                        xtype: 'actioncolumn',
                        align: 'center',
                        items: [{
                            tooltip: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_WX_TAG_STD.edit", "编辑"),
                            iconCls: 'edit',
                            sortable: false,
                            handler: 'editTagInfo'
                        }]
                    }],
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 1000,
                        store: tagStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }]
            }]
        });
        this.callParent();
    },
    buttons:[{
        text:Ext.tzGetResourse("TZ_WX_USER_COM.TZ_WX_TAG_STD.save","保存"),
        name:'saveTagBtn',
        iconCls:'save',
        handler:'setTagForUser'
    },{
        text: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_WX_TAG_STD.ensure","确定"),
        name:'ensureTagBtn',
        iconCls: "ensure",
        handler: 'setTagForUser'
    },{
        text: Ext.tzGetResourse("TZ_HDBM_GL_COM.TZ_WX_TAG_STD.close","关闭"),
        iconCls: "close",
        handler: 'closeTagList'
    }]
});