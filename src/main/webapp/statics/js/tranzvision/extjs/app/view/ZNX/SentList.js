
Ext.define('KitchenSink.view.ZNX.SentList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.ZNX.MsgSentListModel',
        'KitchenSink.view.ZNX.MsgSentListStore',
        'KitchenSink.view.ZNX.MsgController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'SentList',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'MsgController',
    style:"margin:10px",
    multiSelect: true,
    minHeight:500,
    title: '发件箱列表',
    viewConfig: {
        preserveScrollOnRefresh: true,
        preserveScrollOnReload: true
    },
    /*viewConfig: {
        enableTextSelection: true
    },*/
    header:false,
    // frame: true,
    dockedItems:[
        {
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'querySentlist'},"-",
                //{text:"新增",tooltip:"新增控件",iconCls:"add",handler:'addKj'},"-",
                // {text:"查看",tooltip:"查看选中的信息",name:"toolbarEdit",iconCls:"edit",handler:'editMsg'},"-",
                {text:"删除",tooltip:"删除选中的信息",iconCls:"remove",handler:'SenterdeleteMsg'}
            ]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.ZNX.MsgSentListStore();
        Ext.apply(this, {
            columns: [{
                text: '站内信编号',
                dataIndex: 'MsgID',

                width:100
            },{
                //text: '收藏标记',
                dataIndex: 'MsgFlag',
                menuDisabled: true,
                text: '<span class="x-fa fa-heart"></span>',
                width: 40,
                renderer: function(value) {
                    return '<span class="x-fa fa-heart'+ (value ? '' : '-o') +'"></span>';
                }


            },{
                text: '接收人',
                dataIndex: 'RecID',
                minWidth:140
            },{
                text: '主题',
                dataIndex: 'MsgSub',
                minWidth:140,
                flex:1
            },{
                text: '时间',
                dataIndex: 'MsgDate',
                minWidth:140


            }/*,  {
             dataIndex: 'has_attachments',
             text: '<span class="x-fa fa-paperclip"></span>',
             width: 40,
             renderer: function(value) {
             return value ? '<span class="x-fa fa-paperclip"></span>' : '';
             }
             }*//*,
             {
             xtype: 'actioncolumn',
             header:'< img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  preview" />',
             width:70,
             renderer:function(v){
             if(v=='N'){
             return "失效";
             }else if(v=='Y'){
             return "生效";
             }
             }
             },{
             xtype: 'actioncolumn',
             header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.releaseOrUndo","发布/撤销") ,
             //sortable: false,
             minWidth:100,
             width:100,
             items:[
             {
             iconCls: '',
             tooltip: '',
             handler:'releaseOrUndo',
             getClass: function(v, metadata , r,rowIndex ,colIndex ,store ){
             if (store.getAt(rowIndex).get("msOprId")=='') {
             return '';
             }else{
             if(store.getAt(rowIndex).get("releaseOrUndo")=='Y'){
             metadata['tdAttr'] = "data-qtip=撤销";
             return 'revoke';
             }else{
             metadata['tdAttr'] = "data-qtip=发布";
             return 'publish';
             }
             }
             }
             }
             ]
             }*/
            ],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});

