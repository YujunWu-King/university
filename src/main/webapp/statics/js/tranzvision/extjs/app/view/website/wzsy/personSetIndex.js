Ext.define('KitchenSink.view.website.wzsy.personSetIndex', {
    extend: 'Ext.panel.Panel',
    xtype: 'personSetIndex',
    controller:'personSetController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.website.wzsy.personSetController',
        'KitchenSink.view.website.wzsy.personSetStore'
    ],
    title: '用户显示信息项配置',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    listeners: {
        afterrender: function(panel) {
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_SITE_PERSON_COM","PageID":"TZ_SITE_PERSON_STD","OperateType":"QF","comParams":{}}';
            Ext.tzLoad(tzParams,
                function(responseData) {
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
        }
    },
    items: [
        {
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
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '  机构名称',
                    name: 'name',
                    readOnly:true,
                    cls:'lanage_1'
                }, {
                    xtype: 'checkboxfield',
                    name: 'photo',
                    boxLabel:'<span style="font-weight:bold;">显示首页个人头像</span>',
                    hideLabel:true
                },{
                    xtype:'checkboxfield',
                    name:'photo2',
                    hideLabel:true,
                    boxLabel: '<span style="font-weight:bold;">显示个人信息头像</span>'
               }
            ]
        },
        {
            xtype: 'grid',
            title: '注册项列表',
            minHeight:500,
            frame: true,
            columnLines: true,
            style: "margin:0 8px",
            multiSelect: true,
            viewConfig: {
                enableTextSelection: true
            },

            plugins: [{
                ptype: 'cellediting',
                listeners: {
                    beforeedit: function( editor, context, eOpts ){
                        var dataIndex = context.field;
                        var isSysField = context.record.data.isSysField;
                        if(dataIndex == "regFieldType" && isSysField =="Y"){
                            editor.cancelEdit();
                            return false;
                        }
                        var regFieldType = context.record.data.regFieldType;
                        if(dataIndex == "defaultValue" && regFieldType == "DROP"){
                            editor.cancelEdit();
                            return false;
                        }
                    }
                }
            }],
            store: {
                type: 'personSetStore'
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
            columns: [
                {
                    text: '顺序',
                    dataIndex: 'order',
                    width: 60
                },
                {
                    text: '注册项ID',
                    dataIndex: 'regId',
                    width: 140
                },
                {
                    text: '名称',
                    dataIndex: 'regName',
                    minWidth: 100,
                    flex: 1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false,
                        emptyText:"不允许为空！"
                    }
                },
                {
                    text: '英文名称',
                    dataIndex: 'regEnName',
                    minWidth: 100,
                    flex: 1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: true
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '展示在网站首页',
                    dataIndex: 'isShowWzsy',
                    flex: 1,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;
                            var isEnable = store.getAt(rowIndex).data.isEnable;
                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }
                            if(regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');

                        if (regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                            return (new Ext.grid.column.CheckColumn).renderer(false);

                        }else if(regId == "TZ_REALNAME" || regId == "TZ_GENDER" || regId == "TZ_EMAIL"){

                            cellmeta.tdCls = "x-item-disabled";
                            return (new Ext.grid.column.CheckColumn).renderer(true);

                        }else{
                            return (new Ext.grid.column.CheckColumn).renderer(value);
                        }
                    }
                }
            ]
        }],
    buttons: [
        {
            text: '保存',
            iconCls: "save",
            handler: 'onPersonSetSave'
        }
    ]
});