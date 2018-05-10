Ext.define('KitchenSink.view.interviewManagement.interviewArrange.addStudentWin', {
    extend: 'Ext.window.Window',
    requires: [
    	'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.addStudentStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListController'
	],
	reference: 'addStudentWin',
    xtype: 'addStudentWin',
	controller:'interviewArrangeSetStuListController',
	
	width: 1000,
	height: 500,
	minWidth: 800,
	minHeight: 450,
    title: '选择考生',
	layout: 'fit',
	resizable: false,
	modal: true,
	ignoreChangesFlag:true,
	
	constructor: function(config){
		this.classID = config.classID;
		this.batchID = config.batchID;

		this.callback = config.callback;
		
		this.callParent();	
	},
	
	initComponent: function (){
		var me = this;
		var store = new KitchenSink.view.interviewManagement.interviewArrange.addStudentStore({
			classID: me.classID,
			batchID: me.batchID
		});
		
		Ext.apply(this,{    
			items: [{
		        xtype: 'grid',
		        frame: false,
                columnLines: true,
                border: true,
				minHeight: 400,
                plugins: [{
                        ptype: 'gridfilters'
                }],
                selModel:{
                    type: 'checkboxmodel',
					mode: "MULTI"
                },
                multiSelect:true,
                
                dockedItems:[{
					xtype:"toolbar",
					items:[{
						text:"搜索",iconCls: "query",handler: "searchStudents"
					}]
				}],
				store:store ,
                columns: [{
                	text: '报名表编号',
                	dataIndex: 'appId',
                	width: 100,
                	hidden: true
                },{
                	text: "面试申请号",
                	dataIndex: 'mssqh',
                	width: 100,
                	minWidth: 80
                },{
                    text: '姓名',
                    sortable: true,
                    width: 80,
                	minWidth: 80,
                    dataIndex: 'stuName',
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '报考班级',
                	dataIndex: 'className',
                	width: 120,
                	minWidth: 100,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '申请面试批次',
                	dataIndex: 'batchName',
                	width: 100,
                	minWidth: 80,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '地区',
                	dataIndex: 'area',
                	width: 100,
                	minWidth: 80,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '公司名称',
                	dataIndex: 'componey',
                	width: 120,
                	minWidth: 100,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '手机',
                	dataIndex: 'mobile',
                	width: 120,
                	minWidth: 100,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
                	text: '邮箱',
                	dataIndex: 'email',
                	width: 120,
                	minWidth: 100,
                	filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    },
                    flex:1
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 200,
                    store:store,
                    displayInfo: true,
                    plugins: new Ext.ux.ProgressBarPager()
                }
			}],
		});
		this.callParent();
	},
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'ensureAddStudent'
	},{
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType("window").close();
		}
	}]
});