Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.showMsTBPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'showMsTB',
    controller: 'appFormClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.chart.*',
        'Ext.Window', 
        'Ext.layout.container.Fit',
        'Ext.fx.target.Sprite', 
        'Ext.window.MessageBox',
        'Ext.grid.filters.Filters'
    ],
    bodyPadding:10,
    constructor: function (obj){
    	
       // this.classID=obj.classID;
        this.callParent();
    },
    initComponent:function(){
    	
    
    	var mystore = Ext.create('Ext.data.JsonStore', {
    	    fields: ['name', 'data1', 'data2', 'data3'],
    	    data: [
    	        { 'name': 'n1', 'data1': 10, 'data2': 12, 'data3': 14},
    	        { 'name': 'n2', 'data1': 7,  'data2': 8,  'data3': 16},
    	        { 'name': 'n3', 'data1': 5,  'data2': 2,  'data3': 14},
    	        { 'name': 'n4', 'data1': 2,  'data2': 14, 'data3': 6},
    	        { 'name': 'n5', 'data1': 4,  'data2': 4,  'data3': 36}
    	    ]
    	});  

        Ext.apply(this,{
        	collapsible:true,
        	layout: 'fit',
        	autoHeight:true,
        	height:400,
        	width: 1500,  
            items: [{
                xtype: 'chart', 
                style: 'background:#fff',
                height: 600,  
                id: 'linechart',  
                width: 1000,  
                animate: true,  
                //insetPadding: 20,  
                store: mystore,  
                shadow: true,
                theme: 'Category1',  
                layout:'fit',
                axes: [    
                    {  
                        type: 'Numeric', 
                        minimum: 0,
                        position: 'left',
                        fields:  ['data1', 'data2', 'data3'],  
                        title: '分布比率（%）',
                        //minorTickSteps: 1,
                        grid: {  
                            odd: {  
                                opacity: 1,  
                                fill: '#ddd',  
                                stroke: '#bbb',  
                                'stroke-width': 0.5  
                            }  
                        }
                    },
                    {  
                        type: 'Category',  
                        position: 'bottom',
                        fields: ['name'],  
                        title: '分布区间'
                    }
                ],  
                series:[
							{ 
							    type: 'line',  
							    highlight: {  
							        size: 7,  
							        radius: 7  
							    },  
							    axis: 'left',
							    fill: false,
							    xField: 'name',  
							    yField: 'data1',  
							    markerConfig: {  
							        type: 'circle',  
							        size: 4,  
							        radius: 4,  
							        'stroke-width': 0  
							    },  
							    smooth:true
							},{  
				                type: 'line',  
				                highlight: {  
				                    size: 7,  
				                    radius: 7  
				                },  
				                axis: 'left',
				                fill: false,
				                xField: 'name',  
				                yField: 'data2',  
				                markerConfig: {  
				                    type: 'circle',  
				                    size: 4,  
				                    radius: 4,  
				                    'stroke-width': 0  
				                },  
				                smooth:true
				            },{  
				                type: 'line',  
				                xField: 'name',  
				                yField: 'data3',
				                smooth:true
				            }
                        ],  
                legend: {  

                    position: 'top'  
                } 
            }]
        })
        this.callParent();
    },
    title: "图表",
    bodyStyle:'overflow-y:hidden;overflow-x:hidden'
});
