pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Drive Sell"
include(":app")
include(":core")
include(":feature")
include(":feature:auth")
include(":feature:home")
include(":feature:search")
include(":feature:favourites")
include(":feature:profile")
include(":feature:add_car")
include(":feature:settings")
include(":feature:menu")
include(":data")
include(":data:user")
include(":data:cars")
include(":core:common")
include(":core:cars")
include(":feature:car_details")
include(":feature:users_ads")
