Ext.define('KitchenSink.view.viewPsStudentListInfo.ImportksInfoWindow', {
	extend: 'Ext.window.Window',
	xtype: 'importPsStu',
	controller: 'viewxscontrol',
	actType: 'add',
	requires: ['Ext.data.*', 
	'Ext.grid.*',
	'Ext.util.*', 
	'Ext.toolbar.Paging',
	'Ext.ux.ProgressBarPager', 
	'Ext.selection.CellModel', 
	'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController',
	'KitchenSink.view.viewPsStudentListInfo.ImportksInfoWindowStore',
	'KitchenSink.view.viewPsStudentListInfo.ImportksInfoWindowModel'
	],
	reference: "importPsStu",
	autoScroll: false,
	actType: 'add',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_DRKS_STD.addks", "导入考生列表"),
	frame: true,
	height: 500,
	width:380,

	initComponent: function() {
		var store=new KitchenSink.view.viewPsStudentListInfo.ImportksInfoWindowStore();
		Ext.apply(this, {

			items: [{
				xtype: 'form',
				reference:'cbaform',
				items: [{
					xtype: 'textfield',
					name: 'classId',
					hidden:true
					
					
				},{
					xtype: 'textfield',
					name: 'batchId',
					hidden:true
					
				},{ xtype:"displayfield",
                    fieldLabel:" ",
                    labelSeparator:"",
                    name:"ImpRecsCount"
                }]
			},

			{
				xtype: 'grid',
				columnLines: true,
				style: "margin:0px",
				selModel: {
					type: 'checkboxmodel'
				},
				plugins:[
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        clicksToEdit: 1,   //单击进行编辑
                        pluginId: 'msResImpGridCellEditing'
                    })
                ],

				layout: 'fit',
				minHeight: 300,

				reference: 'mspsKsDrGrid',
				columns: [  {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_DRKS_STD.ksNameg", "考生姓名"),
					dataIndex: 'ksName',
					width: 200,
					editor: {
                                   allowBlank: false
                                 }
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_DRKS_STD.appInsIdg", "报名表编号"),
					dataIndex: 'appInsId',
					width: 150,
					editor: {
                                   allowBlank: false
                                 }
					
				}],
				 store:store
				    
			}],

			buttons: [{
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_DRKS_STD.sure", "确定"),
				handler: 'addksDrSave',
				iconCls: 'ensure'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_DRKS_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'addksDrClose'
			}]

		});
		this.callParent();
	}

});