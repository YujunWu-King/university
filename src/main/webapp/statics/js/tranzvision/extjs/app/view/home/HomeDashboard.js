Ext.define('KitchenSink.view.home.HomeDashboard', {
    extend: 'Ext.dashboard.Dashboard',
    alias: 'widget.homedashboard',
    requires: [
        'KitchenSink.view.enrollmentManagement.applicationForm.classManagement',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReview',
        'KitchenSink.view.enrollmentManagement.tag.tagList',
        'KitchenSink.view.enrollmentManagement.color.colorList',
        'KitchenSink.view.classManage.classManage',
        'KitchenSink.view.enrollProject.projectMg.projectMgList',
        'KitchenSink.view.home.markets.Markets',
		'KitchenSink.view.home.stocks.Stocks',
        'KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHome'
    ],
    listeners:{
        resize:"resizeSaveState"
    },
    parts: {
		basicForm: {
                viewTemplate: {
                    title: '百度',
					closable:false,
					collapsible:false,
					draggable:false,
                    items: [
					/*{
                        xtype: 'panel',
						items:[
						{html:'<img src="http://101.200.181.213:8550/ueditor/jsp/upload/image/1446520683494080430.png" width="100%" height="100%">'}
						]
                    }*/
					{
						xtype:'externallink'
					,externalLink:"http://www.baidu.com"}
				    
					]
                }
            },
		stocks: {
                viewTemplate: {
                    title: 'Markets',
                    items: [{
                        xtype: 'markets'
                    }]
                }
            },

            stockTicker: {
                viewTemplate: {
                    title: 'Stocks',
                    items: [{
                        xtype: 'stocks'
                    }]
                }
            },        
        testGrid: {
            viewTemplate: {
                title: '最近报考考生',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'appFormAuditing',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid2: {
            viewTemplate: {
                title: '类别设置',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'colorSet',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid3: {
            viewTemplate: {
                title: '标签设置',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'tagSet',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid4: {
            viewTemplate: {
                title: '材料评审',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'interviewReview',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid5: {
            viewTemplate: {
                title: '班级管理',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'classManage',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid6: {
            viewTemplate: {
                title: '项目管理',
                tools:[
                    {
                        type: 'gear',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'refresh',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'plus',
                        handler:'onDashboardSet'
                    },
                    {
                        type: 'help',
                        handler:'onDashboardSet'
                    }
                ],
                items: [{
                    xtype: 'projectMg',
                    style:"margin:0px",
                    frame:false
                }]
            }
        },testGrid7: {
            viewTemplate: {
                title:'待办任务',
                items:[{
                    xtype:'dbrwHome',
                    style:'margin:0px',
                    frame:false
                }]
            }
        }
    },
    style:'background:white;'
});