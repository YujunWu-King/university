/**
 * Created by caoy on 2016/9/18.   外部站点批量发布
 */
Ext.define('KitchenSink.view.siteManage.outsiteBatch.batchReleasePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'batchReleasePanel',
    controller: 'batchReleaseC',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.outsiteBatch.batchReleaseController',
        'KitchenSink.view.siteManage.outsiteBatch.batchReleaseModel',
        'KitchenSink.view.siteManage.outsiteBatch.batchReleaseStore'
    ],
    title: 'CMS批量发布',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    //actType: 'add',//默认新增
    /*dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closebatch'}]
    },{
        xtype:"toolbar",
        items:[
            {text:"新增",tooltip:"发布",iconCls:"save",handler:'onBatchSave'},"-",
            {text:"编辑",tooltip:"刷新",iconCls: 'refresh',handler:'onBatchreshfresh'}
        ]
    }],*/
    initComponent: function () {
        var store = new KitchenSink.view.siteManage.outsiteBatch.batchReleaseStore();
        Ext.apply(this, {
        items: [{
            xtype: 'form',
            reference: 'comRegForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
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
                xtype: 'combobox',
                fieldLabel: '发布类型',
                forceSelection: true,
                allowBlank: false,
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_FBLX"),
                typeAhead: true,
                queryMode: 'local',
                name: 'batchType',
                listeners: {
                    select: function (combo, record, index) {
                        form = combo.findParentByType("form").getForm();
                        // A:按站点发布   B:按栏目发布
                        if (combo.getValue() == "A") {
                            form.findField("coluId").hide();
                            form.findField("coluName").hide();
                            form.findField("siteId").show();
                            form.findField("siteName").show();
                        } else {
                            form.findField("coluId").show();
                            form.findField("coluName").show();
                            form.findField("siteId").hide();
                            form.findField("siteName").hide();
                        }
                    }
                }
            }, {
                layout: {
                    type: 'column'
                },
                items: [{
                    columnWidth: .55,
                    xtype: 'textfield',
                    fieldLabel: '外部站点编号',
                    name: 'siteId',
                    editable: false,
                    hidden: true,
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearPmtSearchSite'
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "pmtSearchSite"
                        }
                    }
                }, {
                    columnWidth: .45,
                    xtype: 'displayfield',
                    hideLabel: true,
                    style: 'margin-left:5px',
                    hidden: true,
                    name: 'siteName'
                }]
            }, {
                layout: {
                    type: 'column'
                },
                items: [{
                    columnWidth: .55,
                    xtype: 'textfield',
                    fieldLabel: '栏目编号',
                    name: 'coluId',
                    editable: false,
                    hidden: true,
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearPmtSearchColu'
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "pmtSearchColu"
                        }
                    }
                }, {
                    columnWidth: .45,
                    xtype: 'displayfield',
                    hideLabel: true,
                    style: 'margin-left:5px',
                    hidden: true,
                    name: 'coluName'
                }]
            }]
        }, {
            buttons: [{
                text: '发布',
                iconCls: "save",
                handler: 'onBatchSave'
            }, {
                text: '刷新',
                iconCls: "refresh",
                handler: 'onBatchreshfresh'
            }]
        },{
            xtype: 'grid',
            title: '发布结果列表',
            frame: true,
            columnLines: true,
            height: 350,
            store: store,
            reference: 'pageRegGrid',
            style: "margin:10px",
            multiSelect: true,
            columns: [{
                text: '批量发布ID',
                dataIndex: 'batchId',
                hidden: true
            }, {
                text: '发布类型',
                dataIndex: 'batchType',
                width: 240,
                renderer: function (value, metadata, record) {
                    if (value == "A") {
                        return "按站点发布";
                    } else if (value == "B") {
                        return "按栏目发布";
                    }
                }
            }, {
                text: '站点/栏目',
                dataIndex: 'objectName',
                width: 240
            }, {
                text: '发布时间',
                dataIndex: 'batchDate',
                minWidth: 100,
                flex: 1
            }, {
                text: '发布人',
                dataIndex: 'opr',
                minWidth: 100,
                flex: 1
            }, {
                text: '结束时间',
                dataIndex: 'endDate',
                minWidth: 100,
                flex: 1
            }, {
                text: '运行状态',
                dataIndex: 'batchStatus',
                minWidth: 100,
                renderer: function (value, metadata, record) {
                    if (value == "Y") {
                        return "发布成功";
                    } else if (value == "N") {
                        return "发布失败";
                    } else if (value == "C") {
                        return "发布中";
                    }
                }
            }]
        }]
        });
        this.callParent();
    }
});

