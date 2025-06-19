/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.woof.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.woof.R

/**
 * A data class to represent the information presented in the dog card
 */
data class Dog(
    @DrawableRes val imageResourceId: Int,
    @StringRes val name: Int,
    @StringRes val description: Int,
    val age: Int,
    @StringRes val breed: Int,
    @StringRes val gender: Int
)

val dogs = listOf(
    Dog(
        imageResourceId = R.drawable.koda,
        name = R.string.dog_name_1,
        description = R.string.dog_description_1,
        age = 2,
        breed = R.string.breed_australian_shepherd,
        gender = R.string.gender_male
    ),
    Dog(
        imageResourceId = R.drawable.lola,
        name = R.string.dog_name_2,
        description = R.string.dog_description_2,
        age = 16,
        breed = R.string.breed_poodle,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.frankie,
        name = R.string.dog_name_3,
        description = R.string.dog_description_3,
        age = 2,
        breed = R.string.breed_french_bulldog,
        gender = R.string.gender_male
    ),
    Dog(
        imageResourceId = R.drawable.nox,
        name = R.string.dog_name_4,
        description = R.string.dog_description_4,
        age = 8,
        breed = R.string.breed_border_collie,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.faye,
        name = R.string.dog_name_5,
        description = R.string.dog_description_5,
        age = 8,
        breed = R.string.breed_golden_retriever,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.bella,
        name = R.string.dog_name_6,
        description = R.string.dog_description_6,
        age = 14,
        breed = R.string.breed_labrador,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.moana,
        name = R.string.dog_name_7,
        description = R.string.dog_description_7,
        age = 2,
        breed = R.string.breed_siberian_husky,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.tzeitel,
        name = R.string.dog_name_8,
        description = R.string.dog_description_8,
        age = 7,
        breed = R.string.breed_dachshund,
        gender = R.string.gender_female
    ),
    Dog(
        imageResourceId = R.drawable.leroy,
        name = R.string.dog_name_9,
        description = R.string.dog_description_9,
        age = 4,
        breed = R.string.breed_german_shepherd,
        gender = R.string.gender_male
    )
)
