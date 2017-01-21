Ext.define('KitchenSink.view.enrollProject.projectMg.projectInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'projectInfo', 
	controller: 'projectMgController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollProject.projectMg.professionStore',
		'KitchenSink.view.enrollProject.projectMg.managerStore',
		'KitchenSink.view.enrollProject.projectMg.classStore',
		'KitchenSink.view.enrollProject.projectMg.userWindow'
	],
  	title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.projectDetail","项目详情"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	proGrid:{},
    items: [{
        xtype: 'form',
        reference: 'projectForm',
		layout: {
			type: 'vbox',
			align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },
        items: [{
           	xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.projectId","项目编号"),
			name: 'projectId',
			//editable: false,
			readOnly: true,
			cls:'lanage_1'
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.projectName","项目名称"),
			name: 'projectName',
			/*
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			*/
            allowBlank: false
        },{
           	xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.projectType","所属类别"),
			name: 'projectType',
			emptyText:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.pleaseSelect","请选择..."),
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_PRJ_TYPE_ID',
    		displayField: 'TZ_PRJ_TYPE_NAME',
			store:new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_PRJ_TYPE_T',
				condition:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						operator:"01",
						type:"01"
					},
					TZ_PRJ_TYPE_STATUS:{
						value: 'Y',
							operator:"01",
							type:"01"
					}
				},
				result:'TZ_PRJ_TYPE_ID,TZ_PRJ_TYPE_NAME'
			})

        },{
            xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.isOpen","开通标识"),
			name: 'isOpen',
			emptyText:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.pleaseSelect","请选择..."),
            queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
			store:new KitchenSink.view.common.store.appTransStore("TZ_XMGL_ISOPEN"),
			allowBlank: false
        }, {
            xtype: 'tagfield',
            fieldLabel:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.projectSite","所属站点"),
            name: 'sites',
            store:new KitchenSink.view.common.store.comboxStore({
                recname:'PS_TZ_WEBSIT_SET_VW',
                condition:{
                	TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        operator:'01',
                        type:'01'
                    }
                },
                result:'TZ_SITEI_ID,TZ_SITEI_NAME'
            }),
            valueField: 'TZ_SITEI_ID',
            displayField: 'TZ_SITEI_NAME',
            filterPickList:true,
            createNewOnEnter: false,
            createNewOnBlur: false,
            queryMode: 'remote',
            listeners:{
                'select': function(combo,record,index,eOpts)//匹配下拉值之后置空输入文字
                {
                    var me = this;
                    me.inputEl.dom.value = "";
                }
            }
        },{
			layout: {
				type: 'column'
			},
			//bodyStyle:'padding:0 0 10px 0',
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.appOnFormModel","在线报名表模板"),
				name: 'appOnFormModel',
				editable: false	,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchAppFormTmp"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				style:'margin-left:5px',
				name: 'appFormName'	
			}]
        },/*,{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ps_appf_modal","评审报名表模版"),
				name: 'ps_appf_modal',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchAppFormTmp"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'ps_appf_modal_desc',
				style:'margin-left:8px'
			}]
		},*/{
			layout: {
				type: 'column'
			},
			//bodyStyle:'padding:0 0 10px 0',
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.applyScheduModel2","报名流程模版"),
				//fieldLabel:'报名流程模版',
				name: 'applyScheduModel',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchScheduModel"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				style:'margin-left:5px',
				name: 'appScheduModName'	
			}]
        },{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.smtDtTmpId","递交资料模型"),
				name: 'smtDtTmpId',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchSmtDtTmp"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				style:'margin-left:5px',
				name: 'smtDtName'	
			}]
        },/*20170118-ZXW*/{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.clps_cj_modal","材料评审成绩模型"),
				name: 'clps_cj_modal',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "choiceScoreModal"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'clps_cj_modal_desc',
				style:'margin-left:8px'
			}]
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.msps_cj_modal","面试评审成绩模型"),
				name: 'msps_cj_modal',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "choiceScoreModal"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'msps_cj_modal_desc',
				style:'margin-left:8px'
			}]
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.cs_cj_modal","自动初筛成绩模型"),
				name: 'cs_cj_modal',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "choiceScoreModal"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'cs_cj_modal_desc',
				style:'margin-left:8px'
			}]
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ttbl","淘汰比率"),
				name: 'ttbl'				
			},{
				columnWidth:.6,
				xtype: 'displayfield',
                hideLabel: true,
				name: 'ttbl2',
				style:'margin-left:8px'
			}]
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ksbq","自动标签规则"),
				name: 'ksbq',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "choiceBqzModal"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'ksbq_desc',
				style:'margin-left:8px'
			}]
		},{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.4,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.fmqd","负面清单规则"),
				name: 'fmqd',
				editable: false,
				triggers: {
					search: {
						cls: 'x-form-search-trigger',
						handler: "choiceBqzModal"
					}
				}
			},{
				columnWidth:.6,
				xtype: 'displayfield',
				hideLabel: true,
				name: 'fmqd_desc',
				style:'margin-left:8px'
			}]
		}/*20170118 end**/,{
			  xtype: 'tabpanel',
			  frame: true,
			  activeTab: 0,
			  plain:false,
			  resizeTabs:true,
			  defaults :{
				  autoScroll: false
			  },
			  
			  listeners:{
				  tabchange:function(tabPanel, newCard, oldCard){
					  var queryType;
					  var projectId = tabPanel.findParentByType('form').getForm().findField('projectId').getValue();
					  
					  if (newCard.tabType != "XMMS"){
						  this.doLayout();
						  if (newCard.firstLoad){
							  var tzStoreParams = '{"projectId":"'+projectId+'","queryType":"' + newCard.tabType + '"}';
							  newCard.store.tzStoreParams = tzStoreParams;
							  newCard.store.load();
							  newCard.firstLoad = false;
						  }
					  }
				  }
			  },
			  items:[{
					title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.XMMS","项目描述"),
					tabType: 'XMMS',
					xtype: 'form',
					reference: 'projectDescForm',
					layout: {
			        	  type: 'vbox',
			        	  align: 'stretch'
			        },
					border: false,
		        	bodyPadding: 10,
					bodyStyle:'overflow-y:auto;overflow-x:hidden',
					items: [{
						//xtype: 'htmleditor',  
						xtype: 'ueditor',
						fieldLabel: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.XMMS","项目描述"),
						name: 'projectDesc',
						hideLabel: true,
						height: 300,
						zIndex: 900
					}]
			  },{
	        	  	title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ZYFX","专业方向"),
					tabType: 'ZYFX',
					firstLoad: true,
					xtype: 'grid',
					height: 315, 
					frame: true,
					columnLines: true,
					name: 'professionGrid',
					reference: 'professionGrid',
					style:"margin:10px",
				 	 selModel: {
					  	type: 'checkboxmodel'
					  },
					store: {
						type: 'professionStore'
					},
					dockedItems:[{
						xtype:"toolbar",
						items:[
							//{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.add","新增"),iconCls:"add",handler:"addProfessionAtLast"},"-",
							{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.add","新增"),iconCls:"add",handler:"addPrjZYFX"},"-",
							{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.edit","编辑"),iconCls:"edit",handler:"editPrjZYFX"},"-",
							{
								text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.tbarDelete","删除"),
								tooltip:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.tbarTipDelete","删除"),
								iconCls:"delete",
								handler:'deleteZYFX'
							}
						]
					}],
					/*plugins: {
							ptype: 'cellediting',
							pluginId: 'professionCellediting',
							clicksToEdit: 1
					},
					listeners: {
						cellclick: function(grid, td, cellIndex, record, tr, rowIndex){
							//console.log(cellIndex);
							if (cellIndex == 1 && record.data["isSaved"] == "Y"){
								return false;	
							}
						}
					},*/
					viewConfig: {
						plugins: {
							ptype: 'gridviewdragdrop',
							containerScroll: true,
							dragGroup: this,
							dropGroup: this
						},
						listeners: {
							drop: function(node, data, dropRec, dropPosition) {
								data.view.store.beginUpdate();
								var items = data.view.store.data.items;
								for(var i = 0;i< items.length;i++){
									items[i].set('sortNum',i+1);
								}
								data.view.store.endUpdate();
							}
						}
					},
					columns: [{
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.sortNum","序号"),
						dataIndex: 'sortNum',
						width:60
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.professionId","专业方向ID"),
						dataIndex: 'professionId',
						width:150,
						editor: {
							xtype: 'textfield'	
						}
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.professionName","专业方向名称"),
						dataIndex: 'professionName',
						width:200,
						minWidth: 150,
						flex: 1,
						editor: {
							xtype: 'textfield'	
						}
					},
					{
						menuDisabled: true,
						sortable: false,
						width:60,
						xtype: 'actioncolumn',
						align: 'center',
						items:[
							{iconCls: 'edit',tooltip:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.delete","编辑"), handler: 'editCurrentRow'},
							{iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.delete","删除"), handler: 'deleteCurrentRow'}
						]
					}]
        	  },{
	        	  	title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.GLRY","管理人员"),
					tabType: 'GLRY',
					firstLoad: true,
					xtype: 'grid',
					height: 315, 
					frame: true,
					columnLines: true,
					name: 'managerGrid',
					reference: 'managerGrid',
					style:"margin:10px",
					store: {
						type: 'managerStore'
					},
					dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.add","新增"),iconCls:"add",handler:"addManagerAtLast"}
						]
					}],
					columns: [{
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.managerName","姓名"),
						dataIndex: 'managerName',
						width:150
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.managerPhone","电话"),
						dataIndex: 'managerPhone',
						sortable: false,
						width:150
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.managerEmail","邮箱"),
						dataIndex: 'managerEmail',
						sortable: false,
						minWidth:150,
						flex:1
					},{
						menuDisabled: true,
						sortable: false,
						align: 'center',
						width:60,
						xtype: 'actioncolumn',
						items:[
							{iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.delete","删除"), handler: 'deleteCurrentRow'}
						]
					}]
        	  },{
	        	  	title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.BJGL","班级管理"),
					tabType: 'BJGL',
					firstLoad: true,
					xtype: 'grid',
					height: 315, 
					frame: true,
					columnLines: true,
					name: 'classGrid',
					reference: 'classGrid',
					style:"margin:10px",
					store: {
						type: 'classStore'
					},
					columns: [{
						xtype:'linkcolumn',
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.className","班级名称"),
						dataIndex: 'className',
						items:[{
							getText: function(v, meta, rec) {
								return v;
							},
							handler: "viewClassInfo"
						}],
						width:200,
						minWidth: 200,
						flex: 1
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.startDate","开始日期"),
						dataIndex: 'startDate',
						sortable: false,
						minWidth:150,
						flex: 1
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.endDate","结束日期"),
						dataIndex: 'endDate',
						sortable: false,
						minWidth: 150,
						flex: 1
					},{ 
						text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.openAppOnline","是否开通在线报名"),
						dataIndex: 'openAppOnline',
						sortable: false,
						minWidth: 150,
						flex: 1
					}],
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 5,
						listeners:{
							afterrender: function(pbar){
								var grid = pbar.findParentByType("grid");
								pbar.setStore(grid.store);
							}
						},
						plugins: new Ext.ux.ProgressBarPager()
					}
        	  }]
        }]
    }],
    buttons: [{
			text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.save","保存"),
			iconCls:"save",
			handler: 'onProjectSave'
		}, {
			text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.ensure","确定"),
			iconCls:"ensure",
			handler: 'onProjectEnsure'
		}, {
			text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROINFO_STD.close","关闭"),
			iconCls:"close",
			handler: 'onProjectClose'
		}]
});

