package com.spa.urbanstep.model.response

class ListItem {
    var id: Int? = null
    var name: String? = null
    var is_form: Int? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0

    constructor()

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    constructor(name: String, lat: Double, long: Double) {
        this.name = name
        this.lat = lat
        this.long = long
    }
}