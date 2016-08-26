Ext.syncRequire("KitchenSink.view.template.user.dropBoxSetStore");
Ext.define('KitchenSink.view.template.user.dropBoxSetWindow', {
    extend: 'Ext.window.Window',
    xtype: 'dropBoxSetWindow',
    requires: [
            'Ext.data.*',
            'Ext.grid.*',
            'Ext.util.*',
            'Ext.toolbar.Paging',
            'Ext.ux.ProgressBarPager',
            'KitchenSink.view.template.user.regManageController',
            'KitchenSink.view.template.user.dropBoxSetStore'
    ],
    title: '注册项下拉框取值配置',
    reference: 'dropBoxSetWindow',
    width: 600,
    minWidth: 300,
    minHeight: 200,
    y:20,
    layout: 'fit',
    resizable: true,
    modal: true,
    actType: 'add',
    items: [
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            hidden: true,
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 150,
                labelStyle: 'font-weight:bold'
            },
            items: [{
                //xtype: 'hiddenfield',
            	xtype: 'textfield',
                fieldLabel: "siteId",
                name: 'siteId',
                allowBlank: false
            },{
                xtype: 'hiddenfield',
                fieldLabel: "注册项ID",
                name: 'regId',
                allowBlank: false
            }]
        },
        {
            xtype: 'grid',
            minHeight: 200,
            maxHeight: 300,
            id: 'dropBoxSetGrid',
            name: 'dropBoxSetGrid',
            dockedItems: [{
                xtype: 'toolbar',
                items: [{
                    iconCls: 'add',
                    text: '新增',
                    tooltip: "新增选项",
                    handler: 'addOption'
                },
                    "-", {
                        iconCls: 'remove',
                        text: '删除',
                        tooltip: "删除选中的数据",
                        handler: 'deleteOptions'
                    }]
            }],
            columnLines: true,
            selModel: {
                type: 'checkboxmodel'
            },
            reference: 'dropBoxSetGrid',
            store: {
                type: 'dropBoxSetStore'
            },
            plugins: {
                ptype: 'cellediting',
                pluginId: 'dropBoxSetCellediting'
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
            	text: '站点Id',
                sortable: false,
                dataIndex: 'siteId'
            },{
                xtype: 'hiddenfield',
                text: '排序',
                sortable: false,
                dataIndex: 'order',
                hidden: true
            },
                {
                    text: '选项值',
                    sortable: false,
                    dataIndex: 'optId',
                    editor: {
                        xtype: 'textfield',
						maxLength: 10,
                        allowBlank: false
                    }
                },
                {
                    text: '描述',
                    sortable: false,
                    dataIndex: 'optName',
                    sortable: false,
                    flex: 2,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                },
                {
                    text: '英文描述',
                    sortable: false,
                    dataIndex: 'optEnName',
                    flex: 2,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: true
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: "是否默认",
                    dataIndex: 'isSelect',
                    width: 90,
                    listeners: {
                        checkchange: function(col, rowIndex, checked, eOpts ){
                            var grid = col.findParentByType("grid");
                            //页面注册信息数据
                            var store = grid.getStore();
                            var _r = store.getAt(rowIndex);
                            var index = store.findBy(function (record) {
                                return record.get('isSelect') == true && record.id != _r.id;
                            });
                            if(checked){
                                if (index >= 0) {
                                    store.getAt(index).set('isSelect', false);
                                }
                            }else{
                                if(index < 0){
                                    store.getAt(0).set('isSelect', true);
                                }
                            }
                        }
                    }
                },
                {
                    menuDisabled: true,
                    sortable: false,
                    width: 60,
                    align:'center',
                    xtype: 'actioncolumn',
                    items: [{
                        iconCls: 'remove',
                        tooltip: '删除',
                        handler: 'deleteOption'
                    }]
                }]
        }],
        buttons: [
            {
                text: '保存',
                iconCls: "save",
                handler: 'onOptionsSave'
            },
            {
                text: '确定',
                iconCls: "ensure",
                handler: 'onOptionsEnsure'
            },
            {
                text: '关闭',
                iconCls: "close",
                handler: 'onOptionsClose'
            }
        ]
});