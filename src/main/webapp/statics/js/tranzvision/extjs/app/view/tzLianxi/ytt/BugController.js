Ext.define('BugController',{
    extend:'Ext.app.ViewController',
    alias:'controller.BugController',
    requires:[
        'BugWindow'
    ],

    //新增bug--------------------------------------------------------------------------------------------------------------------------------------
    addBug:function(btn){
        var baseBtn=btn;
        //var win =new BugWindow();
        var win=Ext.create('Ext.Window',{
            width:1300,
            height:700,
            isAdd:true,
            actType:'edit',/*edit:编辑；view：查看*/
            plain:true,
            modal:true,
            bodyStyle:'padding:15px;',
            controller:'BugController',
            closeAction:'close',
            closable:true,
            items:[{
                xtype:'form',
                bodyPadding: 10,
                width:'fit',
                height:650,
                autoScroll:true,
                region: 'center',
                bodyStyle:'border:none;',
                defaultType: 'textfield',
                blankText:'不允许为空',
                reference:'BugForm',
                defaults:{
                    labelSeparator:':',
                    labelAlign:'left'
                },
                items:[{
                    style:'margin-top:5px',
                    xtype:'displayfield',
                    fieldLabel: 'Bug编号',
                    name: 'bugID',
                    value:'TZ_NEXT'
                },
                    {
                            xtype:'textfield',
                            fieldLabel: 'Bug名称',
                            name: 'bugName',
                            allowBlank:false
                        },
                    {
                                xtype: 'combo',
                                fieldLabel: '处理状态',
                                name: 'bugStatus',
                                store: {
                                    type: 'BugStaStore'
                                },
                                style: {
//                                    'margin-left': '40px'
                                },
                                triggerAction: 'all',
                                editable: false,
                                displayField: 'TZ_TYPE_DTL',
                                emptyText: '请选择处理状态',
                                valueField: 'TZ_TYPE_ID',
                                queryMode: 'local'
                            }

                    ,
                    {
                            xtype:'textfield',
                            fieldLabel: '录入人',
                            name: 'recOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '录入日期',
                                name: 'recDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            },
                    {
                            xtype:'textfield',
                            fieldLabel: '责任人',
                            name: 'resOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '期望解决日期',
                                name: 'expDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            } ,
                    {
                        xtype: 'htmleditor',
                        fieldLabel: '描述信息',
                        name:'bugDesc'
                    }
                    ,{
                        xtype: 'filefield',
                        hideLabel: false,
                        fieldLabel:'上传附件',
                        emptyText: '请选择……'
                    }
                ]
            }
            ],
            buttons:[
                {
                    xtype: "button",
                    text: "确定",
                    name:'win_btn_ok',
                    handler: function(btn){
                        var win=btn.up('window');
                        var form = win.down("form");
                        if(form.getForm().findField('bugID').getValue()=='TZ_NEXT')
                        {
                            var bug_id="ytt_bug"+Math.floor(Math.random()*100);
                            form.getForm().findField("bugID").setValue(bug_id);
                        };

                        var tz_bug={

                            bugID:form.getForm().findField('bugID').getValue(),//bug_id
                            bugName:form.getForm().findField('bugName').getValue(),//名称
                            bugStatus:form.getForm().findField('bugStatus').getValue(),//状态
                            recOprID:form.getForm().findField('recOprID').getValue(),//录入人
                            recDate:form.getForm().findField('recDate').getValue(),//录入日期
                            resOprID:form.getForm().findField('resOprID').getValue(),//责任人
                            expDate:form.getForm().findField('expDate').getValue(),//期望解决日期
                            bugDesc:form.getForm().findField('bugDesc').getValue()//描述
                        };
                        var jsonData = {};
                        jsonData.update = tz_bug;
                        // console.log(jsonData.update);
                        jsonData.oprType = 'saveBug';
//                    jsonData.manager = 'ytt';
                        var store=baseBtn.findParentByType('grid').store;
                        var content = Ext.JSON.encode(jsonData);
                        console.log(content);
                        Ext.Ajax.request({
                            url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                            params: {params:content},
                            success: function(){
                                store.reload();
                            }
                        });

                        win.isAdd = false;
                        win.close();

                    }
                },
                {
                xtype: "button",
                text: "保存",
                handler: function(btn){
                    var win=btn.up('window');
                    var form = win.down("form");
                    if(form.getForm().findField('bugID').getValue()=='TZ_NEXT') {
                        var bug_id;
                        var store=baseBtn.findParentByType('grid').store;

                        do{
                            //如何获取grid查重
                            bug_id = "ytt_bug";
                            bug_id += Math.floor(Math.random() * 100);
                        }
                        while(store.find('bugID', bug_id) == -1);

                            console.log('已查重，生成bugID：'+bug_id);
                            form.getForm().findField("bugID").setValue(bug_id);
                    }

                    var tz_bug={

                        bugID:form.getForm().findField('bugID').getValue(),//bug_id
                        bugName:form.getForm().findField('bugName').getValue(),//名称
                        bugStatus:form.getForm().findField('bugStatus').getValue(),//状态
                        recOprID:form.getForm().findField('recOprID').getValue(),//录入人
                        recDate:form.getForm().findField('recDate').getValue(),//录入日期
                        resOprID:form.getForm().findField('resOprID').getValue(),//责任人
                        expDate:form.getForm().findField('expDate').getValue(),//期望解决日期
                        bugDesc:form.getForm().findField('bugDesc').getValue()//描述
                    };
                    var jsonData = {};
                    jsonData.update = tz_bug;
                   // console.log(jsonData.update);
                    jsonData.oprType = 'saveBug';
//                    jsonData.manager = 'ytt';

                    var content = Ext.JSON.encode(jsonData);
                    console.log(content);
                    Ext.Ajax.request({
                        url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                        params: {params:content},
                        success: function(){
                            store.reload();
                        }
                    });

                    win.isAdd = false;

                }

            },

                {
                    xtype: "button",
                    text: "关闭",
                    name:'win_btn_close',
                    handler: 'win_close'
                }
            ]
        });
        win.title="Bug新增";
        win.isAdd=true;
        win.show();
    },
    //删除bug--------------------------------------------------------------------------------------------------------------------------------------
    deleteBug:function(btn){
        var grid=btn.up('grid');
        var rec = grid.getSelectionModel().getSelection();// 返回值为 Record 数组
        if(rec.length==0){
            Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!');
        }else {
        Ext.MessageBox.confirm('提示', '确定删除该记录?', function (btn) {
            if (btn != 'yes') {
                return
            }

            if (rec.length > 0) {
                var bug_remove_set=[];
                for(var i=0;i< rec.length;i++)
                {

                   var bug_remove={
                        bugID:rec[i].getData().bugID
                    };
                    bug_remove_set.push(bug_remove);

                }
                var jsonData = {};

                jsonData.content = bug_remove_set;
                console.log(bug_remove_set);
                jsonData.oprType = 'removeBug';
//                console.log(grid_rec);
//                console.log(grid_rec.getData().bugID);

                var content = Ext.JSON.encode(jsonData);
                console.log(content);

                Ext.Ajax.request({
                    url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                    params: {params:content},
                    success: function(){
                        grid.getStore().reload();
                    }
                });

            }
        });
        }
    },
    //查看bug--------------------------------------------------------------------------------------------------------------------------------------
    viewBug:function(grid, rowIndex, colIndex){

        //var win =new BugWindow() ;
        var win=Ext.create('Ext.Window',{
            width:1300,
            height:700,
            isAdd:false,
            actType:'edit',/*edit:编辑；view：查看*/
            plain:true,
            modal:true,
            bodyStyle:'padding:15px;',
            controller:'BugController',
            closeAction:'close',
            closable:true,
            items:[{
                xtype:'form',
                bodyPadding: 10,
                width:'fit',
                height:650,
                autoScroll:true,
                region: 'center',
//                style:'border-width:0 0 0 0;border:0;',
                bodyStyle:'border:none',
                defaultType: 'textfield',
                blankText:'不允许为空',
                reference:'BugForm',
                defaults:{
                    labelSeparator:':',
                    labelAlign:'left'
                },
                items:[{
                    style:'margin-top:10px',
                    xtype:'displayfield',
                    fieldLabel: 'Bug编号',
                    name: 'bugID',
                    value:'TZ_NEXT'
                },
                   {
                            xtype:'textfield',
                            fieldLabel: 'Bug名称',
                            name: 'bugName',
                            allowBlank:false
                        },
                    {
                                xtype: 'combo',
                                fieldLabel: '处理状态',
                                name: 'bugStatus',
                                store: {
                                    type: 'BugStaStore'
                                },
                                style: {
//                                    'margin-left': '40px'
                                },
                                triggerAction: 'all',
                                editable: false,
                                displayField: 'TZ_TYPE_DTL',
                                emptyText: '请选择处理状态',
                                valueField: 'TZ_TYPE_ID',
                                queryMode: 'local'
                            }
                        ,
                    {
                            xtype:'textfield',
                            fieldLabel: '录入人',
                            name: 'recOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '录入日期',
                                name: 'recDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            },
                    {
                            xtype:'textfield',
                            fieldLabel: '责任人',
                            name: 'resOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '期望解决日期',
                                name: 'expDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            },
                    {
                        xtype: 'htmleditor',
                        fieldLabel: '描述信息',
                        name:'bugDesc'
                    }
                    ,{
                        xtype: 'filefield',
                        name:'bugfile',
                        hideLabel: false,
                        fieldLabel:'上传附件',
                        emptyText: '请选择……'
                    }
                ]
            }
            ],
            buttons:[
                {
                    xtype: "button",
                    text: "关闭",
                    handler: 'win_close'
                }
            ]
        });

        var rec = grid.getStore().getAt(rowIndex);
        var bugID=rec.data.bugID;
        var params = {"oprType": "getBug", "bugID":bugID};
        Ext.Ajax.request({ //与后台交互，向后台传送数据
            url: '/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
            params: {
                params:Ext.JSON.encode(params)
            },
            success: function(response){

                win.title="Bug查看";
                win.actType='view';
                win.show();

                var form = win.down("form");
                var grid_rec= grid.getStore().getAt(rowIndex);

                var form = win.child('form').getForm();//获得windows下面的子窗口form窗口
                form.setValues(Ext.JSON.decode(response.responseText));//decode的作用是把字符串变成对象


                console.log(response.responseText);
            }
        });
        var form = win.down("form");
        //只读form
        form.getForm().findField("bugID").setReadOnly(true);
        form.getForm().findField('bugName').setReadOnly(true);
        form.getForm().findField('bugStatus').triggerAction="";
        form.getForm().findField('recOprID').setReadOnly(true);
        form.getForm().findField("recDate").setReadOnly(true);
        form.getForm().findField('resOprID').setReadOnly(true);
        form.getForm().findField('expDate').setReadOnly(true);
        form.getForm().findField('bugDesc').setReadOnly(true);
        var file=form.getForm().findField('bugDesc');
//        form.getForm().findField('bugfile').setReadOnly(true);
        form.getForm().findField('bugfile').disable();



        //窗口按钮
    },
    //编辑bug---------------------------------------------------------------------------------------------------------------------------------------
    editBug:function(grid, rowIndex, colIndex){

//        var win =new BugWindow() ;
        var win=Ext.create('Ext.Window',{
            width:1300,
            height:700,
            isAdd:false,
            actType:'edit',/*edit:编辑；view：查看*/
            plain:true,
            modal:true,
            bodyStyle:'padding:15px;',
            controller:'BugController',
            closeAction:'close',
            closable:true,
            items:[{
                xtype:'form',
                bodyPadding: 10,
                width:'fit',
                height:650,
                autoScroll:true,
                region: 'center',
//                style:'border-width:0 0 0 0;border:0;',
                bodyStyle:'border:none',
                defaultType: 'textfield',
                blankText:'不允许为空',
                reference:'BugForm',
                defaults:{
                    labelSeparator:':',
                    labelAlign:'left'
                },
                items:[{
                    //style:'margin-top:10px',
                    xtype:'displayfield',
                    fieldLabel: 'Bug编号',
                    name: 'bugID',
                    value:'TZ_NEXT'
                },
                    {
                            xtype:'textfield',
                            fieldLabel: 'Bug名称',
                            name: 'bugName',
                            allowBlank:false
                        },
                    {
                                xtype: 'combo',
                                fieldLabel: '处理状态',
                                name: 'bugStatus',
                                store: {
                                    type: 'BugStaStore'
                                },
                                style: {
                                    //'margin-left': '40px'
                                },
                                triggerAction: 'all',
                                editable: false,
                                displayField: 'TZ_TYPE_DTL',
                                emptyText: '请选择处理状态',
                                valueField: 'TZ_TYPE_ID',
                                queryMode: 'local'
                            }
                       ,
                    {
                            xtype:'textfield',
                            fieldLabel: '录入人',
                            name: 'recOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '录入日期',
                                name: 'recDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            }
                    ,
                    {
                            xtype:'textfield',
                            fieldLabel: '责任人',
                            name: 'resOprID',
                            allowBlank:false
                        },
                    {
                                xtype: 'datefield',
                                fieldLabel: '期望解决日期',
                                name: 'expDate',
                                style: {
//                                    'margin-left': '40px'
                                },
                                format:'Y-m-d',
                                allowBlank: false
                            } ,
                    {
                        xtype: 'htmleditor',
                        fieldLabel: '描述信息',
                        name:'bugDesc'
                    }
                    ,{
                        xtype: 'filefield',
                        hideLabel: false,
                        fieldLabel:'上传附件',
                        emptyText: '请选择……'
                    }
                ]
            }
            ],
            buttons:[
                {
                    xtype: "button",
                    text: "确定",
                    name:'win_btn_ok',
                    handler: function(btn){
                        var win=btn.up('window');
                        var form = win.down("form");

                        var tz_bug={
                            bugID:form.getForm().findField('bugID').getValue(),//bug_id
                            bugName:form.getForm().findField('bugName').getValue(),//名称
                            bugStatus:form.getForm().findField('bugStatus').getValue(),//状态
                            recOprID:form.getForm().findField('recOprID').getValue(),//录入人
                            recDate:form.getForm().findField('recDate').getValue(),//录入日期
                            resOprID:form.getForm().findField('resOprID').getValue(),//责任人
                            expDate:form.getForm().findField('expDate').getValue(),//期望解决日期
                            bugDesc:form.getForm().findField('bugDesc').getValue()//描述
                        };
                        var jsonData = {};
                        jsonData.update = tz_bug;
                        jsonData.oprType = 'saveBug';

                        var store=grid.getStore();
                        var content = Ext.JSON.encode(jsonData);
                        console.log(content);
                        Ext.Ajax.request({
                            url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                            params: {params:content},
                            success: function(){
                                store.reload();
                            }
                        });
                        win.close();
                    }
                },
                {
                    xtype: "button",
                    text: "保存",
                    name:'win_btn_save',
//                    id:'save',
                    handler: function(btn){
                        var win=btn.up('window');
                        var form = win.down("form");

                        var tz_bug={
                            bugID:form.getForm().findField('bugID').getValue(),//bug_id
                            bugName:form.getForm().findField('bugName').getValue(),//名称
                            bugStatus:form.getForm().findField('bugStatus').getValue(),//状态
                            recOprID:form.getForm().findField('recOprID').getValue(),//录入人
                            recDate:form.getForm().findField('recDate').getValue(),//录入日期
                            resOprID:form.getForm().findField('resOprID').getValue(),//责任人
                            expDate:form.getForm().findField('expDate').getValue(),//期望解决日期
                            bugDesc:form.getForm().findField('bugDesc').getValue()//描述
                        };
                        var jsonData = {};
                        jsonData.update = tz_bug;
                        jsonData.oprType = 'saveBug';

                        var store=grid.getStore();
                        var content = Ext.JSON.encode(jsonData);
                        console.log(content);
                        Ext.Ajax.request({
                            url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                            params: {params:content},
                            success: function(){
                                store.reload();
                            }
                        });
                    }
                },

                {
                    xtype: "button",
                    text: "关闭",
                    handler: 'win_close'
                }
            ]
        });

        var rec = grid.getStore().getAt(rowIndex);
        var bugID=rec.data.bugID;
        var params = {"oprType": "getBug", "bugID":bugID};
        Ext.Ajax.request({ //与后台交互，向后台传送数据
            url: '/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
            params: {
                params:Ext.JSON.encode(params)
            },
            success: function(response){

                win.title="Bug编辑";
                win.actType='edit',
                win.show();

                var form = win.down("form");
                var grid_rec= grid.getStore().getAt(rowIndex);

                var form = win.child('form').getForm();//获得windows下面的子窗口form窗口
                form.setValues(Ext.JSON.decode(response.responseText));//decode的作用是把字符串变成对象
                console.log(response.responseText);
            }
        });

    },
    //删除bug---------------------------------------------------------------------------------------------------------------------------------------
    removeBug:function(grid, rowIndex, colIndex){

        var sel = grid.getSelectionModel();
        Ext.MessageBox.confirm('提示', '确定删除该记录?', function (btn) {
            if (btn != 'yes') {
                return
            }
            var sel = grid.getSelectionModel();

            //仅从store删除
            sel.select(rowIndex);
            grid.getStore().remove(sel.getSelection());
            //remove immediately
            /*if (grid.getStore().getCount() > 0) {
                sel.select(rowIndex);


                var grid_rec= grid.getStore().getAt(rowIndex);
                var jsonData = {};
                var bug_remove={
                    bugID:grid_rec.getData().bugID
                };
                jsonData.content = [bug_remove];
                jsonData.oprType = 'removeBug';
//                console.log(grid_rec);
//                console.log(grid_rec.getData().bugID);

                var content = Ext.JSON.encode(jsonData);
                console.log(content);

                Ext.Ajax.request({
                    url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
                    params: {params:content},
                    success: function(){
                        grid.getStore().reload();
                    }
                });

            }*/
        });
    },
    //生成bug_id------------------------------------------------------------------------------------------------------------------------------------
    generate_bug_id:function bug_id(store){
        //如何获取grid查重
        var bug_id="ytt_bug";
        bug_id+=Math.floor(Math.random()*100);

        if (store.find('bugID',bug_id)==-1)
        {
            console.log("已生成Bug编号:递归验证并返回唯一的Bug编号，continue");
            return bug_id;}
        else{
            console.log("出现重复重新generateBug编号");
            return  bug_id();
        }
    },
    //窗口保存按钮----------------------------------------------------------------------------------------------------------------------------------
    win_save:function (btn) {
        /*var win=btn.up('window');
        var grid=btn.up('window').up('grid');
        console.log(grid);
        var form = win.down("form");
        if (win.isAdd) {
            //var bug_id=this.generate_bug_id(store);
            var bug_id="ytt_bug"+Math.floor(Math.random()*100);
            form.getForm().findField("bugID").setValue(bug_id);
        }

        var jsonData = {};
        jsonData.update = form.getForm().getValues();
        jsonData.type = 'saveBug';
        jsonData.manager = 'ytt';

//      var store=this.view().child('grid').store;
      //  var store=gridbtn.findParentByType('grid').store;

        var content = Ext.JSON.encode(jsonData);
        Ext.Ajax.request({
            url: 'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
            params: {params:content},
            success: function(){
                store.reload();
            }
        });

        win.isAdd = false;
        console.log('json', content);

        alert("保存");*/
    },
    //窗口确定按钮----------------------------------------------------------------------------------------------------------------------------------
    win_ok:function (btn) {
        /*this.win_save();
        this.win_close();*/
    },
    //窗口关闭按钮----------------------------------------------------------------------------------------------------------------------------------
    win_close:function(btn){
        var win = btn.up("window");
        win.close();
    }
});
