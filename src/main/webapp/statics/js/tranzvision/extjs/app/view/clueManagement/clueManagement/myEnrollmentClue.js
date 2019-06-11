Ext.define('KitchenSink.view.clueManagement.clueManagement.myEnrollmentClue',{
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'tranzvision.extension.grid.Exporter',
        'KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueController',
        'KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueStore',
        'KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueModel'
    ],
    xtype: 'myEnrollmentClue',
    controller: 'myEnrollmentClueController',
    reference:'myEnrollmentCluePanel',
    name: 'myEnrollmentClue',
    title: '我的招生线索',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    columnLines:true,
    multiSelect:true,
    plugins:[
        {
            ptype: 'gridfilters',
            controller: 'myEnrollmentClueController'
        }
    ],
    viewConfig: {
        enableTextSelection: true
    },
    selModel: {
        type: 'checkboxmodel'
    },
    header:false,
    frame:true,
    dockedItems:[
        {
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询",iconCls:"query",handler:"searchMyEnrollmentClue"},"-",
                {text:"新建线索",tooltip:"新建线索",iconCls:"add",handler:"addMyEnrollmentClue"},"->",
                {
                    xtype:'splitbutton',
                    text:"更多操作",
                    iconCls: 'list',
                    glyph:61,
                    menu:[
                        {
                            text:'导出查询结果',
                            iconCls:'excel',
                            handler:'exportSearchExcel'
                        },{
                            text:"查看历史导出并下载",
                            iconCls:'download',
                            handler:'downloadHisExcel'
                        },{
                            text:'批量导入线索',
                            iconCls:'excel',
                            name:'importExcel',
                            handler:'importExcel'
                        },{
                            text:'批量发送邮件',
                            iconCls:'email',
                            name:'sendEnrollClueEmails',
                            handler:'sendEnrollClueEmails'
                        },{
                            text:'查看邮件发送历史',
                            iconCls:'email',
                            name:'viewEnrollClueEmailsHis',
                            handler:'viewEnrollClueEmailsHis'
                        },{
        					text:'批量发送短信',
        					iconCls:'publish',
                            name:'sendEmlSelPers',
        					handler:'sendSmsSelPers'
                        },,{
        					text:'查看短信发送历史',
        					iconCls:'publish',
                            name:'viewSmsHistory',
        					handler:'viewSmsHistory'
                        },{
                            text:'快速处理线索',
                            iconCls:'set',
                            menu:[
                                {
                                    text:"过往状态",
                                    handler:"viewClueOldState"
                                },{
                                    text:"退回",
                                    handler:"dealWithBack"
                                },{
                                    text:"关闭",
                                    handler:"dealWithClose"
                                },{
                                    text:"转交",
                                    handler:"dealWithGive"
                                },{
                                    text:"延迟联系",
                                    handler:"dealWithDelayContact"
                                }
                            ]
                        }
                    ]
                }
            ]
        },{
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"closeMyEnrollmentClue"}
            ]
        }
    ],
    initComponent:function(){
        var me = this;

        var myEnrollClueStore =new KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueStore();
        //默认显示未关闭的数据
        myEnrollClueStore.tzStoreParams='{"cfgSrhId":"TZ_XSXS_MYXS_COM.TZ_XSXS_MYXS_STD.TZ_XSXS_INFO_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID +'","TZ_LEAD_STATUS-operator": "02","TZ_LEAD_STATUS-value": "G"}}';


        //类别
        var initData=[];
        var colorSortFilterOptions=[];//类别的过滤器
        var validColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_XS_XSLB_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_COLOUR_SORT_ID,TZ_COLOUR_NAME,TZ_COLOUR_CODE',
            listeners: {
                load: function (store, records, successful, eOpts) {
                    for (var i = 0; i < records.length; i++) {
                        colorSortFilterOptions.push([records[i].data.TZ_COLOUR_SORT_ID, records[i].data.TZ_COLOUR_NAME]);
                    }
                }
            }
        });

        //所有的类别
        var allColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_XS_XSLB_V',
            condition:{
            },
            result:'TZ_COLOUR_SORT_ID,TZ_COLOUR_NAME,TZ_COLOUR_CODE',
            listeners: {
                load: function (store, records, successful, eOpts) {
                    for (var i = 0; i < records.length; i++) {
                        initData.push(records[i].data);
                    }
                }
            }
        });
        allColorSortStore.load();

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOUR_SORT_ID","TZ_COLOUR_NAME","TZ_COLOUR_CODE"
            ],
            data:initData
        });



        //状态
        var leadStatusFilterOptions=[];//状态的过滤器
        var leadStatusStore =  new KitchenSink.view.common.store.appTransStore("TZ_LEAD_STATUS");
        leadStatusStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                leadStatusFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        //创建方式
        var createWayFilterOptions=[];//创建方式的过滤器
        var createWayStore =  new KitchenSink.view.common.store.appTransStore("TZ_RSFCREATE_WAY");
        createWayStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                createWayFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        //报考状态
        var bkStatusFilterOptions=[];//报考状态的过滤器
        var bkStatusStore =  new KitchenSink.view.common.store.appTransStore("TZ_LEAD_BMB_STATUS");
        bkStatusStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                bkStatusFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        Ext.apply(this,{
            columns:[
                {
                    xtype:'rownumberer',
                    width:30
                },
                {
                    text:'客户信息',
                    menuDisabled:true,
                    columns:[
                        {
                            text:'姓名',
                            lockable   : false,
                            dataIndex:'cusName',
                            width:70,
                            filter:{
                                type:'string'
                            },
                            xtype:'linkcolumn',
                            handler:'editSelClueInfo',
                            renderer:function(v) {
                                this.text=v;
                                this.tooltip=v;
                                return;
                            }
                        },{
                            text:'手机',
                            lockable   : false,
                            dataIndex:'cusMobile',
                            width:105,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'邮箱',
                            lockable: false,
                            dataIndex:'email',
                            width:120,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'公司',
                            lockable   : false,
                            dataIndex:'comName',
                            width:120,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'职位',
                            lockable   : false,
                            dataIndex:'cusPos',
                            width:100,
                            filter:{
                                type:'string'
                            }
                        },/*{
                            text:'常住地',
                            lockable   : false,
                            dataIndex:'cusPlace',
                            width:70,
                            filter:{
                                type:'string'
                            }
                        },*/{
                            text:'报考状态',
                            lockable:false,
                            dataIndex:"bmStateDesc",
                            width:80,
                            filter:{
                                type:'list',
                                options:bkStatusFilterOptions
                            }
                        },{
                            text:'备注',
                            dataIndex:'cusBz',
                            width:100,
                            filter:{
                                type:'string'
                            }
                        },{
                        	text:'联系报告',
    	                    lockable   : false,
    	                    menuDisabled:false,
    	                    resizable: true,
    	                    minwidth:	80,
    	                    xtype:'actioncolumn',
    	                    align:'center',
    	                    items:[
    	                        {iconCls:'audit',tooltip:'查看和添加联系报告',handler:'addContactReport',
                                    isDisabled: function (view, rowIndex, colIndex, item, record) {
                                    	var clueId = record.data.clueId;
    		                        	var length;
    		                        	var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_CONNECT_RPT_STD","OperateType":"QF","comParams":{"leadID":"'+clueId+'"}}';
    		                            Ext.tzLoadAsync(tzParams, function (responseData) {
    	                                    length = responseData.reports.length;
    	                                });
    		                            if(length == 0){
    		                            	return true;
    		                            }
    		                            return false;
                                    }}
    	                    ]
                        },{
                        	text:'查看活动',
    	                    lockable   : false,
    	                    menuDisabled:false,
    	                    minwidth:	80,
    	                    xtype:'actioncolumn',
    	                    align:'center',
    	                    items:[
    	                        {iconCls:'audit',tooltip:'查看活动',handler:'seeActivity'/*,
    		                        getClass : function(v, metadata, record){
    		                        	var clueId = record.data.clueId;
    		                        	var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"queryAct","comParams":{"clueId":"'+clueId+'"}}';
    		                            Ext.tzLoad(tzParams, function (responseData) {
    	                                    var root = responseData.root;
    	                                    console.log(root.length)
    	                                    if(root.length == 0){
    	                                    	//return 'x-hidden';
    	                                    }else{
    	                                    	return 'icon-circle-red';
    	                                    }
    	                                });
    		                        	
    		                        }*/,
                                    isDisabled: function (view, rowIndex, colIndex, item, record) {
                                    	var clueId = record.data.clueId;
    		                        	var length;
    		                        	var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"queryAct","comParams":{"clueId":"'+clueId+'"}}';
    		                            Ext.tzLoadAsync(tzParams, function (responseData) {
    	                                    length = responseData.total;
    	                                });
    		                            if(length == 0){
    		                            	return true;
    		                            }
    		                            return false;
                                    }}
    	                    ]
                        }
                    ]
                },{
                    text:'线索信息',
                    menuDisabled:true,
                    columns:[
                        {
                            text:'机构ID',
                            dataIndex:'orgID',
                            hidden:true
                        },{
                            text:'线索状态',
                            lockable:false,
                            dataIndex:"clueStateDesc",
                            width:90,
                            filter:{
                                type:'list',
                                options:leadStatusFilterOptions
                            }
                        },{
                            text:'责任人',
                            lockable:false,
                            dataIndex:"zrPer",
                            width:100,
                            filter:{
                                type:'string'
                            }
                        },/*{
                            text:'推荐人',
                            lockable:false,
                            dataIndex:"recommendPer",
                            width:70,
                            filter:{
                                type:'string'
                            }
                        },*/{
                            text:'创建时间',
                            lockable:false,
                            dataIndex:'createDate',
                            width:125,
                            renderer : Ext.util.Format.dateRenderer('Y-m-d H:i'),
                            /*
                            xtype:'datecolumn',
                            format:'Y-m-d  H:i',
                            */
                            filter:{
                                type:'date',
                                format:'Y-m-d',
                                active:true
                            }
                        },{
                            text:'创建方式',
                            lockable:false,
                            dataIndex:'createWayDesc',
                            width:90,
                            filter:{
                                type:'list',
                                options:createWayFilterOptions
                            }
                        },/*{
                            text:'线索ID',
                            lockable:false,
                            dataIndex:'clueId',
                            width:70,
                            filter:{
                                type:'string'
                            }
                        },*/{
                            text:'类别',
                            dataIndex:'colorType',
                            lockable:false,
                            width:130,
                            hidden:false,
                            filter:{
                                type:'list',
                                options:colorSortFilterOptions
                            },
                            renderer:function(v) {
                                if (v) {
                                    var rec = allColorSortStore.find('TZ_COLOUR_SORT_ID', v, 0, false, true, true);
                                    if (rec > -1) {
                                        return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #" + allColorSortStore.getAt(rec).get("TZ_COLOUR_CODE") + "'></div><div style='margin-left:40px;'>" + allColorSortStore.getAt(rec).get("TZ_COLOUR_NAME") + "</div>";
                                    } else {
                                        return " ";
                                    }
                                }
                            }
                        },{
                            text:'关闭原因',
                            lockable:false,
                            dataIndex:'gbyy',
                            flex:1,
                            minWidth: 100,
                            filter:{
                                type:'string'
                            }
                        }
                    ]
                }
            ],
            store:myEnrollClueStore,
            bbar:{
                xtype:'pagingtoolbar',
                pageSize:20,
                store:myEnrollClueStore,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
