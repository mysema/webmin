/*
 * Bookmarks app specific JavaScript
 * Copyright 2007 Mysema Ltd.
 *  
 */

if (bo == null) var bo = {};

$(document).ready(function(){
// create delete handlers 
	$('.bookmark a.deleteLink').each(function(){  
		var id = $(this).parents('.bookmark').get(0).id.substring(1);
		$(this).click(function(){bo._deleteBookmark(id);});			
	});
	 
	// create edit link handlers 
	$('.bookmark a.editLink').each(function(){ 
		var id = $(this).parents('.bookmark').get(0).id.substring(1);
		$(this).click(function(){bo._editBookmark(id);});
	});
	
	// toggle links
	var toggleble = $('.toggleble');
	$('.toggleble a.toggle').click(function(){ toggleble.toggle(); });
});

/* Private functions */

/**
 * Display a confirmation dialog for the deletion of the given bookmark
 * 
 * @param {String} id id of the bookmark
 */
bo._deleteBookmark = function(id){
	var vid = '#v'+id;		
	
	// callbacks
	var closeCB = function(){$(vid).remove();};			
	var noCB = function(){  
		$(vid+' .confirm').remove(); $(vid+' .actions').show();
	};		
	var yesCB = function(){ServiceFacade.deleteBookmark(id, closeCB);};	
	
	// DOM manipulation
	$(vid+' .actions').after(['<span class="confirm">',
			'<span class="red">delete this post? </span>',
			'<a href="#">no</a> / <a href="#">yes</a>',
		'</span>'].join('')).hide();
	$(vid+' .confirm a:first').click(noCB);
	$(vid+' .confirm a:last').click(yesCB);
};	

/* Bookmark edit */

/**
 * Create an edit dialog for editing the properties of the given bookmark
 * 
 * @param {String} id id of the bookmark
 */
bo._editBookmark = function(id){
	var eid = '#e'+id; var vid = '#v'+id;
	// get data from view
	var b = {id: id, title: $(vid+' .bm').text(), 
		address: $(vid+' .bm').attr('href'),
		description: $(vid+' .description').text(),
		tagsAsString: $(vid +' .tagsAsString').text()
	};
	
	// callbacks
	var saveCB = function(){bo._saveBookmark(id);};
	var cancelCB = function(){bo._removeEdit(vid,eid);}
						
	$(vid).after(['<div class="edit" id="e',id,'">',
			bo._editSection('title', 'title', b.title),
			bo._editSection('address', 'address', b.address),
			bo._editSection('description', 'description',  b.description),
			bo._editSection('tags', 'tagsAsString', b.tagsAsString),
			'<label>&nbsp;</label><button>save</button><button>cancel</save>',
		'</div>'].join('')).hide();
	$(eid+' button:first').click(saveCB);
	$(eid+' button:last').click(cancelCB);				 
}

/**
 * 
 * @param {String} label
 * @param {String} cl
 * @param {String} value
 */
bo._editSection = function(label,cl,value){
	return ['<div>','<label for="',label,'">',label,'</label>',
		'<input class="',cl,'" type="text" value="',value,'">',
	'</div>'].join('');
};

/**
 * Remove $(eid) via slide up and show $(vid) 
 * 
 * @param {String} vid selector for view section
 * @param {String} eid selector for edit section
 */
bo._removeEdit = function(vid,eid){
	// slide up eid and remove it
	$(eid).slideUp('fast', function(){$(eid).remove(); $(vid).show();});
}

/**
 * Save the given bookmark
 * 
 * @param {String} id id of bookmark
 */
bo._saveBookmark = function(id) {
	var eid = '#e'+id; var vid = '#v'+id;
	// get data from edit
	var b = {id: id, title: $(eid+' .title').val(),
		address: $(eid+' .address').val(),
		description: $(eid+' .description').val(),
		tagsAsString: $(eid+' .tagsAsString').val()
	};	
	
	// callbacks
	var successCB = function() { 
		bo._updateViewAfterSave(b,vid);
		bo._removeEdit(vid,eid);
	};
		
	// send data to server	
	ServiceFacade.updateBookmark(b, successCB); 
}

/**
 * Update the bookmark elements in the view after saving it
 * 
 * @param {Object} b properties of bookmark
 * @param {String} vid selector for view section
 */
bo._updateViewAfterSave = function(b,vid){
	// update view
	$(vid+' .bm').text(b.title); 
	$(vid+' .bm').attr('href', b.address);
	$(vid+' .description').text(b.description);	
	$(vid+' .tagsAsString').text(b.tagsAsString);
	
	// remove the old tag links
	var tagsEl = $(vid+' .tags').empty().get(0);
	
	// template for link creation
	var template = $(vid+' .tagTemplate').html();
	var tags = b.tagsAsString.split(' ');
	
	// create new tag links 		
	for (var i=0; i < tags.length; i++){
		var addr = template.replace(/XXX/, tags[i]);
		$(tagsEl).append(['<a href="',addr,'">',tags[i],'</a>'].join('')).append(' ');
	};
}