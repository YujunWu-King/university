Ext.define('Ext.toolbar.tranzvision.Breadcrumb', {
    override: 'Ext.toolbar.Breadcrumb',

    privates: {
        /**
         * Handles a click on a breadcrumb button
         * @private
         * @param {Ext.button.Split} button
         * @param {Ext.event.Event} e
         */
        _onButtonClick: function(button, e) {
            if (this.getUseSplitButtons()) {
                var me = this;
                var node = this.getStore().getNodeById(button._breadcrumbNodeId);

                this.setSelection(node);

                me.fireEvent('breadcrumbmenuclick', me, node);
            }
        },

        /**
         * Handles a click on a button menu
         * @private
         * @param {Ext.menu.Menu} menu
         * @param {Ext.menu.Item} item
         * @param {Ext.event.Event} e
         */
        _onMenuClick: function(menu, item, e) {
                var me = this;
                var node = this.getStore().getNodeById(item._breadcrumbNodeId);

                this.setSelection(node);

                me.fireEvent('breadcrumbmenuclick', me, node);
        }
    }
});

Ext.define('KitchenSink.view.navigation.Breadcrumb', {
    extend: 'Ext.toolbar.Toolbar',
    id: 'tranzvision-framework-navigation-breadcrumb',
    xtype: 'navigation-breadcrumb',

    config: {
        selection: null
    },

    initComponent: function() {
        this.items = [{
            xtype: 'tool',
            type: 'down',
            tooltip: TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00007'],
            listeners: {
                click: 'showTreeNav'
            }
        }, {
            xtype: 'breadcrumb',
            reference: 'toolbar',
            selection: this.getSelection(),
            flex: 1,
            // hide glyphs on the buttons (too busy)
            showIcons: false,
            store: 'navigation'
        },{
			xtype:"toolbar",
			style: {
	            margin: '-5px 5px -5px -5px'
	        }
		}
		];

        this.callParent();

        this._breadcrumbBar = this.items.getAt(1);
    },

    updateSelection: function(node) {
        if (this.rendered) {
            this._breadcrumbBar.setSelection(node);
        }
    },

    private_setSelection: function(node) {
        if (this.rendered) {
            this._breadcrumbBar._needsSync = true;
            this.setSelection(node);
        }
    }
});
