Ext.define('KitchenSink.view.bugManagement.bugCk.mybugInfoPanel', {
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bugManagement.bugCk.bugCkAttachmentStore'
    ],
    extend : 'Ext.panel.Panel',
    controller:'bugCkController',
    autoScroll:false,
    actType:'add',
//    name:'bugInfo',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'任务信息',
    frame:true,
    initComponent:function(){
        Ext.apply(this,{
            items:[
                {
                xtype:'form',
                reference:'bugInfoForm',
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
                items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '编号',
                    name: 'bugID',
                    editable:false,
                    cls:'lanage_1',
                    value:'NEXT'
                },{
                        xtype: 'textfield',
                        fieldLabel: '功能模块',
                        name: 'bugGNMK',
                        editable:false,
                        cls:'lanage_1',
                        allowBlank: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '说明',
                    editable:false,
                    cls:'lanage_1',
                    name: 'bugName'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '类型',
                    editable:false,
                    cls:'lanage_1',
                    name: 'bugType',
                    listeners:{
                        change:function(field) {
                            switch(field.getValue()) {
                                case '0':
                                    field.setValue('Bug');
                                    break;
                                case '1':
                                    field.setValue( '工作任务');
                                    break;
                            }
                        }
                    }
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '优先级',
                    editable:false,
                    cls:'lanage_1',
                    name: 'priority',
                    listeners:{
                        change:function(field) {
                            switch(field.getValue()) {
                                case '0':
                                    field.setValue('P0');
                                    break;
                                case '1':
                                    field.setValue( 'P1');
                                    break;
                                case '2':
                                    field.setValue( 'P2');
                                    break;
                            }
                        }
                    }
                },
                {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '录入人',
                        cls:'lanage_1',
                        editable:false,
                        name: 'recOprID',
                        allowBlank: false
                    },{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:15px',
                            name: 'recOprName'
                    }]
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '录入日期',
                    name: 'recDate',
                    allowBlank: false,
                    readOnly:true,
                    cls:'lanage_1',
                    maxValue: new Date(),
                    format:'Y-m-d'
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '期望解决日期',
                    name: 'expDate',
                    readOnly:true,
                    cls:'lanage_1',
                    format:'Y-m-d'
                },
                {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '责任人',
                        editable:false,
                        cls:'lanage_1',
                        name: 'resOprID',
                        allowBlank: false
                    },{
                        columnWidth:.5,
                        xtype: 'displayfield',
                        hideLabel: true,
                        style:'margin-left:15px',
                        name: 'resOprName'
                    }]
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '处理状态',
                    editable:false,
                    cls:'lanage_1',
                    name: 'status',
                    listeners:{
                        change:function(field) {
                            switch(field.getValue()) {
                                case '0':
                                    field.setValue('新建');
                                    bugWithdrawSubmitBtn.setDisabled(true);
                                    bugFinSubmitBtn.setDisabled(false);
                                    break;
                                case '1':
                                    field.setValue('已分配');
                                    bugWithdrawSubmitBtn.setDisabled(true);
                                    bugFinSubmitBtn.setDisabled(false);
                                    break;
                                case '2':
                                    field.setValue('已完成(待审核)');
                                    bugFinSubmitBtn.setDisabled(true);
                                    bugWithdrawSubmitBtn.setDisabled(false);
                                    break;
                                case '3':
                                    field.setValue('关闭');
                                    bugFinSubmitBtn.setDisabled(true);
                                    bugWithdrawSubmitBtn.setDisabled(false);
                                    break;
                                case '4':
                                    field.setValue('重新打开');
                                    bugWithdrawSubmitBtn.setDisabled(true);
                                    bugFinSubmitBtn.setDisabled(false);
                                    break;
                                case '5':
                                    field.setValue('取消');
                                    bugFinSubmitBtn.setDisabled(true);
                                    bugWithdrawSubmitBtn.setDisabled(false);
                                    break;
                                case '6':
                                    field.setValue('暂时搁置');
                                    bugWithdrawSubmitBtn.setDisabled(true);
                                    bugFinSubmitBtn.setDisabled(false);
                                    break;
                            }
                        }
                    }
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '预计结束日期',
                    name: 'estDate',
                    allowBlank: false,
                    minValue: new Date(),
                    format:'Y-m-d'
                },
                {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'numberfield',
                        fieldLabel: '进度百分比',
                        name: 'bugPercent',
                        minValue: 0,
                        maxValue:100,
                        listeners:{
                            change:function(field){
                                var percent = field.getValue();
                                var form = field.findParentByType('form').getForm();
                                var status = form.findField('status').getValue();

                                if(percent=='100'){
                                    if(status!='已完成(待审核)'&&status!='关闭'&&status!='取消'){
                                    Ext.MessageBox.alert("提示","你的工作进度已经是100%，请注意提交任务。");}
                                }
                            }
                        }
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
                        items:[{

                            items:[{
//                                xtype: 'datefield',
                                xtype: 'textfield',
                                name: 'bugUpdateDTTM',
                                fieldLabel: '进度更新时间',
                                cls:'lanage_1',
//                                format: 'Y-m-d H:i:s',
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
                        ]},
                {
                        xtype: 'textfield',
                        fieldLabel: '测试状态',
                        editable:false,
                        cls:'lanage_1',
                        name: 'bugMigration',
                        listeners:{
                            change:function(field) {
                                switch(field.getValue()) {
                                    case '0':
                                        field.setValue('已迁移UAT但未测试');
                                        break;
                                    case '1':
                                        field.setValue( 'UAT环境测试通过');
                                        break;
                                    case '2':
                                        field.setValue( '已迁移生产');
                                        break;
                                }
                            }
                        }
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
                        autoScroll: true
                    },
                    minHeight:300,
//                    height:document.body.clientHeight,
                    items:[{
                        title:'描述信息',
                        xtype:'form',
                        height:450,
                        style:'border:0',
                        items:[{
                            xtype: 'displayfield',
                            resizable:true,
                            autoScroll: true,
                            name: 'descript',
                            zIndex:999
                        }]
                    },
                    {
                        xtype: 'grid',
                        title:'附件集',
                        layout:'fit',
                        frame: true,
                        columnLines: true,
                        name: 'attachmentGrid',
                        reference: 'attachmentGrid',
                        style:"border:0px",
                        store:{
                            type:'bugCkAttachmentStore'
                        },
                        columns: [
/*                            {
                            text: 'BUG编号',
                            dataIndex: 'bugID',
                            sortable: false,
                            hidden: true
                        },
                            {
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
                        }]
                    }]
                }]
            }],
            buttons:[{
                text: '完成并提交',
                reference:'bugFinSubmitBtn',
                handler:'bugFinSubmit',
                iconCls:'ensure'
            },{
                text: '撤回提交',
                reference:'bugWithdrawSubmitBtn',
                handler:'bugWithdrawSubmit',
                iconCls:'revoke'
            },{
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
            }]
        });

        this.callParent();
        var bugFinSubmitBtn=this.lookupReference('bugFinSubmitBtn');
        var bugWithdrawSubmitBtn=this.lookupReference('bugWithdrawSubmitBtn');
    }
});
