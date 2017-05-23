Ext.define('KitchenSink.view.scoreModelManagement.scoreModelInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'scoreModelInfo', 
	controller: 'scoreModelMgController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.scoreModelManagement.scoreItemsStore'
	],
  	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.scoreModelDefn","成绩模型定义"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',

	constructor: function(config, callback){
		this.actType = config.actType;
		this.reloadGrid = callback;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var me = this;
		
		var IDTypeReg = /^[0-9a-zA-Z_]+$/;
		Ext.apply(Ext.form.field.VTypes, {
            IdValType: function(val, field) {
                var bolFlag;
                bolFlag = IDTypeReg.test(val);
                return bolFlag;
            },
            IdValTypeText: '只能输入字母、数字和下划线'
        });
		
		//有效状态Store
		var effeStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_ISVALID");
		//总分分布统计模型Store
		var totalFbStore = new KitchenSink.view.common.store.comboxStore({
            recname:'PS_TZ_FBDZ_TBL',
            condition:{
            	TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_M_FBDZ_ZT:{
                	value:'Y',
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_M_FBDZ_ID,TZ_M_FBDZ_NAME'
        });
		
		var dfItemsstore = new KitchenSink.view.scoreModelManagement.scoreItemsStore();
		var pwItemsstore = new KitchenSink.view.scoreModelManagement.scoreItemsStore();
		
		
		var hiddenTab = false;
		var modeIdReadOnly = false;
		var modeIdCls = "";
		if(this.actType == "Add"){
			hiddenTab = true;
		}else{
			modeIdReadOnly = true;
			modeIdCls = "lanage_1";
		}
		
		
        Ext.apply(this, {
		    items: [{        
		        xtype: 'form',
		        reference: 'scoreModelForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
		        },
		        border: false,
		        bodyPadding: 10,
		        
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 140,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'hiddenfield',
					name: 'orgId'
		        },{
		           	xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.modelId","模型ID"),
					name: 'modelId',
					allowBlank: false,
					readOnly: modeIdReadOnly,
					vtype: 'IdValType',
					cls: modeIdCls,
		            afterLabelTextTpl: [
		                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
		            ],
		        },{
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.modeName","模型名称"),
					name: 'modeName',
					maxLength: 50,
		            allowBlank: false
		        },{
		           	xtype: 'combo',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.status","状态"),
					name: 'status',
		            queryMode: 'local',
		            editable:false,
		            valueField: 'TValue',
		    		displayField: 'TSDesc',
					store: effeStatusStore,
					allowBlank: false
		        },{
		        	layout: {
						type: 'column'
					},
					bodyStyle:'padding:0 0 5px 0',
		        	items:[{
		        		columnWidth: 1,
		        		xtype: 'textfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.treeName","树名称"),
						name: 'treeName',
			            allowBlank: false,
			            vtype: 'IdValType',
			            triggers: {
							search: {
								cls: 'x-form-search-trigger',
								handler: "pmtSearchScoreModelTree"
							}
						}
		        	},{
		        		xtype:'button',
						text:'<span style="text-decoration:underline;color:blue;">设置树结构</span>',
						textAlign: 'right',
						border:false,
						width: 100,
						style:{
							background: 'white',
							boxShadow:'none'
						},
						handler: 'setTreeManager'
		        	}]
		        	
		        },{
		            xtype: 'combo',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.totalScoreModel","总分分布统计模型"),
					name: 'totalScoreModel',
		            queryMode: 'remote',
		            editable:false,
					valueField: 'TZ_M_FBDZ_ID',
		    		displayField: 'TZ_M_FBDZ_NAME',
		    		store: totalFbStore
		        },{
		        	xtype: 'displayfield',
		        	fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.orgDesc","机构"),
					name: 'orgDesc',
		        },{
		        	xtype: 'displayfield',
		        	fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.addOprName","添加人"),
					name: 'addOprName',
		        },{
		        	xtype: 'displayfield',
		        	fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.addoprDttm","添加时间"),
					name: 'addoprDttm',
		        },{
		        	xtype: 'displayfield',
		        	fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.updateOprName","修改人"),
					name: 'updateOprName',
		        },{
		        	xtype: 'displayfield',
		        	fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.updateOprDttm","修改时间"),
					name: 'updateOprDttm',
		        },{
					  xtype: 'tabpanel',
					  frame: true,
					  activeTab: 0,
					  plain:false,
					  resizeTabs:true,
					  defaults :{
						  autoScroll: false
					  },
					  hidden: hiddenTab,
					  
					  listeners:{
						  /*tabchange:function(tabPanel, newCard, oldCard){
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
						  }*/
					  },
					  items:[{
						  	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.dfViewScoreItems","打分页面显示成绩项"),
							tabType: 'A',
							firstLoad: true,
							xtype: 'grid',
							height: 315, 
							frame: true,
							columnLines: true,
							name: 'dfScoreItemsGrid',
							reference: 'dfScoreItemsGrid',
							style:"margin:10px",
						 	selModel: {
							   type: 'checkboxmodel'
							},
							store: dfItemsstore,
							dockedItems:[{
								xtype:"toolbar",
								items:[
									{
										text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.add","新增"),
										iconCls:"add",
										handler:"addScoreItem"
									},"-",
									{
										text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.remove","删除"),
										iconCls:"remove",
										handler:'deleteScoreItems'
									}
								]
							}],
							plugins: {
									ptype: 'cellediting',
									pluginId: 'scoreItemsCellediting',
									clicksToEdit: 1
							},
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
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.sortNum","序号"),
								dataIndex: 'sortNum',
								width:60
							},{ 
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.viewName","显示名称"),
								dataIndex: 'viewName',
								width:150,
								flex: 1,
								editor: {
									xtype: 'textfield'	
								}
							},{ 
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.itemId","成绩项ID"),
								dataIndex: 'itemId',
								width:200,
								minWidth: 150,
								flex: 1,
								editor: {
									xtype: 'textfield',
									editable: false,
									triggers: {
				                        search: {
				                            cls: 'x-form-search-trigger',
				                            handler: "selectScoreModelItemId"
				                        }
				                    }
								}
							},
							{
								menuDisabled: true,
								sortable: false,
								width:60,
								xtype: 'actioncolumn',
								align: 'center',
								items:[
									/*{iconCls: 'add',tooltip:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.add","新增"), handler: 'addRowAfterCurrent'},*/
									{iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.remove","删除"), handler: 'deleteCurrentRow'}
								]
							}],
							bbar: {
								xtype: 'pagingtoolbar',
								pageSize: 10,
								store:dfItemsstore,
								plugins: new Ext.ux.ProgressBarPager()
							}
		        	  },{
						  	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.pwViewScoreItems","评委打印页显示成绩项"),
							tabType: 'B',
							firstLoad: true,
							xtype: 'grid',
							height: 315, 
							frame: true,
							columnLines: true,
							name: 'pwScoreItemsGrid',
							reference: 'pwScoreItemsGrid',
							style:"margin:10px",
						 	selModel: {
							   type: 'checkboxmodel'
							},
							store: pwItemsstore,
							dockedItems:[{
								xtype:"toolbar",
								items:[
									{
										text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.add","新增"),
										iconCls:"add",
										handler:"addScoreItem"
									},"-",
									{
										text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.remove","删除"),
										iconCls:"remove",
										handler:'deleteScoreItems'
									}
								]
							}],
							plugins: {
									ptype: 'cellediting',
									pluginId: 'scoreItemsCellediting',
									clicksToEdit: 1
							},
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
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.sortNum","序号"),
								dataIndex: 'sortNum',
								width:60
							},{ 
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.viewName","显示名称"),
								dataIndex: 'viewName',
								width:150,
								flex: 1,
								editor: {
									xtype: 'textfield'	
								}
							},{ 
								text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.itemId","成绩项ID"),
								dataIndex: 'itemId',
								width:200,
								minWidth: 150,
								flex: 1,
								editor: {
									xtype: 'textfield',
									editable: false,
									triggers: {
				                        search: {
				                            cls: 'x-form-search-trigger',
				                            handler: "selectScoreModelItemId"
				                        }
				                    }
								}
							},
							{
								menuDisabled: true,
								sortable: false,
								width:60,
								xtype: 'actioncolumn',
								align: 'center',
								items:[
									/*{iconCls: 'add',tooltip:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.add","新增"), handler: 'addRowAfterCurrent'},*/
									{iconCls: 'remove',tooltip:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.remove","删除"), handler: 'deleteCurrentRow'}
								]
							}],
							bbar: {
								xtype: 'pagingtoolbar',
								pageSize: 10,
								store:pwItemsstore,
								plugins: new Ext.ux.ProgressBarPager()
							}
		        	  }]
		        }]
		    }]
        });
		
        this.callParent();
    },
    buttons: [{
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.save","保存"),
		iconCls:"save",
		closePanel: 'N',
		handler: 'onScoreModelSave'
	}, {
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.ensure","确定"),
		iconCls:"ensure",
		closePanel: 'Y',
		handler: 'onScoreModelEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.close","关闭"),
		iconCls:"close",
		handler: 'onScoreModelClose'
	}]
})
