Ext.define('KitchenSink.view.processDispatchMg.processRunInfoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processRunInfoWindow',
    controller: 'processDispatchCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.processDispatchMg.processDispatchController'
    ],
    title: '进程运行详细信息',
    reference: 'processRunInfoWindow',
    id:'runInfoWin',
    width: 700,
    height: 500,
    minWidth: 200,
    minHeight: 100,
    layout: 'fit',
    resizable: true,
    modal: true,
    actType: 'add',

    items: [{
        xtype: 'form',
        id: 'runInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold;font-size:12px'
        },
        items: [
            {
                xtype: 'displayfield',
                style: 'margin-left:20px',
                fieldStyle: 'color:blue;',
                value: "进程"
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'textfield',
                        columnWidth: .45,
                        name: 'processInstanceId',
                        hidden: true,
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        name: 'processInstance',
                        labelWidth: 80,
                        fieldLabel: "进程实例"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        name: 'runPlatType',
                        style: 'margin-left:50px',
                        labelWidth: 100,
                        fieldLabel: "运行平台类型"
                    }
                ]
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        name: 'processName',
                        labelWidth: 80,
                        fieldLabel: "进程名称"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        name: 'processDesc',
                        style: 'margin-left:50px',
                        labelWidth: 100,
                        fieldLabel: "进程描述"
                    }
                ]
            },
            {
                xtype: 'displayfield',
                fieldStyle: 'color:blue;',
                style: 'margin-left:20px',
                value: "运行"
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        name: 'runConId',
                        labelWidth: 80,
                        fieldLabel: "运行控制ID"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        labelWidth: 100,
                        name: 'runServer',
                        style: 'margin-left:50px',
                        fieldLabel: "运行服务器"
                    }
                ]
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        labelWidth: 80,
                        name: 'loop',
                        fieldLabel: "循环"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        labelWidth: 100,
                        name: 'status',
                        style: 'margin-left:50px',
                        fieldLabel: "运行状态"
                    }
                ]
            }, {
                xtype: 'displayfield',
                style: 'margin-left:20px',
                fieldStyle: 'color:blue;',
                value: "日期/时间"
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        name: 'requestTime',
                        labelWidth: 80,
                        fieldLabel: "请求创建时间"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        name: 'runStartTime',
                        style: 'margin-left:50px',
                        labelWidth: 100,
                        fieldLabel: "可运行开始时间"
                    }
                ]
            }, {
                xtype: 'fieldcontainer',
                layout: 'column',
                style: 'margin-left:50px',
                items: [
                    {
                        xtype: 'displayfield',
                        columnWidth: .45,
                        name: 'processStartTime',
                        labelWidth: 80,
                        fieldLabel: "进程开始时间"
                    },
                    {
                        xtype: 'displayfield',
                        columnWidth: .55,
                        name: 'processEndTime',
                        labelWidth: 100,
                        style: 'margin-left:50px',
                        fieldLabel: "进程结束时间"
                    }
                ]
            }, {
                xtype: 'displayfield',
                value: '查看进程运行日志',
                style: 'margin-top:50px;margin-left:20px',
                width: 100,
                fieldStyle: 'color:blue;',
                listeners: {
                    render: function (p) {
                        // Append the Panel to the click handler's argument list.
                        p.getEl().on('click', function (p) {

                            //获取运行实例ID
                            var formValues = Ext.getCmp('runInfoForm').getForm().getValues();
                            var win = Ext.getCmp('runInfoWin');
                            var processInstanceId = formValues['processInstanceId'];
                            win.close();
                            console.log(processInstanceId)

                            //是否有访问权限
                            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JC_DISPATCH_COM"]["TZ_PROCESS_LOG"];
                            if (pageResSet == "" || pageResSet == undefined) {
                                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                                return;
                            }
                            //该功能对应的JS类
                            var className = pageResSet["jsClassName"];
                            if (className == "" || className == undefined) {
                                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PROCESS_LOG，请检查配置。');
                                return;
                            }

                            var contentPanel, cmp, className, ViewClass, clsProto;
                            var themeName = Ext.themeName;

                            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                            contentPanel.body.addCls('kitchensink-example');

                            if (!Ext.ClassManager.isCreated(className)) {
                                Ext.syncRequire(className);
                            }
                            ViewClass = Ext.ClassManager.get(className);
                            clsProto = ViewClass.prototype;

                            if (clsProto.themes) {
                                clsProto.themeInfo = clsProto.themes[themeName];

                                if (themeName === 'gray') {
                                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
                                } else if (themeName !== 'neptune' && themeName !== 'classic') {
                                    if (themeName === 'crisp-touch') {
                                        clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                                    }
                                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
                                }
                                // <debug warn>
                                // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
                                if (!clsProto.themeInfo) {
                                    Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                                        themeName + '\'. Is this intentional?');
                                }
                                // </debug>
                            }

                            cmp = new ViewClass();
                            cmp.actType = "update";

                            cmp.on('afterrender',function(panel){
                                tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_PROCESS_LOG","OperateType":"QF","comParams":{"processInstanceId":"'+processInstanceId+'"}}';
                                var	logGrid = panel.child('grid');
                                Ext.tzLoad(tzParams,function(responseData){

                                    var tzStoreParams = '{"processInstanceId":"'+processInstanceId+'"}';
                                    logGrid.store.tzStoreParams = tzStoreParams;
                                    logGrid.store.load();

                                });
                            });
                            tab = contentPanel.add(cmp);

                            contentPanel.setActiveTab(tab);

                            Ext.resumeLayouts(true);

                            if (cmp.floating) {
                                cmp.show();
                            }
                        });
                    },
                }
            }
        ],
    }],
    buttons: [{
        text: '关闭',
        iconCls: "close",
        handler: function (btn) {
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }],
});
