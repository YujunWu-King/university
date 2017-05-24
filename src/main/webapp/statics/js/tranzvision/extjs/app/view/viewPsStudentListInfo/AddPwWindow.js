Ext.define('KitchenSink.view.viewPsStudentListInfo.AddPwWindow', {
	extend: 'Ext.window.Window',
	xtype: 'addpwwindow',
	//title: '',
	width: 650,
	//height: 620,
	minWidth: 400,
	minHeight: 420,
	maxHeight: 600,
	resizable: true,
	modal: true,
	closeAction: 'destroy',
	ignoreChangesFlag: true,
	//让框架程序不要提示用户保存的属性设置
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	requires: ['Ext.ux.ProgressBarPager', 
	'KitchenSink.view.viewPsStudentListInfo.AddPwWindowStore', 
	'KitchenSink.view.viewPsStudentListInfo.AddPwWindowModel', 
	'KitchenSink.view.common.store.comboxStore'],
	listeners: {
		resize: function(win) {
			win.doLayout();
		}
	},

	initComponent: function() {
		var me = this;
		var groupidarray = "";
		var groupnamearray = "";
		var store = new KitchenSink.view.viewPsStudentListInfo.AddPwWindowStore();
		var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"JUDGROUPALL","comParams":{}}';
		Ext.tzLoad(tzParams, function(responseData) {
			//资源集合信息数据
			var formData = responseData.judggroup;
			var groupid = formData.substring(0, formData.indexOf("|"));
			var groupname = formData.substring(formData.indexOf("|") + 1, formData.length + 1);
			console.log(groupid);
			console.log(groupname);
			groupidarray = groupid.split(",");
			groupnamearray = groupname.split(",");
		});


		Ext.apply(this, {
			items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				ignoreLabelWidth: true,
				fieldDefaults: {
					msgTarget: 'side',
					labelStyle: 'font-weight:bold'
				},

				items: [{
					layout: 'column',
					items: [{
						xtype: 'displayfield',
						labelWidth: 150,
						width: 220,
						labelSeparator: '',
						fieldLabel: '评委账号',
						value: '包含',
						name: 'judgeID'
					}, {
						xtype: 'textfield',
						columnWidth: 1,
						hideEmptyLabel: true,
						name: 'judgeID',
						value: ''
					}]
				}, {
					layout: 'column',
					items: [{
						xtype: 'displayfield',
						labelWidth: 150,
						width: 220,
						labelSeparator: '',
						fieldLabel: '评委姓名',
						value: '包含',
						name: 'judgeID'
					}, {
						xtype: 'textfield',
						columnWidth: 1,
						hideEmptyLabel: true,
						name: 'judgeName',
						value: ''
					}]
				}]




				,
				buttonAlign: 'left',
				buttons: [{
					text: '搜索',
					iconCls: "search",
					handler: function(btn) {
						me.searchPrompt();
					}
				}, {
					text: '清除',
					iconCls: "clean",
					handler: function(btn) {
						//搜索信息表单
						var form = btn.findParentByType("form").getForm();
						//重置表单
						form.reset();
					}
				}]
			}, {
				//html:html,
				itemId: 'resultDesc'
				// html:"<span style='color:red; margin-left:10px;'>搜索结果记录总数超过"+this.maxRow+"条，当前只展示前"+this.maxRow+"条搜索记录，请输入搜索条件缩小搜索范围。</span>"
			}, {
				xtype: 'grid',
				reference: 'judggrid',
				height: 'auto',
				minHeight: 180,
				title: '搜索结果列表',
				plugins: {
					ptype: 'cellediting'
				},
				selModel: {
					type: 'checkboxmodel'
				},
				//frame: true,
				columnLines: true,
				//style:"margin:10px",
				dockedItems: [{
					xtype: "toolbar",
					items: [/*{

						text: '添加',
						handler: this.onAddClick

					},*/ {
						xtype: 'splitbutton',
						text: '批量评委组',
						reference: 'batchsetsplit',
						iconCls: 'set',
						menu: ''

					}]
				}],
				columns: [{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.judgId", "评委账号"),
					dataIndex: 'judgId',
					width: 150,
					editor: {
						allowBlank: false
					},
					hidden:true
				},
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.judgId", "评委账号"),
					dataIndex: 'judzhxx',
					width: 150,
					editor: {
						allowBlank: false
					}
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.judgName", "评委姓名"),
					dataIndex: 'judgName',
					width: 150,
					editor: {
						allowBlank: false
					}
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.judgGroup", "评委组"),
					dataIndex: 'judgGroupId',
					name: 'judgGroupId',
					width: 150,
					editor: {
						xtype: 'combobox',
						valueField: 'TZ_CLPS_GR_ID',
						displayField: 'TZ_CLPS_GR_NAME',
						store: new KitchenSink.view.common.store.comboxStore({
							recname: 'TZ_MSPS_GR_TBL',
							condition: {
								TZ_JG_ID: {
									value: Ext.tzOrgID,
									operator: "01",
									type: "01"
								}
							},
							result: 'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
						})
					},
					flex: 1,
					//动态renderer
					renderer: function(v) {
						for (var i in groupidarray) {
							if (v == groupidarray[i]) {
								return groupnamearray[i];
							}
						}
					}
				}],

				store: store,
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 50,
					listeners: {
						afterrender: function(pbar) {
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
			}],
			buttons: [{
				text: '确定',
				iconCls: "ensure",
				handler: function(btn) {
					selksList = "";
					//获取窗口
					var win = btn.findParentByType("window");
					//选中行
					var selection = win.child("grid").getSelectionModel().getSelection();
					//选中行长度
					var checkLen = selection.length;
					if (checkLen == 0) {
						Ext.Msg.alert("提示", "未选中记录");
						return;
					} else {
						for (var i = 0; i < checkLen; i++) {
							/*if (selection[i].data.judgGroupId == "") {
									Ext.Msg.alert("提示", "请为" + selection[i].data.judgName + "评委设置分组");
									return;
								}else{*/
								if (selksList == "") {
			
									selksList = Ext.JSON.encode(selection[i].data);
								}

							 else {
									selksList = selksList + ',' + Ext.JSON.encode(selection[i].data);
								}
								}							
							}
/*
						}*/
						var form=btn.findParentByType('setmspsruler').down('form').getForm();
						var gridlist=btn.findParentByType('setmspsruler').down('grid[reference=pwlistgrid]');
					    var classId = form.findField('classId').getValue();
		                var batchId = form.findField('batchId').getValue();
		                
		                var comparams = '{"classId":' + classId + '},{"batchId":' + batchId + '},' + selksList + '';

					    var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_JUDGES_STD","OperateType":"U","comParams":{"add":[' + comparams + ']}}';
					
					    console.log(tzParams);
		                Ext.tzSubmit(tzParams, function() {
		               	gridlist.getStore().reload();

		                }, "添加成功!", true, this)
						gridlist.getStore().reload();


					//修改密码信息表单
					var form = win.child("form").getForm();
					//重置表单
					form.reset();
					//关闭窗口
					win.close();
				}
			}, {
				text: '关闭',
				iconCls: "close",
				handler: function(btn) {
					//获取窗口
					var win = btn.findParentByType("window");
					//修改密码信息表单
					var form = win.child("form").getForm();
					//关闭窗口
					win.close();
				}
			}]
		});
		this.callParent();
	},
	searchPrompt: function() {
		var srhconditions = [];
		//搜索信息表单
		var form = this.child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		//搜索结果列表
		var grid = this.child("grid");
		//搜索结果数据源
		var store = grid.getStore();
		//搜索条件

		//交互参数
		store.tzStoreParams = '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.TZ_MSJU_REL_VW","condition":{"OPRID-operator": "07","OPRID-value": "' + formParams.judgeID + '","TZ_REALNAME-operator": "07","TZ_REALNAME-value": "' + formParams.judgeName + '","TZ_JG_ID-operator": "07","TZ_JG_ID-value": "' + Ext.tzOrgID + '"}}';
		store.load();
	},
	

});