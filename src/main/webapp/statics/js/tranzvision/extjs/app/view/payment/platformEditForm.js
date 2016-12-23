//--------------------
Ext.define('KitchenSink.view.payment.platformEditForm', {
    extend: 'Ext.window.Window',
    xtype: 'platformEditForm',
    reference:'platformEditForm',  
    
    title: '增加账户',
    width: 800,
    height: 450,
    minWidth: 450,
    minHeight: 350,
    layout: 'fit',
    plain: true,
    
    items: [{
        xtype: 'form',

        defaultType: 'textfield',
        fieldDefaults: {
            labelWidth: 100
        },
        
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        
        bodyPadding: 5,
        border: false,

        items: [{
            fieldLabel: '平台ID',
            name: 'platformId',
            allowBlank: false
        },{
            fieldLabel: '平台名称',
            name: 'platformName',
            allowBlank: false
        },{
            fieldLabel: '接口地址',
            name: 'platformInterface',
            allowBlank: false
        },{
            fieldLabel: '回掉地址',
            name: 'returnUrl',
            allowBlank: false
        },
        {
            fieldLabel: '接口处理类',
            name: 'dealClass',
            allowBlank: false
        },{
            fieldLabel: '平台状态',
            xtype: 'combobox',
            displayField: 'state',
            anchor: '-15',
            queryMode: 'local',
            name: 'platformState',
            hiddenName:'platformState',
            valueField: 'TValue',
            cls:'lanage_1',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_PACCOUNT_STATE"),
            editable:false
        },{
            fieldLabel: '处理等待时间',
            name: 'waitTime',
            allowBlank: false
        },
        {
            fieldLabel: '平台说明',
            name: 'platformDescribe'
        }
        ]
    }],

    buttons: [{
        text: '保存',
        handler:'savePlatform'
    },'-',{
        text: '确认',
        handler:'ensurePlatform'
    },'-',{
        text: '关闭',
        handler:'closeForm'
    },'-','-']
});


//----------------------
