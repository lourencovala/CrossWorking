package ps.crossworking.testData

import ps.crossworking.dto.CategoryApiOutput
import ps.crossworking.model.CategoryOutput
import java.util.*

val categoriesData = listOf(
    CategoryApiOutput(UUID.fromString("bd1fde89-4ccd-4c51-a4a4-e39dc40009dd"), "Technology"),
    CategoryApiOutput(UUID.fromString("313ad4b1-0d2d-407e-8a6c-7bc32c91c3fe"), "Cooking"),
    CategoryApiOutput(UUID.fromString("fbf8df2b-254d-48ff-899a-ca6340326ea9"), "Sports"),
    CategoryApiOutput(UUID.fromString("42534b44-b0da-45dd-ae99-6eea504f9579"), "Crafts"),
    CategoryApiOutput(UUID.fromString("0d9ee4c2-990e-4d46-ba9a-2b2dd3903ae4"), "Arts"),
    CategoryApiOutput(UUID.fromString("cbaa5a3d-5d4f-432e-8fb8-426918c1df90"), "Tools"),
    CategoryApiOutput(UUID.fromString("413b00be-1dd5-46fa-a62a-150ce0589669"), "Other")
)