extra.apply {
    set("isCiServer", System.getenv().containsKey("CIRRUS_CI"))
    set("isMasterBranch", System.getenv()["CIRRUS_BRANCH"] == "main")
    set("buildCacheHost", System.getenv().getOrDefault("CIRRUS_HTTP_CACHE_HOST", "localhost:12321"))
}

pluginManagement {
    repositories {
        google()
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
buildCache {
    local {
        isEnabled = !(extra["isCiServer"] as Boolean)
    }
    remote<HttpBuildCache> {
        url = uri("http://${extra["buildCacheHost"]}/")
        isEnabled = !(extra["isCiServer"] as Boolean)
        isPush = !(extra["isMasterBranch"] as Boolean)
    }
}

rootProject.name = "FixMyLinks"
include(":app")
 