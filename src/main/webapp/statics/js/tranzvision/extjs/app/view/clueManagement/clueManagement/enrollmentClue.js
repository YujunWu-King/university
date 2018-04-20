Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClue',{
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
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueController',
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueStore',
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueModel'
    ],
    xtype: 'enrollmentClue',
    controller: 'enrollmentClueController',
    reference:'enrollmentCluePanel',
    name: 'enrollmentClue',
    title: '招生线索管理',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    columnLines:true,
    multiSelect:true,
    plugins:[
        {
            ptype: 'gridfilters',
            controller: 'enrollmentClueController'
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
                {text:"查询",tooltip:"查询",iconCls:"query",handler:"searchEnrollmentClue"},"-",
                {text:"新建线索",tooltip:"新建线索",iconCls:"add",handler:"addEnrollmentClue"},"-",
                {text:"查看问题线索",tooltip:"查看问题线索",iconCls:"view",handler:"viewProblemClue"},"-",
                {text:"批量调整选中线索责任人",tooltip:"批量调整选中线索责任人",iconCls:"set",handler:"giveClueBatch"},"-",
                {text:"开启自动分配",tooltip:"开启自动分配",iconCls:"set",name:"openAuto",handler:"openAutoAssign"},"-",
                {text:"关闭自动分配",tooltip:"关闭自动分配",iconCls:"set",name:"closeAuto",handler:"closeAutoAssign"},"-",
                {text:"选中线索自动分配",tooltip:"选中线索自动分配",iconCls:"set",handler:"autoAssignClue"},"->",
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
                            name:'viewEmailHistory',
                            handler:'viewEmailHistory'
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
            items:['->', {minWidth:80,text:"关闭",iconCls:"close",handler:"closeEnrollmentClue"}
            ]
        }
    ],
    listeners:{
        afterrender:function(panel) {
            var tzParams = '{"ComID":"TZ_XSXS_ZSXS_COM","PageID":"TZ_XSXS_ZSXS_STD","OperateType":"tzAutoAssignFlag","comParams":{}}';
            Ext.tzLoad(tzParams, function (response) {
                var autoFlag = response.autoFlag;
                var openAuto = panel.down("button[name=openAuto]");
                var closeAuto = panel.down("button[name=closeAuto]");
                if(autoFlag=="Y") {
                    openAuto.setDisabled(true);
                } else {
                    closeAuto.setDisabled(true);
                }

            });
        }
    },
    initComponent:function(){
        var enrollClueStore =new KitchenSink.view.clueManagement.clueManagement.enrollmentClueStore();
        //默认显示未关闭的数据
        enrollClueStore.tzStoreParams='{"cfgSrhId":"TZ_XSXS_ZSXS_COM.TZ_XSXS_ZSXS_STD.TZ_ZSXS_INFO_VW","defaultFlag":"Y","condition":{}}';

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
                                dataIndex:'name',
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
                                dataIndex:'mobile',
                                width:100,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'公司',
                                lockable   : false,
                                dataIndex:'companyName',
                                width:150,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'职位',
                                lockable   : false,
                                dataIndex:'position',
                                width:110,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'常住地',
                                lockable   : false,
                                dataIndex:'localAddress',
                                width:80,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'报考状态',
                                lockable   : false,
                                dataIndex:'applyState',
                                width:85,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'备注',
                                lockable   : false,
                                dataIndex:'memo',
                                width:120,
                                filter:{
                                    type:'string'
                                }
                            }
                        ]
                    },{
                        text:'线索信息',
                        menuDisabled:true,
                        columns:[
                            {
                                text:'线索状态',
                                lockable   : false,
                                dataIndex:"clueStateDesc",
                                width:80,
                                filter:{
                                    type:'list',
                                    options:leadStatusFilterOptions
                                }
                            },{
                                text:'责任人人员ID',
                                dataIndex:'chargeOprid',
                                hidden:true
                            },{
                                text:'责任人',
                                dataIndex:"chargeName",
                                width:85,
                                filter:{
                                    type:'string'
                                }
                            },/*{
                                text:'推荐人',
                                lockable   : false,
                                dataIndex:"recommendPer",
                                width:85,
                                filter:{
                                    type:'string'
                                }
                            },*/{
                                text:'创建时间',
                                lockable   : false,
                                dataIndex:'createDttm',
                                width:125,
                                renderer : Ext.util.Format.dateRenderer('Y-m-d H:i'),
                                /*xtype:'datecolumn',
                                format:'Y-m-d H:i',
                                 */
                                filter:{
                                    type:'date',
                                    format:'Y-m-d',
                                    active:true
                                }

                            },{
                                text:'创建方式',
                                lockable   : false,
                                dataIndex:'createWayDesc',
                                width:85,
                                filter:{
                                    type:'list',
									options:createWayFilterOptions
                                }
                            },{
                                text:'线索ID',
                                lockable   : false,
                                dataIndex:'clueId',
                                width:70,
                                filter:{
                                    type:'string'
                                }
                            },{
                                text:'关闭/退回原因',
                                lockable   : false,
                                dataIndex:'reason',
                                flex:1,
                                filter:{
                                    type:'string'
                                }
                            }
                        ]
                    }
                ],
                store:enrollClueStore,
                bbar:{
                    xtype:'pagingtoolbar',
                    pageSize:10,
                    store:enrollClueStore,
                    plugins: new Ext.ux.ProgressBarPager()
                }
        });
        this.callParent();
    }
});