Ext.define('KitchenSink.view.artTypeManagement.artTypeInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'artTypeInfo',
    controller: 'artTypeController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.template.user.regManageController',
        'KitchenSink.view.template.user.regManageStore'
    ],
    title: '内容类型定义',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    listeners: {
        afterrender: function(panel) {
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"QF","comParams":{}}';
            Ext.tzLoad(tzParams,
                function(responseData) {
                    var formData = responseData.formData;
                    form.setValues(formData);
                    
                    var grid = panel.child("grid");
        			//注册项数据
        			var store = grid.getStore();
        			store.load();
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
                    xtype: 'checkboxgroup',
                    fieldLabel: '注册方式',
                    columns:1,
                    allowBlank: false,
                    blankText:"您至少选择一种注册方式！",
                    items:[
                        {boxLabel:'邮件',name:'activate',inputValue:'EMAIL'},
                        {boxLabel:'短信',name:'activate',inputValue:'MOBILE'}
                    ]
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '机构名称',
                    name: 'name',
                    readOnly:true,
                    cls:'lanage_1'
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
                    text: '是否启用',
                    dataIndex: 'isEnable',
                    width:100,
                    listeners: {
                        "beforecheckchange": function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;

                            if(regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                            /*取消启用时，要求检查TZ_REALNAME、TZ_FIRST_NAME、TZ_LAST_NAME必选其一*/
                            if(!checked){
                                var enableSize = 0;
                                for(var i=0; i< store.getCount(); i++){
                                    var zcxbh = store.getAt(i).data.regId;
                                    var sfqy = store.getAt(i).data.isEnable;
                                    if((zcxbh == "TZ_REALNAME" || zcxbh == "TZ_FIRST_NAME" || zcxbh == "TZ_LAST_NAME") && sfqy && zcxbh != regId ){
                                        enableSize ++;
                                    }
                                }
                                if((regId == "TZ_REALNAME" || regId == "TZ_FIRST_NAME" || regId == "TZ_LAST_NAME") && enableSize < 1){
                                    Ext.Msg.alert("提示","姓名、FIRST_NAME、LAST_NAME至少选择一项！");
                                    return false;
                                }
                                /*如果不启用，除去姓名、性别、邮件外， 后面的是否外网显示、是否必填设置清空*/
                                if(regId != "TZ_REALNAME" && regId != "TZ_GENDER" && regId != "TZ_EMAIL"){
                                    var record = store.getAt(rowIndex);
                                    record.data.isShowWzsy = false;
                                    console.log(record.data);
                                    record.data.isRequired = false;
                                   // record.set('isShowWzsy',false);
                                }
                                if(regId == "TZ_REALNAME"){
                                    var record2 = store.getAt(rowIndex);
                                    record2.data.isRequired = false;
                                }
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                        var regId = record.get('regId');
                        if (regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            //如何为当前TD添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                        }
                        return (new Ext.grid.column.CheckColumn).renderer(value);
                    }
                },
                {
                    text: '注册项类型',
                    dataIndex: 'regFieldType',
                    width:110,
                    editor: {
                        xtype: 'combobox',
                        store: {
                            fields: [
                                {
                                    name: 'regTypeValue'
                                },
                                {
                                    name: 'regTypeDesc'
                                }
                            ],
                            data: [
                                {
                                    regTypeValue: 'INP',
                                    regTypeDesc: '文本框'
                                },
                                {
                                    regTypeValue: 'DROP',
                                    regTypeDesc: '下拉框'
                                }
                            ]
                        },
                        displayField: 'regTypeDesc',
                        valueField: 'regTypeValue',
                        editable: false
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var isSysField = record.get('isSysField');

                        if (isSysField == "Y"){
                            //如何为当前TD添加disabled属性？
                            cellmeta.style = "color:#adadad";
                        }
                        if (value == 'DROP') {
                            return "下拉框";
                        } else {
                            return "文本框";
                        }
                    }
                },
                {
                    text: '默认值',
                    dataIndex: 'defaultValue',
                    minWidth: 40,
                    flex: 1,
                    editor: {
                        xtype: 'textfield'
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regFieldType = record.get('regFieldType');
                        if (regFieldType == "DROP"){
                            cellmeta.tdCls = "x-item-disabled";
                            record.set('defaultValue', "");
                        }
                        return value;
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '是否必填',
                    dataIndex: 'isRequired',
                    width:100,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;

                            var isEnable = store.getAt(rowIndex).data.isEnable;
                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }

                            if(regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');
                        if (regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                        }else{
                            var isEnable = record.get("isEnable");
                            if(!isEnable){
                               // cellmeta.tdCls = "x-item-disabled";
                            }
                        }

                        return (new Ext.grid.column.CheckColumn).renderer(value);
                    }
                },
                {
                    text: '设置下拉项',
                    width: 130,
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                        var isSysField = store.getAt(rowIndex).data.isSysField;
                        var regFieldType = store.getAt(rowIndex).data.regFieldType;

                        if(isSysField == "Y" || regFieldType == "INP"){
                            return '<span style="color:#adadad">设置下拉项</span>';
                        }else{
                            return '<a href="javascript:void(0);">设置下拉项</a>';
                        }
                    },
                    listeners:{
                        click:'onReSetDropVal'
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

                        }/*else if(regId == "TZ_REALNAME" || regId == "TZ_GENDER" || regId == "TZ_EMAIL"){

                            cellmeta.tdCls = "x-item-disabled";
                           // return (new Ext.grid.column.CheckColumn).renderer(true);
                            return (new Ext.grid.column.CheckColumn).renderer(value);*//*保持gird与数据库中的值一致*//*
                        }*/else
                        {
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
            handler: 'onUserRegSave'
        },
        {
            text: '预览',
            iconCls: "preview",
            handler: 'onUserRegPreView'
        },{
            text: '关闭',
            iconCls:"close",
            handler: 'onPanelClose'
        }
    ]
});