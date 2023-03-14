/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var offset = 250;
var duration = 300;
$(window).scroll(function () {
    if ($(this).scrollTop() > offset) {
        $(".back-to-top").fadeIn(duration);
    } else {
        $(".back-to-top").fadeOut(duration);
    }
});
$(".back-to-top").click(function (event) {
    event.preventDefault();
    $("html, body").animate({scrollTop: 0}, duration);
    return false;
});

