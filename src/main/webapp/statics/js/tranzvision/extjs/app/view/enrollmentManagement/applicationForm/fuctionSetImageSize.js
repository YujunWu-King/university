Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.fuctionSetImageSite', {
    extend: 'Ext.panel.Panel',
    xtype: 'fuctionSetImageSite',

    setImageSize:function(eml,t){
        var $aBtn = $(eml);
        var $boxInier = $aBtn.closest(".photoButton").prev(".fancybox-outer").children(".fancybox-inner");
        var $boxWrap = $aBtn.closest(".fancybox-wrap");
        var w = parseInt($boxInier.css("width"));
        var h = parseInt($boxInier.css("height"));

        var width,height;
        if(t == "S"){
            width = w * 0.9;
            height = h * 0.9;
        } else if(t == "B"){
            width = w * 1.1;
            height = h * 1.1;
        }
        //浏览器窗口宽度
        if (window.innerWidth){
            winWidth = window.innerWidth;
        }else if ((document.body) && (document.body.clientWidth)){
            winWidth = document.body.clientWidth;
        }

        var $oBtn = $aBtn.siblings("a");

        if (width >= winWidth){
            $aBtn.removeClass("big");
            $aBtn.addClass("big-enabledClick");
        }else if(width <= 150){
            $aBtn.removeClass("small");
            $aBtn.addClass("small-enabledClick");

        }else{
            if ($oBtn.hasClass("small-enabledClick")){
                $oBtn.removeClass("small-enabledClick");
                $oBtn.addClass("small");
            }
            if ($oBtn.hasClass("big-enabledClick")){
                $oBtn.removeClass("big-enabledClick");
                $oBtn.addClass("big");
            }

            var wrapWidth = width+20;
            var left = winWidth/2-wrapWidth/2;

            $boxInier.css('width',width+'px');
            $boxInier.css('height',height+'px');

            $boxWrap.css('width',wrapWidth+'px');
            $boxWrap.css('left',left)
        }
    }
});