    Ext.define('KitchenSink.view.dispatchLoop.dispatchLoopPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'dispatchLoopInfo',
    controller: 'dispatchLoopCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.dispatchLoop.dispatchLoopController',
        'KitchenSink.view.dispatchLoop.dispatchLoopStore'
    ],
    title: '调度循环信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'dispatchLoopInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [
            {
            xtype: 'combobox',
            editable:false,
            fieldLabel: '归属机构',
            forceSelection: true,
            valueField: 'orgId',
            displayField: 'orgName',
            store: new KitchenSink.view.orgmgmt.orgListStore(),
            queryMode: 'local',
            name: 'orgId',
            emptyText:'请选择机构',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank:false
        },{
            xtype: 'textfield',
            fieldLabel: '循环名称',
            name: 'loopName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank:false
        },{
            xtype: 'textfield',
                fieldLabel: '循环描述',
                name: 'loopDesc',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
        },{
            xtype: 'combobox',
            editable:false,
            fieldLabel: '生效/失效',
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_ISVALID"),
            queryMode: 'remote',
            name: 'status',
            emptyText:'生效/失效',
            afterLabelTextTpl: [
            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        ],
            allowBlank:false
    }]
    },{
        xtype: 'tabpanel',
        items:[{
            style:'margin-top:20px',
            xtype:'form',
            name:'yearForm',
            title:'年份',
            items:[{
                xtype: 'radio',
                id:'yearOne',
                name:'loopYear',
                checked:true,
                style:'margin-left:50px',
                inputValue:"limitYear",
                boxLabel: "不限定，任意年份"
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopYear',
                    id:'yearTwo',
                    columnWidth:.15,
                    boxLabel: "指定年份范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始年份',
                    labelWidth: 60,
                    name:'beginYear',
                    displayField: "Name",
                    emptyText: "-----请选择起始年份-----",
                    editable: false,
                    valueField: "Value",
                    columnWidth:.2,
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: new Date().getFullYear(), Value: 1 },
                            { Name: new Date().getFullYear() + 1, Value: 2  },
                            { Name: new Date().getFullYear() + 2, Value: 3  },
                            { Name: new Date().getFullYear() + 3, Value: 4  },
                            { Name: new Date().getFullYear() + 4, Value: 5 }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止年份',
                    columnWidth:.25,
                    labelWidth: 60,
                    name:'endYear',
                    emptyText: "-----请选择截止年份-----",
                    editable: false,
                    style:'margin-left:20px',
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: new Date().getFullYear(), Value: 1 },
                            { Name: new Date().getFullYear() + 1, Value: 2  },
                            { Name: new Date().getFullYear() + 2, Value: 3  },
                            { Name: new Date().getFullYear() + 3, Value: 4  },
                            { Name: new Date().getFullYear() + 4, Value: 5 }
                        ]
                    })
                }]
            },{
                xtype: 'fieldcontainer',
                layout:'column',
                style:'margin-top:10px;margin-left:50px',
                items:[{
                    xtype: 'radio',
                    id:'yearThree',
                    name:'loopYear',
                    columnWidth:.15,
                    boxLabel: "指定年份列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'yearList',
                    emptyText: "格式：YYYY,…  取值范围：1970-2099  例如：2001,2002"
                }]
            },{
                xtype: 'fieldcontainer',
                layout:'column',
                style:'margin-top:10px;margin-left:50px',
                items:[{
                    xtype: 'radio',
                    name:'loopYear',
                    columnWidth:.15,
                    boxLabel: "指定年份循环间隔"
                },{
                    xtype: 'textfield',
                    name:'yearLoopInterval',
                    columnWidth:.45,
                    emptyText: "格式：YYYY/N  取值范围：1970-2099  例如：2001/4"
                }]
            }]
        },{
            xtype:'form',
            style:'margin-top:20px',
            name:'monthForm',
            title:'月份',
            items:[{
                xtype: 'radio',
                name:'loopMonth',
                style:'margin-left:50px',
                boxLabel: "不限定，任意月份"
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopMonth',
                    columnWidth:.15,
                    boxLabel: "指定月份范围"
                },{
                    xtype: 'combobox',
                    labelWidth: 60,
                    fieldLabel: '起始月份',
                    emptyText: "------请选择起始月份------",
                    editable: false,
                    name: 'beginMonth',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    labelWidth: 60,
                    fieldLabel: '截止月份',
                    name:'endMonth',
                    columnWidth:.25,
                    emptyText: "------请选择截止月份------",
                    editable: false,
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  }
                        ]
                    })
                }]
            },{
                xtype: 'fieldcontainer',
                layout:'column',
                style:'margin-top:10px;margin-left:50px',
                items:[{
                    xtype: 'radio',
                    name:'loopMonth',
                    columnWidth:.15,
                    boxLabel: "指定月份列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'monthList',
                    emptyText: "格式：N1,N2,…  取值范围：1-12  例如：1,2,3"
                }]
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopMonth',
                    columnWidth:.15,
                    boxLabel: "指定月份循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'monthLoopInterval',
                    emptyText: "格式：M/N  取值范围：1-12  例如：5/4"
                }]
            }]
        },{
            xtype:'form',
            style:'margin-top:20px',
            name:'dayForm',
            title:'日、周',
            items:[{
                xtype: 'radio',
                name:'loopDay',
                style:'margin-left:30px',
                boxLabel: "按“日-月”模式循环"
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'radio',
                name:'loopDay',
                boxLabel: "不限定，任意日期"
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopDay',
                    columnWidth:.15,
                    boxLabel: "指定日期范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始日期',
                    labelWidth: 60,
                    emptyText: "------请选择起始日期------",
                    editable: false,
                    name:'beginDay1',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止日期',
                    labelWidth: 60,
                    columnWidth:.25,
                    emptyText: "------请选择截止日期------",
                    name:'endDay1',
                    editable: false,
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopDay',
                    columnWidth:.15,
                    boxLabel: "指定日期列表"
                },{
                    xtype: 'textfield',
                    name:'day1List',
                    columnWidth:.45,
                    emptyText: "格式：N1,N2,…  取值范围：1-31  例如：1,2,3"
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopDay',
                    columnWidth:.15,
                    boxLabel: "指定日期循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'day1LoopInterval',
                    emptyText: "格式：D/N  取值范围：1-31  例如：5/4"
                }]
            },{
                xtype: 'radio',
                name:'loopDay',
                style:'margin-top:10px;margin-left:50px',
                boxLabel: "每月最后一天"
            },{
                xtype: 'radio',
                name:'loopDay',
                style:'margin-top:10px;margin-left:50px',
                boxLabel: "每月最后一个工作日"
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopDay',
                    columnWidth:.15,
                    boxLabel: "离指定日期最近的一个工作日"
                },{
                    xtype: 'combobox',
                    fieldLabel: '指定日期',
                    labelWidth: 60,
                    emptyText: "------请选择起始日期------",
                    name:'beginDate1',
                    editable: false,
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  }
                        ]
                    })
                }]
            },{
                xtype: 'radio',
                name:'loopWeek',
                style:'margin-left:30px',
                boxLabel: "按“日-周”模式循环"
            },{
                style:'margin-top:10px;margin-left:50px',
                name:'loopWeek',
                xtype: 'radio',
                boxLabel: "不限定，任意日期"
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopWeek',
                    columnWidth:.15,
                    boxLabel: "指定日期范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始日期',
                    labelWidth: 60,
                    emptyText: "------请选择起始日期------",
                    editable: false,
                    name:'beginDay2',
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: '周一', Value: 1 },
                            { Name: '周二', Value: 2  },
                            { Name: '周三', Value: 3  },
                            { Name: '周四', Value: 4  },
                            { Name: '周五', Value: 5  },
                            { Name: '周六', Value: 6  },
                            { Name: '周日', Value: 7  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止日期',
                    labelWidth: 60,
                    columnWidth:.25,
                    emptyText: "------请选择截止日期------",
                    editable: false,
                    name:'endDay2',
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: '周一', Value: 1 },
                            { Name: '周二', Value: 2  },
                            { Name: '周三', Value: 3  },
                            { Name: '周四', Value: 4  },
                            { Name: '周五', Value: 5  },
                            { Name: '周六', Value: 6  },
                            { Name: '周日', Value: 7  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopWeek',
                    columnWidth:.15,
                    boxLabel: "指定日期列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'day2List',
                    emptyText: "格式：N1,N2,…  取值范围：1-7  例如：1,2,3"
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopWeek',
                    columnWidth:.15,
                    boxLabel: "指定日期循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'day2LoopInterval',
                    emptyText: "格式：D/N  取值范围：1-7  例如：5/4"
                }]
            },{
                xtype: 'radio',
                name:'loopWeek',
                style:'margin-top:10px;margin-left:50px',
                boxLabel: "每周最后一天"
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopWeek',
                    columnWidth:.15,
                    boxLabel: "每周最后一个周几"
                },{
                    xtype: 'combobox',
                    fieldLabel: '指定日期',
                    labelWidth: 60,
                    emptyText: "------请选择指定日期------",
                    editable: false,
                    name:'appointedDate1',
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: '周一', Value: 1 },
                            { Name: '周二', Value: 2  },
                            { Name: '周三', Value: 3  },
                            { Name: '周四', Value: 4  },
                            { Name: '周五', Value: 5  },
                            { Name: '周六', Value: 6  },
                            { Name: '周日', Value: 7  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopWeek',
                    columnWidth:.15,
                    boxLabel: "每月第几个周几"
                },{
                    xtype: 'combobox',
                    fieldLabel: '指定周次',
                    labelWidth: 60,
                    emptyText: "------请选择指定周次------",
                    editable: false,
                    name:'appointedWeek',
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: '第一周', Value: 1 },
                            { Name: '第二周', Value: 2  },
                            { Name: '第三周', Value: 3  },
                            { Name: '第四周', Value: 4  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '指定日期',
                    labelWidth: 60,
                    style:'margin-left:40px',
                    emptyText: "------请选择指定日期------",
                    name:'appointedDate2',
                    editable: false,
                    forceSelection: true,
                    columnWidth:.25,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: '周一', Value: 1 },
                            { Name: '周二', Value: 2  },
                            { Name: '周三', Value: 3  },
                            { Name: '周四', Value: 4  },
                            { Name: '周五', Value: 5  },
                            { Name: '周六', Value: 6  },
                            { Name: '周日', Value: 7  }
                        ]
                    })
                }]
            }]
        },{
            xtype:'form',
            style:'margin-top:20px',
            name:'hourForm',
            title:'时',
            items:[{
                xtype: 'radio',
                name:'loopHour',
                style:'margin-left:50px',
                boxLabel: "不限定，任意小时"
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopHour',
                    columnWidth:.15,
                    boxLabel: "指定小时范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始小时',
                    labelWidth: 60,
                    emptyText: "------请选择起始小时------",
                    name:'beginHour',
                    editable: false,
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止小时',
                    labelWidth: 60,
                    columnWidth:.25,
                    emptyText: "------请选择截止小时------",
                    name:'endHour',
                    editable: false,
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopHour',
                    columnWidth:.15,
                    boxLabel: "指定小时列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'hourList',
                    emptyText: "格式：N1,N2,…  取值范围：0-23  例如：1,2,3"
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopHour',
                    columnWidth:.15,
                    boxLabel: "指定小时循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'hourLoopInterval',
                    emptyText: "格式：H/N  取值范围：0-23/1-23  例如：8/4"
                }]
            }]
        },{
            xtype:'form',
            name:'minuteForm',
            style:'margin-top:20px',
            title:'分',
            items:[{
                xtype: 'radio',
                name:'loopMin',
                style:'margin-left:50px',
                boxLabel: "不限定，任意分钟"
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopMin',
                    columnWidth:.15,
                    boxLabel: "指定分钟范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始分钟',
                    labelWidth: 60,
                    emptyText: "------请选择起始分钟------",
                    name:'beginMinute',
                    editable: false,
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                        	
                        	{ Name: 0, Value: 0 },
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  },
                            { Name: 25, Value: 25 },
                            { Name: 26, Value: 26  },
                            { Name: 27, Value: 28  },
                            { Name: 29, Value: 29  },
                            { Name: 30, Value: 30  },
                            { Name: 31, Value: 31  },
                            { Name: 32, Value: 32  },
                            { Name: 33, Value: 33  },
                            { Name: 34, Value: 34  },
                            { Name: 35, Value: 35  },
                            { Name: 36, Value: 36  },
                            { Name: 37, Value: 37  },
                            { Name: 38, Value: 38  },
                            { Name: 39, Value: 39  },
                            { Name: 40, Value: 40  },
                            { Name: 41, Value: 41  },
                            { Name: 42, Value: 42  },
                            { Name: 43, Value: 43  },
                            { Name: 44, Value: 44  },
                            { Name: 45, Value: 45  },
                            { Name: 46, Value: 46  },
                            { Name: 47, Value: 47  },
                            { Name: 48, Value: 48  },
                            { Name: 49, Value: 49  },
                            { Name: 50, Value: 50  },
                            { Name: 51, Value: 51  },
                            { Name: 52, Value: 52  },
                            { Name: 53, Value: 53  },
                            { Name: 54, Value: 54  },
                            { Name: 55, Value: 55  },
                            { Name: 56, Value: 56  },
                            { Name: 57, Value: 57  },
                            { Name: 58, Value: 58  },
                            { Name: 59, Value:59  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止分钟',
                    labelWidth: 60,
                    columnWidth:.25,
                    name:'endMinute',
                    emptyText: "------请选择截止分钟------",
                    editable: false,
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                        	
                        	{ Name: 0, Value: 0 },
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  },
                            { Name: 25, Value: 25 },
                            { Name: 26, Value: 26  },
                            { Name: 27, Value: 28  },
                            { Name: 29, Value: 29  },
                            { Name: 30, Value: 30  },
                            { Name: 31, Value: 31  },
                            { Name: 32, Value: 32  },
                            { Name: 33, Value: 33  },
                            { Name: 34, Value: 34  },
                            { Name: 35, Value: 35  },
                            { Name: 36, Value: 36  },
                            { Name: 37, Value: 37  },
                            { Name: 38, Value: 38  },
                            { Name: 39, Value: 39  },
                            { Name: 40, Value: 40  },
                            { Name: 41, Value: 41  },
                            { Name: 42, Value: 42  },
                            { Name: 43, Value: 43  },
                            { Name: 44, Value: 44  },
                            { Name: 45, Value: 45  },
                            { Name: 46, Value: 46  },
                            { Name: 47, Value: 47  },
                            { Name: 48, Value: 48  },
                            { Name: 49, Value: 49  },
                            { Name: 50, Value: 50  },
                            { Name: 51, Value: 51  },
                            { Name: 52, Value: 52  },
                            { Name: 53, Value: 53  },
                            { Name: 54, Value: 54  },
                            { Name: 55, Value: 55  },
                            { Name: 56, Value: 56  },
                            { Name: 57, Value: 57  },
                            { Name: 58, Value: 58  },
                            { Name: 59, Value:59  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopMin',
                    columnWidth:.15,
                    boxLabel: "指定分钟列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'listMinute',
                    emptyText: "格式：N1,N2,…  取值范围：0-59  例如：0,1,2,3"
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopMin',
                    columnWidth:.15,
                    boxLabel: "指定分钟循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'minuteLoopInterval',
                    emptyText: "格式：H/N  取值范围：0-59/1-59  例如：8/4"
                }]
            }]
        },{
            xtype:'form',
            name:'secondForm',
            style:'margin-top:20px',
            title:'秒',
            items:[{
                xtype: 'radio',
                name:'loopSecond',
                style:'margin-left:50px',
                boxLabel: "不限定，任意秒"
            },{
                xtype: 'fieldcontainer',
                style:'margin-top:10px;margin-left:50px',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopSecond',
                    columnWidth:.15,
                    boxLabel: "指定秒范围"
                },{
                    xtype: 'combobox',
                    fieldLabel: '起始秒',
                    labelWidth: 60,
                    name:'beginSecond',
                    emptyText: "------请选择起始秒------",
                    editable: false,
                    forceSelection: true,
                    columnWidth:.2,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                        	
                        	{ Name: 0, Value: 0 },
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  },
                            { Name: 25, Value: 25 },
                            { Name: 26, Value: 26  },
                            { Name: 27, Value: 28  },
                            { Name: 29, Value: 29  },
                            { Name: 30, Value: 30  },
                            { Name: 31, Value: 31  },
                            { Name: 32, Value: 32  },
                            { Name: 33, Value: 33  },
                            { Name: 34, Value: 34  },
                            { Name: 35, Value: 35  },
                            { Name: 36, Value: 36  },
                            { Name: 37, Value: 37  },
                            { Name: 38, Value: 38  },
                            { Name: 39, Value: 39  },
                            { Name: 40, Value: 40  },
                            { Name: 41, Value: 41  },
                            { Name: 42, Value: 42  },
                            { Name: 43, Value: 43  },
                            { Name: 44, Value: 44  },
                            { Name: 45, Value: 45  },
                            { Name: 46, Value: 46  },
                            { Name: 47, Value: 47  },
                            { Name: 48, Value: 48  },
                            { Name: 49, Value: 49  },
                            { Name: 50, Value: 50  },
                            { Name: 51, Value: 51  },
                            { Name: 52, Value: 52  },
                            { Name: 53, Value: 53  },
                            { Name: 54, Value: 54  },
                            { Name: 55, Value: 55  },
                            { Name: 56, Value: 56  },
                            { Name: 57, Value: 57  },
                            { Name: 58, Value: 58  },
                            { Name: 59, Value:59  }
                        ]
                    })
                },{
                    xtype: 'combobox',
                    fieldLabel: '截止秒',
                    labelWidth: 60,
                    columnWidth:.25,
                    name:'endSecond',
                    emptyText: "------请选择截止秒------",
                    editable: false,
                    style:'margin-left:40px',
                    forceSelection: true,
                    displayField: "Name",
                    valueField: "Value",
                    store:Ext.create("Ext.data.Store", {
                        fields: ["Name", "Value"],
                        data: [
                        	
                        	{ Name: 0, Value: 0 },
                            { Name: 1, Value: 1 },
                            { Name: 2, Value: 2  },
                            { Name: 3, Value: 3  },
                            { Name: 4, Value: 4  },
                            { Name: 5, Value: 5  },
                            { Name: 6, Value: 6  },
                            { Name: 7, Value: 7  },
                            { Name: 8, Value: 8  },
                            { Name: 9, Value: 9  },
                            { Name: 10, Value: 10  },
                            { Name: 11, Value: 11  },
                            { Name: 12, Value: 12  },
                            { Name: 13, Value: 13  },
                            { Name: 14, Value: 14  },
                            { Name: 15, Value: 15  },
                            { Name: 16, Value: 16  },
                            { Name: 17, Value: 17  },
                            { Name: 18, Value: 18  },
                            { Name: 19, Value: 19  },
                            { Name: 20, Value: 20  },
                            { Name: 21, Value: 21  },
                            { Name: 22, Value: 22  },
                            { Name: 23, Value: 23  },
                            { Name: 24, Value: 24  },
                            { Name: 25, Value: 25 },
                            { Name: 26, Value: 26  },
                            { Name: 27, Value: 28  },
                            { Name: 29, Value: 29  },
                            { Name: 30, Value: 30  },
                            { Name: 31, Value: 31  },
                            { Name: 32, Value: 32  },
                            { Name: 33, Value: 33  },
                            { Name: 34, Value: 34  },
                            { Name: 35, Value: 35  },
                            { Name: 36, Value: 36  },
                            { Name: 37, Value: 37  },
                            { Name: 38, Value: 38  },
                            { Name: 39, Value: 39  },
                            { Name: 40, Value: 40  },
                            { Name: 41, Value: 41  },
                            { Name: 42, Value: 42  },
                            { Name: 43, Value: 43  },
                            { Name: 44, Value: 44  },
                            { Name: 45, Value: 45  },
                            { Name: 46, Value: 46  },
                            { Name: 47, Value: 47  },
                            { Name: 48, Value: 48  },
                            { Name: 49, Value: 49  },
                            { Name: 50, Value: 50  },
                            { Name: 51, Value: 51  },
                            { Name: 52, Value: 52  },
                            { Name: 53, Value: 53  },
                            { Name: 54, Value: 54  },
                            { Name: 55, Value: 55  },
                            { Name: 56, Value: 56  },
                            { Name: 57, Value: 57  },
                            { Name: 58, Value: 58  },
                            { Name: 59, Value:59  }
                        ]
                    })
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopSecond',
                    columnWidth:.15,
                    boxLabel: "指定秒列表"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'secondList',
                    emptyText: "格式：N1,N2,…  取值范围：0-59  例如：0,1,2,3"
                }]
            },{
                style:'margin-top:10px;margin-left:50px',
                xtype: 'fieldcontainer',
                layout:'column',
                items:[{
                    xtype: 'radio',
                    name:'loopSecond',
                    columnWidth:.15,
                    boxLabel: "指定秒循环间隔"
                },{
                    xtype: 'textfield',
                    columnWidth:.45,
                    name:'secondLoopInterval',
                    emptyText: "格式：H/N  取值范围：0-59/1-59  例如：8/4"
                }]
            }]
        },{
            xtype:'form',
            name:'customForm',
            style:'margin-top:20px',
            title:'高级',
            items:[
                {
                    xtype: 'checkboxfield',
                    style:'margin-left:50px',
                    boxLabel: "自定义循环期间"
                },{
                    style:'margin-top:10px;margin-left:100px',
                    xtype: 'fieldcontainer',
                    layout:'column',
                    items:[{
                        xtype: 'textfield',
                        columnWidth:.25,
                        labelWidth:20,
                        fieldLabel: '年',
                        name:'customYear',
                        emptyText: "空值或1970-2099  通配符：,-*/"
                    },{
                        xtype: 'textfield',
                        columnWidth:.25,
                        style:'margin-left:20px',
                        labelWidth:20,
                        name:'customMonth',
                        fieldLabel: '月',
                        emptyText: "1-12或者JAN-DEC  通配符：,-*/"
                    },{
                        xtype: 'textfield',
                        columnWidth:.25,
                        style:'margin-left:20px',
                        labelWidth:20,
                        name:'customWeek',
                        fieldLabel: '周',
                        emptyText: "1-7或者SUN-SAT  通配符：,-*?/L#"
                    }]
                },{
                    style:'margin-top:10px;margin-left:100px',
                    xtype: 'fieldcontainer',
                    layout:'column',
                    items:[{
                        xtype: 'textfield',
                        columnWidth:.25,
                        labelWidth:20,
                        name:'customDay',
                        fieldLabel: '日',
                        emptyText: "1-31  通配符：,-*?/LW"
                    },{
                        xtype: 'textfield',
                        columnWidth:.25,
                        style:'margin-left:20px',
                        labelWidth:20,
                        name:'customHour',
                        fieldLabel: '时',
                        emptyText: "0-23  通配符：,-*/"
                    },{
                        xtype: 'textfield',
                        columnWidth:.25,
                        style:'margin-left:20px',
                        labelWidth:20,
                        name:'customMinute',
                        fieldLabel: '分',
                        emptyText: "0-59  通配符：,-*/"
                    },{
                        xtype: 'textfield',
                        columnWidth:.25,
                        style:'margin-left:20px',
                        labelWidth:20,
                        name:'customSecond',
                        fieldLabel: '秒',
                        emptyText: "0-59  通配符：,-*/"
                    }]
                },{
                    xtype:'panel',
                    style:'margin-top:20px;margin-left:80px',
                    html:'说明：<br />1、年份的取值范围为1970至2099；<br />2、一周中每天取值范围为1至7，或者也可以使用SUN-SAT英文简称；另外，一周的第一天是星期天，最后一天是星期六；<br />' +
                        '3、月分的取值范围为1至12；<br />4、日期的取值范围为1至31，但需要注意由于大小月、闰年等历法规则，有的月份有31天，有的月只有30天、29天或者28天；<br />' +
                        '5、小时的取值范围为0至23；分钟、秒的取值范围为0至59；<br />6、一般建议通配符“,”、“-”、“*”和“/”不要联合使用；<br />7、日期字段使用通配符“L”和“W”可以联合使用，表示月份中的最后' +
                        '一个工作日；如果单独使用“L”则表示月份中的最后一天；如果以“NW”的格式使用通配符，则表示月份中离指定日期“N”最近的一个工作日，但若指定“1W”，如果1号不是工作日，则表示1号后的第一个工作日；<br />' +
                        '8、周字段中通配符的“L”的用法为“NL”，其中“N”表示周一到周日中的一天，其含义为某月中的最后一个周几；通配符“#”的用法为“N#N”，其中第一个“N”表示某一周中的周几，第二个“N”表示一月中的第几周；<br />' +
                        '9、“周”字段和“日”字段互斥，如果填写了“周”字段，则“日”字段的值将被自动设置为“?”，表示未指定，反之亦然；'
                }
            ]
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onDispatchLoopInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onDispatchLoopInfoEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onDispatchLoopInfoClose'
    }]
});
