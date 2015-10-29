Ext.define('KitchenSink.view.Header', {
    extend: 'Ext.Container',
    xtype: 'appHeader',
    id: 'tranzvision-framework-app-header',
    title: TranzvisionMeikecityAdvanced.Boot.getTitle(),
	userinfo:TranzvisionMeikecityAdvanced.Boot.getUserinfo(),
    height: 52,
    layout: {
        type: 'hbox',
        align: 'middle'
    },

    initComponent: function() {
        document.title = this.title;

        this.items = [{
            xtype: 'component',
            id: 'tranzvision-framework-app-header-logo'
        },{
            xtype: 'component',
            id: 'tranzvision-framework-app-header-title',
            html: this.title,
            flex: 1
        },{
            xtype: 'component',
            id: 'tranzvision-framework-app-header-user',
            html: this.userinfo,
            flex: 1
        }];

        if (!Ext.getCmp('options-toolbar')) {
            this.items.push({
                xtype: 'themeSwitcher'
            });
        }

        this.callParent();
    }
});
