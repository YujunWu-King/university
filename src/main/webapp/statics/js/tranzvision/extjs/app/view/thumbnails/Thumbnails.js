Ext.define('KitchenSink.view.thumbnails.Thumbnails', {
    extend: 'Ext.view.View',
    xtype: 'thumbnails',
    cls: 'thumbnails',
    reference: 'contentView',
    region: 'center',
    store: 'Thumbnails',
    itemSelector: '.thumbnail-item',

    initComponent: function() {
        var backgrounds = {
            tranzvision: ['Sencha-Examples','square'],
            maple: ['Sencha-Examples','square'],
            simple:['FontAwesome','fa-rounded-square']
        };

        this.tpl =
            '<tpl for=".">' +
                '<div class="thumbnail-item">' +
                '<div class="thumbnail-icon-wrap icon-' + backgrounds[Ext.themeName][1] +
                '" style="font-family:'+backgrounds[Ext.themeName][0]+'">' +
                '<div class="thumbnail-icon {iconCls}"></div>' +
                '</div>' +
                '<div class="thumbnail-text">{text}</div>' +
                '</div>' +
                '</tpl>';

        this.callParent();
    }
});
