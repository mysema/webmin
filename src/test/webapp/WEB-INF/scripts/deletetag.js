/*
 * Bookmarks app specific JavaScript
 * Copyright 2007 Mysema Ltd.
 *  
 */

if (bo == null) var bo = {};

$(document).ready(function(){
	$('#submit').click(function(){bo._deleteTag();});
}); 

/** private functions */

bo._deleteTag = function(){ 
	var oldTag = $('#oldtag').selectedValues()[0];
	ServiceFacade.deleteTag(oldTag,function(){ 
		$('#oldtag').removeOption(oldTag);
		alert('tag was deleted successfully'); 
	});
}
