Ext.define('KitchenSink.view.home.Home', {
    extend: 'Ext.container.Container',
    alias: 'widget.home',
    requires: [
        'Ext.layout.container.Border',
        'Ext.dashboard.Dashboard',
        'KitchenSink.view.home.HomeDashboard',
        'KitchenSink.view.home.HomeController'
    ],
    layout: {
        type: 'border'
    },
    controller: 'home',
    initComponent: function () {
        var columnWidths,defaultContent;

        var tzParams = '{"ComID":"TZ_GD_HOME_COM","PageID":"TZ_GD_HOME_STD","OperateType":"QF","comParams":{}}';


        this.getController().loadAsync(tzParams,function(responseData){
            if(responseData.state!=undefined){
                columnWidths =responseData.state.columnWidths;
                defaultContent =responseData.state.items;
            }else{
                columnWidths=[0.50,0.50];
                defaultContent =  [{
                    type: 'userInfo',
                    columnIndex: 0,
                    height: 300
                },{
                    type: 'testGrid2',
                    columnIndex: 1,
                    height: 300
                }, {
                    type: 'testGrid3',
                    columnIndex: 0,
                    height: 300
                },{
                    type: 'testGrid4',
                    columnIndex: 1,
                    height: 300
                },{
                    type: 'testGrid5',
                    columnIndex: 0,
                    height: 300
                },{
                    type: 'testGrid6',
                    columnIndex: 1,
                    height: 300
                }];
            }

        });

        Ext.apply(this, {
            items: [
                {
                    xtype: 'toolbar',
                    region: 'north',
                    cls: 'app-header',
                    overflowHandler: 'Menu',
                    height: 40,
                    style:'background:white',
                    layout: {
                        type: 'hbox',
                        align: 'middle'
                    },
                    items: [{
                        text:'收藏夹',
                        menu:[
                            {text:'班级管理'},
                            {text:'项目管理'},
                            {text:'报名表审核'},
                            {text:'报名表模板管理'},
                            {text:'会员用户管理'},
                            {text:'机构基础信息'}
                        ]
                    }, '-', {
                        text:'常用链接',
                        menu: [{
                            text:'学院网站',
                            menu:[{
                                text:'Link 1'
                            },
                                {
                                    text:'Link 2'
                                },
                                {
                                    text:'Link 3'
                                }]
                        },{
                            text:'教育部官方网站'
                        },{
                            text:'人民网'
                        },{
                            text:'12306'
                        },{
                            text:'北京创景咨询有限公司'
                        },{
                            text:'其它网站',
                            menu:[{
                                text:'Link 4'
                            },
                                {
                                    text:'Link 5'
                                },
                                {
                                    text:'Link 6'
                                }]
                        }]
                    }, "->",
                        {
                            xtype: 'splitbutton',
                            text: '设置首页',
                            iconCls:'set',
                            menu: [{
                                text:'页面布局',
                                iconCls:'audit',
                                menu: [{
                                    text: '一列布局',
                                    handler: 'onLayoutSet',
                                    layoutType:1
                                },{
                                    text: '两列布局',
                                    handler: 'onLayoutSet',
                                    layoutType:2
                                },{
                                    text: '三列布局',
                                    handler: 'onLayoutSet',
                                    layoutType:3
                                }]
                            },{
                                text:'首页内容',
                                menu:[{
                                    text: '个人信息',
                                    handler: 'onAddComponent',
                                    addType:'userInfo'
                                },{
                                    text: '类别设置',
                                    handler: 'onAddComponent',
                                    addType:'testGrid2'
                                },{
                                    text: '标签设置',
                                    handler: 'onAddComponent',
                                    addType:'testGrid3'
                                },{
                                    text: '材料评审',
                                    handler: 'onAddComponent',
                                    addType:'testGrid4'
                                },{
                                    text: '班级管理',
                                    handler: 'onAddComponent',
                                    addType:'testGrid5'
                                },{
                                    text: '项目管理',
                                    handler: 'onAddComponent',
                                    addType:'testGrid6'
                                },{
                                    text: '待办任务',
                                    handler:'onAddComponent',
                                    addType:'testGrid7'
                                }]
                            }]
                        }
                    ]
                },{
                    xtype: 'homedashboard',
                    reference: 'homedashboard',
                    region: 'center',
                    stateful: false,
                    columnWidths:columnWidths,
                    defaultContent: defaultContent
                }]
        });
        this.callParent();
    }
});
