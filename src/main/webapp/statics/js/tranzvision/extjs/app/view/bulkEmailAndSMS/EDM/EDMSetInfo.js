var refreshTaskMgr=new Ext.util.TaskRunner();
Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'EDMSetInfo',
    controller: 'EDMSetController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetController',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetStore'
    ],
    title: 'EDM统计',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    yjqfId:'',
    listeners: {
        close:function(t){
            for(var i =0;i<refreshTaskMgr.tasks.length;i++){
                if(refreshTaskMgr.tasks[i].id== t.pageId){
                    refreshTaskMgr.stop(refreshTaskMgr.tasks[i]);
                }
            }
        },
        afterrender:function(t){
            var params;
            var refreshTask = {
                run: function () {
                    if (t.yjqfId != " "){
                        params = '"emlQfId":"' + t.yjqfId + '"';
                        var tzParams = '{"ComID":"TZ_GK_EDM_COM","PageID":"TZ_VIEW_TDRZ_STD","OperateType":"QF","comParams":{' + params + '}}';
                        Ext.Ajax.request({
                            url: Ext.tzGetGeneralURL,
                            params: {tzParams: tzParams},
                            success: function (response, opts) {
                                //返回值内容
                             var jsonText = response.responseText;
                             var jsonObject = Ext.util.JSON.decode(jsonText);
                             var status=jsonObject.comContent.runStatus;
                             if (status==""||status=="1" || status=="2"||status=="3"||status=="8"||status=="9") {
                                 t.down('button[reference=runTxYqBtn]').setDisabled(false);
                             } else {
                                t.down('button[reference=runTxYqBtn]').setDisabled(true);
                             }
                        }
                        });
                    }
                },
                interval: 5000,
                scopt: this
            };
            refreshTaskMgr.start(refreshTask);
        }
    },
   initComponent: function(){
   var store=new KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetStore();
   Ext.apply(this, {
        dockedItems: [
            {xtype: "toolbar",
                dock: "bottom",
                ui: "footer",
                items: ['->', {minWidth: 80, text: "关闭", iconCls: "close", handler: "onEDMSetClose"} ]
            }
        ],
        items: [
            {
                xtype: 'form',
                reference: 'themeForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: '邮件ID',
                        name: 'emailID',
                        hidden: true
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '邮件主题',
                        name: 'emailSubject'
                    },
                    {
                        xtype: 'displayfield',
                        name: 'emailSender',
                        fieldLabel: '发件人'
                    },
                    {
                        xtype: 'displayfield',
                        name: 'emailReceiver',
			            autoScroll:false,
                        fieldLabel: '收件人'
                    },
                    {
                        xtype:'textfield',
                        name:'proStat',
                        fieldLabel:'进程状态',
                        value:'SUC',
                        hidden:true,
                        listenners:{
                            change:function(proStateField, newValue, oldValue, eOpts){
                               // if(newValue="")
                            }
                        }
                    },
                    {
                        layout: {
                            type:'hbox'
                        },
                        padding:'0 0 0 0',
                        xtype:"toolbar",
                        items:["->",{
                            xtype:'button',
                            text:'<span style="color:#fff">查看收件人</span>',
                            handler:'viewReceiver',
			                autoScroll:false,
                            baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
                        },{
                            xtype:'button',
                            text:'<span style="color:#fff">运行退信引擎</span>',
                            handler:'runTuiXinYq',
                            reference:'runTxYqBtn',
                            baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
                        },{
                            xtype:'button',
                            text:'<span style="color: #fff">查看运行状态</span>',
                            handler:'viewTxQk',
                            baseCls:'x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-small x-btn-inner x-btn-inner-default-small'
                        }]
                    },
                    {
                        xtype: 'displayfield',
                        name: 'sendTime',
                        fieldLabel: '发送时间'
                    },
                  {     xtype: 'displayfield',
                        fieldLabel:'邮件内容',
                        resizable:false,
                        autoScroll: false,
                        name: 'emailContent',
                        zIndex:999
                        }
                ]
            },{
				xtype: 'panel',
                title: '指标统计',
				border: false,
                bodyPadding: 0,
				layout: {
					type: 'column'
				},
				items:[{
					columnWidth:.5,	
					xtype: 'component',
					name:'funnelPic'
				},{
					columnWidth:.5,
					xtype: 'grid',
					frame: false,
					//title: '指标统计',
					columnLines: true,
					//style: "margin:0px 8px 8px 8px",
					store: {
						type: 'EDMSetStore'
					},
					columns: [
						{
							text: '指标名称',
							dataIndex: 'zhiBiaoName',
							width: 100,
							flex:1
						},
						{
							text: "数量",
							xtype: 'linkcolumn',
							dataIndex: 'zhiBiaoNum',
							width: 90,
							flex:1,
							sortable: false,
							handler: 'viewZhiBiaoItemNum',
							renderer: function (v) {
								this.text = v;
								return;
							}
						},
						{
							text: '百分比',
							dataIndex: 'zhiBiaoPercent',
							width: 90,
							flex:1
						},
						{
							xtype: 'linkcolumn',
							text: '注释',
							width: 90,
							sortable: false,
							items: [
								{
									text: '注释',
									tooltip: '',
									getClass: function (v, meta, record, rowIndex) {
										var zhiBiaoDesc = record.data.zhiBiaoDesc;
										if (zhiBiaoDesc == 'FSZS') {
											meta['tdAttr'] = "data-qtip='一次邮件群发活动，发送邮件总数'";//动态设置tooltip
										} else if (zhiBiaoDesc == 'FSCGS') {
											meta['tdAttr'] = "data-qtip='一次邮件群发活动，发送邮件成功数'";
										} else if (zhiBiaoDesc == 'YTS') {
											meta['tdAttr'] = "data-qtip='一次邮件群发活动，因邮件地址不存在、邮件地址被注销、邮件地址拼写错误等原因造成的退信'";
										} else if (zhiBiaoDesc == 'RTS') {
											meta['tdAttr'] = "data-qtip='一次邮件群发活动，因邮箱满、ISP临时拒信、网络拥堵、网络故障等原因造成的退信'";
										} else if (zhiBiaoDesc == 'ZDKS') {
											meta['tdAttr'] = "data-qtip='在同一封邮件活动中，邮件被打开的总次数'";
										} else if (zhiBiaoDesc == 'JDKS') {
											meta['tdAttr'] = "data-qtip='在同一封邮件活动中，邮件被打开的总次数（不包含重复打开数）'";
										} else if (zhiBiaoDesc == 'ZDJS') {
											meta['tdAttr'] = "data-qtip='在同一封邮件活动中，邮件/链接被点击的总次数'";
										} else if (zhiBiaoDesc == 'JDJS') {
											meta['tdAttr'] = "data-qtip='在同一封邮件活动中，独立点击一封邮件中的一个或多个链接的收件人总数'";
										} else if (zhiBiaoDesc == 'TDS') {
											meta['tdAttr'] = "data-qtip='一次邮件群发活动，不愿意再次收到该邮件的收件人'";
										}
									}
								}
							]
						}
					],
					store:store,
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
				}]
			}
        ]
     })
 this.callParent();
},
 constructor: function (config) {
     this.callParent();
     var runTxYqBtn=this.lookupReference("runTxYqBtn");
 }
});


