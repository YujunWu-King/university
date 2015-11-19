Ext.define('KitchenSink.view.common.promptSearchWindow', {
    extend: 'Ext.window.Window',
    xtype: 'promptSearchWindow',
    //title: '',
    width: 650,
    //height: 620,
    minWidth: 400,
    minHeight: 420,
    maxHeight: 600,
    resizable: true,
    modal: true,
    closeAction: 'destroy',
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    requires:[
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.common.store.promptStore'
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },

    initComponent: function(){
        var me = this;
        //搜索结果列
        var columns = [];
        //序号
        columns.push({xtype: 'rownumberer',text:'序号'});
        //搜索结果字段
        var srhresults = this.srhresult;
        var size = 0;
        for(var fieldName in srhresults) {
            size++;
        }
        var flex = 1/size;
        for(var fieldName in srhresults) {
            if(this.multiselect){
                var column = new Ext.grid.column.Column({
                    text: srhresults[fieldName],
                    dataIndex: fieldName,
                    flex: flex
                });
            }else{
                var column = {
                    xtype:'linkcolumn',
                    text: srhresults[fieldName],
                    items:[{
                        getText: function(v, meta, rec) {
                            return v;
                        },
                        //text: srhresults[fieldName],
                        handler: function(grid, rowIndex){
                            me.selOneRow(grid, rowIndex);
                        }
                    }],
                    dataIndex: fieldName,
                    flex: flex
                };
            }

            columns.push(column);
        }
        //单选、多选
        var selModel;
        if(this.multiselect){
            selModel = "checkboxmodel";
        }else{
            selModel = "rowmodel";
        }

        //搜索条件
        var conItems = [];
        var srhconditions = this.condition.srhConFields;
        for(var fieldName in srhconditions) {
            //操作符
            var operator = srhconditions[fieldName].operator;
            //字段类型
            var type = srhconditions[fieldName].type;
            //操作符描述
            var oprDesc = "";
            switch (operator)
            {
                case '01':
                    oprDesc = "等于";
                    break;
                case '02':
                    oprDesc = "不等于";
                    break;
                case '03':
                    oprDec = "大于";
                    break;
                case '04':
                    oprDesc = "大于等于";
                    break;
                case '05':
                    oprDesc = "小于";
                    break;
                case '06':
                    oprDesc = "小于等于";
                    break;
                case '07':
                    oprDesc = "包含";
                    break;
                case '08':
                    oprDesc = "开始于";
                    break;
                case '09':
                    oprDesc = "结束于";
                    break;
                case '10':
                    oprDesc = "存在于";
                    break;
                case '11':
                    oprDesc = "为空";
                    break;
                case '12':
                    oprDesc = "不为空";
                    break;
                default:
                    if(type == "01"){
                        oprDesc = "开始于";
                    }else{
                        oprDesc = "等于";
                    }
            }

            var typeField = {};
            switch (type)
            {
                case '01'://字符串
                    typeField = {
                        xtype: 'textfield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        value:srhconditions[fieldName].value,
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };
                    break;
                case '02'://数字
                    typeField = {
                        xtype: 'numberfield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        value:srhconditions[fieldName].value,
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };
                    break;
                case '03'://日期
                    typeField = {
                        xtype: 'datefield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        value:srhconditions[fieldName].value,
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };
                    break;
                case '04'://时间
                    typeField = {
                        xtype: 'timefield',
                        columnWidth: 1,
                        increment: 30,
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        value:srhconditions[fieldName].value,
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };
                case '05'://下拉框
                    //combo配置
                    var appTransId = srhconditions[fieldName].appTransId;
                    typeField = {
                        xtype: 'combobox',
                        columnWidth: 1,
                        increment: 30,
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        forceSelection: true,
                        value:srhconditions[fieldName].value,
                        store: new KitchenSink.view.common.store.appTransStore(appTransId),
                        queryMode:'local',
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };
                    break;
                default:
                    typeField = {
                        xtype: 'textfield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: fieldName+'-value',
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    };

            }
            var fieldItem = {
                layout:'column',
                items:[{
                    xtype: 'displayfield',
                    labelWidth: 150,
                    width: 220,
                    labelSeparator:'',
                    fieldLabel:srhconditions[fieldName].desc,
                    value: oprDesc,
                    name: fieldName+'-desc'
                },typeField]
            }
            conItems.push(fieldItem);
        }
        //搜索结果数据
        var recname = this.recname;
        var condition = this.condition;
        var srhresult = this.srhresult;
        var maxRow = this.maxRow;
        var store = new KitchenSink.view.common.store.promptStore(recname,condition,srhresult,maxRow);
        var html = "";
        store.on("load",function(store, records, successful, eOpts){
            if(store.maxRow != -1 && store.totalCount == store.maxRow){
                html = "<span style='color:red; margin-left:10px;'>搜索结果记录总数超过"+store.maxRow+"条，当前只展示前"+store.maxRow+"条搜索记录，请输入搜索条件缩小搜索范围。</span>";
                //me.getComponent("resultDesc").html = html;
                Ext.apply(me.getComponent("resultDesc"),{
                    html: html
                });
                me.getComponent("resultDesc").show();
            }else{
                me.getComponent("resultDesc").hide();
            }
            me.doLayout();
            me.show();
        });
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                ignoreLabelWidth: true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },

                items: conItems,
                buttonAlign: 'left',
                buttons:[{
                    text: '搜索',
                    iconCls:"search",
                    handler: function(btn){
                        me.searchPrompt();
                    }
                },{
                    text: '清除',
                    iconCls:"clean",
                    handler: function(btn){
                        //搜索信息表单
                        var form = btn.findParentByType("form").getForm();
                        //重置表单
                        form.reset();
                    }
                }]
            },{
                html:html,
                itemId: 'resultDesc'
                //html:"<span style='color:red; margin-left:10px;'>搜索结果记录总数超过"+this.maxRow+"条，当前只展示前"+this.maxRow+"条搜索记录，请输入搜索条件缩小搜索范围。</span>"
            },{
                xtype: 'grid',
                //height: 320,
                height: 'auto',
                minHeight:180,
                title: '搜索结果列表',
                //frame: true,
                columnLines: true,
                //style:"margin:10px",
                columns: columns,
                selModel: {
                    type: selModel
                },
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 50,
                    listeners:{
                        afterrender: function(pbar){
                            var grid = pbar.findParentByType("grid");
                            pbar.setStore(grid.store);
                        }
                    },
                    displayInfo: true,
                    displayMsg: '显示{0}-{1}条，共{2}条',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    emptyMsg: '没有数据显示',
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }],
            buttons: [{
                text: '确定',
                iconCls:"ensure",
                hidden: !me.multiselect,
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("window");
                    //选中行
                    var selection = win.child("grid").getSelectionModel().getSelection();
                    //选中行长度
                    var checkLen = selection.length;
                    if(checkLen == 0){
                        Ext.Msg.alert("提示","未选中记录");
                        return;
                    }
                    //回调函数
                    win.callback(selection);
                    //修改密码信息表单
                    var form = win.child("form").getForm();
                    //重置表单
                    form.reset();
                    //关闭窗口
                    win.close();
                }
            },{
                text: '关闭',
                iconCls:"close",
                handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("window");
                    //修改密码信息表单
                    var form = win.child("form").getForm();
                    //关闭窗口
                    win.close();
                }
            }]
        });

        this.callParent();
    },
    searchPrompt: function(){
        //搜索信息表单
        var form = this.child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        //搜索结果列表
        var grid = this.child("grid");
        //搜索结果数据源
        var store = grid.getStore();
        //搜索条件
        var condition = this.condition;
        var srhconditions = condition.srhConFields;
        for(var fieldName in srhconditions) {
            var fieldValue = formParams[fieldName+"-value"];
            srhconditions[fieldName].value = fieldValue;
        }
        //搜索表或视图
        var recname = this.recname;
        //交互参数
        store.tzStoreParams = '{"OperateType":"'+store.tzType+'","maxRow":"'+store.maxRow+'","recname":"'+recname+'","condition":'+Ext.encode(condition)+',"result":"'+store.srhresults+'"}';
        store.load();
    },
    selOneRow: function(grid, rowIndex){
        var rec = grid.getStore().getAt(rowIndex);
        var selection = [];
        selection.push(rec);
        //回调函数:增加返回值，判断是否关闭窗口
        if(this.callback(selection)==undefined){
            //关闭窗口
            this.close();
        };
    },
    constructor: function (config) {
        //搜索表或视图名称
        this.recname = config.recname;
        //搜索描述信息
        this.title = config.searchDesc;
        //最大记录数
        this.maxRow = config.maxRow;
        //是否可多选
        this.multiselect = config.multiselect;
        //搜索条件
        this.condition = config.condition;
        //搜索结果字段
        this.srhresult = config.srhresult;
        //回调函数
        this.callback = config.callback;

        this.callParent();
    }
});
