package com.spa.urbanstep.model.response

class FeedbackResponse {

    var project_feedback_data: ArrayList<ProjectFeedback>? = null
    var authority_feedback_data: ArrayList<AuthFeedback>? = null
    var portal_feedback_data: ArrayList<PortalFeedback>? = null

    class ProjectFeedback {
        var project_name: String? = null
        var project_description: String? = null
        var location: String? = null
        var feedback: String? = null
        var date_time: String? = null
    }

    class AuthFeedback {
        var authority_name: String? = null
        var rating: String? = null
        var feedback: String? = null
    }

    class PortalFeedback {
        var rating: String? = null
        var feedback: String? = null
    }
}