$(document).ready(function(){
    
    
    // 切换
     tab(".tabwrap .tabhead",".tabwrap .tabNote","tab_on","mousedown");
    // 弹窗

    $("#unfinish").click(function(){
       $(".Shade").show();
       $(".zxj_unfinish").show();
  
    });
   $("#recom_search").click(function(){
       $(".Shade").show();
       $(".zxj_recommend").show();
  
   });
   $(".zxj_close").click(function(){
       $(".Shade").hide();
       $(".zxj_recommend").hide();
       $(".zxj_unfinish").hide();
       
   });
  //文字超出隐藏
   
   var module = document.getElementById("date_body_view");
   var module1 = document.getElementById("date_body_view1");
   var module2 = document.getElementById("date_body_view2");
   var module3 = document.getElementById("date_body_view3");

   $clamp(module, {clamp:2});
   $clamp(module1, {clamp:2});
   $clamp(module2, {clamp:2});
   $clamp(module3, {clamp:2});
  
  //  // 轮播
  //   var mySwiper = new Swiper('.swiper-container',{
  //   pagination: '.pagination',
  //   paginationClickable: true
   
  // })
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

 

