Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewRule',{
	extend: 'Ext.panel.Panel',
	xtype: 'materialsReviewRule',
	controller: 'materialsReviewRuleController',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*', 
    	'Ext.toolbar.Paging', 
    	'Ext.ux.ProgressBarPager', 
    	'Ext.ux.MaximizeTool',
		'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewJudgeStore',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewRuleController'
    ],
	title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.title","设置评审规则"),
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	actType:'',
	judgeGroupData:'',
    constructor: function(obj) {
        this.classId = obj.classId;
        this.batchId = obj.batchId;
		this.judgeGroupData = obj.judgeGroupData;
        this.callParent();
    },
    initComponent: function() {

		//所属评委组
		/*var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
			recname:'TZ_CLPS_GR_TBL',
			condition:{
				TZ_JG_ID:{
					value:Ext.tzOrgID,
					operator:'01',
					type:'01'
				}
			},
			result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
		});*/

		var judgeGroupStore = Ext.create("Ext.data.Store", {
			fields: ["TZ_CLPS_GR_ID", "TZ_CLPS_GR_NAME"],
			data: this.judgeGroupData
		});

    	Ext.apply(this,{
    		items:[{
    			xtype:'form',
    			reference:'materialsReviewRuleForm',
    			layout:{
    				type:'vbox',
    				align:'stretch'
    			},
    			border:false,
    			bodyPadding:10,
    			bodyStyle:'overflow-y:auto;overflow-x:hidden',
    			fieldDefaults:{
    				msgTarget:'side',
    				labelWidth:110,
    				labelStyle:'font-weight:bold'
    			},
    			items:[{
    				xtype:'textfield',
    				name:'classId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'batchId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.className","报考班级"),
    				name:'className',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.batchName","批次"),
    				name:'batchName',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.bkksNum","报考考生数量"),
    				name:'bkksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.clpsksNum","材料评审考生"),
    				name:'clpsksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				beforeBodyEl:[
    					'<li id="{id}-viewApplicantsEl" data-qtip="查看考生" data-ref="viewApplicantsEl" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;margin-top:4px;position:absolute">' +
    						'<div class="x-tagfield-item-text" style="padding-right:4px;">查看考生</div>' +
    					'</li>'
    				],
    				childEls:[
    					'viewApplicantsEl'
    				],
    				listeners:{
    					change:function(field){
    						var materialReviewApplicants = field.getValue();
    						var length = materialReviewApplicants.toString().length||1;
    						if(length>0) {
    							var viewApplicantsEl = this.viewApplicantsEl;
    							if(viewApplicantsEl) {
    								var tpl = Ext.create('Ext.XTemplate','margin-left:{width}px;');
    								var data = {
    										width:25+length*8
    								};
    								var marginLeftStyle= tpl.apply(data);
    								viewApplicantsEl.applyStyles(marginLeftStyle);
    								if(materialReviewApplicants) {
    									viewApplicantsEl.addListener("click","viewApplicationForm");
    								} else {
    									viewApplicantsEl.addListener("click",function(){
    										Ext.Msg.alert(Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.tip","提示"),
    												Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.noApplicants","当期班级批次下没有考生"));
    									});
    								}
    							}
    						}
    					}
    				}
    			},{
					xtype:'textfield',
					name:'dqpsStatus',
					hidden:true
				},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.reviewStatusDesc","当前评审状态"),
    				name:'dqpsStatusDesc',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				ignoreChangesFlag:true
    			},{
    				layout:{
    					type:'column'
    				},
    				padding:'0 0 8px 0',
    				items:[{
    					xtype:'datefield',
    					fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.startDate","开始日期"),
    					format:'Y-m-d',
    					name:'startDate',
    					columnWidth:.5
    				},{
    					xtype:'timefield',
    					fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.startTime","时间"),
    					labelWidth:38,
    					style:'margin-left:5px',
    					format:'H:i',
    					name:'startTime',
    					value:'08:30',
    					columnWidth:.5
    				}]
    			},{
    				layout:{
    					type:'column'
    				},
    				padding:'0 0 8px 0',
    				items:[{
    					xtype:'datefield',
    					fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.endDate","结束日期"),
    					format:'Y-m-d',
    					name:'endDate',
    					columnWidth:.5
    				},{
    					xtype:'timefield',
    					fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.endTime","时间"),
    					labelWidth:38,
    					style:'margin-left:5px',
    					format:'H:i',
    					name:'endTime',
    					value:'17:30',
    					columnWidth:.5
    				}]
    			},{
    				xtype:'displayfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.notice","注意"),
    				value:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.noticeInfo","开始结束时间仅作为提醒评委使用，不会作为自动开关使用。"),
    				labelWidth:38,
    				labelStyle:'font-weight:noraml'
    			},{
    				xtype:'tabpanel',
    				frame:true,
    				listeners:{
    					beforetabchange:function(tabPanel,newCard,oldCard) {
    						if(newCard.name=="materialJudgeForm") {
    							if(!newCard.child("grid").store.isLoaded()) {
									var form = this.findParentByType("panel").getForm();
									var classId = form.findField("classId").getValue();
									var batchId = form.findField("batchId").getValue();
    								var tzParams = '{"classId":"'+classId+'","batchId":"'+batchId+'"}';
    								var store = newCard.child("grid").store;
    								store.tzStoreParams = tzParams;
    								store.load();
    								this.doLayout();
    							}
    						} 
    					}
    				},
    				items:[{
    					title:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.materialDesc","材料评审说明"),
    					name:'materialDescPanel',
    					items:[{
    						xtype:'ueditor',
    						height:300,
    						zIndex:900,
    						name:'materialDesc'
    					}]
    				},{
    					title:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.materialJudge","材料评委"),
    					xtype:'form',
    					name:'materialJudgeForm',
    					minHeight:300,
    					autoHeight:true,
    					items:[{
    						xtype:'grid',
    						minHeight:160,
    						name:'materialJudgeGrid',
    						columnLines:true,
    						autoHeight:true,
    						selModel:{
    							type:'checkboxmodel'
    						},
							plugins:[{
								ptype:'cellediting',
								clicksToEdit:1
							}],
							dockedItems:[{
								xtype:'toolbar',
								items:[{
									xtype:'displayfield',
									value:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.perExaminee1","每位考生要求被"),
									hideLabel:true
								},{
									xtype:'numberfield',
									fieldLabel:"",
									name:'judgeNumSet',
									allowDecimals:false,
									minValue:0,
									value:0,
									style:'margin-left:5px;margin-right:5px;',
									negativeText:'评委数量不能为负数',
									nanText:'{0}不是有效的数字',
									width:130,
									hideLabel:true,
									listeners:{
										change:function(field,newValue,oldValue,eOpts) {
											//要求评审人次更新
											var form = field.findParentByType("materialsReviewRule").down("form[name=statisticsNumForm]").getForm();
											var clpsksNum = form.findField("clpsksNum").getValue();
											var reviewNumSet = clpsksNum*parseInt(newValue);
											form.findField("reviewNumSet").setValue(reviewNumSet);

											//所属评委组更新
											/*var classId = form.findField("classId").getValue();
											var batchId = form.findField("batchId").getValue();

											var grid = field.findParentByType("materialsReviewRule").down("grid[name=materialJudgeGrid]");

											var judgeGroupParams =  '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
												'"OperateType":"tzGetJudgeGroup","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';
											Ext.tzLoad(judgeGroupParams,function(responseData) {
												var judgeGroupData = responseData.groupData;

												judgeGroupStore = Ext.create("Ext.data.Store", {
													fields: ["TZ_CLPS_GR_ID", "TZ_CLPS_GR_NAME"],
													data: judgeGroupData
												});
											});*/

										}
									}
								},{
									xtype:'displayfield',
									value:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.perExaminee2","个评委审批"),
									hideLabel:true,
									style:'margin-right:10px;'
								},{
									text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.addJudge","新增评委"),
									iconCls:'add',
									tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.addJudge","新增评委"),
									handler:"addJudge"
								},'->',{
									xtype:'splitbutton',
									text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.more","更多操作"),
									iconCls:'list',
									glyph:61,
									menu:[{
										text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.resetPassword","批量重置选中评委密码"),
										name:'resetPassword',
										handler:'resetPassword'
									},{
										text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.sendEmail","给选中评委发送邮件"),
										name:'sendEmail',
										handler:'sendEmail'
									},{
										text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.sendMessage","给选中评委发送短信"),
										name:'sendMessage',
										handler:'sendMessage'
									},{
										text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.exportJudge","批量导出评委"),
										name:'exportJudge',
										handler:'exportJudge'
									}]
								}]
							}],
    						columns:[{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeId","评委账号"),
    							dataIndex:'judgeId',
    							minWidth:100,
    							flex:1
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeName","评委"),
    							dataIndex:'judgeName',
    							minWidth:110,
								flex:1
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeMobile","手机"),
    							dataIndex:'judgeMobile',
    							minWidth:130
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeEmail","邮箱"),
    							dataIndex:'judgeEmail',
    							minWidth:180
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeGroup","所属评委组"),
    							dataIndex:'judgeGroup',
    							minWidth:100,
								flex:1,
								editor:{
									xtype:'combobox',
									editable:false,
									emptyText:'请选择...',
									valueField:'TZ_CLPS_GR_ID',
									displayField:'TZ_CLPS_GR_NAME',
									store:judgeGroupStore,
									queryMode:'local',
									triggerAction:'all'
								},
								renderer:function(v) {
									if(v) {
										var rec = judgeGroupStore.find('TZ_CLPS_GR_ID',v,0,false,true,true);
										if(rec>-1) {
											return judgeGroupStore.getAt(rec).data.TZ_CLPS_GR_NAME;
										} else {
											return "请选择..."
										}
									}
								}
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeExamineeNum","评审考生人数"),
    							dataIndex:'judgeExamineeNum',
    							minWidth:130,
								editor:{
									xtype:'numberfield',
									minValue:0,
									allowDecimals:false
								}
    						},{
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeStatusDesc","评委状态"),
    							dataIndex:'judgeStatusDesc',
    							minWidth:130
    						},{
    							menuDisabled:true,
    							sortable:false,
    							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.operate","操作"),
    							width:60,
    							align:'center',
    							xtype:'actioncolumn',
    							items:[{
    								iconCls:'remove',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),handler:'removeJudge'
    							}]
    						}],
    						store:{
								view:this,
								type:'materialsReviewJudgeStore',
								listeners:{
									datachanged:function(store,meta,epts) {
										var form = store.view.down("form[name=statisticsNumForm]").getForm();
										var reviewNumCount = 0;
										for(var i=0;i<store.getCount();i++) {
											var record = store.getAt(i);
											reviewNumCount = reviewNumCount + parseInt(record.get("judgeExamineeNum"));
										}
										//当前选择评委评议总人次
										form.findField("judgeNumTotal").setValue(reviewNumCount);
									}
								}
							}
    					},{
    						xtype:'panel',
    						items:[{
    							title:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.statisticsNum","人数统计"),
    							glyph:85,
    							xtype:'form',
    							name:'statisticsNumForm',
    							autoHeight:true,
    							layout:{
    								type:'vbox',
    								align:'stretch'
    							},
    							fieldDefaults:{
    								msgTarget:'side',
    								labelWidth:200,
    								labelStyle:'font-weight:normal',
    								padding:'0 0 0 10px',
    								height:25
    							},
    							items:[{
    								xtype:'displayfield',
    								fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.clpsksNum2","材料评审考生人数"),
    								name:'clpsksNum'
    							},{
    								xtype:'displayfield',
    								fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.reviewNumSet","要求评审人次"),
    								name:'reviewNumSet'
    							},{
    								xtype:'displayfield',
    								fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeNumTotal","当前选择评委评议总人次"),
    								name:'judgeNumTotal'
    							}]
    						}]
    					}]
    				}]
    			}]
    		}]
    	});
    	this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.save","保存"),
        iconCls: "save",
		name:'onRuleSave',
        handler: 'onRuleSave'
    },
    {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.ensure","确定"),
        iconCls: "ensure",
		name:'onRuleEnsure',
        handler: 'onRuleSave'
    },
    {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.close","关闭"),
        iconCls: "close",
        handler: 'onRuleClose'
    }]
});