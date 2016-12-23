//--------------------
Ext.define('KitchenSink.view.payment.accountEditForm', {
    extend: 'Ext.window.Window',
    xtype: 'accountEditForm',
    reference:'accountEditForm',  
    
    title: '增加账户',
    width: 800,
    height: 360,
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
            fieldLabel: '账户号',
            name: 'accountId',
            allowBlank: false
        }, {
            fieldLabel: '账户名',
            name: 'accountName',
            allowBlank: false
        }, {
            fieldLabel: '账户Key',
            name: 'accountKey',
            allowBlank: false
        },{
            fieldLabel: '账户状态',
            xtype: 'combobox',
            displayField: 'state',
            anchor: '-15',
            queryMode: 'local',
            name: 'accountState',
            hiddenName:'accountState',
            valueField: 'TValue',
            cls:'lanage_1',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_PACCOUNT_STATE"),
            editable:false,
            allowBlank: false
        },{
            fieldLabel: '账户描述',
            name: 'accountDescribe',
            allowBlank: false
        },{
            fieldLabel: '支付平台',
            xtype: 'combobox',
            displayField: 'platformName',
            valueField:'platformId',
            anchor: '-15',
            name: 'accountPlatform',
            hiddenName:'accountPlatform',
            editable:false,
            allowBlank: false
           }
        ]
    }],

    buttons: [{
        text: '保存',
        handler:'saveAccount'
    },'-',{
        text: '确认',
        handler:'ensureAccount'
    },'-',{
        text: '关闭',
        handler:'closeForm'
    },'-','-']
});


//----------------------
