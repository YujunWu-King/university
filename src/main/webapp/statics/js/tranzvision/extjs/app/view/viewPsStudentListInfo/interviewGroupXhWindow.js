//面试组内序号
Ext.define('KitchenSink.view.viewPsStudentListInfo.interviewGroupXhWindow', {
    extend: 'Ext.window.Window',
    reference: 'interviewGroupXhWindow',
    controller: 'viewxscontrol',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController'
    ],
    title: '面试组组内排序',
    bodyStyle: 'overflow-y:hidden;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    width: 650,
    ignoreChangesFlag: true,
    y: 10,
    minWidth: 400,
    resizable: true,
    modal: true,
    listeners: {
        resize: function (win) {
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {

        var pwstore = new KitchenSink.view.common.store.comboxStore({
            recname: 'PS_TZ_PWZ_VW',
            condition: {
                TZ_JG_ID: {
                    value: Ext.tzOrgID,
                    operator: "01",
                    type: "01"
                },
                TZ_CLASS_ID: {
                    value: this.classID,
                    operator: "01",
                    type: "01"
                }
                ,
                TZ_APPLY_PC_ID: {
                    value: this.batchID,
                    operator: "01",
                    type: "01"
                }
            },
            result: 'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
        });
        pwstore.sort('TZ_CLPS_GR_NAME', 'ASC');
        Ext.apply(this, {
            items: [
                {
                    xtype: 'form',
                    reference: 'classForm',
                    ignoreChangesFlag: false,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelStyle: 'font-weight:bold'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 100,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [{
                        xtype: 'textfield',
                        hidden: true,
                        name: 'classID'
                    }, {
                        xtype: 'textfield',
                        hidden: true,
                        name: 'batchID'
                    }, {
                        xtype: 'combobox',
                        fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.auditStates", "评委组"),
                        forceSelection: true,
                        editable: false,
                        store: pwstore,
                        valueField: 'TZ_CLPS_GR_ID',
                        displayField: 'TZ_CLPS_GR_NAME',
                        queryMode: 'local',
                        name: 'jugGroupId',
                        listeners: {	// select监听函数
                            change: 'changeGroupToMs'
                        }
                    }, {
                        xtype: 'combobox',
                        fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.mszStates", "面试组"),
                        forceSelection: true,
                        editable: false,
                        store: Ext.create('Ext.data.Store', {
                            fields: ['TZ_GROUP_ID', 'TZ_GROUP_DESC']
                        }),
                        valueField: 'TZ_GROUP_ID',
                        displayField: 'TZ_GROUP_DESC',
                        queryMode: 'local',
                        name: 'msGroupId',
                        listeners: {	// select监听函数
                            change: 'changeMsToGrid'
                        }
                    }
                    ]
                }, {
                    xtype: 'tabpanel',
                    items: [{
                        xtype: 'grid',
                        height: 'auto',
                        title: '学生列表',
                        autoHeight: true,
                        minHeight: 120,
                        columnLines: true,
                        reference: 'pageGrid',
                        viewConfig: {
                            plugins: {
                                ptype: 'gridviewdragdrop',
                                containerScroll: true,
                                dragGroup: this,
                                dropGroup: this
                            },
                            listeners: {
                                drop: function (node, data, dropRec, dropPosition) {
                                    data.view.store.beginUpdate();
                                    var items = data.view.store.data.items;
                                    items.forEach((item, index) => {
                                        item.set('sXh', index + 1);
                                    });
                                    data.view.store.endUpdate();
                                }
                            }
                        },
                        enableDragDrop: true,
                        //style:"margin:10px",
                        store: Ext.create('Ext.data.Store', {
                            fields: [{name: 'ins'}, {name: 'sXh'}, {name: 'sName'}, {name: 'msh'}]
                        }),
                        plugins: [{
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        }],
                        columns: [{
                            text: '姓名',
                            dataIndex: 'sName',
                            minWidth: 150,
                            sortable: false,
                            flex: 1
                        }, {
                        	text: '报名表编号',
                            dataIndex: 'ins',
                            width: 150,
                            sortable: false
                            //hidden: true
                        }, {
                            text: '报名号',
                            dataIndex: 'msh',
                            width: 150,
                            sortable: false,
                            hidden: true
                        }, {
                            text: '序号',
                            dataIndex: 'sXh',
                            width: 150,
                            sortable: false
                        }]
                    }]
                }
            ],
            buttons: [{
                text: '保存',
                iconCls: "save",
                handler: 'onMsGroupSave'
            }, {
                text: '确定',
                iconCls: "ensure",
                handler: 'onMsGroupEnsure'
            }, {
                text: '关闭',
                iconCls: "close",
                handler: function (btn) {
                    //获取窗口
                    var win = btn.findParentByType("window");
                    var form = win.child("form").getForm();
                    //关闭窗口
                    win.close();
                }
            }]
        });
        this.callParent();
    }

});
