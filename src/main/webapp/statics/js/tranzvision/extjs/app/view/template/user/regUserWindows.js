/**
 * Created by admin on 2015/5/4.
 */

Ext.define('KitchenSink.view.template.user.regUserWindows', {
    extend: 'Ext.window.Window',
    xtype: 'regUserWindows',
    requires: [
                'Ext.data.*',
                'Ext.grid.*',
                'Ext.util.*',
                'Ext.toolbar.Paging',
                'Ext.ux.ProgressBarPager',
                'KitchenSink.view.template.user.regManageController',
                'KitchenSink.view.template.user.regManageModel',
                'KitchenSink.view.template.user.regManageStore'
    ],
    title: '用户注册项管理',
    reference: 'regUserWindows',
    width: 900,
    height: 550,
    minWidth: 300,
    minHeight: 380,
    resizable: true,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    modal: true,
    closeAction: 'hide',
    listeners: {
        afterrender: function(panel) {
            var form = panel.child('form').getForm();
            var grid = panel.child('grid');

            var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_WIN_STD","OperateType":"QF","comParams":{}}';

            Ext.tzLoad(tzParams,
                function(responseData) {
                    //组件注册信息数据
                    var formData = responseData.formData;
                    form.setValues(formData);
                    //页面注册信息列表数据
                    grid.store.pageID = 'TZ_REGGL_WIN_STD';
                    grid.store.load();
                });
        }
    },
    items: [{
        xtype: 'form',
        reference: 'themeForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle: 'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: '机构名称',
            name: 'name',
            disabled: true
        },
            {
                xtype: 'textfield',
                fieldLabel: '发布状态',
                name: 'state',
                disabled: true
            }]
    },
        {
            xtype: 'grid',
            title: '用户注册项管理',
            frame: true,
            columnLines: true,
            selModel: {
                type: 'checkboxmodel'
            },
            style: "margin:8px",
            multiSelect: true,
            viewConfig: {
                enableTextSelection: true
            },
            header: false,

            plugins: {
                ptype: 'cellediting',
                clicksToEdit: 1
            },
            store: {
                type: 'regManageStore'
            },
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop',
                    dragText: '拖拽进行选项的排序'
                },
                listeners: {
                    drop: function(node, data, dropRec, dropPosition) {
                        data.view.store.beginUpdate();
                        var items = data.view.store.data.items;
                        for (var i = 0; i < items.length; i++) {
                            items[i].set('order', i + 1);
                        }
                        data.view.store.endUpdate();
                    }
                }
            },
            columns: [{
                text: '排列顺序',
                dataIndex: 'order',
                width: 60
            },
                {
                    text: '注册项ID',
                    dataIndex: 'regId',
                    width: 240
                },
                {
                    text: '名称',
                    dataIndex: 'regName',
                    minWidth: 100,
                    flex: 1
                },
                {
                    xtype: 'checkcolumn',
                    text: '是否启用',
                    dataIndex: 'isEnable',
                    minWidth: 100,
                    flex: 1,
                    listeners: {
                        "beforecheckchange": function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var order = store.getAt(rowIndex).data.order;

                            if (parseInt(order) < 7) {
                                return false;
                            }
                        }
                    }
                },
                {
                    text: '注册项类型',
                    dataIndex: 'regFieldType',
                    minWidth: 100,
                    flex: 1,
                    editor: {
                        xtype: 'combobox',
                        store: {
                            fields: [{
                                name: 'regTypeValue'
                            },
                                {
                                    name: 'regTypeDesc'
                                }],
                            data: [{
                                regTypeValue: 'INP',
                                regTypeDesc: '文本框'
                            },
                                {
                                    regTypeValue: 'DROP',
                                    regTypeDesc: '下拉框'
                                }]
                        },
                        displayField: 'regTypeDesc',
                        valueField: 'regTypeValue',
                        editable: false,
                        listeners: {
                            change: function(col, newValue, oldValue, eOpts) {

                            }
                        }
                    },
                    renderer: function(v) {
                        if (v == 'DROP') {
                            return "下拉框";
                        } else {
                            return "文本框";
                        }
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '是否必填',
                    dataIndex: 'isRequired',
                    minWidth: 100,
                    flex: 1,
                    disabled: true,
                    listeners: {
                        "beforecheckchange": function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var order = store.getAt(rowIndex).data.order;

                            if (parseInt(order) < 7) {
                                return false;
                            }
                        }
                    }
                },
                {
                    menuDisabled: true,
                    sortable: false,
                    width: 60,
                    xtype: 'actioncolumn',
                    items: [{
                        iconCls: 'edit',
                        tooltip: '编辑',
                        handler: 'onReSetDropVal'
                    }]
                }]
        }],
    buttons: [{
        text: '保存',
        iconCls: "save",
        handler: 'onRegUserWinSave'
    },
        {
            text: '确定',
            iconCls: "ensure",
            handler: 'onRegUserWinEnsure'
        },
        {
            text: '关闭',
            iconCls: "close",
            handler: 'onRegUserWinClose'
        }]
});