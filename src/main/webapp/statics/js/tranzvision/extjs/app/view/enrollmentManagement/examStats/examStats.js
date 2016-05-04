/**
 * 学生报考多项目统计
 */
Ext.define('KitchenSink.view.enrollmentManagement.examStats.examStats', {
	extend: 'Ext.panel.Panel',
	xtype: 'examStats',
	controller: 'studentsExamController',
	requires: ['Ext.data.*',
	           'Ext.grid.*', 
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'KitchenSink.view.enrollmentManagement.examStats.studentsExamStore',
	           'KitchenSink.view.enrollmentManagement.examStats.studentsInfoStore',
	           'KitchenSink.view.enrollmentManagement.examStats.studentsExamController',
	           'KitchenSink.view.enrollmentManagement.examStats.projectListWindow',
	           'KitchenSink.view.enrollmentManagement.examStats.searchStuInfoWindow',
	           'tranzvision.extension.grid.Exporter'],
	//默认新增
	title: '学生报考多项目统计',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	actType:'add',
	statsId: '',
	username: '',
	isMulti: '',
	isFirst:true,

	listeners: {
		afterrender: function(panel) {
			console.log(panel);
			var store = panel.down("grid[id=classGrid]").getStore();
			store.load({
				callback: function(records, operation, success) {
					//加载学生报考多项目信息
					if(panel.isFirst){
						panel.isFirst = false;
						panel.getController().createStuInfo(store);
					}
				}
			});
		}
	},
	initComponent: function() {
		var myGridStore = new KitchenSink.view.enrollmentManagement.examStats.studentsExamStore({
			statsId: this.statsId
		});
		me = this;
		Ext.apply(this, {
			items: [{
				xtype: 'form',
				reference: 'studentsExamForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				bodyStyle: 'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 140,
					labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'textfield',
					fieldLabel: '统计编号',
					name: 'statsId',
					value:me.statsId,
					hidden: true
				},
				{
					xtype: 'combo',
					fieldLabel: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.projectName", "待统计学生所属项目"),
					name: 'projectName',
					queryMode: 'local',
					editable: false,
					value: me.projectName,
					valueField: 'TZ_CLASS_ID',
					displayField: 'TZ_CLASS_NAME',
					store: new KitchenSink.view.common.store.comboxStore({
						recname: "PS_TZ_CLASS_INF_T",
						condition: {TZ_JG_ID: {value: Ext.tzOrgID,operator: "01",type: "01"}},
						result: 'TZ_CLASS_ID,TZ_CLASS_NAME'
					}),
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: function(field){field.setValue();}
                        }
                    }
				},
				{
					xtype: 'panel',
					title: '统计项目',
	                collapsible:true,
	                collapsed:false,
	                frame:true,
					items: [{
						xtype: 'grid',
						id:'classGrid',
						store: myGridStore,
						dockedItems: [{
							xtype: 'toolbar',
							items: [
								{text: "新增",tooltip: "新增",iconCls: "add",handler: "addStatsClass"},
								{text: "删除",tooltip: "删除",iconCls: "remove",handler: "delStatsClass"},
								{text: "生成/重新生成报表",tooltip: "生成/重新生成报表",handler: "generateReports"}
							]
						}],
						selModel: {
							type: 'checkboxmodel'
						},
			            viewConfig: {
			                plugins: {
			                    ptype: 'gridviewdragdrop',
			                    dragText: '拖拽进行选项的排序'
			                },
			                listeners: {
			                    drop: function(node, data, dropRec, dropPosition) {
			                        data.view.store.beginUpdate();
			                        var items = data.view.store.data.items;
			                        for (var i = 0; i < items.length; i++) {
			                            items[i].set('order', i + 1);
			                        }
			                        data.view.store.endUpdate();
			                    }
			                }
			            },
						columns: [{
							text: '统计编号',
							dataIndex: 'statsId',
							hidden: true
						},
						{
							text: '班级编号',
							dataIndex: 'classId',
							hidden: true
						},
						{
			                text: '排列顺序',
			                dataIndex: 'order',
			                hidden: true
			            },
						{
							text: '班级名称',
							dataIndex: 'className',
							width: 600
						},
						{
							text: '状态',
							dataIndex: 'classStatus',
							width: 300,
							renderer: function(value) {
								if (value == 'Y') {
									return '开通';
								} else {
									return '未开通';
								}
							}
						},
						{
							menuDisabled: true,
							xtype: 'actioncolumn',
							text: '操作',
							menuText: '操作',
							align: 'center',
							items: [{iconCls: 'remove',tooltip: '删除',handler: 'deleteClass'}],
							flex: 1
						}]
					}]
				},
				{
					xtype: 'panel',
					title: '学生报考多项目信息',
	                style: 'margin-top:15px;',
	                name: 'stuInfo',
	                collapsible:false,
	                collapsed:false,
	                frame:true
				}]
			}]
		});
		this.callParent();
		
	},
	constructor: function(appColumns, colStore) {
		me = this;
		var tzParams = '{"ComID":"TZ_EXAM_COUNT_COM","PageID":"TZ_EXAM_COUNT_STD","OperateType":"EJSON","comParams":{"type":"PNAME"}}';
		Ext.tzLoadAsync(tzParams, function(formData) {
			me.statsId = formData.statsId;
			me.projectName = formData.projectName;
			if(formData.statsId != ""){
				me.actType = "update";
			}
		});
		this.callParent();
	},
	buttons: [
	          {text: '保存',iconCls: "save",handler: 'onStatsSave'},
	          {text: '确定',iconCls: "ensure",handler: 'onStatsEnsure'},
	          {text: '关闭',iconCls: "close",handler: 'onStatsClose'}
	]
});