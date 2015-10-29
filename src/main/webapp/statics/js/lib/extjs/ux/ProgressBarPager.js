/**
 * Plugin for displaying a progressbar inside of a paging toolbar
 * instead of plain text.
 */
Ext.define('Ext.ux.ProgressBarPager', {

    requires: ['Ext.ProgressBar'],
    /**
     * @cfg {Number} width
     * <p>The default progress bar width.  Default is 225.</p>
     */
    width   : 225,
    /**
     * @cfg {String} defaultText
     * <p>The text to display while the store is loading.  Default is 'Loading...'</p>
     */
    defaultText    : 'Loading...',
    /**
     * @cfg {Object} defaultAnimCfg
     * <p>A {@link Ext.fx.Anim Ext.fx.Anim} configuration object.</p>
     */
    defaultAnimCfg : {
        duration: 1000,
        easing: 'bounceOut'
    },

    /**
     * Creates new ProgressBarPager.
     * @param {Object} config Configuration options
     */
    constructor : function(config) {
        if (config) {
            Ext.apply(this, config);
        }
    },
    //public
    init : function (parent) {
        /*add the item of number after filter */
        if(parent.findParentByType('grid').findPlugin('gridfilters')){
            parent.filterNumMsg = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00079");
            var clearFiltersText = TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00080");
            var filterNumItem = {
                    xtype: 'tbtext',
                    itemId: 'filterNumItem',
                    baseCls:Ext.baseCSSPrefix+'panel-header-title-light',
                    style:'margin-left:15px',
                    hidden:true
                },
                tbseparator = {
                    xtype : 'tbseparator',
                    itemId: 'filterSeparator',
                    hidden:true
                },
                clearFilters = {
                    xtype:'button',
                    itemId: 'clearFilters',
                    tooltip:clearFiltersText,
                    text:'<span class="themeColor">'+clearFiltersText+'</span>',
                    handler:this.clearFilters,
                    hidden:true
                }
            parent.insert(11,filterNumItem);
            parent.insert(12,tbseparator);
            parent.insert(13,clearFilters);
            parent.store.addListener('load',function( store, records, successful, eOpts){
                this.toggleFilterNum(store)
            },this);
            parent.store.addListener('filterchange',function(store, filters, eOpts){
                this.toggleFilterNum(store)
            },this);
        }

        var displayItem;
        if (parent.displayInfo) {
            this.parent = parent;

            displayItem = parent.child("#displayItem");
            if (displayItem) {
                parent.remove(displayItem, true);
            }

            this.progressBar = Ext.create('Ext.ProgressBar', {
                text    : this.defaultText,
                width   : this.width,
                animate : this.defaultAnimCfg,
                style: {
                    cursor: 'pointer'
                },
                listeners: {
                    el: {
                        scope: this,
                        click: this.handleProgressBarClick
                    }
                }
            });

            parent.displayItem = this.progressBar;

            parent.add(parent.displayItem);
            Ext.apply(parent, this.parentOverrides);
        }
    },
    // private
    // This method handles the click for the progress bar
    handleProgressBarClick : function(e){
        var parent = this.parent,
            displayItem = parent.displayItem,
            box = this.progressBar.getBox(),
            xy = e.getXY(),
            position = xy[0]- box.x,
            pages = Math.ceil(parent.store.getTotalCount() / parent.pageSize),
            newPage = Math.max(Math.ceil(position / (displayItem.width / pages)), 1);

        parent.store.loadPage(newPage);
    },

    // private, overriddes
    parentOverrides  : {
        // private
        // This method updates the information via the progress bar.
        updateInfo : function(){
            if(this.displayItem){
                var count = this.store.getCount(),
                    pageData = this.getPageData(),
                    message = count === 0 ?
                        this.emptyMsg :
                        Ext.String.format(
                            this.displayMsg,
                            pageData.fromRecord, pageData.toRecord, this.store.getTotalCount()
                        ),
                    percentage = pageData.pageCount > 0 ? (pageData.currentPage / pageData.pageCount) : 0;

                this.displayItem.updateProgress(percentage, message, this.animate || this.defaultAnimConfig);
            }
        }
    },
    toggleFilterNum:function(store){
        var parent = this.parent;
        if(store.filters.length>0){
            parent.child('#filterNumItem').setVisible(true);
            parent.child('#filterSeparator').setVisible(true);
            parent.child('#clearFilters').setVisible(true);
            parent.child('#filterNumItem').setText("<span style='padding:0 4px;'>"+Ext.String.format(parent.filterNumMsg, store.getCount())+"</span>");
        }else{
            parent.child('#filterNumItem').setVisible(false);
            parent.child('#filterSeparator').setVisible(false);
            parent.child('#clearFilters').setVisible(false);
        }
    },
    clearFilters:function(){
        this.findParentByType('grid').filters.clearFilters();
        var store =  this.findParentByType('grid').getStore();
       if(store&&store.isFiltered()){
           store.clearFilter(true);
           store.reload();
       }
    }
});

