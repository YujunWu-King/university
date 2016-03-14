Ext.define('KitchenSink.view.main.Main', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border',
        'KitchenSink.view.main.MainController',
        'KitchenSink.view.main.MainModel',
        'KitchenSink.view.Header',
        'KitchenSink.view.ThemeSwitcher',
        'KitchenSink.view.ContentPanel',
        'KitchenSink.view.navigation.Breadcrumb',
        'KitchenSink.view.CodePreview'
    ],

    controller: 'main',
    viewModel: 'main',

    layout: 'border',
    stateful: true,
    stateId: 'tranzvision-framework-kitchensink-viewport',

    items: [{
        region: 'north',
        xtype: 'appHeader'
    }, {
        region: 'center',
        xtype: 'contentPanel',
        reference: 'contentPanel',
        dockedItems: [{
            xtype: 'navigation-breadcrumb',
            reference: 'breadcrumb>'
        }]
    }, {
        xtype: 'codePreview',
        region: 'east',
        id: 'tranzvision-framework-east-region',
        itemId: 'tranzvision-framework-codePreview',
        stateful: true,
        stateId: 'tranzvision-framework-mainnav.east',
        split: true,
        collapsible: true,
        collapsed: true,
        width: 350,
        minWidth: 100,
		hidden: true
    }],

    applyState: function(state) {
        this.getController().applyState(state);

    },

    getState: function() {
        return this.getController().getState();
    }
});
