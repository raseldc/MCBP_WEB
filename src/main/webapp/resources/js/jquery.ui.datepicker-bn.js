/* Bangla initialisation for the jQuery UI date picker plugin. */
/* Based on the en-BD initialisation. */
( function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define( [ "../widgets/datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}( function( datepicker ) {

datepicker.regional[ "bn-BD" ] = {
	closeText: "Done",
	prevText: "পূর্ববর্তী",
	nextText: "পরবর্তী",
	currentText: "আজ",
	monthNames: [ "জানুয়ারী","ফেব্রুয়ারী","মার্চ","এপ্রিল","মে","জুন",
	"জুলাই","আগস্ট","সেপ্টেম্বর","অক্টোবর","নভেম্বর","ডিসেম্বর" ],
	monthNamesShort: [ "জানুয়ারী","ফেব্রুয়ারী","মার্চ","এপ্রিল","মে","জুন",
	"জুলাই","আগস্ট","সেপ্টেম্বর","অক্টোবর","নভেম্বর","ডিসেম্বর" ],
	dayNames: [ "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার" ],
	dayNamesShort: [ "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি" ],
	dayNamesMin: [ "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি" ],
	weekHeader: "Wk",
	dateFormat: "dd/mm/yy",
	firstDay: 7,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: "" };
//datepicker.setDefaults( datepicker.regional[ "bn-BD" ] );

return datepicker.regional[ "bn-BD" ];

} ) );