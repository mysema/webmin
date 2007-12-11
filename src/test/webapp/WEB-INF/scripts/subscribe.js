/*
 * Bookmarks app specific JavaScript
 * Copyright 2007 Mysema Ltd.
 *  
 */

if (bo == null) var bo = {};

$(document).ready(function(){
	$('.subform .submit').click(bo._subscribe)
	
	$('.subscription a.deleteLink').each(function(){
		var id = $(this).parents('.subscription').get(0).id.substring(1);
		$(this).click(function(){bo._deleteSubscription(id);});			
	});
}); 

/* Private functions */

/**
 * 
 * @param {String} id id of the subscription to be deleted
 */
bo._deleteSubscription = function(id){
	ServiceFacade.deleteSubscription(id, function(){
		$('#s'+id).remove();	
	});	
}

/**
 * 
 */
bo._subscribe = function(){
	var tag =  $('.subform .tag').val();
	var user = $('.subform .user').val();
	ServiceFacade.subscribe(user, tag, function(id){
		bo._addNewSubscription(id, user, tag);
	});
}

/**
 * 
 * @param {String} id id of subscription
 * @param {String} user username of user
 * @param {String} tag tagName of subscription
 */
bo._addNewSubscription = function(id,user,tag){
	var toString = (user != '' ? user : "*") + "/" + (tag != '' ? tag : "*");
	if (id != -1){			
		var text = ['<tr class="subscription" id="s',id,'">',
			'<td>&nbsp;</td><td>',toString,'</td>',
		  	'<td>',
		  		'&gt; <a href="#" title="edit subscription label" class="editLabelLink">edit label</a> ',
		  		'&gt; <a href="#" title="delete this subscription" class="deleteLink">delete subscription</a>',
		  	'</td></tr>'].join('');
		$('.subscriptions').append(text);
		
		// register listeners
		$('#s'+id+' a.deleteLink').click(function(){bo._deleteSubscription(id);});	
	}else{
		alert('Subscription '+toString+' already exists');
	}
	
}
