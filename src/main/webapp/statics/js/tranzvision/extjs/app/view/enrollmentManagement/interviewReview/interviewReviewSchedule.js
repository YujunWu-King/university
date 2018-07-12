Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewSchedule', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewReviewSchedule',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.chart.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewController', 
        'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleJudgeStore',
        'KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleAppsStore'
    ],
    title: '面试评审进度管理',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor: function (classID,batchID,transValue){
        this.classID=classID;
        this.batchID=batchID;
        this.transValue = transValue;
        this.callParent();
    },
    statisticsGrid:'',
    initComponent:function(){
        var classID =this.classID;
        var batchID = this.batchID;
        var statisticsGridDataModel;
        var transValue = this.transValue;
        var interviewReviewScheduleAppsStore = new KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewScheduleAppsStore();
        var interviewReviewScheduleChartStore=new Ext.data.JsonStore({
            fields:['name','data1'],
            data:[]
        });
        var interviewReviewScheduleChartStore2=new Ext.data.JsonStore({
            fields:[],
            data:[]
        });
        var dockedItemBtn,columnsItems;
        
        dockedItemBtn = {
                xtype: "toolbar",
                items: [
                    {text: "查询", tooltip: "查询", handler: "searchMsksList"},"-",
                    {text: "计算所有考生标准差", tooltip: "计算所有考生标准差", handler: "calculate"}
                ]
            };
            columnsItems = [
                {
                    text: "报名表编号",
                    dataIndex: 'insID',
                    align:'center',
                    minWidth: 150
                },
                {
                	text:"报名表模板ID",
                	dataIndex:'clpsBmbTplId',
                	hidden:true
                },
                {
                    header: "姓名",
                    dataIndex: 'name',
                    align:'center',
                    minWidth: 100,
                    xtype:'linkcolumn',
                    handler:'viewThisApplicationForm',
                    renderer:function(v){
                        this.text=v;
                        return ;
                    }
                } ,
                {
                    text: "性别",
                    dataIndex: 'gender',
                    align:'center',
                    minWidth: 30,
                    renderer: function (v) {
                        var x;
                        var genderStore = transValue.get("TZ_GENDER");
                        if(x = genderStore.find('TValue',v)>=0){
                            x=+x;
                            return genderStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                },
                {
                    text: "面试申请号",
                    dataIndex: 'mshID',
                    align:'center',                    
                    minWidth: 100
                },
                {
                    text: "面试资格",
                    dataIndex: 'viewQua',
                    align:'center',                    
                    minWidth: 130,
                    flex:1
                },
                {
                    text: "评委间偏差",
                    dataIndex: 'judgePC',
                    align:'center',                    
                    minWidth: 130,
                    flex:1
                },
                {
                    text: "评委信息",
                    dataIndex: 'judgeNames',
                    name:'judgeNames',
                    align:'center',
                    minWidth: 100,
                    flex:1,
                    renderer:function(v) {
                        if (v) {
                            return '<a class="tz_lzh_interviewReview_app" href = "javaScript:void(0)">' + v + '</a>';
                        } else {
                            return "";
                        }
                    },
                    listeners: {
                        click: 'viewJudge'
                    }
                },
                {
                    text: "评审状态",
                    dataIndex: 'judgeStatus',
                    align:'center',
                    minWidth: 150,
                    flex:1,
                    /*renderer: function (v,grid,record) {
                        var x;
                        var KSPWPSEHNZT = transValue.get("TZ_MSPS_KSZT");
                        if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                            return v==='N' ? KSPWPSEHNZT.getAt(x).data.TSDesc+"("+record.data.progress+")":KSPWPSEHNZT.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }*/
                } ,
                {
                    text: "录取状态",
                    dataIndex: 'status',
                    align:'center',
                    minWidth: 150,
                    renderer: function (v) {
                        var x;
                        var admitStore = transValue.get("TZ_LUQU_ZT");
                        if((x = admitStore.find('TValue',v))>=0){
                            return admitStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                }
           ]
            
	        /*var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"IFS","comParams":{"type":"IFP","classID":"'+this.classID+'","batchID":"'+this.batchID+'"}}';
	        Ext.tzLoadAsync(tzParams,function(respData){
	            
	        });*/
        
        //柱状图chart和曲线图chart
        var columnChart;
        var lineChart;
        columnChart = new Ext.chart.Chart({
            width: 860,
            height: 400,
            animate: true,
            store: interviewReviewScheduleChartStore,
            shadow: true,
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['data1'],
                minimum: 0,
                maximum:100,
                label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                title: '平均分',
                grid: true,
                minimum: 0
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['name'],
                title: '评委列表'
            }],
            series: [{
                type: 'column',
                axis: 'bottom',
                highlight: true,
                xField: 'name',
                yField: 'data1'
            }]
        });
        
        lineChart = new Ext.chart.Chart({
            xtype: 'chart',
            width: 860,
            height: 800,
            style: 'background:#fff',
            animate: true,
            store: interviewReviewScheduleChartStore2,
            shadow: true,
            theme: 'Category1',
            legend: {position: 'top'},
            axes: [
                {
                    type: 'Numeric',
                    position: 'left',
                    fields: [chartfields],
                    label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                    title: '分布比率',
                    grid: true,
                    maximum:100,
                    minimum:0
                },
                {
                    type: 'Category',
                    position: 'bottom',
                    fields: ['fbName'],
                    title: '分布区间',
                    label: {rotate: {degrees: 270}}
                }
            ]
        });
        //添加两个空的图表，解决没有内容时伸缩和点击放大后添加不进数据的问题

        var mainPageFrame = Ext.create('Ext.Panel',
            {
                xtype:'form',
                title: '图表区',
                collapsible:true,
                collapsed:true,
                plugins: {
                    ptype:'maximize'
                },
                name:'averageChart',
                items: [
                    lineChart
                ]
            });


        //柱状图和曲线图最大值最小值
        var columnMaxCount = 100;
        var columnMinCount = 0;
        var lineMaxCount = 100;
        var lineMinCount = 0;
        var chartfields;

        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD",' +
            '"OperateType":"QG","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","type":"getPwDfData"}}';

        Ext.tzLoadAsync(tzParams,function(respData) {
            var tmpArray = respData.pw_dfqk_grid;

            statisticsGridDataModel = {gridFields:[],gridColumns:[],gridData:[]};
            var tmpObject = {columns:[]};
            for(var i=0;i<tmpArray.length;i++)
            {
                var colName = '00' + (i + 1);
                colName = 'col' + colName.substr(colName.length - 2);
                var tmpColumn;
                
                if(i<5){
                	tmpColumn = {
                			text     : tmpArray[i][colName],
                            sortable : false,
                            dataIndex: colName,
                            width:85
                        };
                }else{
                	tmpColumn = {
                			text     : tmpArray[i][colName],
                            sortable : false,
                            dataIndex: colName,
                            flex:1
                        };
                }

                statisticsGridDataModel['gridColumns'].push(tmpColumn);
                statisticsGridDataModel['gridFields'].push({name:colName});
            }

            tmpArray = respData.pw_dfqk_grid_data;

            for(var i=0;i<tmpArray.length;i++)
            {
                var tmpdataArray = tmpArray[i].field_value;
                var dataRow = [];
                for(var j=0;j<tmpdataArray.length;j++) {
                    var colName = '00' + (j + 1);
                    colName = 'col' + colName.substr(colName.length - 2);
                    dataRow.push(tmpdataArray[j][colName]);
                }
                statisticsGridDataModel['gridData'].push(dataRow);
            }


        });

        this.statisticsGrid = statisticsGridDataModel;
        Ext.apply(this,{
            items: [
                {
                    xtype: 'form',
                    reference: 'interviewProgressForm',
                    name:'interviewProgressForm',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        labelWidth: 110,
                        labelStyle: 'font-weight:bold'
                    },

                    items: [
                        {
                            xtype: 'textfield',
                            name: 'classID',
                            hidden: true
                        },
                        {
                            xtype: 'textfield',
                            name: 'batchID',
                            hidden: true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "报考班级",
                            name: 'className',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "批次",
                            name: 'batchName',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "报考考生数量",
                            name: 'totalStudents',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                       // {
                        //    xtype: 'textfield',
                        //    fieldLabel: "材料评审考生",
                        //    name: 'materialStudents',
                        //    fieldStyle:'background:#F4F4F4',
                        //    readOnly:true
                        //},
                        {
                            xtype: 'textfield',
                            fieldLabel: "面试评审考生",
                            name: 'interviewStudents',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'fieldset',
                            title: '<span style="font-weight: bold;">阶段状态</span>',
                            collapsible: true,
                            defaults: {
                                labelWidth:120,
                                anchor: '100%',
                                layout: {
                                    type: 'hbox',
                                    defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                }
                            },
                            items:[{
                                layout: {
                                    type: 'column'
                                } ,
                                padding:'8px 0 4px 0',
                                items:[
                                    //使用layout form嵌套以避免IE中出现错位的BUG
                                    {
                                        columnWidth:.25,
                                        layout:'form',
                                        items:[{
                                            xtype: 'displayfield',
                                            fieldLabel: "开始时间",
                                            name: 'startDateTime',
                                            readOnly:true
                                        }]
                                    }, {
                                        columnWidth:.25,
                                        layout:'form',
                                        items:[{
                                            xtype: 'displayfield',
                                            fieldLabel: "结束时间",
                                            name: 'endDateTime',
                                            readOnly:true
                                        }]
                                    }, 
                                    {
                                        columnWidth:.2,
                                        layout:'form',
                                        items:[{
                                            xtype: 'displayfield',
                                            style:'margin-left:2em',
                                            fieldLabel:"评审进度",
                                            name:'progress',
                                            ignoreChangesFlag: true
                                        }]
                                    },
                                    {
                                        columnWidth:.2,
                                        layout:'form',
                                        items:[{
                                            xtype: 'displayfield',
                                            fieldLabel:"评审状态",
                                            name:'interviewStatus',
                                            ignoreChangesFlag: true
                                        }]
                                    },
                                    {
                                        style:'margin-right:20px',
                                        xtype: 'button',
                                        flagType:'positive',
                                        defaultColor:'',
                                        name:'startup',
                                        setType:0,
                                        text: '启动',
                                        handler: 'startInterview',
                                        width: 100
                                    },{
                                        style:'margin-left:0px',
                                        xtype:'button',
                                        text:'关闭',
                                        name:'finish',
                                        flagType:'positive',
                                        defaultColor:'',
                                        setType:0,
                                        handler:'endInterview',
                                        width:100
                                    }
                                ]},                                
                                {
                                	layout: {
                                        type: 'column'
                                    } ,
                                    padding:'0px 0 8px 4px',
                                    items:[
                                    	{
                                            xtype: 'checkboxfield',
                                            fieldLabel: '评委可见统计表',
                                            inputValue:'Y',
                                            name: 'judgeTJB',
                                            ignoreChangesFlag: true
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            style: 'margin-left:88px',
                                            fieldLabel: '评委可见分布图',
                                            inputValue:'Y',
                                            name: 'judgeFBT',
                                            ignoreChangesFlag: true
                                        }
                                   ]
                                }
                            ]
                        },
                        {
                            xtype: 'tabpanel',
                            frame: true,
                            activeTab: 0,
                            plain: false,
                            resizeTabs: true,
                            defaults: {
                                autoScroll: false
                            },
                            listeners:{
                                tabchange:function(tabPanel, newCard, oldCard){
                                    if (newCard.name == "statisticalInfoForm"){
                                        var form = tabPanel.findParentByType('interviewReviewSchedule').child('form').getForm();

                                    }
                                }
                            },
                            items: [
                                {
                                    title: '评委信息',
                                    xtype: 'form',
                                    name:'judgeFormInfo',
                                    style:'margin-left:0',
                                    layout: {
                                        type: 'vbox',
                                        align: 'stretch'
                                    },
                                    items: [{
                                        layout: {
                                            type: 'column'
                                        } ,
                                        //padding:'10px 0 5px 10px',
                                        items:[
                                            {
                                                margin:'8px',
                                                xtype: 'displayfield',
                                                value: "<b>要求每位考生被</b>",
                                                labelWidth:1
                                            },
                                            {
                                                margin:'8px',
                                                xtype: 'displayfield',
                                                name: 'judgeCount',
                                                style:'margin-left:5px;margin-right:5px;',
                                                labelWidth:1
                                            },
                                            /*{
                                                xtype: 'numberfield',
                                                margin:'8px',
                                                labelWidth:1,
                                                name: 'judgeCount',
                                                minValue:0,
                                                style:'margin-left:5px;margin-right:5px;',
                                                negativeText:'评委数量不能为负数',
                                                nanText:'{0}不是有效的数字',
                                                hideLabel: true,
                                                width:130,
                                                ignoreChangesFlag:true
                                            },*/{
                                                margin:'8px',
                                                xtype: 'displayfield',
                                                //style:'margin-left:1px',
                                                value: "<b>个评委审批</b>",
                                                //hideLabel: true,
                                                labelWidth:70
                                            }
                                        ]},
                                        {
                                            xtype: 'grid',
                                            minHeight: 150,
                                            autoHeight:true,
                                            name:'interviewJudgeGrid',
                                            border:false,
                                            store:{
                                                type:'interviewReviewScheduleJudgeStore'
                                            },
                                            multiSelect: true,
                                            selModel: {
                                                selType: 'checkboxmodel'
                                            },
                                            dockedItems: [
                                                {
                                                    xtype: "toolbar",
                                                    items: [
                                                        {text: "暂停选中的评委账户", tooltip: "暂停选中的评委账户", handler: "pause"},
                                                        "-",
                                                        {text: "启用选中的评委账户", tooltip: "启用选中的评委账户", handler: "setUsual"},
                                                        "-",
                                                        {text: "提交评委数据", tooltip: "提交评委数据",  handler: "submitData"},
                                                        "-",
                                                        {text: "设置提交状态为未提交", tooltip: "设置提交状态为未提交", handler: "setNoSubmit"},
                                                        "-",
                                                        {text: "刷新", tooltip: "刷新", handler: "refreshPw"},
                                                        "-",
                                                        {text: "打印评分总表",tooltip: "打印评分总表" , handler: "printPFZB"},
                                                        "->",
                                                        {text: "撤销面试数据", tooltip: "撤销面试数据", handler: "revokeData"},

                                                    ]
                                                }
                                            ],
                                            columns: [{
                                                width: 1
                                            	},{
                                                    xtype: 'rownumberer',
                                                    width: 50,
                                                    align:'center'
                                                },
                                                {
                                                    text: "评委帐号",
                                                    dataIndex: 'judgeID',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "姓名",
                                                    dataIndex: 'judgeName',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "评委组编号",
                                                    dataIndex: 'judgeGroup',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "抽取数量/已提交数量",
                                                    dataIndex: 'hasSubmited',
                                                    align:'center',
                                                    minWidth: 100,
                                                    flex: 1
                                                },
                                                {
                                                    text: "提交状态",
                                                    dataIndex: 'submitYN',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1,
                                                    renderer: function (v) {
                                                        var x;
                                                        var judgeStatus = transValue.get("TZ_MSPS_ZT");
                                                        if((x = judgeStatus.find('TValue',v))>=0){
                                                            return judgeStatus.getAt(x).data.TSDesc;
                                                        }else{
                                                            return v;
                                                        }
                                                    }
                                                },
                                                {
                                                    text: "账户状态",
                                                    name:'accountStatus',
                                                    dataIndex: 'accountStatus',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1,
                                                    renderer:function(v){
                                                        var x;
                                                        var jugaccStatusStore = transValue.get("TZ_PWEI_ZHZT");
                                                        if((x = jugaccStatusStore.find('TValue',v))>=0){
                                                            return jugaccStatusStore.getAt(x).data.TSDesc;
                                                        }else{
                                                            return v;
                                                        }
                                                    }
                                                }
                                            ]
                                        } ]},
                                {
                                    xtype: 'grid',
                                    title: '考生名单',
                                    minHeight: 260,
                                    name:'interviewStudentGrid',
                                    columnLines: true,
                                    autoHeight: true,
                                    hidden:true,
                                    listeners:{
                                        activate:'stuListActive'
                                    },
                                    selModel: {
                                        type: 'checkboxmodel'
                                    },
                                    /*store:{
                                        type:'interviewReviewScheduleAppsStore'
                                    },*/
                                    dockedItems: [dockedItemBtn],
                                    columns: columnsItems,
                                    store:interviewReviewScheduleAppsStore,
                                    bbar: {
                                        xtype: 'pagingtoolbar',
                                        pageSize: 10,
                                        store: interviewReviewScheduleAppsStore,
                                        plugins: new Ext.ux.ProgressBarPager()
                                    }
                                    
                                },{
                                    xtype: 'form',
                                    title: '统计信息',
                                    autoHeight:true,
                                    name:'statisticalInfoForm',
                                    hidden:true,
                                    items:[
                                         Ext.create('Ext.grid.Panel', {
                                            store: Ext.create('Ext.data.ArrayStore', {
                                                fields: statisticsGridDataModel['gridFields'],
                                                data: statisticsGridDataModel['gridData']
                                            }),
                                             minHeight:80,
                                            //stateful: true,
                                             selModel: {
                                                 type: 'checkboxmodel'
                                             },
                                            name:'statisticalGrid',
                                            columns: statisticsGridDataModel['gridColumns'],
                                            listeners:{
            	                            	beforeselect:function(_this,record,index,opts){
            	                            		var data = record.getData();
            	                            		var pwId = data["col01"];
            	                            		if(pwId!=undefined&&pwId==''){
            	                            			return false;
            	                            		}
            	                            	}
            	                            },
                                            header: false,
                                            border:false,
                                             dockedItems:[{
                                                 xtype:"toolbar",
                                                 /*dock: 'bottom',*/
                                                 items:[
                                                	 {
                 	                                    text: "计算选中评委的标准评分分布",
                 	                                    tooltip: "计算选中评委的标准评分分布",
                 	                                    handler:'calcuScoreDist'
                 	                                }, "-",
                                                     {text:"刷新图表",tooltip:"刷新图表",iconCls: "refresh",handler:'showMSTB'
                 	                                }
                                                 ]
                                             }],
                                            viewConfig: {
                                                enableTextSelection: true
                                            }
                                        })/*,mainPageFrame*/]
                                }]
                        }
                    ]
                }
            ]

        });
        this.callParent();

    },
    buttons: [{
        text: '保存',
        name:"save",
        iconCls:"save",
        handler: 'onScheduleSave'
    }, {
        text: '确定',
        name:'ensure',
        iconCls:"ensure",
        handler: 'onScheduleEnsure'
    }, {
        text: '关闭',
        name:'close',
        iconCls:"close",
        handler: 'onScheduleClose'
    }]
});
