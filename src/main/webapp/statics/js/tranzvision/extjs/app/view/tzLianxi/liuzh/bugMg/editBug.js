Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.editBug', {
    requires: [
        'KitchenSink.view.tzLianxi.liuzh.bugMg.bugStore'
    ],
    extend:'Ext.panel.Panel',
    title: '修改Bug' ,
    autoScroll : true,
    bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
    items: [
        new Ext.form.Panel({
            xtype:'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            bodyPadding: 10,
            //heigth: 600,
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',
            items: [{
                xtype: 'textfield',
                fieldLabel: '编号',
                name: 'BugID',
                allowBlank:false,
                editable:false,
                value:'BugID'
            },{
                xtype: 'textfield',
                fieldLabel: '说明',
                name: 'name',
                allowBlank:false,
                value:'name'
            },{
                xtype: 'datefield',
                fieldLabel: '录入日期',
                name: 'inputDate',
                allowBlank:false,
                value:'inputData',
                maxValue: new Date()
            },{
                layout: {
                    type: 'column'
                },
                items:[{
                    xtype: 'textfield',
                    fieldLabel: '录入人',
                    name: 'inputOprID',
                    fieldWidth:200,
                    allowBlank:false,
                    editable:false,
                    value:'inputOprID',
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "selectOprID"
                        }
                    }
                }
                    ,{
                        columnWidth:.5,
                        xtype: 'displayfield',
                        hideLabel: true,
                        style:'margin-left:8px',
                        name: 'recOprName'
                    }
                ]
            }, {
                layout: {
                    type: 'column'
                },
                items:[{
                    xtype: 'textfield',
                    fieldLabel: '责任人',
                    name: 'responsableOprID',
                    fieldWidth:200,
                    allowBlank:false,
                    editable:false,
                    value:'responsableOprID',
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "selectOprID"
                        }
                    }
                }
                    ,{
                        columnWidth:.5,
                        xtype: 'displayfield',
                        hideLabel: true,
                        style:'margin-left:8px',
                        name: 'resOprName'
                    }
                ]
            },{
                xtype: 'datefield',
                fieldLabel: '期望解决日期',
                name: 'espectDate',
                allowBlank:false,
                minValue: new Date(),
                value:'espectDate'
            },{
                xtype: 'combo',
                fieldLabel: '处理状态',
                name: 'status',
                emptyText: '请选择',
                queryMode: 'local',
                editable: false,
                valueField: 'conValue',
                displayField: 'condition',
                store:status,
                value:'status',
                maxeHeight:150,
                width:300
            },{
                xtype: 'tabpanel',
                frame: true,
                activeTab: 0,
                plain: false,
                resizeTabs: true,
                defaults: {
                    autoScroll: false
                },
                items: [{
                    title: "描述信息",
                    xtype: 'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                    items: [{
                        xtype: 'htmleditor',
                        fieldLabel: '描述信息',
                        name: 'descript',
                        hideLabel: true
                    }]
                },{
                    title: "上传附件",
                    xtype: 'grid',
                    frame: true,
                    columnLines: true,
                    layout:'column',
                    tbar:[
                        {text:'上传附件',  tooltip:'上传附件',type:'buttons',handler:'upLoad'}
                    ],
                    columns: [{
                        text: '附件名',
                        dataIndex: 'attachName',
                        sortable: false,
                        width: 300
                    }, {
                        text: '描述信息',
                        dataIndex: 'attachDescript',
                        sortable: false,
                        width: 400,
                        flex: 1,
                        editor: {
                            xtype: 'textfield'
                        }
                    },{
                        text: '操作',
                        sortable: false,
                        width: 100,
                        xtype:'actioncolumn',
                        items: [
                            {icon: 'delete.png', tooltip: '删除附件',handler:'deleteBug'}
                        ]
                    }]
                }]
            }],
            buttons:[
                {text:'保存',handler:'savaBug'},
                {text:'确定',handler:'ensureBug'},
                {text:'取消',handler:'cancel'}
            ]
        })
    ]
});