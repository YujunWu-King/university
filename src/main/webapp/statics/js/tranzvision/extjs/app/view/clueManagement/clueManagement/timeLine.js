/*
 @class:timePoint extend Ext.container.Container
 property:{
 lineFlag  ==>   标识当前linePoint是佛需要显示线条
 content   ==>   右侧显示的HTML文本内容,可以通过为一个元素记入“data-事件名=函数名”的方式注册事件，程序会自动到
 当前timePoint的父timeLine对象的controller中调用同名函数
 sinceText ==>   左侧信息栏中的HTML文本内容
 imgSrc    ==>   左侧信息栏中的图片路劲
 tagFlag   ==>   左侧信息栏是否只显示文本，如果是文本会按照特定样式展示
 autoLine  ==>   是否自动添加线条，如是则会在timeLine有所修改完成尝试修改最后一个timePoint为无线条，之前的最后一个如果不再是左后一个，则添加线条
 imgListenerString ==> 用于为左侧图片添加事件处理,都是用“data-事件名=方法名”的形式
 textListenerString ==> 用于为左侧文字描述部分添加事件处理，都是用“data-事件名=方法名”的形式
 }
 */
Ext.define("KitchenSink.view.clueManagement.clueManagement.timePoint",{
    extend:"Ext.container.Container",
    xtype:"timePoint",
    lineFlag:true,
    initComponent:function(){
        var rightSide = typeof this.config.content === 'string'?{
            xtype:"container",
            cls:this.config.lineFlag===false?"time-line-end-"+this.findParentByType("timeLine").ram:"time-line-warp-"+this.findParentByType("timeLine").ram,
            //this.config.lineFlag?"time-line-warp":"time-line-end",
            layout:'container',
            items:[{
                xtype:'form',
                html:this.config.content
            }]
        }:{
            xtype:"container",
            cls:this.config.lineFlag===false?"time-line-end-"+this.findParentByType("timeLine").ram:"time-line-warp-"+this.findParentByType("timeLine").ram,
            //this.config.lineFlag?"time-line-warp":"time-line-end",
            layout:'container',
            items:this.config.content
        };
        Ext.apply(this,{
            items:[{
                xtype:"container",
                width:150,
                style:"float:left;text-align:center;z-index:44;font-size:10px",
//                html:this.config.imgSrc?'<img '+(this.config.imgListenerString||"")+' style="border-radius:50%" width="56" height="56" src="'+this.config.imgSrc+'"><div '+(this.config.textListenerString||"")+' style="color:#656565">'+this.config.sinceText+'</div>'
//                    :this.config.tagFlag?'<div '+(this.config.textListenerString||"")+' style="width:100px;text-align:center;border:solid 3px #35baf6;color:#35baf6;padding:7px 5px;margin-bottom:15px;font-size:14px;margin-left:-8px;background-color:#fff;margin-top:0px">'+this.config.sinceText+'</div>'
//                    :'<div '+(this.config.textListenerString||"")+' style="margin-top:47px;font-color:#656565">'+this.config.sinceText+'</div>'
                html: '<div '+(this.config.textListenerString||"")+' style="margin-top:12px;font-color:#656565">'+this.config.sinceText+'</div>'
            },this.config.tagFlag?{
                xtype:"container",
                cls:this.config.lineFlag===false?"nocontent-time-line-end-"+this.findParentByType("timeLine").ram:"nocontent-time-line-warp-"+this.findParentByType("timeLine").ram
            }:rightSide]
        });
        this.callParent();
    },
    style:"width:100%",
    padding: 0

});

/*
 @class:timeLine extend Ext.container.Container
 */
Ext.define("KitchenSink.view.clueManagement.clueManagement.timeLine",{
    extend:"Ext.container.Container",
    xtype:"timeLine",
    bodyStyle:"overflow-x:hidden;overflow-y:scroll",
    style:"border:1px solid #ccc;box-shadow:0 1px 2px 0 rgba(0, 0, 0, 0.2);background-color:#fff",
    padding:'15px 15px 15px 0px',
    /*
    *overwrite constructor
    * 默认加载后添加css样式
    */

    constructor:function(config){
        //为新添加的样式加入随机后缀，避免与系统样式重复
        var ram = Math.random()*1e9>>>0;
        (config.items||(config.items=[])).unshift({xtype:"container",
            hidden:true,
            name:'lzh_css_insert_hidden',
            html:'<style>.time-line-warp-'+ram+':before{content:"";position:absolute;width:75px;height:70%;border-right:solid 2px #35baf6;left:0;z-index: 0;top:38%;}.nocontent-time-line-warp-'+ram+':before{content:"";position:absolute;width:41px;height:90%;border-right:solid 2px #35baf6;left:0;z-index: 0;top:10%;}.time-line-warp-'+ram+'>div{margin:0 0 15px 104px;border:1px solid #ddd;position:static;padding:15px;display: block;width:auto !important;}.time-line-end-'+ram+'>div{margin:0 0 12px 104px;border:1px solid #ddd;position:static;padding:15px;display: block;width:auto !important;}.time-line-warp-'+ram+'>div:before{content:"";display:block;position:absolute;width:0;height:0;left:0;top:0;border-top:9px solid transparent;border-bottom:9px solid transparent;border-right:9px solid #ddd;margin:15px 0 0 142px;}.time-line-end-'+ram+'>div:before{content:"";display:block;position:absolute;width:0;height:0;left:0;top:0;border-top:9px solid transparent;border-bottom:9px solid transparent;border-right:9px solid #ddd;margin:15px 0 0 142px;}.time-line-warp-'+ram+'>div:after{content:"";display:block;position:absolute;width:0;height:0;left:0;top:0;border-top:9px solid transparent;border-bottom:9px solid transparent;border-right:9px solid #fff;margin:15px 0 0 143px;}.time-line-end-'+ram+'>div:after{content:"";display:block;position:absolute;width:0;height:0;left:0;top:0;border-top:9px solid transparent;border-bottom:9px solid transparent;border-right:9px solid #fff;margin:15px 0 0 143px;}.time-line-warp-'+ram+' div img {width:100%;display:block;}.time-line-end-'+ram+' div img {width:100%;display:block;}</style>'
        });
        config.ram = ram;
        Ext.apply(this,config);
        this.callParent();
    },

    /*@overwrite add
     *参数：config(Ext.Component/Object/Ext.Component[]/Object[])
     *功能：在add前判断是否需要自动配置线条并配置
     */
    add:function(config){
        var isFormInsert = typeof config === "number"?config:false;
        //由于extjs container的insert方法会调用add，在处理参数时需检查config是否是数字（insert的第一个参数是数字）
        if(typeof config !== "number" || config === this.items.items.length){
            //将添加前处于最后位置的timepoint设置为显示时间线条
            var item = this.items.items[this.items.items.length-1];
            if(item&&item.autoLine){
                var dom = item.el.dom.querySelector(".time-line-end-"+this.ram)||item.el.dom.querySelector("nocontent-time-line-end-"+this.ram);
                dom&&(dom.className = dom.className.replace(/time-line-end/,"time-line-warp"));
            }
        }
        if(!(config instanceof Array)){
            if(typeof config === "number"){
                //数字表示是insert方法调用的add
                config = Array.prototype.slice.call(arguments[1]);
            }else{
                config = Array.prototype.slice.call(arguments);
            }
        }
        var last = config.length-1;
        if(typeof isFormInsert !=="number"){
            //直接调用的add
            if(config[last].autoLine){
                config[last].lineFlag=false;
            }
            this.callParent(config);
        }else{
            //insert调用的add
            if(isFormInsert === this.items.items.length && config[last].autoLine){
                config[last].lineFlag=false;
            }
            this.callParent([isFormInsert,config]);

        }

    },
    /*@overwrite remove
     *参数：componoent(Ext.Component/String)，[autoDestroy](boolean)
     *功能：在remove后判断是否需要自动配置线条并配置
     */
    remove:function(componoent,autoDestroy){
        //不会删除添加样式的container
        if(componoent.name !== "lzh_css_insert_hidden") {
            this.callParent([componoent, autoDestroy]);
            //将删除后处于最后位置的timepoint设置为不显示时间线条
            var item = this.items.items[this.items.items.length - 1];
            if (item && item.autoLine) {
                var dom = item.el.dom.querySelector(".time-line-warp-" + this.ram) || item.el.dom.querySelector(".nocontent-time-line-warp-" + this.ram);
                dom && (dom.className = dom.className.replace(/time-line-warp/, "time-line-end"));
            }
        }
    },
    /*@overwrite insert
     *参数：num(Number),componoent(Ext.Component/Object/Ext.Component[]/Object[])
     *功能：在insert前判断是否需要自动配置线条并配置
     */
    /*insert 会调用add，在add中统一处理
     insert:function(num,componoent){
     if(num === this.items.items.length-1){
     //新增到最后一列才做处理
     this.add(componoent);
     }else{
     this.callParent([num,componoent]);
     }
     },
     */
    listeners:{
        /**
         *在渲染结束后为容器添加事件监听，所有子容器中所有元素的事件都会调用timeline controller的对应方法。
         *仅支持原生的事件，timepoint右侧HTML内容去添加事件的格式为为”data-事件名=方法名“,会调用controller中对应的方法名
         */
        afterrender:function(componoent){
            var dom = componoent.el.dom;
            for (var x in dom){
                if(x.match(/^on[\s\S]+$/)){
                    (function(x){
                        //添加事件监听
                        dom[x] = function(e){
                            var eventName = x.match(/^on([\s\S]+)$/)[1],
                            //在span或a标签下，e.target指向标签内部的文字，而不是对应的DOM元素
                                target = e.target instanceof HTMLElement?e.target:e.target.parentNode,
                                funcName = target.getAttribute('data-'+eventName);
                            if(funcName){
                                componoent.controller[funcName](e,componoent);
                            }
                        }
                    })(x);
                }
            }
        }
    },

})
