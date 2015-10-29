Ext.define('KitchenSink.view.bugManagement.bugMg.bugInfoPanel', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bugManagement.bugMg.bugAttachmentStore',
        'KitchenSink.view.bugManagement.bugProg.bugProgController',
        'KitchenSink.view.bugManagement.bugProg.bugProgStore'
    ],
    extend : 'Ext.panel.Panel',
    controller:'bugMgController',
    autoScroll:false,
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'任务信息',
    frame:true,
    initComponent:function(){
        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin:'8px',
                style:'border:0px',
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '编号',
                    name: 'bugID',
                    editable:false,
                    readOnly:true,
                    cls:'lanage_1',
                    value:'NEXT'

                },{
                    xtype: 'combo',
                    fieldLabel: '功能模块',
                    name: 'bugGNMK',
                    store:new KitchenSink.view.common.store.comboxStore({
                        pageSize:0,
                        recname:'TZ_BUGMG_GNMK_V',
                        condition:{
                            TZ_BUG_GNMK:{
                                value:' ',
                                operator:'02',
                                type:'01'
                            }
                        },
                        result:'TZ_BUG_GNMK'
                    }),
                    typeAhead:true,
                    autoShow:true,
                    valueField:'TZ_BUG_GNMK',
                    displayField:'TZ_BUG_GNMK',
                    queryMode:'local',
                    allowBlank: true
                },{
                    xtype: 'textfield',
                    fieldLabel: '说明',
                    name: 'bugName',
                    allowBlank: false
                },{
                    xtype: 'combo',
                    fieldLabel: '类型',
                    name: 'bugType',
                    allowBlank: false,
                    editable:false,
//                    queryMode: 'local',
                    valueField: 'bugType',
                    displayField: 'typeDesc',
                    store: {
                        fields: ["bugType", "typeDesc"],
                        data: [
                            {bugType: "0", typeDesc: "Bug"},
                            {bugType: "1", typeDesc: "工作任务"}
                        ]
                    },
                    value:"0"
                },{
                    xtype: 'combo',
                    fieldLabel: '优先级',
                    name: 'priority',
                    allowBlank: false,
                    editable:false,
                    emptyText: '',
//                    queryMode: 'local',
                    valueField: 'priority',
                    displayField: 'desc',
                    store: {
                        fields: ["priority", "desc"],
                        data: [
                            {priority: "0", desc: "P0"},
                            {priority: "1", desc: "P1"},
                            {priority: "2", desc: 'P2'}
                        ]
                    }
                },{
                    xtype: 'combo',
                    fieldLabel: '处理状态',
                    name: 'status',
                    allowBlank: false,
                    editable:false,
//                    queryMode: 'local',
                    valueField: 'status',
                    displayField: 'desc',
                    store: {
                        fields: ["status", "desc"],
                        data: [
                            {status: "0", desc: "新建"},
                            {status: "1", desc: "已分配"},
                            {status: "2", desc: '已完成(待审核)'},
                            {status: "3", desc: '关闭'},
                            {status: "4", desc: '重新打开'},
                            {status: "5", desc: "取消"},
                            {status: "6", desc: "暂时搁置"}
                        ]
                    },
                    value:"0"
                },{
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '责任人',
                        name: 'resOprID',
                        editable:true,
                        allowBlank: true,/*主要是录 bug，但责任人是谁，可能不清楚，由其他管理员分配*/
                        promptValidation:{
                            recName:'TZ_AQ_YHXX_TBL',
                            presetFields:{
                                TZ_JG_ID:{
                                    value: Ext.tzOrgID,
                                    type: '01'
                                }
                            },
                            valueOfResult:'TZ_DLZH_ID',
                            displayOfResult:'TZ_REALNAME',
                            displayField:'resOprName',
                            errorMsg:'该责任人不存在'
                        },
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "selectOprID"
                            }
                        }
                    },{
                        columnWidth:.5,
                        xtype: 'displayfield',
                        hideLabel: true,
                        style:'margin-left:15px',
                        name: 'resOprName'
                    }]
                },{
                    xtype: 'datefield',
                    fieldLabel: '期望解决日期',
                    name: 'expDate',
                    format:'Y-m-d',
                    allowBlank:true
                },{
                    xtype: 'datefield',
                    fieldLabel: '预计结束日期',
                    name: 'estDate',
                    format:'Y-m-d'
                },{
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'numberfield',
                        fieldLabel: '进度百分比',
                        allowBlank: false,
                        minValue: 0,
                        maxValue: 100,
                        value:0,
                        name: 'bugPercent'
                    },{
                        columnWidth:.5,
                        xtype: 'displayfield',
                        hideLabel: true,
                        value:'%',
                        style:'font-size:12px;margin-left:15px'
                    }]
                },{
                    layout: {
                        type: 'hbox'
                    },
                    items:[
                        {
                            items:[{
//                                xtype: 'datefield',
                                xtype: 'textfield',
                                name: 'bugUpdateDTTM',
                                fieldLabel: '进度更新时间',
                                cls:'lanage_1',
//                                format: 'Y-m-d h:i',
                                readOnly:true
                            }]
                        },
                        {
                            items:[{
                                xtype: 'button',
                                style:'margin-left:15px',
                                text:"查看任务进度日志",
                                handler:"viewBugProg"
                            }]
                        }
                    ]
                },
                    {
                        layout: {
                            type: 'column',
                            align: 'stretch'
                        },
                        items:[{
                            xtype: 'textfield',
                            fieldLabel: '录入人',
                            name: 'recOprID',
                            allowBlank: false,
                            editable:true,
                            value:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                            promptValidation:{
                                recName:'TZ_AQ_YHXX_TBL',
                                presetFields:{
                                    TZ_JG_ID:{
                                        value: Ext.tzOrgID,
                                        type: '01'
                                    }
                                },
                                valueOfResult:'TZ_DLZH_ID',
                                displayOfResult:'TZ_REALNAME',
                                displayField:'recOprName',
                                errorMsg:'该录入人不存在'
                            },
                            triggers: {
                                search: {
                                    cls: 'x-form-search-trigger',
                                    handler: "selectOprID"
                                }
                            }
                        },{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            value:TranzvisionMeikecityAdvanced.Boot.firstName,
                            style:'margin-left:15px',
                            name: 'recOprName'

                        }]
                    },{
                        xtype: 'datefield',
                        fieldLabel: '录入日期',
                        name: 'recDate',
                        allowBlank: false,
                        maxValue:new Date(),
                        value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,0),"Y-m-d"),
//                        value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d"),
                        format:'Y-m-d'
                    },{
                        xtype: 'combo',
                        fieldLabel: '测试状态',
                        name: 'bugMigration',
                        allowBlank: true,
                        editable:false,
//                        queryMode: 'local',
                        valueField: 'bugMigration',
                        displayField: 'migrationDesc',
                        store: {
                            fields: ["bugMigration", "migrationDesc"],
                            data: [
                                {bugMigration: "0", migrationDesc: "已迁移UAT但未测试"},
                                {bugMigration: "1", migrationDesc: "UAT环境测试通过（待迁移生产）"},
                                {bugMigration: "2", migrationDesc: "已迁移生产"}
                            ]
                        }
//                        ,value:"0"
                    },
                    {
                        xtype: 'textareafield',
                        grow: true,
                        name: 'bugBZ',
                        fieldLabel: '备注'
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
                        items:[

                            {
                                title:'描述信息',
                                xtype:'form',
                                name:'bugDescForm',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex:999,
                                    height: 415,
                                    allowBlank: true

                                }]
                            },
                            {
                                xtype: 'grid',
                                title:'附件集',
                                height: 415,
                                frame: true,
                                columnLines: true,
                                name: 'attachmentGrid',
                                reference: 'attachmentGrid',
                                style:"border:0px",
                                store:{
                                    type:'bugAttachmentStore'
                                },
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                                tbar: [
                                    {
                                        xtype: 'form',
                                        bodyStyle:'padding:10px 0px 0px 0px',
                                        items:[{
                                            xtype: 'fileuploadfield',
                                            buttonText: '上传附件',
                                            name: 'attachmentUpload',
                                            buttonOnly:true,
                                            width: 88,
                                            listeners:{
                                                change:'addAttach',
                                                keydown:'checkAtta'
                                                /*function(file, value, eOpts){
                                                 addAttach(file, value, "ATTACHMENT");
                                                 }*/
                                            }
                                        }]},"-",
                                    {iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteArtAttenments'}],
                                columns: [
/*                                    {
                                        text: 'BUG编号',
                                        dataIndex: 'bugID',
                                        sortable: false,
                                        hidden: true
                                    },*/
/*                                    {
                                        text: '附件编号',
                                        dataIndex: 'attachmentID',
                                        hidden: true,
                                        sortable: false
                                    },{
                                        text: '附件url',
                                        dataIndex: 'attachmentUrl',
                                        sortable: false,
                                        hidden: true
                                    },*/
                                    {
                                        text: '附件名称',
                                        dataIndex: 'attachmentName',
                                        sortable: false,
                                        minWidth: 100,
                                        flex: 1,
                                        renderer: function(v,d){
                                            return '<a href="'+ d.record.data.attachmentUrl +'" target="_blank">'+v+'</a>';
                                        }
                                    },{
                                        menuDisabled: true,
                                        sortable: false,
                                        width:60,
                                        xtype: 'actioncolumn',
                                        items:[
                                            {iconCls: 'remove',tooltip: '删除', handler: 'deleteArtAttenments'}
                                        ]
                                    }]
                            }]
                    }]
            }],
            buttons:[
                {
                    text: '发送邮件',
                    handler:'sendEmail',
                    iconCls:'send'
                },
                {
                    text: '保存',
                    handler:'bugInfoSave',
                    iconCls:'save'
                },{
                    text:'确定',
                    handler:'bugInfoEnsure',
                    iconCls:'ensure'
                },{
                    text:'关闭',
                    iconCls:'close',
                    handler:'bugInfoClose'
                }
            ]
        });
        this.callParent();
    }
});
