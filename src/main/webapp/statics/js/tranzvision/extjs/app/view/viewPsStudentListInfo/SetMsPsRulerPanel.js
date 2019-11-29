Ext.define('KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanel', {
	extend: 'Ext.panel.Panel',
	xtype: 'setmspsruler',
	controller: 'setrulercontroller',
	actType: 'add',
	requires: ['Ext.data.*', 'Ext.grid.*', 'Ext.util.*', 'Ext.toolbar.Paging', 'Ext.ux.ProgressBarPager', 'KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelController', 'Ext.selection.CellModel', 'KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelStore', 'KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelModel'],
	/*autoScroll: false,*/
	actType: 'add',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.MSPSKSMD", "设置面试规则"),
	frame: true,
/*listeners: {
     resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
     var buttonHeight = 42; button height plus panel body padding
     var formHeight = 30;
     var formPadding = 20;
     var grid = panel.child('grid[name=appFormApplicants]');
     grid.setHeight(height - formHeight - buttonHeight - formPadding);
     }
     },*/
	initComponent: function() {
/**
 * 修改人：丁鹏
 * 时间:2019年11月19日10:09:59
 * sdstore、storeGly、storeMsz
 * */
		let sdstore = Ext.create("Ext.data.Store", {
            fields: ["TValue", "TSDesc"],
            data: [{
                TSDesc: "上午",
                TValue: 'A'
            }, {
                TSDesc: "下午",
                TValue: 'B'
            }]
        });
        var store = new KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelStore();
        var storeGly = new Ext.data.Store({
            fields: ['glyId', 'glyZh', 'glyName', 'glyLb', 'glyState'],
            pageSize: 200,
            autoLoad: true,
            comParams: '',
            proxy: new Ext.data.proxy.Ajax(
                {
                    url: Ext.tzGetGeneralURL(),
                    reader:
                        {
                            type: 'json',
                            totalProperty: 'comContent.total',
                            rootProperty: 'comContent.root',
                            messageProperty: 'state.errdesc'
                        }
                }).setExtraParams({tzParams: '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"GETGLY","comParams":""}'})
        });
        var storeMsz = new Ext.data.Store({
            fields: ['mszid', 'mszmj','mszsd', 'mszkssj', 'mszjssj'],
            pageSize: 200,
            autoLoad: true,
            comParams: '',
            proxy: new Ext.data.proxy.Ajax(
                {
                    url: Ext.tzGetGeneralURL(),
                    reader:
                        {
                            type: 'json',
                            totalProperty: 'comContent.total',
                            rootProperty: 'comContent.root',
                            messageProperty: 'state.errdesc'
                        }
                }).setExtraParams({tzParams: '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"MSZLIST","comParams":""}'})
        });


		Ext.apply(this, {

			items: [{
				xtype: 'form',
				frame: true,
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				margin: '8px',
				style: 'border:0px',

				items: [{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.classIdname", "报考班级"),
					name: 'className',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true
					//value: '105'

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.batchIdname", "批次"),
					name: 'batchName',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true
					//value: '2'

				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.classId", "班级编号"),
					name: 'classId',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				hidden:true
					//value: '105'

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.batchId", "批次编号"),
					name: 'batchId',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				hidden:true
					//value: '2'

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.ksNum", "报考考生数量"),
					name: 'ksNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true

				/*}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.reviewClpsKsNum", "材料审批考生"),
					name: 'reviewClpsKsNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true */
 
				}, {
					layout: {
						type: 'hbox'
					},
					items: [{
						items: [{
							//                                xtype: 'datefield',
							xtype: 'textfield',
							name: 'reviewKsNum',
							fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.reviewKsNum", "参与面试考生"),
							// cls:'lanage_1',
							//                                format: 'Y-m-d h:i',
							//readOnly:true
							allowBlank: false,
							fieldStyle:'background:#F4F4F4',
    				        readOnly:true
						}]
					}, {
						items: [{
							xtype: 'button',
							style: 'margin-left:15px',
							text: "查看考生",
							handler: "viewStudentInfo"
						}]
					}]
				}, {
					layout: {
						type: 'hbox'
					},
					items: [{
						items: [{
							//                                xtype: 'datefield',
							xtype: 'datefield',
							fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.startDate", "开始日期"),
							format: 'Y-m-d',
							name: 'StartDate',
							//afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
							//allowBlank: false


						}]
					}, {
						items: [{
							style: 'margin-left:15px',
							xtype: 'timefield',
							fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.startTime", "时间"),
							format: 'H:i',
							name: 'StartTime',
							//afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
							//allowBlank: false,
							value: "08:30"



						}]
					}]
				}, {
					layout: {
						type: 'hbox'
					},
					items: [{
						items: [{
							//                                xtype: 'datefield',
							xtype: 'datefield',
							fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.end Date", "结束日期"),
							format: 'Y-m-d',
							name: 'EndDate',
							//afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
							//allowBlank: false


						}]
					}, {
						items: [{
							style: 'margin-left:15px',
							xtype: 'timefield',
							fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.end Time", "时间"),
							format: 'H:i',
							name: 'EndTime',
							//afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
							//allowBlank: false,
							value: "08:30"



						}]
					}]
				}, {
					xtype: 'label',
					text: '请注意：开始结束时间仅作为提醒评委使用，不会作为自动开关使用。',
					cls: 'lable_1',
					style: "margin:0px"
				},

				{
					xtype: 'tabpanel',
					frame: true,
					activeTab: 0,
					plain: false,
					resizeTabs: true,
					listeners: {
						beforetabchange: 'beforeOnTabchange'
					},

					items: [{
						title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.interviewExplain", "面试评审说明"),
						xtype: 'form',
						// name:'adcertMergHtml',
						layout: {
							type: 'vbox',
							align: 'stretch'
						},
						height: 285,
						style: 'border:0',
						items: [{
							xtype: 'ueditor',
							name: 'desc',
							zIndex: 999,
							height: 285,
							allowBlank: true

						}]
					}, {
						title: "面试评委",
						xtype: 'form',
						name: 'pwlbgrid',
				        minHeight: 285,
				        autoHeight:true,
						style: 'border:0',
						items: [{
							xtype: 'grid',
							columnLines: true,
							style: "margin:8px",
							name: 'adprjgrid',
							layout: 'fit',
							minHeight: 200,
							autoHeight:true,
							plugins: {
								ptype: 'cellediting'
							},
							reference: 'pwlistgrid',
							selModel: {
								type: 'checkboxmodel'
							},
							dockedItems: [{
								xtype: "toolbar",
								items: [{
									xtype: 'numberfield',
									fieldLabel: '每位考生要求被',
									//allowBlank: false,
									minValue: 0,
									maxValue: 20,
									//value: 0,
									name: 'ksRevedpwnum',
									fieldStyle: 'font-weight: bold;',
									width: 180,
									hidden: true
								}, {
									columnWidth: .5,
									xtype: 'displayfield',
									hideLabel: true,
									value: '个评委审批',
									fieldStyle: 'font-weight: bold;margin-top: 7.5px !important;',
									hidden: true
								}, 
								//"-",
								{
									xtype: 'numberfield',
									fieldLabel: '一共',
									labelWidth: 50,
									//allowBlank: false,
									minValue: 0,
									maxValue: 20,
									//value: 0,
									name: 'countTeamnum',
									fieldStyle: 'font-weight: bold;',
									width: 130,
									hidden: true
								}, {
									columnWidth: .5,
									xtype: 'displayfield',
									hideLabel: true,
									value: '个评委组',
									fieldStyle: 'font-weight: bold;margin-top: 7.5px !important;',
									hidden: true
								}, 
								//"-",
								{
									text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.addpw", "新增评委"),
									tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.adddata", "新增评委"),
									iconCls: "add",
									handler: 'addpwInfom'
								}, 
								//"->",
								/*{
									text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.edit1", "编辑"),
									tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.editdata", "编辑"),
									iconCls: "edit",
									handler: 'importScore'
								}, "->",*/
								{
									xtype: 'splitbutton',
									text: '更多操作',
									iconCls: 'list',
									glyph: 61,
									menu: [{
										text: '批量重置选中评委密码',
										handler: 'batcResetPassword'
									}, {
										text: '批量导出评委',
										handler: 'exportPwinform'
									}]
								}

								]
							}],

							columns: [{
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.judgId", "评委账号"),
								dataIndex: 'judgId',
								width: 130,
								hidden:true
							},{
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.judgzh", "评委账号"),
								dataIndex: 'judzhxx',
								width: 130
							}, {
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.judgName", "评委姓名"),
								dataIndex: 'judgName',
								width: 130
							}, {

								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.judgGroupId", "所属评委组"),
								dataIndex: 'judgGroupId',
								name: 'judgGroup',
								width: 200,
								editor: {
									xtype: 'tagfield',
									//xtype: 'combobox',
									valueField: 'TZ_CLPS_GR_ID',
									displayField: 'TZ_CLPS_GR_NAME',
									store: []
									//store: ''
								},
								flex: 1,
								
								//动态renderer
								renderer: 'readervalue'
							},{
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.yypw", "英语评委"),
								xtype:'checkcolumn',
								dataIndex: 'judgType',
								width: 100,
								sortable:false
							},{
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.groupLeader", "组长"),
								xtype:'checkcolumn',
								dataIndex: 'groupleader',
								width: 100,
								sortable:false
							}, {								
								text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.judgState", "评委状态"),
								dataIndex: 'judgState',
								width: 130,
								editor: {
						        xtype: 'combobox',
						        valueField: 'TValue',
                                displayField: 'TSDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_MSPS_PWZT")
					            },
					            renderer: function(v) {
						           if (v == 'A') {
							       return "正常";
						          }  else {
						            	return "暂停";
						          }
					          }
							},{
					            text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.handle", "操作"),
					            menuDisabled: true,
					            sortable: false,
					            width: 110,
					            align: 'center',
					            xtype: 'actioncolumn',
					            flex: 1,
					            items: [{
						          iconCls: 'remove',
						          tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.delete", "删除"),
						          handler: 'deleteMsPw'
					            }]
				             }],
							store: store,	
							bbar: {
			                    xtype: 'pagingtoolbar',
			                    pageSize: 200,
			                    store: store,
			                    displayInfo: true,
			                    displayMsg:"显示{0}-{1}条，共{2}条",
			                    beforePageText:"第",
			                    afterPageText:"页/共{0}页",
			                    emptyMsg: "没有数据显示",
			                    plugins: new Ext.ux.ProgressBarPager()
			                }
						}]
					},
					/**
					 * 修改：丁鹏
					 * 时间：2019/11/18
					 */
					{
                        title: "管理员",
                        xtype: 'form',
                        name: 'glygrid',
                        //id: 'glygrid',
                        minHeight: 285,
                        autoHeight: true,
                        style: 'border:0',
                        items: [{
                            xtype: 'grid',
                            columnLines: true,
                            style: "margin:8px",
                            name: 'glyLgrid',
                            layout: 'fit',
                            minHeight: 200,
                            autoHeight: true,
                            plugins: {
                                ptype: 'cellediting'
                            },
                            reference: 'glylistgrid',
                            dockedItems: [{
                                xtype: "toolbar",
                                items: [
                                    {
                                        text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.addgly", "新增管理员"),
                                        tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.addgly", "新增管理员"),
                                        iconCls: "add",
                                        handler: 'addGlyInfom'
                                    }
                                ]
                            }],
                            columns: [{
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.glyId", "管理员ID"),
                                dataIndex: 'glyId',
                                width: 200,
                                hidden: true
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.glyZh", "管理员账号"),
                                dataIndex: 'glyZh',
                                width: 200
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.glyName", "管理员姓名"),
                                dataIndex: 'glyName',
                                width: 200
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.glyLb", "管理员类别"),
                                dataIndex: 'glyLb',
                                name: 'glyLb',
                                width: 200,
                                editor: {
                                    xtype: 'combobox',
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    store: new KitchenSink.view.common.store.appTransStore("TZ_MS_GLY")
                                },
                                renderer: 'readervalueLb'
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.glyState", "管理员状态"),
                                dataIndex: 'glyState',
                                width: 200,
                                editor: {
                                    xtype: 'combobox',
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    hiddenValue: 'TValue',
                                    store: new KitchenSink.view.common.store.appTransStore("TZ_EFF_STATUS")
                                },
                                renderer: function (v) {
                                    if (v == 'A') {
                                        return "有效";
                                    } else {
                                        return "无效";
                                    }
                                }
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.handle", "操作"),
                                menuDisabled: true,
                                sortable: false,
                                width: 110,
                                align: 'center',
                                xtype: 'actioncolumn',
                                flex: 1,
                                items: [{
                                    iconCls: 'remove',
                                    tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.delete", "删除"),
                                    handler: 'deleteGly'
                                }]
                            }],
                            store: storeGly,
                            bbar: {
                                xtype: 'pagingtoolbar',
                                pageSize: 200,
                                store: storeGly,
                                displayInfo: true,
                                displayMsg: "显示{0}-{1}条，共{2}条",
                                beforePageText: "第",
                                afterPageText: "页/共{0}页",
                                emptyMsg: "没有数据显示",
                                plugins: new Ext.ux.ProgressBarPager()
                            }
                        }]
                    },
					{
                        title: "面试组",
                        xtype: 'form',
                        name: 'mszgrid',
                        minHeight: 285,
                        autoHeight: true,
                        style: 'border:0',
                        items: [{
                            xtype: 'grid',
                            columnLines: true,
                            style: "margin:8px",
                            name: 'mszLgrid',
                            layout: 'fit',
                            minHeight: 200,
                            autoHeight: true,
                            dockedItems: [{
                                xtype: "toolbar",
                                items: [
                                    {
                                        text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.addmsz", "新增面试组"),
                                        tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.addmsz", "新增面试组"),
                                        iconCls: "add",
                                        handler: 'addMszInfom'
                                    }
                                ]
                            }],
                            plugins: {
                                ptype: 'cellediting'
                            },
                            reference: 'mszLgrid',
                            columns: [{
                                dataIndex: 'mszid',
                                width: 200,
                                hidden: true
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.mszmj", "面试组"),
                                dataIndex: 'mszmj',
                                width: 200,
                                flex: 1,
                                editor: {
                                    xtype: 'textfield'
                                },
                                allowBlank: false
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.mssd", "面试时段"),
                                dataIndex: 'mszsd',
                                hidden: true,
                                width: 200,
                                flex: 1,
                                editor: {
                                    xtype: 'combobox',
                                    valueField: 'TValue',
                                    displayField: 'TSDesc',
                                    hiddenValue: 'TValue',
                                    store: sdstore
                                },
                                renderer: function (v) {
                                    if (v === "A") {
                                        return "上午"
                                    } else {
                                        return "下午"
                                    }
                                }
                            }, {
                                hidden: true,
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.mskssj", "开始时间"),
                                dataIndex: 'mszkssj',
                                width: 200,
                                editor: {
                                    xtype: 'timefield',
                                    format: 'H:i',
                                    name: 'StartTime',
                                    minValue: '07:00'
                                },
                                flex: 1,
                                allowBlank: false,
                                renderer: function (v) {
                                    let time = Ext.Date.format(v, 'H:i');
                                    if (!time) {
                                        time = v;
                                    }
                                    return time;
                                }
                            }, {
                                hidden: true,
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.msjssj", "结束时间"),
                                dataIndex: 'mszjssj',
                                name: 'glyLb',
                                width: 200,
                                editor: {
                                    xtype: 'timefield',
                                    format: 'H:i',
                                    name: 'EntTime'
                                },
                                flex: 1,
                                allowBlank: false,
                                renderer: function (v) {
                                    let time = Ext.Date.format(v, 'H:i');
                                    if (!time) {
                                        time = v;
                                    }
                                    return time;
                                }
                            }, {
                                text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.handle", "操作"),
                                menuDisabled: true,
                                sortable: false,
                                width: 110,
                                align: 'center',
                                xtype: 'actioncolumn',
                                flex: 1,
                                items: [{
                                    iconCls: 'remove',
                                    tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.delete", "删除"),
                                    handler: 'deleteMsZ'
                                }]
                            }],
                            store: storeMsz,
                            bbar: {
                                xtype: 'pagingtoolbar',
                                pageSize: 200,
                                store: storeMsz,
                                displayInfo: true,
                                displayMsg: "显示{0}-{1}条，共{2}条",
                                beforePageText: "第",
                                afterPageText: "页/共{0}页",
                                emptyMsg: "没有数据显示",
                                plugins: new Ext.ux.ProgressBarPager()
                            }
                        }]
                    }]
				}]
			}],

			buttons: [

			{
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.save", "保存"),
				handler: 'onpwinfodescSave',
				iconCls: 'save'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.sure", "确定"),
				handler: 'ensurepwinfromSave',
				iconCls: 'ensure'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'onPwinfoClose'
			}]
		});
		this.callParent();
	},
	onAddClick: function(btn) {
		// Create a model instance
		var rec = new KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListModel({
			classId: '2',
			batchId: '张三',
			ksOprId: 0,
			ksName: '22',
			mshId: '100',
			appInsId: ''
		});

		hbfsAllRecs = btn.findParentByType("grid[reference=mspsksGrid]").store.getRange();
		btn.findParentByType("grid[reference=mspsksGrid]").getStore().insert(hbfsAllRecs.length, rec);
		// this.cellEditing.startEdit(rec, 0);
	},
	onRemoveClick: function(grid, rowIndex) {
		grid.getStore().removeAt(rowIndex);
	}
});