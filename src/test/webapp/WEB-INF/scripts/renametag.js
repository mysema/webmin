/*
 * Bookmarks app specific JavaScript
 * Copyright 2007 Mysema Ltd.
 *  
 */

if (bo == null) var bo = {};

$(document).ready(function(){
	$('#submit').click(function(){bo._renameTag();});
}); 

/** private functions */ 
 
bo._renameTag = function(){
	var oldTag = $('#oldtag').selectedValues()[0];
	var newTags = $('#newtag').val();
	ServiceFacade.replaceTags(oldTag,newTags,function(){
		$('#oldtag').removeOption(oldTag);
		alert('tag was successfully renamed.');
	});
}