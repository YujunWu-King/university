Ext.define('KitchenSink.view.security.menu.menuTreeStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.menuTreeStore',
    rootVisible: true,
    constructor: function(config) {
    	
        var me = this;
        var items;
        var root;
        var tzParams ='{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"TREE","menuId":"'+ config.menuId+'","dataLanguage":"' + config.dataLanguage + '"}}';
	      Ext.tzLoadAsync(tzParams,function(responseData){
            root = responseData.root;
        });

				items = root.children;

        me.callParent([Ext.apply({
            root: {
                text: root.text,
                nodeId: root.nodeId,
                id: root.nodeId,
                menuYxState: root.menuYxState,
                comId: root.comId,
								bigImgId: root.bigImgId,
								smallImgId: root.smallImgId,
								helpId: root.helpId,
								NodeType: root.NodeType,
								operateNode: root.operateNode,
								rootNode: config.menuId,
								comName: root.comName,
                expanded: root.expanded,
                children: me.getChartNavItems(items)
                //children: charts ? me.getChartNavItems() : me.getNavItems()
            }
        }, config)]);
    },

    addIconClasses: function (items) {
    	  
        for (var item, i = items.length; i-- > 0; ) {
            item = items[i];

            if (!('iconCls' in item)) {
                item.iconCls = 'icon-' + item.largeIcon;
            }

            if (!('glyph' in item)) {
                // sets the font-family
                item.glyph = '32@Sencha-Examples';
            }

            if (item.children) {
                this.addIconClasses(item.children);
            }
        }

        return items;
    },

    getChartNavItems: function(items)
    {	
    	return this.addIconClasses(items); 
    	
       // return this.addIconClasses(TranzvisionMeikecityAdvanced.Boot.mainMenu);
        /*var combinationExamples,
            items = this.addIconClasses([
            {
                text: 'Column Charts',
                id: 'column-charts',
                expanded: true,
                description: 'Column charts provide a visual comparison of numbers or frequency against different discrete ' +
                             'categories or groups. These charts display vertical bars to represent information in a way + ' +
                             'that allows for quick generalizations regarding your data.',
                children: [
                    { id: 'column-basic', text: 'Basic', leaf: true },
                    { id: 'column-stacked', text: 'Stacked', leaf: true },
                    { id: 'column-stacked-100', text: '100% Stacked', leaf: true },
                    { id: 'column-renderer', text: 'With Renderer', leaf: true },
                    { id: 'column-multi-axis', text: 'Multiaxis', leaf: true }
                ]
            },
            {
                text: '3D Column Charts',
                id: 'column-charts-3d',
                expanded: true,
                description: '3D Column charts provide a visual comparison of numbers or frequency against different discrete ' +
                             'categories or groups. These charts display vertical bars to represent information in a way + ' +
                             'that allows for quick generalizations regarding your data.',
                children: [
                    { id: 'column-basic-3d', text: 'Basic', leaf: true },
                    { id: 'column-grouped-3d', text: 'Grouped', leaf: true },
                    { id: 'column-stacked-3d', text: 'Stacked', leaf: true },
                    { id: 'column-stacked-100-3d', text: '100% Stacked', leaf: true },
                    { id: 'column-negative-3d', text: 'Negative values', leaf: true },
                    { id: 'column-renderer-3d', text: 'With Renderer', leaf: true }
                ]
            },
            {
                text: 'Bar Charts',
                id: 'bar-charts',
                expanded: true,
                description: 'Bar charts provide a visual comparison of numbers or frequency against different discrete ' +
                             'categories or groups. These charts display horizontal bars to represent information in a way + ' +
                             'that allows for quick generalizations regarding your data.',
                children: [
                    { id: 'bar-basic', text: 'Basic', leaf: true },
                    { id: 'bar-stacked', text: 'Stacked', leaf: true },
                    { id: 'bar-stacked-100', text: '100% Stacked', leaf: true }
                ]
            },
            {
                text: '3D Bar Charts',
                id: 'bar-charts-3d',
                expanded: true,
                description: '3D Bar charts provide a visual comparison of numbers or frequency against different discrete ' +
                             'categories or groups. These charts display horizontal bars to represent information in a way + ' +
                             'that allows for quick generalizations regarding your data.',
                children: [
                    { id: 'bar-basic-3d', text: 'Basic', leaf: true },
                    { id: 'bar-stacked-3d', text: 'Stacked', leaf: true },
                    { id: 'bar-stacked-100-3d', text: '100% Stacked', leaf: true },
                    { id: 'bar-negative-3d', text: 'Negative values', leaf: true }
                ]
            },
            {
                text: 'Line Charts',
                id: 'line-charts',
                expanded: true,
                description: 'Line charts display information as a series of markers that are connected by lines.' +
                             'These charts are excellent for showing underlying patterns between data points.',
                children: [
                    { id: 'line-basic', text: 'Basic', leaf: true },
                    { id: 'line-marked', text: 'Basic + Markers', leaf: true },
                    { id: 'line-spline', text: 'Spline', leaf: true },
                    { id: 'line-marked-spline', text: 'Spline + Markers', leaf: true },
                    { id: 'line-plot', text: 'Plot', leaf: true },
                    { id: 'line-markers', text: 'With Image Markers', leaf: true },
                    { id: 'line-crosszoom', text: 'With Zoom', leaf: true },
                    { id: 'line-renderer', text: 'With Renderer', leaf: true }
                ]
            },
            {
                text: 'Area Charts',
                id: 'area-charts',
                expanded: true,
                description: 'Area charts display data by differentiating the area between lines. They are often ' +
                             'used to measure trends by representing totals over time.',
                children: [
                    { id: 'area-basic', text: 'Basic', leaf: true },
                    { id: 'area-stacked', text: 'Stacked', leaf: true },
                    { id: 'area-stacked-100', text: '100% Stacked', leaf: true },
                    { id: 'area-negative', text: 'Negative Values', leaf: true }
                ]
            },
            {
                text: 'Scatter Charts',
                id: 'scatter-charts',
                expanded: true,
                description: 'Scatter charts are diagrams that are used to display data as a collection of points.' +
                             'They are perfect for showing multiple measurements to aid in finding correlation ' +
                             'between variables.',
                children: [
                    { id: 'scatter-basic', text: 'Basic', leaf: true },
                    { id: 'scatter-custom-icons', text: 'Custom Icons', leaf: true },
                    { id: 'scatter-bubble', text: 'Bubble', leaf: true }
                ]
            },
            {
                text: 'Financial Charts',
                id: 'financial-charts',
                expanded: true,
                description : 'Financial charts provide a simple method for showing the change in price over time. ' +
                              'A quick look at these charts provides information regarding financial highs, lows, ' +
                              'opens, and closes.',
                children: [
                    { id: 'financial-candlestick', text: 'Candlestick', leaf: true },
                    { id: 'financial-ohlc', text: 'OHLC', leaf: true }
                ]
            },
            {
                text: 'Pie Charts',
                id: 'pie-charts',
                expanded: true,
                description: 'Pie charts show sectors of data proportional to the whole. They are excellent for ' +
                             'providing a quick and simple comparison of a category to the whole.',
                children: [
                    { id: 'pie-basic', text: 'Basic', leaf: true },
                    { id: 'pie-custom', text: 'Spie', leaf: true },
                    { id: 'pie-donut', text: 'Donut', leaf: true },
                    { id: 'pie-3d', text: '3D Pie', leaf: true }
                ]
            },
            {
                text: 'Radial Charts',
                id: 'radial-charts',
                expanded: true,
                description: 'Radial charts offer a flat view of data involving multiple variable quantities. They are ' +
                             'generally used to show performance metrics because they easily emphasize strengths and ' +
                             'weaknesses from a simple two-dimensional perspective.',
                children: [
                    { id: 'radial-basic', text: 'Basic', leaf: true },
                    { id: 'radial-filled', text: 'Filled', leaf: true },
                    { id: 'radial-marked', text: 'Marked', leaf: true },
                    { id: 'radial-multi-axis', text: 'Multiaxis', leaf: true }
                ]
            },
            {
                text: 'Gauge Charts',
                id: 'guage-charts',
                expanded: true,
                description: 'Gauge charts contain a single value axis that provides simple visualization for dashboards.' +
                             'They are generally used to show the current status or heartbeat with a single point of data.',
                children: [
                    { id: 'gauge-basic', text: 'Basic', leaf: true }
                ]
            },
            {
                text: 'Combination Charts',
                id: 'combination-charts',
                expanded: true,
                description: 'Sencha Charts gives you the ability to easily join several chart types into one chart. ' +
                             'This gives developers the ability to show multiple series in a single view.',
                children: combinationExamples = [
                    { id: 'combination-pareto', text: 'Pareto', leaf: true },
                    { id: 'combination-dashboard', text: 'Interactive Dashboard', leaf: true },
                    { id: 'unemployment', text: 'Infographic', leaf: true },
                    { id: 'combination-theme', text: 'Custom Theme', leaf: true },
                    { id: 'combination-bindingtabs', text: 'Binding & Tabs', leaf: true}
                ]
            },
            {
                text: 'Drawing',
                id: 'drawing',
                expanded: true,
                description: 'The Sencha Draw package allows developers to create cross-browser compatible and mobile ' +
                             'friendly graphics, text, and shapes. You can even create a standalone drawing tool!',
                children: [
                    { id: 'free-paint', text: 'Free Paint', leaf: true },
                    { id: 'hit-test', text: 'Hit Testing', leaf: true },
                    { id: 'intersections', text: 'Path Intersections', leaf: true },
                    { id: 'draw-composite', text: 'Composite', leaf: true }
                ]
            }
        ]);
        if (Ext.isiOS || Ext.isIE8) {
            combinationExamples.splice(2, 1);
        }
        return items;*/
    },

/*
    getNavItems: function()
    {
       return this.addIconClasses(TranzvisionMeikecityAdvanced.Boot.mainMenu);
    }
    */
});
