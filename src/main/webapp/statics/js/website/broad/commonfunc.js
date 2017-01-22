function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
}

function getOperatorType(){
	
	var thisUrl = window.location.pathname;
	if(thisUrl.indexOf("/decorate/")>=0){
		return "D";
	}else if(thisUrl.indexOf("/preview/")>=0){
		return "P";
	}else{
		return "R";
	}
	
}

    //copy from zxj.js start
    $(document).ready(function(){
        
        
        // 切换
         tab(".tabwrap .tabhead",".tabwrap .tabNote","tab_on","mousedown");
         tab(".zxj_mba .step_head",".zxj_mba .step_Note","","mousedown");
        // 弹窗

        $(".sq_btn").click(function(){
           $(".Shade").show();
           $(".sq_pop").show();
      
        });
       // $("#recom_search").click(function(){
       //     $(".Shade").show();
       //     $(".zxj_recommend").show();
      
       // });
       // $(".zxj_close").click(function(){
       //     $(".Shade").hide();
       //     $(".zxj_recommend").hide();
       //     $(".zxj_unfinish").hide();
           
       // });
      //文字超出隐藏
       
       var module = document.getElementById("date_body_view");
       var module1 = document.getElementById("date_body_view1");
       var module2 = document.getElementById("date_body_view2");
       var module3 = document.getElementById("date_body_view3");

       $clamp(module, {clamp:2});
       $clamp(module1, {clamp:2});
       $clamp(module2, {clamp:2});
       $clamp(module3, {clamp:2});
      


    });

      function tab(nav,content,on,type)
          {
            $(nav).children().bind(type,(function(){
           
              var $tab=$(this);
              var tab_index=$tab.prevAll().length;
              var $content = $(content).children();
              $(nav).children().removeClass(on);
              $tab.addClass(on);
              $content.hide();
              $content.eq(tab_index).show();
            }));
          }
        var up;
        function openUpload(){
            up = $.layer({
                type: 2,
                title: false,
                fix: false,
                closeBtn: 2,
                shadeClose: false,
                shade : [0.3 , '#000' , true],
                border : [3 , 0.3 , '#000', true],
                offset: ['50%',''],
                area: ['840px','610px'],
                iframe: {src:'upload_photo.html'}
            });
        }

        function closeTheIFrameImDone()
        {
            layer.close(i);

            // Now crack on with whatever
        }
        function closeM()
        {
            layer.close(m);

            // Now crack on with whatever
        }
        

