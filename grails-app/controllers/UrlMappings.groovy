/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}