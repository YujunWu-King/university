Ext.define('KitchenSink.view.classManage.ClassInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'ClassInfo', 
	controller: 'ClassInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
		'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.DataView.DragSelector',
        'Ext.ux.DataView.LabelEditor' ,
		'KitchenSink.view.classManage.ZyfxModel',
		'KitchenSink.view.classManage.ZyfxStore',
		'KitchenSink.view.classManage.BmlcStore',
		'KitchenSink.view.classManage.PcglStore',
		'KitchenSink.view.classManage.GlryStore',
		'KitchenSink.view.classManage.DjzlStore',
        'KitchenSink.view.classManage.classInfoController',
		'KitchenSink.view.classManage.userWindows'
	],
    title: '班级详情', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	//class_id: 'NEXT',
	constructor: function (bj_id){
		//console.log(bj_id);
		this.bj_id=bj_id;
		this.callParent();
	},
	initComponent: function(){
		var me = this;
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_QTXX","OperateType":"QF","comParams":{"bj_id":"'+this.bj_id+'"}}';
		//搜索条件
		var conItems = [];
		//加载数据
		Ext.tzLoadAsync(tzParams,function(responseData){
			var formData = responseData.formData;
			for(var fieldName in formData) {
				//字段类型
				var fldType = formData[fieldName].fldType;
				//如果是下拉框,则初始化下拉框默认值;
				var downCondition = null;
				var downConditionStr = "";
				if(fldType == "L"){
					downConditionStr = '{ "TZ_JG_ID":{ "value" : "EMBA", "operator":"01", "type":"01"}}';
					downCondition = Ext.JSON.decode(downConditionStr);
				}
				//var promptTable = formData[fieldName].promptTable;
				//var promptTableFld = formData[fieldName].promptTableFld;
				//var promptTableFldDesc = formData[fieldName].promptTableFldDesc;
				
				var show_name=formData[fieldName].show_name;
				var fieldValue=formData[fieldName].zd_name;
				var typeField = {};
				switch (fldType)
				{
					case 'T'://字符串
					typeField = {
						xtype: 'textfield',
						columnWidth: 1,
						fieldLabel:show_name,
						hideEmptyLabel: true, 
						name: fieldName,
						value: fieldValue
					};
					break;
					case 'N'://数字
					typeField = {
						xtype: 'numberfield',
						columnWidth: 1,
						hideEmptyLabel: true, 
						value: fieldValue,
						fieldLabel:show_name,
						name: fieldName
					};
					break;
					case 'D'://日期
					typeField = {
						xtype: 'datefield',
						columnWidth: 1,
						hideEmptyLabel: true,
						format: 'Y-m-d', 
						fieldLabel:show_name,
						value: fieldValue,
						name: fieldName
					};
					break;
					case 'L'://下拉框
					typeField = {
						xtype: 'combobox',
						//hideEmptyLabel: true,
						//width: 357,
						columnWidth: 1,
						autoSelect: false,
						fieldLabel:show_name,
						store: new KitchenSink.view.common.store.comboxStore({
							recname: 'TZ_C_ATTR_OPT_T',
							condition:{
								TZ_IS_USED:{
									value:'Y',
									operator:"01",
									type:"01"
								},
								TZ_JG_ID:{
									value:Ext.tzOrgID,
									operator:"01",
									type:"01"
								},
								TZ_ATTRIBUTE_ID:{
									value:fieldName,
									operator:"01",
									type:"01"
								}
							},
							result:'TZ_DROP_DOWN_ID,TZ_DROP_DOWN_VALUE'
						}),
						valueField: 'TZ_DROP_DOWN_ID',
						displayField: 'TZ_DROP_DOWN_VALUE',
						//typeAhead: true,
						queryMode: 'remote',
						name: fieldName,
						value: fieldValue
					};
					break;
					default:
					typeField = {
						xtype: 'textfield',
						columnWidth: 1,
						hideEmptyLabel: true,
						value: fieldValue, 
						name: fieldName
					};
				}
				var fieldItem = {
					layout:'column',
					bodyPadding: 10,
					items:[typeField]
				}
				conItems.push(fieldItem);
			}
			if (conItems=="")
			{
				typeField = {
					text:"未设置更多信息数据项",
					html: "未设置更多信息数据项",
					minHeight:30
				};
				var fieldItem1 = {
					layout:'column',
					bodyPadding: 6,
					items:[typeField]
				};
				//console.log(fieldItem1);
				conItems.push(fieldItem1);
			}
			typeField = {
				xtype:"button",
				columnWidth:.3,
				text:'跳转到班级个性化设置页面',
				margin:'0 0 0 10',
				handler: 'Bmmb_Qr'
			};
			var fieldItem2 = {
					layout:'column',
					bodyPadding: 6,
					items:[typeField]
				};
			conItems.push(fieldItem2);
		});
		Ext.apply(this,{
			items: [
				{
					xtype: 'form',
					name:'form_1',
					reference: 'siteAccountForm',
					layout: {
						type: 'vbox',
						align: 'stretch'
					},
					border: false,
					bodyPadding: 10,
					bodyStyle:'overflow-y:auto;overflow-x:hidden',
					buttonAlign: 'center',
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 170,
						labelStyle: 'font-weight:bold'
					},
					items: [
						{
							xtype: 'textfield',
							readOnly:true,
							fieldLabel: '班级编号',
							name: 'bj_id',
							value: 'NEXT'
						},{
							xtype: 'textfield',
							fieldLabel: '班级名称',
							name: 'bj_name',
							allowBlank: false
						},{
							layout: {
								type: 'column'
							},
							items:[{
								columnWidth:.5,
								xtype: 'textfield',
								fieldLabel: '所属项目',
								name: 'xm_id',
								editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "xm_idChoice"
                                    }
                                },
								allowBlank: false,
								listeners:{
									change:function(file, value, eOpts){
										if (file.findParentByType('ClassInfo').actType=="add" ||(file.findParentByType('ClassInfo').actType=="update"&&value!=""&&eOpts!=""))
										{
											Ext.MessageBox.confirm("提示","是否复制项目下报名表模版、递交资料模型等默认数据到当前班级？",callBack);
											function callBack(id){
												if (id=="yes")
												{
												}
											}
										}
										
										//var x = file.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
										//var bj_id=file.findParentByType('form').getForm().findField('bj_id').getValue();
										//x.store.tzStoreParams='{"zl_id":"'+value+'","bj_id":"'+bj_id+'","queryID":"6"}',
										//x.store.reload();
									}
								}
							},{
								columnWidth:.5,
								xtype: 'displayfield',
								hideLabel: true,
								name: 'xm_id_desc',
                                style:'margin-left:8px;'
							}]
						},{
							xtype: 'combobox',
							fieldLabel: '是否开通在线报名',
							editable:false,
							emptyText:'请选择',
							queryMode: 'remote',
							name: 'bm_kt',
							valueField: 'TValue',
							displayField: 'TSDesc',
							store: new KitchenSink.view.common.store.appTransStore("TZ_IS_APP_OPEN")
						},{
							xtype: 'datefield',
							fieldLabel: '班级开始日期',
							format: 'Y-m-d',
							name: 'begin_time'
						},{
							xtype: 'datefield',
							fieldLabel: '班级结束日期',
							format: 'Y-m-d',
							name: 'end_time'
						},{
							xtype: 'datefield',
							fieldLabel: '报名开始日期',
							format: 'Y-m-d',
							name: 'beginBm_time'
						},{
							xtype: 'datefield',
							fieldLabel: '报名结束日期',
							format: 'Y-m-d',
							name: 'endBm_time'
						},{
							layout: {
								type: 'column'
							},
							items:[{
								columnWidth:.5,
								xtype: 'textfield',
								fieldLabel: '在线报名模板',
								name: 'bmb_mb',
								editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "bmb_mbChoice"
                                    }
                                }
							},{
								columnWidth:.5,
								xtype: 'displayfield',
								hideLabel: true,
								name: 'bmb_mb_desc',
                                style:'margin-left:8px'
							}]
						},{
                            layout: {
                                type: 'column'
                            },
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: '材料评审成绩模型',
                                name: 'clps_cj_modal',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceScoreModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'clps_cj_modal_desc',
                                style:'margin-left:8px'
                            }]
                        },{
                            layout: {
                                type: 'column'
                            },
                            bodyStyle:'padding:0 0 10px 0',
                            items:[{
                                columnWidth:.5,
                                xtype: 'textfield',
                                fieldLabel: '面试评审成绩模型',
                                name: 'msps_cj_modal',
                                editable: false,
                                triggers: {
                                    search: {
                                        cls: 'x-form-search-trigger',
                                        handler: "choiceScoreModal"
                                    }
                                }
                            },{
                                columnWidth:.5,
                                xtype: 'displayfield',
                                hideLabel: true,
                                name: 'msps_cj_modal_desc',
                                style:'margin-left:8px'
                            }]
                        },{
							xtype : 'tabpanel',
							activeTab: 0,
							plain:false,
							frame: true,
							resizeTabs:true,
							autoHeight:true,
							defaults :{
								autoScroll: false,
							},
							listeners:{
								tabchange:function(tp,p){
									var queryID;
									var bj_id = tp.findParentByType("form").getForm().findField('bj_id').getValue();
									if(p.title == "班级描述"){
										queryID = "1";
									}
									if(p.title == "专业方向"){
										queryID = "2";
									}
									if(p.title == "批次管理"){
										if(p.child('checkbox').getValue()!=true){
											p.child('grid').hide();
										}else{
											p.child('grid').show();
										}
										queryID = "3";
										p=p.child('grid');
									}
									if(p.title == "管理人员"){
										queryID = "4";
									}
									if(p.title == "报名流程设置"){
										queryID = "5";
									}
									if(p.title == "递交资料设置"){
										queryID = "6";
									}
									if(p.title == "更多信息"){
										queryID = "7";
									}
									this.doLayout();
									if(queryID != 1&&queryID != 7){
										var Params;
										if (queryID==5){
											var _mb=p.down('combobox[name=bmlc_mb]').getValue();
											Params= '{"bj_id":"'+bj_id+'","queryID":"' + queryID + '","lc_id":"'+_mb+'"}';
											p=p.child('grid');
										}else if (queryID==6){
											var _mb=p.down('combobox[name=djzl_mx]').getValue();
											Params= '{"bj_id":"'+bj_id+'","queryID":"' + queryID + '","zl_id":"'+_mb+'"}';
											p=p.child('grid');
										}else{
											Params = '{"bj_id":"'+bj_id+'","queryID":"' + queryID + '"}';
										}
										p.store.tzStoreParams = Params;
										p.store.load();
									}
								}
							},
							items : [
								{
									title: "班级描述",
									items: [
										{
											xtype: 'ueditor',
											name: 'bj_desc'
										}
									]
								},
								//专业方向
								{
									title:'专业方向',
									xtype: 'grid',
									columnLines: true,
									minHeight: 250,
									name:'zyfx_save',
									reference: 'skinGrid',
									store: {
										type: 'ZyfxStore'
									},
									columns: [{
										text: '班级id',
										xtype: 'hiddenfield',
										dataIndex: 'bj_id',
										width: 200
									},{ 
										text: '专业方向ID',
										dataIndex: 'fx_id',
										width: 200
									},{
										text: '专业方向名称',
										dataIndex: 'fx_name',
										minWidth: 5,
										flex: 1
									},{
										text: '操作',
										menuDisabled: true,
										sortable: false,
										width:100,
										xtype: 'actioncolumn',
										items:[
											{iconCls: 'edit',tooltip: '编辑',handler: 'editZyfx'},
											'-',
											{iconCls: 'remove',tooltip: '删除',handler: 'deleteZyfx'}
										]
									}],
									tbar: [{
										xtype:"toolbar",
										items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addZyfx'}]
									}],
									bbar: {
										xtype: 'pagingtoolbar',
										pageSize: 10,
										listeners:{
											afterrender: function(pbar){
												var grid = pbar.findParentByType("grid");
												pbar.setStore(grid.store);
											}
										},
										displayInfo: true,
										displayMsg: '显示{0}-{1}条，共{2}条',
										beforePageText: '第',
										afterPageText: '页/共{0}页',
										emptyMsg: '没有数据显示',
										plugins: new Ext.ux.ProgressBarPager()
									}
								},
								//批次管理
								{
									title: "批次管理",
									name:"grid_pc",
									bodyPadding: 10,
									layout: {
										type: 'vbox',
										align: 'stretch'
									},
									bodyStyle:'overflow-y:auto;overflow-x:hidden',
									fieldDefaults: {
										msgTarget: 'side',
										labelWidth: 100,
										labelStyle: 'font-weight:bold'
									}, 
									items: [
										{
											xtype: 'checkbox',
											boxLabel: '是否有下属批次',
											name: 'bj_xs',
											listeners:{
												change:function(file, value, eOpts){
													var x = file.nextSibling();
													if(value!=true){
														x.store.removeAll();
														x.hide();
													}else{
														x.show();
													}
												}
											}
										},{
											xtype: 'grid',
											height: 360, 
											frame: true,
											name: 'pcgl_save',
											dockedItems: [{
												xtype: 'toolbar',
												items: [
													{iconCls: 'add',text: '新增', tooltip:"新增信息项",handler: 'addPcgl'}
												]
											}],
											columnLines: true,
											selModel:{
												type: 'checkboxmodel'
											},
											reference: 'applyItemGrid',
											style:"margin:10px",
											store: {
												type: 'PcglStore'
											},
											columns: [
												{ 
													text: '班级id',
													sortable: false,
													dataIndex: 'bj_id',
													hidden: true
												},{ 
													text: '批次编号',
													sortable: false,
													dataIndex: 'pc_id',
													hidden: true
												},{ 
													text: '批次名称',
													dataIndex: 'pc_name',
													flex: 1
												},{
													text: '开始日期',
													dataIndex: 'pc_st_time',
													width: 150
												},{
													text: '结束日期',
													dataIndex: 'pc_sp_time',
													width: 150
												},{
													text: '报名截止日期',
													dataIndex: 'pc_stbm_time',
													width: 150
												},{
													text: '发布外网报名',
													dataIndex: 'pc_fb',
													width: 150
												},{
													text: '操作',
													menuDisabled: true,
													sortable: false,
													align: 'center',
													width: 120,
													xtype: 'actioncolumn',
													items:[
														{iconCls: 'edit',tooltip: '编辑',handler: 'editPcgl'},'-',
														{iconCls: 'remove',tooltip: '删除',handler: 'deletePcgl'}
													]
												}
											],
											bbar: {
												xtype: 'pagingtoolbar',
												pageSize: 10,
												listeners:{
													afterrender: function(pbar){
														var grid = pbar.findParentByType("grid");
														pbar.setStore(grid.store);
													}
												},
												displayInfo: true,
												displayMsg: '显示{0}-{1}条，共{2}条',
												beforePageText: '第',
												afterPageText: '页/共{0}页',
												emptyMsg: '没有数据显示',
												plugins: new Ext.ux.ProgressBarPager()
											}
										}
									]
								},
								//管理人员
								{
									title:'管理人员',
									xtype: 'grid',
									columnLines: true,
									name:'glry_save',
									minHeight: 250,
									reference: 'columnGrid',
									store: {
										type: 'GlryStore'
									},
									columns: [{ 
										text: '班级id',
										dataIndex: 'bj_id',
										sortable: false,
										hidden: true
									},{ 
										text: 'OPRID',
										dataIndex: 'ry_id',
										sortable: false,
										hidden: true
									},{
										text: '姓名',
										dataIndex: 'gl_name',
										width: 200
									},{
										text: '电话',
										dataIndex: 'gl_phone',
										width: 150
									},{
										text: '邮件',
										dataIndex: 'gl_email',
										flex: 1
									},{
										text: '操作',
										menuDisabled: true,
										sortable: false,
										width:100,
										xtype: 'actioncolumn',
										items:[
											{iconCls: 'add',tooltip: '添加',handler: 'addGlry'},
											'-',
											{iconCls: 'remove',tooltip: '删除',handler: 'deleteGlry'}
										]
									}],
									tbar: [{
										xtype:"toolbar",
										items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addGlryT'}]
									}],
									bbar: {
										xtype: 'pagingtoolbar',
										pageSize: 10,
										listeners:{
											afterrender: function(pbar){
												var grid = pbar.findParentByType("grid");
												pbar.setStore(grid.store);
											}
										},
										displayInfo: true,
										displayMsg: '显示{0}-{1}条，共{2}条',
										beforePageText: '第',
										afterPageText: '页/共{0}页',
										emptyMsg: '没有数据显示',
										plugins: new Ext.ux.ProgressBarPager()
									}
								},
								//报名流程设置
								{
									title: "报名流程设置",
									name:"grid_pc",
									bodyPadding: 10,
									layout: {
										align: 'stretch'
									},
									bodyStyle:'overflow-y:auto;overflow-x:hidden',
									fieldDefaults: {
										msgTarget: 'side',
										labelWidth: 30,
										labelStyle: 'font-weight:bold'
									}, 
									items: [
										{
											layout: {
												type: 'column'
											},
											items:[
											{
												xtype: 'combobox',
												fieldLabel: '报名流程模板',
												name: 'bmlc_mb',
												queryMode: 'remote',
												editable:false,
												valueField: 'TZ_APPPRO_TMP_ID',
												displayField: 'TZ_APPPRO_TMP_NAME',
												width:280,
												columnWidth:.4,
												labelWidth: 100,
												margin:'0 10 0 0',
												listeners:{
													change:function(file, value, eOpts){
														var x = file.findParentByType('ClassInfo').down('grid[name=applyItemGrid]');
														var bj_id=file.findParentByType('form').getForm().findField('bj_id').getValue();
														x.store.tzStoreParams='{"lc_id":"'+value+'","bj_id":"'+bj_id+'","queryID":"5"}',
														x.store.reload();
													}
												}
											},{
												xtype:"button",
												columnWidth:.15,
												text:'使用此模型',
												margin:'0 0 0 10',
												handler: 'Bmmb_Qr'
											},{
												columnWidth:.2,
												text:'新增报名流程模板',
												html: "<a href='javascript:void(0)' onclick='addBmmb()'>新增报名流程模板</a>",
												margin:'7 0 0 25',
												handler: 'addBmmb'
											}]
										},{
											xtype: 'grid',
											height: 360, 
											frame: true,
											name: 'applyItemGrid',
											plugins: {
												ptype: 'cellediting',
												pluginId: 'bmlc_xq_bj',
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
															items[i].set('bmlc_xh',i+1);
														}
														data.view.store.endUpdate();
													}
												}
											},
											columnLines: true,
											//selModel:{
											//	type: 'checkboxmodel'
											//},
											reference: 'bmlc_xq',
											style:"margin:10px",
											store: {
												type: 'BmlcStore'
											},
											columns: [
												{ 
													text: '班级id',
													sortable: false,
													dataIndex: 'bj_id',
													hidden: true
												},{ 
													text: '报名流程id',
													sortable: false,
													dataIndex: 'bmlc_id',
													hidden: true
												},{ 
													text: '序号',
													dataIndex: 'bmlc_xh',
													width: 150
												},{
													text: '流程名称',
													dataIndex: 'bmlc_name',
													editor: {
														 xtype:'textfield'
													},
													flex: 1
												},{ 
													text: '默认显示内容',
													sortable: false,
													dataIndex: 'bmlc_desc',
													hidden: true
												},{
													//xtype: 'linkcolumn',
													text: '默认显示内容',
													align: 'center',
													groupable: false,
													width: 150,
													renderer: function(v) {
														return '<a href="javascript:void(0)">设置</a>';
													},
													listeners:{
														click:'bmlcMrnrXs'
													}
												},{
													text: '操作',
													menuDisabled: true,
													sortable: false,
													align: 'center',
													width: 120,
													xtype: 'actioncolumn',
													items:[
														{iconCls: 'add',tooltip: '新增',handler: 'addBmlc'},'-',
														{iconCls: 'remove',tooltip: '删除',handler: 'deleteBmlc'}
													]
												}
											],
											bbar: {
												xtype: 'pagingtoolbar',
												pageSize: 10,
												listeners:{
													afterrender: function(pbar){
														var grid = pbar.findParentByType("grid");
														pbar.setStore(grid.store);
													}
												},
												displayInfo: true,
												displayMsg: '显示{0}-{1}条，共{2}条',
												beforePageText: '第',
												afterPageText: '页/共{0}页',
												emptyMsg: '没有数据显示',
												plugins: new Ext.ux.ProgressBarPager()
											}
										}
									]
								},
								//递交资料设置
								{
									title: "递交资料设置",
									name:"grid_pc",
									bodyPadding: 10,
									layout: {
										align: 'stretch'
									},
									bodyStyle:'overflow-y:auto;overflow-x:hidden',
									fieldDefaults: {
										msgTarget: 'side',
										labelWidth: 30,
										labelStyle: 'font-weight:bold'
									},
									items: [
										{
											layout: {
												type: 'column'
											},
											items:[
											{
												xtype: 'combobox',
												fieldLabel: '递交资料模型',
												name: 'djzl_mx',
												queryMode: 'remote',
												editable:false,
												valueField: 'TZ_SBMINF_TMP_ID',
												displayField: 'TZ_SBMINF_TMP_NAME',
												width:280,
												columnWidth:.4,
												labelWidth: 100,
												margin:'0 10 0 0'
												/*listeners:{
													change:function(file, value, eOpts){
														var x = file.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
														var bj_id=file.findParentByType('form').getForm().findField('bj_id').getValue();
														x.store.tzStoreParams='{"zl_id":"'+value+'","bj_id":"'+bj_id+'","queryID":"6"}',
														x.store.reload();
													}
												}*/
											},{
												xtype:"button",
												columnWidth:.15,
												text:'使用此模型',
												margin:'0 0 0 10',
												listeners:{
													click:function(btn){
														var x = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
														var bj_id=btn.findParentByType('form').getForm().findField('bj_id').getValue();
														var _zl_mx=btn.findParentByType('form').getForm().findField('djzl_mx').getValue();
														if (Ext.isEmpty(_zl_mx)){
															Ext.Msg.alert("提示","请先选择模型！");
														}else{
															x.store.tzStoreParams='{"zl_id":"'+_zl_mx+'","bj_id":"'+bj_id+'","queryID":"6"}',
															x.store.reload();
														}
													}
												}
											},{
												columnWidth:.2,
												text:'新增报名流程模板',
												html: "<a href='javascript:void(0)' onclick='add_djzl()'>新增报名流程模板</a>",
												margin:'7 0 0 25'
												//handler: 'addBmmb'
											}]
										},{
											xtype: 'grid',
											height: 360, 
											frame: true,
											name: 'applyItemGrid1',
											plugins: {
												ptype: 'cellediting',
												pluginId: 'djzl_xq_bj',
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
															items[i].set('djzl_xh',i+1);
														}
														data.view.store.endUpdate();
													}
												}
											},
											columnLines: true,
											//selModel:{
											//	type: 'checkboxmodel'
											//},
											reference: 'djzl_qqq',
											style:"margin:10px",
											store: {
												type: 'DjzlStore'
											},
											columns: [
												{ 
													text: '班级id',
													sortable: false,
													dataIndex: 'bj_id',
													hidden: true
												},{ 
													text: '资料设置id',
													sortable: false,
													dataIndex: 'djzl_id',
													hidden: true
												},{ 
													text: '显示顺序',
													dataIndex: 'djzl_xh',
													width: 100
												},{
													text: '内容简称',
													dataIndex: 'djzl_name',
													editor: {
														 xtype:'textfield'
													},
													flex: 1
												},{
													text: '备注',
													dataIndex: 'djzl_bz',
													editor: {
														 xtype:'textfield'
													},
													width: 220
												},{
													//xtype: 'linkcolumn',
													text: '常用回复短语设置',
													align: 'center',
													groupable: false,
													width: 150,
													renderer: function(v) {
														return '<a href="javascript:void(0)">设置</a>';
													},
													listeners:{
														click:'DjzlDyhf'
													}
												},{
													text: '操作',
													menuDisabled: true,
													sortable: false,
													align: 'center',
													width: 120,
													xtype: 'actioncolumn',
													items:[
														{iconCls: 'add',tooltip: '新增',handler: 'addDjzl'},'-',
														{iconCls: 'remove',tooltip: '删除',handler: 'deleteDjzl'}
													]
												}
											],
											bbar: {
												xtype: 'pagingtoolbar',
												pageSize: 10,
												listeners:{
													afterrender: function(pbar){
														var grid = pbar.findParentByType("grid");
														pbar.setStore(grid.store);
													}
												},
												displayInfo: true,
												displayMsg: '显示{0}-{1}条，共{2}条',
												beforePageText: '第',
												afterPageText: '页/共{0}页',
												emptyMsg: '没有数据显示',
												plugins: new Ext.ux.ProgressBarPager()
											}
										}
									]
								},
								//更多信息
								{
									title: "更多信息",
									xtype: 'form',
									name:'form_2',
									items: conItems,
									buttonAlign: 'left'
								}
							]
						}
					]
		
				}
			]
		});
		
		this.callParent();
	},
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
function add_Bmlc(){
	var me = this;
	console.log(me);
	Ext.tzSetCompResourses("TZ_PM_BMLCMBGL_COM");
	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_GD_LCMBCHSE_STD"];
	//var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
	if (pageResSet == "" || pageResSet == undefined) {
		Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
	}
	var className = pageResSet["jsClassName"];
	if (className == "" || className == undefined) {
		Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_LCMBCHSE_STD，请检查配置。');
		return;
	}
	//var win = this.lookupReference('myBmbRegWindow');
	var win="";
	if (!win) {
		Ext.syncRequire(className);
		ViewClass = Ext.ClassManager.get(className);
		win = new ViewClass();
		//this.getView().add(win);
	}else{
		var activeTab = win.items.items[0].getActiveTab();
		document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
	}
	win.show();
	if (!window.mybmb_cj) {
		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"),
			function(i) {
				this.style.backgroundColor = null
			});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			var activeTab = win.items.items[0].getActiveTab();

			var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
	}
};


function add_djzl(me1){
	console.log(me1);
	Ext.tzSetCompResourses("TZ_GD_SMTDTMDL_COM");
	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
		//var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
	if (pageResSet == "" || pageResSet == undefined) {
		Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
	}
	var className = pageResSet["jsClassName"];
	if (className == "" || className == undefined) {
		Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_NEWDJ_STD，请检查配置。');
		return;
	}
var win;
	if (!win) {
		Ext.syncRequire(className);
		ViewClass = Ext.ClassManager.get(className);
		win = new ViewClass();
		me1.getView().add(win);
	}else{
		var activeTab = win.items.items[0].getActiveTab();
		document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
	}
	win.show();
	if (!window.mybmb_cj) {
		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					me1.style.backgroundColor = null
				});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			var activeTab = win.items.items[0].getActiveTab();
			var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
	}

	/*
	var win = me1.lookupReference('myBmbRegWindow');
	if (!win) {
		Ext.syncRequire(className);
		ViewClass = Ext.ClassManager.get(className);
		win = new ViewClass();
		me1.getView().add(win);
	}else{
		var activeTab = win.items.items[0].getActiveTab();
		document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
	}
	win.show();
	if (!window.mybmb_cj) {
		window.mybmb_cj = function(el) {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					me1.style.backgroundColor = null
				});
			el.style.backgroundColor = "rgb(173, 216, 230)";
			var activeTab = win.items.items[0].getActiveTab();
			var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
		}
	}
	*/
};