Ext.define('KitchenSink.view.template.survey.question.wjdcPeoplePanel', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.survey.question.wjdcPeopleModel',
        'KitchenSink.view.template.survey.question.wjdcController',
        'KitchenSink.view.template.survey.question.wjdcPeopleStore',
        'tranzvision.extension.grid.Exporter',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'wjdcPeoplePanel',
    controller: 'wjdcController',
    reference:'wjdcPeoplePanel',
    wjId:'',
    schLrId:'',
    store: {
        type: 'wjdcPeopleStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    plugins:[
        {ptype: 'gridexporter'}
    ], 
    style:"margin:8px",
    multiSelect: true,
    title: '参与人管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:[ '->',/*{minWidth:80,text:"保存",iconCls:"save",handler:"wjdcPeopleSave"},'-',
                {minWidth:80,text:"确定",iconCls:"ensure",handler:"wjdcPeopleSure"},'-',*/
                {minWidth:80,text:"关闭",iconCls:"close",handler:"wjdcInfoClose"},'-'
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'findDcwjPeople'},'->',
            {
                xtype:'splitbutton',
                text:'更多操作',
                iconCls:  'list',
                glyph: 61,
                menu:[
                    {
                        text: '发送短信',
                        handler: 'sendSmsToCyr'
                    }, {
                        text: '发送邮件',
                        handler: 'sendEmailToCyr'
                    },  {
                        text: '导出参与人',
                        handler: 'downloadAllCyr' 
                    }]
            }
        ]
    }], 
    initComponent: function () {
        var store = new KitchenSink.view.template.survey.question.wjdcPeopleStore();
        Ext.apply(this, {
            columns: [
               {
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PERSON_STD.TZ_FIRST_NAME","姓名"),
                    sortable: true, 
                    dataIndex: 'name',
                    width: 200,
                    renderer:function(v){
                        return '<a href="javascript:void(0)">'+v+'</a>';
                    },
                    listeners: {
                       click: 'cyrDcStatusDetail'
                    }
               },
                {
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PERSON_STD.TZ_PHONE","手机"),
                    sortable: true,
                    dataIndex: 'phone',
                    width: 200
                },
                {   text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PERSON_STD.TZ_EMAIL","邮箱"),
                    sortable: true,
                    dataIndex: 'email',
                    width: 220
                },{
                    text: '完成状态',
                    sortable: false,
                    dataIndex: 'dcState', 
                    width: 120
                },
                {   text: '通过/未通过',
                    dataIndex: 'isApply',
                    width: 120,
                    align: 'center',
                    groupable: false,
                    renderer: function (v) {
                        if (v == "Y") {
                            return '<a href="javascript:void(0)">通过</a>';
                        } else {
                        	if(v == "N"){
                        		return '<a href="javascript:void(0)">未通过</a>';
                        	}else{
                        		return "未通过";
                        	}
                            
                        }
                    },
                    listeners: {
                        click: 'cyrScholarStatus'
                    } 
                }],
                store:store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg: '显示{0}-{1}条，共{2}条',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    emptyMsg: '没有数据显示',
                    plugins: new Ext.ux.ProgressBarPager()
                }
        });
        this.callParent();
    } 
});
