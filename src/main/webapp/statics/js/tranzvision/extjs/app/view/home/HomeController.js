Ext.define('KitchenSink.view.home.HomeController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.home',

    onAddComponent: function (sender) {
        var dashboard = this.lookupReference('homedashboard');
        dashboard.addView({
            type: sender.addType
        });
    },

    onLayoutSet: function(sender){
        var view = this.getView();
        var dashboard = this.lookupReference('homedashboard');
        view.remove(dashboard);

        var columnWidths=[],columnIndexs=[];
        switch(sender.layoutType){
            case 1:
                columnWidths =[1.00];
                columnIndexs=[0,0,0,0,0,0]
                break;
            case 2:
                columnWidths =[0.50,0,50];
                columnIndexs=[0,1,0,1,0,1]
                break;
            case 3:
                columnWidths =[0.33,0.33,0.34];
                columnIndexs=[0,1,2,0,1,2]
                break;
        }

        var newDashboard = {
            xtype: 'homedashboard',
            reference: 'homedashboard',
            region: 'center',
            stateful: false,
            columnWidths: columnWidths,
            defaultContent: [
			{
                type: 'basicForm',
                columnIndex: columnIndexs[0],
                height: 300
            },{
                type: 'stockTicker',
                columnIndex: columnIndexs[1],
                height: 300
            },{
                type: 'stocks',
                columnIndex: columnIndexs[2],
                height: 300
            }, {
                type: 'testGrid3',
                columnIndex: columnIndexs[3],
                height: 300
            },{
                type: 'testGrid4',
                columnIndex: columnIndexs[4],
                height: 300
            },{
                type: 'testGrid5',
                columnIndex: columnIndexs[5],
                height: 300
            },{
                type:'testGrid7',
                columnIndex:columnIndexs[6],
                height:300
                }
                ]
        };
        view.add(newDashboard);
        Ext.personalDashboardState = Ext.clone(newDashboard.getState);
    },

    onDashboardSet:function(dashboard){
    },

    /*保存用户自定义dashboard信息*/
    resizeSaveState:function(){
        var dashboard = this.getView().down('dashboard');
        var state = dashboard.getState();

        if(Ext.personalDashboardState==undefined){
            Ext.personalDashboardState = Ext.clone(state);
        }else{
            var columnWidthsSame=true,itemsSame=true;
            columnWidthsSame=Ext.personalDashboardState.columnWidths.toString()==state.columnWidths.toString();
            if(Ext.personalDashboardState.items.length==state.items.length){
                Ext.Array.each(Ext.personalDashboardState.items,function(item,index){

                    if(item.columnIndex!=state.items[index].columnIndex||item.height!=((Ext.getCmp(item.id)!=undefined)?Ext.getCmp(item.id).height:undefined)||item.type!=state.items[index].type){
                        itemsSame=false;
                        return false;
                    }
                });
            }else{
                itemsSame=false;
            }
            if(columnWidthsSame && itemsSame){
                //State is unchanged
            }else{
                Ext.personalDashboardState = Ext.clone(state);
                Ext.Array.each(Ext.personalDashboardState.items,function(item,index){
                    item.height=(Ext.getCmp(item.id).height?Ext.getCmp(item.id).height:item.height);
                })
                //Submit state
                var comParams="update:[{state:'{columnWidths:"+Ext.encode(Ext.personalDashboardState.columnWidths)+",items:"+Ext.encode(Ext.personalDashboardState.items)+"}'}]";
                var tzParams = '{"ComID":"TZ_GD_HOME_COM","PageID":"TZ_GD_HOME_STD","OperateType":"U","comParams":{'+comParams+'}}';
                Ext.tzSubmit(tzParams,function(responseData){
                });
            }
        };
    },
    loadAsync:function(params,callback)
    {
        try
        {
            //获取数据
            Ext.Ajax.request(
                {
                    url: Ext.tzGetGeneralURL,
                    async: false,
                    params:{tzParams: params},
                    success: function(response, opts)
                    {
                        //返回值内容
                        var jsonText = response.responseText;
                        try
                        {
                            var jsonObject = Ext.util.JSON.decode(jsonText);
                            /*判断服务器是否返回了正确的信息*/
                            if(jsonObject.state.errcode == 0)
                            {
                                var theFunc = eval(callback);
                                if (typeof(theFunc) == "function")
                                {
                                    theFunc(jsonObject.comContent);
                                }
                            }
                            else if(jsonObject.state.timeout == true)
                            {
                                try
                                {
                                    if(Ext.getCmp("tranzvision_relogin_20150626") == undefined)
                                    {
                                        var win = new tranzvision.view.window.ReloginWindow();
                                        win.show();
                                    }
                                }
                                catch(e2)
                                {}
                            }
                            else
                            {

                            }
                        }
                        catch(e)
                        {

                        }
                    },
                    failure: function(response, opts)
                    {
                        //错误信息响应报文
                        try
                        {
                            var respText = Ext.util.JSON.decode(response.responseText);

                        }
                        catch(e2)
                        {
                            if(response.responseText == "")
                            {

                            }
                            else
                            {

                            }
                        }
                    },
                    callback: function(opts,success,response)
                    {

                    }
                });
        }
        catch(e1)
        {

        }
    }
});
