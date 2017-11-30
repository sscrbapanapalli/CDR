$(function() {

  $('li.dropdown.custom-close-dropdown a').on('click', function(e) {
    $(this).parent().toggleClass('open');
  });

  $('body').on('click', function(e) {
    if (!$('li.dropdown.custom-close-dropdown').is(e.target) &&
        $('li.dropdown.custom-close-dropdown').has(e.target).length === 0 &&
        $('.open').has(e.target).length === 0 &&
        !$('body').hasClass('modal-open')) {
      $('li.dropdown.custom-close-dropdown').removeClass('open');
    }
  });



  $('.tab-active-with-border').on('show.bs.collapse', function(e) {
    var panel = $(e.target).closest('.panel');
    panel.addClass('active');
    panel.siblings().removeClass('active');
  });
})

$(document).scroll(function() {
  var scrollHeight = $(document).scrollTop(),
      headerHeight = $(window).height(),
      menuHeight = $('.profile-menu').height();

  if(scrollHeight > (headerHeight - menuHeight)) {
    $('.profile-navigation').addClass('fixed');
    $('.layout-gallery__sidebar').addClass('fixed');

  }
  else {
    $('.profile-navigation').removeClass('fixed');
    $('.layout-gallery__sidebar').removeClass('fixed');

  }
});



$(function() {
  var $minMemu = $('.profile-navigation__overflow-menu'),
      $submitMinMemu = $('.profile-navigation__overflow-icon');

  $submitMinMemu.click(function(e) {

    if ($minMemu.hasClass('active')) {
      $minMemu.slideUp(200);
      $minMemu.removeClass('active');
    }
    else {
      $minMemu.slideDown(200);
      $minMemu.addClass('active');
    }
  });
});


$(function(){
  var $masters = $('.step_1, .step_2').find('.photo_cont');
  var $mastersLink = $masters.find('a');
  $masters.on('click', 'a', function(e) {
    e.preventDefault();
    $(this).closest('.photo_cont').toggleClass('active');
  })
});


$(function(){
	$('.notification_block .close_btn').click(function() {
		$('.notification_block').hide();
	})
});

(function($) {
  $(function() {
    $('select').styler({
      selectSearch: true
    });
  });
})(jQuery);
