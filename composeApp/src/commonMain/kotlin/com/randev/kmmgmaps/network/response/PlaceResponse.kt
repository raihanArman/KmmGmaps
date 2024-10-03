package com.randev.kmmgmaps.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    @SerialName("items")
    val items: List<Item?>? = null
) {
    @Serializable
    data class Item(
        @SerialName("access")
        val access: List<Acces?>? = null,
        @SerialName("address")
        val address: Address? = null,
        @SerialName("categories")
        val categories: List<Category?>? = null,
        @SerialName("chains")
        val chains: List<Chain?>? = null,
        @SerialName("contacts")
        val contacts: List<Contact?>? = null,
        @SerialName("distance")
        val distance: Int? = null,
        @SerialName("id")
        val id: String? = null,
        @SerialName("language")
        val language: String? = null,
        @SerialName("ontologyId")
        val ontologyId: String? = null,
        @SerialName("openingHours")
        val openingHours: List<OpeningHour?>? = null,
        @SerialName("payment")
        val payment: Payment? = null,
        @SerialName("position")
        val position: Position? = null,
        @SerialName("references")
        val references: List<Reference?>? = null,
        @SerialName("resultType")
        val resultType: String? = null,
        @SerialName("title")
        val title: String? = null
    ) {
        @Serializable
        data class Acces(
            @SerialName("lat")
            val lat: Double? = null,
            @SerialName("lng")
            val lng: Double? = null
        )

        @Serializable
        data class Address(
            @SerialName("city")
            val city: String? = null,
            @SerialName("countryCode")
            val countryCode: String? = null,
            @SerialName("countryName")
            val countryName: String? = null,
            @SerialName("county")
            val county: String? = null,
            @SerialName("countyCode")
            val countyCode: String? = null,
            @SerialName("district")
            val district: String? = null,
            @SerialName("houseNumber")
            val houseNumber: String? = null,
            @SerialName("label")
            val label: String? = null,
            @SerialName("postalCode")
            val postalCode: String? = null,
            @SerialName("state")
            val state: String? = null,
            @SerialName("stateCode")
            val stateCode: String? = null,
            @SerialName("street")
            val street: String? = null
        )

        @Serializable
        data class Category(
            @SerialName("id")
            val id: String? = null,
            @SerialName("name")
            val name: String? = null,
            @SerialName("primary")
            val primary: Boolean? = null
        )

        @Serializable
        data class Chain(
            @SerialName("id")
            val id: String? = null,
            @SerialName("name")
            val name: String? = null
        )

        @Serializable
        data class Contact(
            @SerialName("email")
            val email: List<Email?>? = null,
            @SerialName("fax")
            val fax: List<Fax?>? = null,
            @SerialName("phone")
            val phone: List<Phone?>? = null,
            @SerialName("www")
            val www: List<Www?>? = null
        ) {
            @Serializable
            data class Email(
                @SerialName("value")
                val value: String? = null
            )

            @Serializable
            data class Fax(
                @SerialName("categories")
                val categories: List<Category?>? = null,
                @SerialName("value")
                val value: String? = null
            ) {
                @Serializable
                data class Category(
                    @SerialName("id")
                    val id: String? = null
                )
            }

            @Serializable
            data class Phone(
                @SerialName("categories")
                val categories: List<Category?>? = null,
                @SerialName("value")
                val value: String? = null
            ) {
                @Serializable
                data class Category(
                    @SerialName("id")
                    val id: String? = null
                )
            }

            @Serializable
            data class Www(
                @SerialName("categories")
                val categories: List<Category?>? = null,
                @SerialName("value")
                val value: String? = null
            ) {
                @Serializable
                data class Category(
                    @SerialName("id")
                    val id: String? = null
                )
            }
        }

        @Serializable
        data class OpeningHour(
            @SerialName("categories")
            val categories: List<Category?>? = null,
            @SerialName("isOpen")
            val isOpen: Boolean? = null,
            @SerialName("structured")
            val structured: List<Structured?>? = null,
            @SerialName("text")
            val text: List<String?>? = null
        ) {
            @Serializable
            data class Category(
                @SerialName("id")
                val id: String? = null
            )

            @Serializable
            data class Structured(
                @SerialName("duration")
                val duration: String? = null,
                @SerialName("recurrence")
                val recurrence: String? = null,
                @SerialName("start")
                val start: String? = null
            )
        }

        @Serializable
        data class Payment(
            @SerialName("methods")
            val methods: List<Method?>? = null
        ) {
            @Serializable
            data class Method(
                @SerialName("accepted")
                val accepted: Boolean? = null,
                @SerialName("id")
                val id: String? = null
            )
        }

        @Serializable
        data class Position(
            @SerialName("lat")
            val lat: Double? = null,
            @SerialName("lng")
            val lng: Double? = null
        )

        @Serializable
        data class Reference(
            @SerialName("id")
            val id: String? = null,
            @SerialName("supplier")
            val supplier: Supplier? = null
        ) {
            @Serializable
            data class Supplier(
                @SerialName("id")
                val id: String? = null
            )
        }
    }
}